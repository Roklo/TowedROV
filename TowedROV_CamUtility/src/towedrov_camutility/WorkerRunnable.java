package towedrov_camutility;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
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
    private Data data;

    String start_char = "<";
    String end_char = ">";
    String sep_char = ":";

    public WorkerRunnable(Socket clientSocket, String serverText, Data data)
    {
        this.clientSocket = clientSocket;
        this.serverText = serverText;
        this.data = data;

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

//                        case "setCmd_Fan":
//                            data.setFanRunning(Boolean.valueOf(value));
//                            System.out.println("Fan set to: " + data.isFanRunning());
//                            this.sendCommandsToArduino();
//                            outToClient.println("<setCmd_Fan:" + data.isFanRunning() + ">");
//                            break;
//                        case "setCmd_Heater":
//                            data.setHeaterRunning(Boolean.valueOf(value));
//                            System.out.println("Heater set to: " + data.isHeaterRunning());
//                            this.sendCommandsToArduino();
//                            outToClient.println("<setCmd_Heater:" + data.isHeaterRunning() + ">");
//                            break;
                        case "setCmd_CameraPitch":
                            data.setCameraPitchValue(parseStringToInt(value));
                            System.out.println("Camera Pitch set to: " + data.getCameraPitchValue());
                            this.sendCommandToArduino();
                            outToClient.println("<setCmd_CameraPitch:" + data.getCameraPitchValue() + ">");
                            break;

                        //Feedback commands
                        case "get_Pressure":
                            outToClient.println("<get_Pressure:" + data.getPressureValue() + ">");
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

    private void sendCommandToArduino()
    {
        // send values to arduino with i2c.
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
