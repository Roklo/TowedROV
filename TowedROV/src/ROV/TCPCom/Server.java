package ROV.TCPCom;

import ROV.*;
import ROV.AlarmSystem.AlarmHandler;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

/**
 * Code taken from
 * http://tutorials.jenkov.com/java-multithreaded-servers/multithreaded-server.html
 *
 * @author <Robin S. Thorholm>
 */
public class Server implements Runnable
{

    protected int serverPort;
    protected ServerSocket serverSocket = null;
    protected boolean isStopped = false;
    protected Thread runningThread = null;
    DataHandler dh = null;
    AlarmHandler alarmHandler = null;

    public Server(int port, DataHandler dh)
    {
        this.serverPort = port;
        this.dh = dh;
        this.alarmHandler = alarmHandler;
    }

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
                    new WorkerRunnable(clientSocket, "Multithreaded Server", dh)
            ).start();
        }
        System.out.println("Server Stopped.");
    }

    private synchronized boolean isStopped()
    {
        return this.isStopped;
    }

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
