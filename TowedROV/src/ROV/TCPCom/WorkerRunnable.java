package ROV.TCPCom;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import ROV.*;

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

    public WorkerRunnable(Socket clientSocket, String serverText, DataHandler dh)
    {
        this.clientSocket = clientSocket;
        this.serverText = serverText;
        this.dh = dh;
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
                    String inputData = inFromClient.readLine();
                    switch (inputData)
                    {
                        case "cmd_lightIntensity":

                            break;

                        case "fb_depthToSeabedEcho":
                            outToClient.println("<fb_depthToSeabedEcho:" + dh.getFb_depthToSeabedEcho() + ">");
                            break;

                        case "ping":
                            //output.write(("<ping:true>").getBytes());
                            outToClient.println("<ping:true>" + welcomeMessageIsSent);
                            welcomeMessageIsSent = true;
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
}
