package ROV.TCPCom;

import ROV.*;
import ROV.AlarmSystem.AlarmHandler;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

/**
 * Code inspired from
 * http://tutorials.jenkov.com/java-multithreaded-servers/multithreaded-server.html
 *
 * This class is responsible for reciving connection and handle them in a new
 * seperat threads
 *
 */
public class Server implements Runnable
{

    protected int serverPort;
    protected ServerSocket serverSocket = null;
    protected boolean isStopped = false;
    protected Thread runningThread = null;
    Data dh = null;
    AlarmHandler alarmHandler = null;

    /**
     * Constructor for server class
     *
     * @param port the port the server is running on
     * @param dh the shared recource data class
     */
    public Server(int port, Data dh)
    {
        this.serverPort = port;
        this.dh = dh;
    }

    /**
     * Responsible for reciving connection and handle them in a new seperat
     * threads
     */
    public void run()
    {
        synchronized (this)
        {
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();
        while (!isStopped())
        {
            Socket clientSocket = null;
            try
            {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e)
            {
                if (isStopped())
                {
                    System.out.println("Server Stopped.");
                    return;
                }
                throw new RuntimeException(
                        "Error accepting client connection", e);
            }
            new Thread(
                    new WorkerRunnable(clientSocket, dh)
            ).start();
        }
        System.out.println("Server Stopped.");
    }

    private synchronized boolean isStopped()
    {
        return this.isStopped;
    }

    /**
     * Responsible for shutting down the server
     */
    public synchronized void stop()
    {
        this.isStopped = true;
        try
        {
            this.serverSocket.close();
        } catch (IOException e)
        {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private void openServerSocket()
    {
        try
        {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e)
        {
            throw new RuntimeException("Cannot open port 8080", e);
        }
    }

}
