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

/**
 * Client class that handles the connection to the server, retrieves the video
 * stream and sends commands to the server
 *
 */
public class TCPClient implements Runnable
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

    String start_char = "<";
    String end_char = ">";
    String sep_char = ":";

    BufferedReader inFromServer;
    PrintWriter outToServer;

    public TCPClient(String IP, int port, Data data)
    {
        this.data = data;
        this.port = port;
        this.IP = IP;
    }

    @Override
    public void run()
    {
        while (!this.connected)
        {
            try
            {
                this.connect(this.IP, this.port);
            } catch (Exception e1)
            {
                //System.out.println("Could not connect to server (" + this.IP + ":" + this.port + "): " + e1.getMessage());
                long sec = 5000;
                //System.out.println("Trying to reconnect in " + sec + " ms...");
                try
                {
                    Thread.sleep(sec);
                } catch (Exception e2)
                {
                }
            }
        }

        boolean finished = false;

        while (!finished)
        {
            try
            {
                //Run

                if (this.connectionResetError && !isConnected())
                {
                    int sec = 5000;
                    System.out.println("Trying to reconnect (" + this.IP + ":" + this.port + ") in " + sec + " sec...");
                    Thread.sleep(sec);
                    this.connect(this.IP, this.port);
                }
            } catch (SocketTimeoutException ex)
            {
                System.out.println("Error: Read timed out");
                this.connectionResetError = true;
                this.connected = false;
            } catch (SocketException ex)
            {
                System.out.println("An error occured: Connection reset");
                this.connectionResetError = true;
                this.connected = false;
            } catch (Exception e)
            {
                System.out.println("An error occured: " + e);
                this.connectionResetError = true;
                this.connected = false;
            }
        }

    }

    /**
     * Sends a command to the server.
     *
     * @param s String containing the command to send
     * @throws IOException Throws IOException if client is disconnected
     */
    public synchronized void sendCommand(String cmd) throws IOException
    {
        try
        {
            if (isConnected())
            {

                String commandString = "<" + cmd + ">";
                outToServer.println(commandString);
                System.out.println("Cmd sent: " + commandString);
                outToServer.flush();

                String serverResponse = inFromServer.readLine();
                System.out.println("Server response: " + serverResponse);
                if (cmd.equals("fb_allData") || cmd.equals("getData"))
                {
                    HashMap<String, String> newDataList = new HashMap<>();
                    String key = "";
                    String value = "";
                    if (serverResponse.contains("<") && serverResponse.contains(">"))
                    {
                        serverResponse = serverResponse.substring(serverResponse.indexOf(start_char) + 1);
                        serverResponse = serverResponse.substring(0, serverResponse.indexOf(end_char));
                        serverResponse = serverResponse.replace("?", "");
                        if (serverResponse.contains(":"))
                        {
                            String[] dataArray = serverResponse.split(sep_char);
                            for (int i = 0; i < dataArray.length; i += 2)
                            {
                                newDataList.put(dataArray[i], dataArray[i + 1]);
                            }
                        } else
                        {
                            System.out.println("No data gotten...");
                        }

                    } else
                    {
                        System.out.println("The data string which was received was not complete...");
                    }

                    this.handleDataFromRemote(newDataList);
                }

            } else
            {
                System.out.println("Command not sent: Not connected to server");
            }

        } catch (SocketTimeoutException ex)
        {
            System.out.println("Error: Read timed out");
            this.connectionResetError = true;
            this.connected = false;
        } catch (SocketException ex)
        {
            System.out.println("An error occured: Connection reset");
            this.connectionResetError = true;
            this.connected = false;
        } catch (Exception e)
        {
            System.out.println("An error occured: " + e);
            this.connectionResetError = true;
            this.connected = false;
        }

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
//        this.inputStream = clientSocket.getInputStream();
//        this.outputStream = clientSocket.getOutputStream();
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

    void sendDepthCommand()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Compare keys to controll values coming in from arduino, and puts correct
     * value to correct variable.
     *
     * @param newDataList
     */
    public void handleDataFromRemote(HashMap<String, String> newDataList)
    {
        for (Map.Entry e : newDataList.entrySet())
        {
            String key = (String) e.getKey();
            String value = (String) e.getValue();

//            switch (key)
//            {
//                case "Satellites":
//                    setSatellites(Integer.parseInt(value));
//                    break;
//                case "Altitude":
//                    setAltitude(Float.parseFloat(value));
//                    break;
//                case "Angle":
//                    setAngle(Float.parseFloat(value));
//                    break;
//                case "Speed":
//                    setSpeed(Float.parseFloat(value));
//                    break;
//                case "Latitude":
//                    setLatitude(Float.parseFloat(value));
//                    break;
//                case "Longitude":
//                    setLongitude(Float.parseFloat(value));
//                    break;
//                case "Depth":
//                    setDepth(Float.parseFloat(value));
//                    break;
//                case "Temp":
//                    setTemperature(Float.parseFloat(value));
//                    break;
//                case "Roll":
//                    setRoll(Integer.parseInt(value));
//                    break;
//                case "Pitch":
//                    setPitch(Integer.parseInt(value));
//                    break;
//                case "Heading":
//                    setHeading(Integer.parseInt(value));
//                    break;
//            }
        }
    }
}
