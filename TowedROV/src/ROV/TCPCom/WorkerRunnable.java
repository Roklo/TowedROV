package ROV.TCPCom;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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

    public WorkerRunnable(Socket clientSocket, String serverText)
    {
        this.clientSocket = clientSocket;
        this.serverText = serverText;
    }

    public void run()
    {
        boolean clientOnline = true;

        try
        {
            BufferedReader inFromClient = new BufferedReader(
                    new InputStreamReader(
                            this.clientSocket.getInputStream()));

            PrintWriter outToClient = new PrintWriter(
                    this.clientSocket.getOutputStream(), true);
            while (clientOnline)
            {

                InputStream input = clientSocket.getInputStream();
                OutputStream output = clientSocket.getOutputStream();

                //String inputData = inFromClient.toString();
                String inputData = inFromClient.readLine();

                switch (inputData)
                {
                    case "ping":
                        output.write(("<ping:true>").getBytes());
                        outToClient.println("<ping:true>");
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
            e.printStackTrace();
        }

    }
}
