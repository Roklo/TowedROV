/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ntnusubsea.gui;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Client class that handles the connection to the server, retrieves the video
 * stream and sends commands to the server
 *
 */
public class TCPpinger implements Runnable
{

    boolean connectionResetError = false;
    private boolean connected = false;
    private static String sentence;
    private static String serverResponse;
    private Socket clientSocket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private int port;
    private String IP;
    private Data data;

    BufferedReader inFromServer;
    PrintWriter outToServer;

    public TCPpinger(String IP, int port, Data data)
    {
        this.data = data;
        this.port = port;
        this.IP = IP;
        try
        {
            this.connect(this.IP, this.port);
        } catch (IOException ex)
        {
            System.out.println("IOException: " + ex.getMessage());
        }
    }

    @Override
    public void run()
    {
        if (this.isConnected())
        {
            data.setRovPing(this.getPing());
            //System.out.println("Ping (ROV): " + data.getRovPing());
        }
    }

    /**
     * Connects the client to the server through a socket and saves the IP and
     * port to the global variables IP and port
     *
     * @param IP IP of the server to connect to
     * @throws IOException Throws an IOException when the connection is
     * unsuccessful
     */
    public void connect(String IP, int port) throws IOException
    {
        clientSocket = new Socket(IP, port);
        clientSocket.setSoTimeout(3000);
        outToServer = new PrintWriter(
                clientSocket.getOutputStream(), true);
        inFromServer = new BufferedReader(new InputStreamReader(
                clientSocket.getInputStream()));
        System.out.println("Success! Connected to server " + this.IP + ":" + this.port);
        this.connected = true;
        this.connectionResetError = false;
    }

    /**
     * Closes the socket if the client is currently connected
     *
     * @throws IOException Throws IOException if there is a problem with the
     * connection
     */
    public void disconnect() throws IOException
    {
        if (clientSocket != null)
        {
            clientSocket.close();
        }
        connected = false;
    }

    public Double getPing()
    {
        double pingValue = 0.00;
        double elapsedTimer = 0;
        double elapsedTimerNano = 0;
        long lastTime = 0;

        String ping = "<Ping:null>";
        lastTime = System.nanoTime();
        String serverResponse = sendData("ping");
        if (serverResponse != null && serverResponse.equals("<ping:true>"))
        {
            elapsedTimerNano = (System.nanoTime() - lastTime);
            elapsedTimer = elapsedTimerNano / 1000000;
            pingValue = elapsedTimer;
            //System.out.println("<Ping: " + elapsedTimer + ">");

            elapsedTimer = 0;
        } else
        {
            pingValue = 0.00;
            ping = "<Ping:null>";
        }
        return pingValue;
    }

    public String sendData(String sentence)
    {
        try
        {
            outToServer.println(sentence);
            //System.out.println("Data is sent...");
            outToServer.flush();
            serverResponse = inFromServer.readLine();

        } catch (SocketTimeoutException ste)
        {
            System.out.println("SocketTimeoutException: " + ste.getMessage());
        } catch (Exception e)
        {
            System.out.println("Exception: " + e.getMessage());
        }
        return serverResponse;
    }

    /**
     * Returns the connection status of the socket
     *
     * @return The connection status of the socket
     */
    public boolean isConnected()
    {
        return connected;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public String getIP()
    {
        return IP;
    }

    public void setIP(String IP)
    {
        this.IP = IP;
    }
}
