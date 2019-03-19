package clientmanualtest;

import java.io.*;
import java.net.*;

/**
 * This class represents a simple TCP Client.
 *
 * @author Håkon Haram
 * @version 0.1
 */
public class ClientManualTest
{

    private static String sentence;
    private static String modifiedSentence;

    /**
     * Creates an instance of the SimpleTCPClient class.
     *
     * @param args Main class argument parametre
     * @throws Exception
     */
    public static void main(String[] args) throws Exception
    {
        double elapsedTimer = 0;
        double elapsedTimerNano = 0;
        long lastTime = 0;
        boolean connectionResetError = false;

        BufferedReader inFromUser = new BufferedReader(
                new InputStreamReader(System.in));
        boolean finished = false;
        boolean connectionEstablished = false;

        while (!finished)
        {
            try
            {
                if (connectionResetError)
                {
                    for (int sec = 5; sec >= 0; sec--)
                    {
                        System.out.println("Trying to reconnect in " + sec + " sec...");
                        Thread.sleep(1000);
                    }
                }
                Socket clientSocket = new Socket("localhost", 9000);

                //clientSocket.setSoTimeout(1000);
                PrintWriter outToServer = new PrintWriter(
                        clientSocket.getOutputStream(), true);

                BufferedReader inFromServer
                        = new BufferedReader(new InputStreamReader(
                                clientSocket.getInputStream()));
                System.out.println("Connection established...");
                connectionResetError = false;
                connectionEstablished = true;
                while (connectionEstablished)
                {
                    sentence = inFromUser.readLine();

                    lastTime = System.nanoTime();
                    outToServer.println(sentence);
                    modifiedSentence = inFromServer.readLine();
                    elapsedTimerNano = (System.nanoTime() - lastTime);
                    if (!modifiedSentence.equals("") || !modifiedSentence.equals(null))
                    {
                        System.out.println(modifiedSentence);

                        elapsedTimer = elapsedTimerNano / 1000000;
                        System.out.println("Ping: " + elapsedTimer + "ms");
                        System.out.println("This would be " + (1000 / elapsedTimer) + " times pr. second");

                        elapsedTimer = 0;
                        if (modifiedSentence.equalsIgnoreCase("Congrats!") || inFromUser.equals("exit"))
                        {
                            connectionEstablished = false;
                            finished = true;
                            clientSocket.close();
                        }
                    } else
                    {
                        System.out.println("Nothing recived from server...");
                    }
                }
            } catch (SocketTimeoutException ex)
            {
                System.out.println("Error: Read timed out");
            } catch (SocketException ex)
            {
                System.out.println("An error occured: Connection reset");
                connectionResetError = true;
            } catch (Exception e)
            {
                System.out.println("An error occured: " + e);
            }
        }
    }

}
