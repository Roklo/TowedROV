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
        BufferedReader inFromUser = new BufferedReader(
                new InputStreamReader(System.in));
        try (Socket clientSocket = new Socket("localhost", 9000))
        {
            boolean finished = false;
            while (!finished)
            {
                PrintWriter outToServer = new PrintWriter(
                        clientSocket.getOutputStream(), true);
                BufferedReader inFromServer
                        = new BufferedReader(new InputStreamReader(
                                clientSocket.getInputStream()));
                sentence = inFromUser.readLine();

                lastTime = System.nanoTime();
                outToServer.println(sentence);
                modifiedSentence = inFromServer.readLine();
                elapsedTimerNano = (System.nanoTime() - lastTime);
                System.out.println("FROM SERVER: " + modifiedSentence);

                elapsedTimer = elapsedTimerNano / 1000000;
                System.out.println("Ping: " + elapsedTimer + "ms");

                elapsedTimer = 0;
                if (modifiedSentence.equalsIgnoreCase("Congrats!") || inFromUser.equals("exit"))
                {
                    finished = true;
                    clientSocket.close();
                }
            }
        } catch (Exception e)
        {
            System.out.println("An error occured." + e);
        }
    }
}
