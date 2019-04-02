package TCPCom;

import java.io.*;
import java.net.*;
import java.nio.channels.SocketChannel;
import ntnusubsea.gui.Data;

/**
 * This class represents a simple TCP Client.
 *
 * @author Håkon Haram
 * @version 0.1
 */
public class ClientManualTest implements Runnable
{

    private static String sentence;
    private static String serverResponse;

    private boolean connected = false;
    private Socket clientSocket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private String IP;
    private final int port = 9000;

    BufferedReader inFromServer;
    PrintWriter outToServer;
//    private ObjectInputStream ois;
//    private PrintStream ps;
//    private Data data;
//    private SocketChannel sc;

    /**
     * Creates an instance of the SimpleTCPClient class.
     *
     * @param args Main class argument parametre
     * @throws Exception
     */
    @Override
    public void run()
    {

        if (connected || !connected)
        {
            try
            {
                // do something
            } catch (Exception e)
            {
            }

        }
//        double elapsedTimer = 0;
//        double elapsedTimerNano = 0;
//        long lastTime = 0;
//        boolean connectionResetError = false;
//
//        BufferedReader inFromUser = new BufferedReader(
//                new InputStreamReader(System.in));
//        boolean finished = false;
//        boolean connectionEstablished = false;

//        while (!finished)
//        {
//            try
//            {
//                if (connectionResetError)
//                {
//                    for (int sec = 5; sec >= 0; sec--)
//                    {
//                        System.out.println("Trying to reconnect in " + sec + " sec...");
//                        Thread.sleep(1000);
//                    }
//                }
//                Socket clientSocket = new Socket("localhost", port);
//
//                //clientSocket.setSoTimeout(1000);
//                PrintWriter outToServer = new PrintWriter(
//                        clientSocket.getOutputStream(), true);
//
//                BufferedReader inFromServer
//                        = new BufferedReader(new InputStreamReader(
//                                clientSocket.getInputStream()));
//                System.out.println("Connection established...");
//                connectionResetError = false;
//                connectionEstablished = true;
//                while (connectionEstablished)
//                {
//                    sentence = inFromUser.readLine();
//
//                    lastTime = System.nanoTime();
//
//                    outToServer.println(sentence);
//                    modifiedSentence = inFromServer.readLine();
//                    elapsedTimerNano = (System.nanoTime() - lastTime);
//                    if (!modifiedSentence.equals("") || !modifiedSentence.equals(null))
//                    {
//                        System.out.println(modifiedSentence);
//
//                        elapsedTimer = elapsedTimerNano / 1000000;
//                        System.out.println("Ping: " + elapsedTimer + "ms");
//                        System.out.println("This would be " + (1000 / elapsedTimer) + " times pr. second");
//
//                        elapsedTimer = 0;
//                        if (modifiedSentence.equalsIgnoreCase("Congrats!") || inFromUser.equals("exit"))
//                        {
//                            connectionEstablished = false;
//                            finished = true;
//                            clientSocket.close();
//                        }
//                    } else
//                    {
//                        System.out.println("Nothing recived from server...");
//                    }
//                }
//            } catch (SocketTimeoutException ex)
//            {
//                System.out.println("Error: Read timed out");
//            } catch (SocketException ex)
//            {
//                System.out.println("An error occured: Connection reset");
//                connectionResetError = true;
//            } catch (Exception e)
//            {
//                System.out.println("An error occured: " + e);
//            }
//        }
    }

    public String ping()
    {
        double elapsedTimer = 0;
        double elapsedTimerNano = 0;
        long lastTime = 0;

        String ping = "<Ping:null>";
        lastTime = System.nanoTime();
        String serverResponse = sendData("ping");
        if (serverResponse.equals("<ping:true>"))
        {
            elapsedTimerNano = (System.nanoTime() - lastTime);
            elapsedTimer = elapsedTimerNano / 1000000;
            System.out.println("<Ping: " + elapsedTimer + ">");

            elapsedTimer = 0;
        } else
        {

            ping = "<ping:null>";
        }

        return ping;
    }

    public synchronized String sendData(String sentence)
    {
        try
        {
            outToServer.println(sentence);
            System.out.println("Data is sent...");
            outToServer.flush();
            serverResponse = inFromServer.readLine();

        } catch (Exception e)
        {
        }

        return serverResponse;

    }

    public void connect(String IP) throws IOException
    {
        clientSocket = new Socket(IP, port);
        this.inputStream = clientSocket.getInputStream();
        this.outputStream = clientSocket.getOutputStream();
        outToServer = new PrintWriter(
                clientSocket.getOutputStream(), true);

        inFromServer = new BufferedReader(new InputStreamReader(
                clientSocket.getInputStream()));
        System.out.println("Connected...");

//        this.ps = new PrintStream(outputStream);
//        this.IP = IP;
//        connected = true;
    }

    public void disconnect() throws IOException
    {
        if (clientSocket != null)
        {
            clientSocket.close();
        }
        connected = false;
    }
}
