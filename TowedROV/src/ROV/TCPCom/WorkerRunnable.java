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
import java.util.Map.Entry;

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
    StartupCalibration StartupCalibration = null;
    
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
                    if (inputData.contains("<") && inputData.contains(">"))
                    {
                        inputData = inputData.substring(inputData.indexOf(start_char) + 1);
                        inputData = inputData.substring(0, inputData.indexOf(end_char));
                        inputData = inputData.replace("?", "");
                        if (inputData.contains(":"))
                        {
                            String[] data = inputData.split(sep_char);
                            key = data[0];
                            value = data[1];
                        } else
                        {
                            String[] data = inputData.split(sep_char);
                            key = data[0];
                        }
                        
                    } else
                    {
                        key = (String) inputData;
                    }
                    if (!dh.getFb_ROVReady())
                    {
                        outToClient.println("Server: ROV not ready");
                    } else
                    {
                        
                        switch (key)
                        {
                            //Commands

                            case "cmd_lightMode":
                                dh.setCmd_lightMode(parseStringToInt(value));
                                System.out.println("LightMode: " + dh.getCmd_lightMode());
                                outToClient.println("Server: OK");
                                break;
                            
                            case "cmd_actuatorPS":
                                dh.setCmd_actuatorPS(parseStringToInt(value));
                                System.out.println("actuatorPS is: " + dh.getCmd_actuatorPS());
                                outToClient.println("Server: OK");
                                
                                break;
                            
                            case "cmd_actuatorSB":
                                dh.setCmd_actuatorSB(parseStringToInt(value));
                                System.out.println("actuatorSB is: " + dh.getCmd_actuatorSB());
                                outToClient.println("Server: OK");
                                break;
                            
                            case "actuator_test":
                                dh.setCmd_actuatorSB(parseStringToInt(value));
                                
                                dh.setCmd_actuatorPS(parseStringToInt(value));

                                //System.out.println("actuatorSB is: " + dh.getCmd_actuatorSB());
                                outToClient.println("Server: OK");
                            
                            case "cmd_targetDistance":
                                dh.setCmd_targetDistance(parseStringToDouble(value));
                                outToClient.println("Server: OK");
                                break;
                            
                            case "cmd_cameraPitch":
                                dh.setCmd_cameraPitch(parseStringToInt(value));
                                System.out.println("cameraPitch is: " + dh.getCmd_cameraPitch());
                                outToClient.println("Server: OK");
                                break;
                            
                            case "cmd_cameraRoll":
                                dh.setCmd_cameraRoll(parseStringToInt(value));
                                System.out.println("cameraRoll is: " + dh.getCmd_cameraRoll());
                                outToClient.println("Server: OK");
                                break;
                            
                            case "cmd_cameraMode":
                                dh.setCmd_cameraMode(parseStringToByte(value));
                                System.out.println("cameraPitch is: " + dh.getCmd_cameraMode());
                                outToClient.println("Server: OK");
                                break;
                            
                            case "cmd_pid_p":
                                dh.setCmd_pid_p(parseStringToDouble(value));
//                                System.out.println("Pid_p is: " + dh.getCmd_pid_p());
                                outToClient.println("Server: OK");
                                break;
                            
                            case "cmd_pid_i":
                                dh.setCmd_pid_i(parseStringToDouble(value));
//                                System.out.println("Pid_i is: " + dh.getCmd_pid_i());
                                outToClient.println("Server: OK");
                                break;
                            
                            case "cmd_pid_d":
                                dh.setCmd_pid_d(parseStringToDouble(value));
//                                System.out.println("Pid_d is: " + dh.getCmd_pid_d());
                                outToClient.println("Server: OK");
                                break;
                            
                            case "cmd_pid_gain":
                                dh.setCmd_pid_gain(parseStringToDouble(value));
//                                System.out.println("Pid_gain is: " + dh.getCmd_pid_gain());
                                outToClient.println("Server: OK");
                                break;
                            
                            case "cmd_emergencySurface":
                                dh.setCmd_emergencySurface(parseStringToBoolean(value));
                                System.out.println("EmergencySurface is: " + dh.isCmd_emergencySurface());
                                outToClient.println("Server: OK");
                                break;
                            
                            case "cmd_BlueLED":
                                dh.setCmd_BlueLED(parseStringToInt(value));
                                outToClient.println("Server: OK");
                                break;
                            
                            case "cmd_rovDepth":
                                dh.setCmd_currentROVdepth(parseStringToDouble(value));
                                outToClient.println("Server: OK");
                                break;
                            
                            case "cmd_targetMode":
                                dh.setcmd_targetMode(parseStringToInt(value));
                                outToClient.println("Server: OK");
                                break;
                            
                            case "cmd_offsetDepthBeneathROV":
                                dh.setCmd_offsetDepthBeneathROV(parseStringToDouble(value));
                                outToClient.println("Server: OK");
                                break;

                            //Feedback commands
                            case "fb_allData":
                                outToClient.println(dh.getDataToSend());
                                //System.out.println("Sent all data");
                                break;
                            
                            case "fb_depthToSeabedEcho":
                                outToClient.println("<fb_depthToSeabedEcho:" + dh.getFb_depthBeneathROV() + ">");
                                break;
                            
                            case "fb_speedThroughWather":
                                outToClient.println("<fb_speedThroughWather:" + dh.getFb_speedThroughWather() + ">");
                                break;
                            
                            case "fb_waterTemperature":
                                outToClient.println("<fb_waterTemperature:" + dh.getFb_waterTemperature() + ">");
                                break;
                            
                            case "fb_pressure":
                                outToClient.println("<fb_pressure:" + dh.getFb_pressure() + ">");
                                break;
                            
                            case "fb_actuatorPSPos":
                                outToClient.println("<fb_actuatorPSPos:" + dh.getFb_actuatorPSPos() + ">");
                                break;
                            
                            case "fb_actuatorSBPos":
                                outToClient.println("<fb_actuatorSBPos:" + dh.getFb_actuatorSBPos() + ">");
                                break;
                            
                            case "fb_tempInternalCamera":
                                outToClient.println("<fb_tempInternalCamera:" + dh.getFb_tempInternalCamera() + ">");
                                break;
                            
                            case "fb_humidityInternalCamera":
                                outToClient.println("<fb_humidityInternalCamera:" + dh.getFb_humidityInternalCamera() + ">");
                                break;
                            
                            case "fb_tempPSactuatorBox":
                                outToClient.println("<fb_tempPSactuatorBox:" + dh.getFb_tempPSactuatorBox() + ">");
                                break;
                            
                            case "fb_tempSBactuatorBox":
                                outToClient.println("<fb_tempSBactuatorBox:" + dh.getFb_tempSBactuatorBox() + ">");
                                break;
                            
                            case "fb_tempMainElBoxFront":
                                outToClient.println("<fb_tempMainElBox:" + dh.getFb_tempMainElBoxFront() + ">");
                                break;
                            
                            case "fb_tempMainElBoxRear":
                                outToClient.println("<fb_tempMainElBox:" + dh.getFb_tempMainElBoxRear() + ">");
                                break;
                            
                            case "fb_tempEchoBox":
                                outToClient.println("<fb_tempEchoBox:" + dh.getFb_tempEchoBox() + ">");
                                break;
                            
                            case "fb_currentDraw":
                                outToClient.println("<fb_currentDraw:" + dh.getFb_currentDraw() + ">");
                                break;
                            
                            case "fb_pitchAngel":
                                outToClient.println("<fb_pitchAngle:" + dh.getFb_pitchAngle() + ">");
                                break;
                            
                            case "fb_rollAngle":
                                outToClient.println("<fb_rollAngle:" + dh.getFb_rollAngle() + ">");
                                break;
                            
                            case "fb_yaw":
                                outToClient.println("<fb_yaw:" + dh.getFb_yaw() + ">");
                                break;
                            
                            case "fb_heading":
                                outToClient.println("<fb_heading:" + dh.getFb_heading() + ">");
                                break;
                            
                            case "fb_waterLeakChannel_1":
                                outToClient.println("<fb_waterLeakChannel_1:" + dh.isFb_waterLeakChannel_1() + ">");
                                break;
                            
                            case "fb_waterLeakChannel_2":
                                outToClient.println("<fb_waterLeakChannel_2:" + dh.isFb_waterLeakChannel_2() + ">");
                                break;
                            
                            case "fb_waterLeakChannel_3":
                                outToClient.println("<fb_waterLeakChannel_3:" + dh.isFb_waterLeakChannel_3() + ">");
                                break;
                            
                            case "fb_waterLeakChannel_4":
                                outToClient.println("<fb_waterLeakChannel_4:" + dh.isFb_waterLeakChannel_4() + ">");
                                break;

                            //Stored Commands
                            case "get_cmd_lightMode":
                                outToClient.println("<get_cmd_lightMode:" + dh.getCmd_lightMode() + ">");
                                break;
                            
                            case "get_cmd_actuatorPS":
                                outToClient.println("<get_cmd_actuatorPS:" + dh.getCmd_actuatorPS() + ">");
                                break;
                            
                            case "get_cmd_actuatorSB":
                                outToClient.println("<get_cmd_actuatorSB:" + dh.getCmd_actuatorSB() + ">");
                                break;

//                            case "get_cmd_depth":
//                                outToClient.println("<get_cmd_depth:" + dh.getCmd_depth() + ">");
//                                break;
                            case "get_cmd_cameraPitch":
                                outToClient.println("<get_cmd_cameraPitch:" + dh.getCmd_cameraPitch() + ">");
                                break;
                            
                            case "get_cmd_cameraRoll":
                                outToClient.println("<get_cmd_cameraRoll:" + dh.getCmd_cameraRoll() + ">");
                                break;
                            
                            case "get_cmd_cameraMode":
                                outToClient.println("<get_cmd_cameraMode:" + dh.getCmd_cameraMode() + ">");
                                break;
                            
                            case "get_cmd_pid_p":
                                outToClient.println("<get_cmd_pid_p:" + dh.getCmd_pid_p() + ">");
                                break;
                            
                            case "get_cmd_pid_i":
                                outToClient.println("<get_cmd_pid_i:" + dh.getCmd_pid_i() + ">");
                                break;
                            
                            case "get_cmd_pid_d":
                                outToClient.println("<get_cmd_pid_d:" + dh.getCmd_pid_d() + ">");
                                break;
                            
                            case "get_cmd_pid_gain":
                                outToClient.println("<get_cmd_pid_gain:" + dh.getCmd_pid_gain() + ">");
                                break;
                            
                            case "get_ROVComPorts":
                                String portListString = "<";
                                for (Entry e : dh.comPortList.entrySet())
                                {
                                    String comPortKey = (String) e.getKey();
                                    String comPortValue = (String) e.getValue();
                                    portListString = portListString + comPortKey + ":" + comPortValue + ":";
                                }
                                portListString = portListString + ">";
                                outToClient.println(portListString);
                                break;

                            //Other  commands
                            case "ping":
                                //output.write(("<ping:true>").getBytes());
                                outToClient.println("<ping:true>" + welcomeMessageIsSent);
                                welcomeMessageIsSent = true;
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
                            
                            case "getAlarms":
                                String completeAlarmListString = "<";
                                
                                for (Map.Entry e : dh.completeAlarmListDh.entrySet())
                                {
                                    key = (String) e.getKey();
                                    if (e.getValue().equals(true))
                                    {
                                        value = "true";
                                    } else
                                    {
                                        value = "false";
                                    }
                                    
                                    completeAlarmListString = completeAlarmListString + key + ":" + value + ":";
                                }
                                outToClient.println(completeAlarmListString + ">");
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
                            
                            case "inhibit_waterLeakSensor_1_Alarm":
                                if (alarmHandler.inhibitedAlarms.get("inhibit_waterLeakSensor_1_Alarm"))
                                {
                                    alarmHandler.inhibitedAlarms.put("inhibit_waterLeakSensor_1_Alarm", false);
                                } else
                                {
                                    alarmHandler.inhibitedAlarms.put("inhibit_waterLeakSensor_1_Alarm", true);
                                }
                                
                                break;
                            
                            case "startupCalibration":
                                StartupCalibration = new StartupCalibration(dh);
                                outToClient.println("Starting calibration...");
                                String data = StartupCalibration.doStartupCalibration();
                                outToClient.println("Server: " + data);
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
    
    private boolean parseStringToBoolean(String value)
    {
        Boolean result = false;
        try
        {
            result = Boolean.valueOf(value);
        } catch (Exception e)
        {
            System.out.println("Exception while parsing to double");
        }
        return result;
    }
    
    private Double parseStringToDouble(String value)
    {
        Double result = 0.00;
        try
        {
            result = Double.valueOf(value);
        } catch (Exception e)
        {
            System.out.println("Exception while parsing to double");
        }
        return result;
    }
    
    private Byte parseStringToByte(String value)
    {
        Byte result = 0;
        try
        {
            result = Byte.valueOf(value);
        } catch (Exception e)
        {
            System.out.println("Exception while parsing to byte");
        }
        return result;
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
