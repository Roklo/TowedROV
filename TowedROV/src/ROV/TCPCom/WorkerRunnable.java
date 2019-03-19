package ROV.TCPCom;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import ROV.*;
import ROV.AlarmSystem.AlarmHandler;
import java.util.Map;

/**
 *
 * @author <Robin S. Thorholm>
 * Code taken from:
 * http://tutorials.jenkov.com/java-multithreaded-servers/multithreaded-server.html
 *
 *
 * Access this website to test if the server is active: http://localhost:9000/
 */
public class WorkerRunnable implements Runnable
{

    protected Socket clientSocket = null;
    protected String serverText = null;
    DataHandler dh = null;
    AlarmHandler alarmHandler = null;

    String start_char = "<";
    String end_char = ">";
    String sep_char = ":";

    public WorkerRunnable(Socket clientSocket, String serverText, DataHandler dh)
    {
        this.clientSocket = clientSocket;
        this.serverText = serverText;
        this.dh = dh;
        this.alarmHandler = alarmHandler;
    }

    public void run()
    {
        boolean clientOnline = true;
        boolean welcomeMessageIsSent = false;
        try
        {
            BufferedReader inFromClient = new BufferedReader(
                    new InputStreamReader(
                            this.clientSocket.getInputStream()));

            PrintWriter outToClient = new PrintWriter(
                    this.clientSocket.getOutputStream(), true);

            InputStream input = clientSocket.getInputStream();
            OutputStream output = clientSocket.getOutputStream();

            while (clientOnline)
            {

                welcomeMessageIsSent = true;

                if (inFromClient.ready())
                {
                    String key = "";
                    String value = "";
                    String inputData = inFromClient.readLine();
                    if (inputData.contains("<") || inputData.contains(">"))
                    {
                        inputData = inputData.substring(inputData.indexOf(start_char) + 1);
                        inputData = inputData.substring(0, inputData.indexOf(end_char));
                        inputData = inputData.replace("?", "");
                        String[] data = inputData.split(sep_char);
                        key = data[0];
                        value = data[1];
                    } else
                    {
                        key = (String) inputData;
                    }

                    switch (key)
                    {
                        case "cmd_lightIntensity":
                            dh.setCmd_lightIntensity(parseStringToInt(value));
                            System.out.println("Light intensity is: " + dh.getCmd_lightIntensity());
                            outToClient.println("Server: OK");

                            break;

                        case "getAlarms":
                            String completeAlarmListString = "<";
                            
                            for (Map.Entry e : dh.completeAlarmListDh.entrySet())
                            {
                                key = (String) e.getKey();
                                if(e.getValue().equals(true))
                                {
                                    value = "true";
                                }
                                else
                                {
                                    value = "false";
                                }
                                
                                completeAlarmListString = completeAlarmListString + key + ":" + value + ":";
                            }
                            outToClient.println(completeAlarmListString + ">");
                            break;

                        case "fb_depthToSeabedEcho":
                            outToClient.println("<fb_depthToSeabedEcho:" + dh.getFb_depthToSeabedEcho() + ">");
                            break;

                        case "ping":
                            //output.write(("<ping:true>").getBytes());
                            outToClient.println("<ping:true>" + welcomeMessageIsSent);
                            welcomeMessageIsSent = true;
                            break;
                        case "waterCh1":
                            if (dh.isFb_waterLeakChannel_1())
                            {
                                dh.setFb_waterLeakChannel_1(false);

                            } else
                            {
                                dh.setFb_waterLeakChannel_1(true);
                            }
                            outToClient.println("WaterLeak: " + dh.isFb_waterLeakChannel_1());

                            break;

                        case "ack":
                            if (dh.isCmd_ack())
                            {
                                dh.setCmd_ack(false);
                            } else
                            {
                                dh.setCmd_ack(true);
                            }
                            outToClient.println("Ack: " + dh.isCmd_ack());
                            break;

                        case "inhibit_waterLeakSensor_1_Alarm":
                            if (alarmHandler.inhibitedAlarms.get("inhibit_waterLeakSensor_1_Alarm"))
                            {
                                alarmHandler.inhibitedAlarms.put("inhibit_waterLeakSensor_1_Alarm", false);
                            } else
                            {
                                alarmHandler.inhibitedAlarms.put("inhibit_waterLeakSensor_1_Alarm", true);
                            }

                            break;

                        case "exit":
                            output.close();
                            input.close();
                            clientOnline = false;
                            break;

                        default:
                            outToClient.println("Error: Not a command");
                            break;

                    }
                }

//            long time = System.currentTimeMillis();
//            output.write(("HTTP/1.1 200 OK\n\nWorkerRunnable: "
//                    + this.serverText + " - "
//                    + time
//                    + "").getBytes());
//            output.write(("\nServer is online").getBytes());
//           System.out.println("Request processed: " + time);
            }

        } catch (IOException e)
        {
            //report exception somewhere.
            System.out.println("Exception: " + e);
            e.printStackTrace();
        }
    }

    private Integer parseStringToInt(String value)
    {
        Integer result = 0;
        try
        {
            result = Integer.valueOf(value);
        } catch (Exception e)
        {
            System.out.println("Exception while parsing to integer");
        }

        return result;
    }

}
