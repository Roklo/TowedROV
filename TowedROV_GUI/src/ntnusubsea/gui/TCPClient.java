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

/**
 * Client class that handles the connection to the server, retrieves the video
 * stream and sends commands to the server
 *
 * @author Marius Nonsvik
 */
public class TCPClient implements Runnable {
    private boolean connected = false;
    private Socket soc;
    private InputStream inputStream;
    private OutputStream outputStream;
    private String IP;
    private final int port = 5001;
    private ObjectInputStream ois;
    private PrintStream ps;
    private Data data;
    private SocketChannel sc;

    public TCPClient(Data data) {
        this.data = data;
    }

    /**
     * Connects the client to the server through a socket and saves the IP and
     * port to the global variables IP and port
     *
     * @param IP IP of the server to connect to
     * @throws IOException Throws an IOException when the connection is
     * unsuccessful
     */
    public void connect(String IP) throws IOException {
        soc = new Socket(IP, port);
        this.inputStream = soc.getInputStream();
        this.outputStream = soc.getOutputStream();
        this.ps = new PrintStream(outputStream);
        this.IP = IP;
        connected = true;
    }

    /**
     * Retrieves the byte array containing sensor data from the ROV and updates
     * these values in the data storage.
     *
     * @throws Exception Throws exception if header is invalid or data is null
     */
    public void receiveData() throws Exception {
        
        byte[] byteArray = new byte[42];
        if (inputStream.available() >= 42) {
            inputStream.read(byteArray, 0, 42);
            data.setActuatorStatus(ByteBuffer.wrap(byteArray).order(ByteOrder.BIG_ENDIAN).get(0));
            data.setDepth(ByteBuffer.wrap(byteArray).order(ByteOrder.BIG_ENDIAN).getFloat(1));
            data.setSeafloorRov(ByteBuffer.wrap(byteArray).order(ByteOrder.BIG_ENDIAN).getFloat(5)); // not tested
            data.setTemperature(ByteBuffer.wrap(byteArray).order(ByteOrder.BIG_ENDIAN).getFloat(9));
            data.setPressure(ByteBuffer.wrap(byteArray).order(ByteOrder.BIG_ENDIAN).getFloat(13));
            data.setRollAngle(ByteBuffer.wrap(byteArray).order(ByteOrder.BIG_ENDIAN).getFloat(17));
            data.setPitchAngle(ByteBuffer.wrap(byteArray).order(ByteOrder.BIG_ENDIAN).getFloat(21));
            data.setLeakStatus(ByteBuffer.wrap(byteArray).order(ByteOrder.BIG_ENDIAN).get(25));
            data.setChannel(ByteBuffer.wrap(byteArray).order(ByteOrder.LITTLE_ENDIAN).getFloat(26), 1);
            data.setChannel(ByteBuffer.wrap(byteArray).order(ByteOrder.LITTLE_ENDIAN).getFloat(30), 2);
            data.setChannel(ByteBuffer.wrap(byteArray).order(ByteOrder.LITTLE_ENDIAN).getFloat(34), 3);
            data.setChannel(ByteBuffer.wrap(byteArray).order(ByteOrder.LITTLE_ENDIAN).getFloat(38), 4);
        }
    }

    /**
     * Sends a command to the server.
     *
     * @param s String containing the command to send
     * @throws IOException Throws IOException if client is disconnected
     */
    public synchronized void sendCommand(String s) throws IOException {
        ps.print(s + System.getProperty("line.separator"));
        outputStream.flush();
        System.out.println(s);
    }


    /**
     * Returns the connection status of the socket
     *
     * @return The connection status of the socket
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Closes the socket if the client is currently connected
     *
     * @throws IOException Throws IOException if there is a problem with the
     * connection
     */
    public void disconnect() throws IOException {
        if (soc != null) {
            soc.close();
        }
        connected = false;
    }

    @Override
    public void run() {
        if (connected) {
            try {
                receiveData();
            } catch (Exception ex) {
                
            }
        }
    }
}
