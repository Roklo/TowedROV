package towedrov_camutility;

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
    private Data data;

    public Server(int port, Data data)
    {
        this.serverPort = port;
        this.data = data;
    }

    public void run()
    {
//        synchronized (this)
//        {
//            this.runningThread = Thread.currentThread();
//        }
        openServerSocket();
        while (!isStopped())
        {
            Socket clientSocket = null;
            try
            {
                System.out.println("Waiting for client...");
                clientSocket = this.serverSocket.accept();
                System.out.println("Client connected!");
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
                    new WorkerRunnable(clientSocket, "Multithreaded Server", data)
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
            System.out.println("\r\nRunning Server: "
                    + "Host=" + serverSocket.getInetAddress().getHostAddress()
                    + " Port=" + serverSocket.getLocalPort());
        } catch (IOException e)
        {
            throw new RuntimeException("Cannot open port 8080", e);
        }
    }

}
