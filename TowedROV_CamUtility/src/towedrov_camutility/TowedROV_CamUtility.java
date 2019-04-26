/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package towedrov_camutility;

/**
 *
 * @author hakon
 */
public class TowedROV_CamUtility
{

    private static Thread serverThread;
    private static Thread serialReadThread;
    private static int serverPort = 6789;
    private static Data data = new Data();
    private static String comPort = "COM3";
    private static int baudRate = 4800;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        serverThread = new Thread(new Server(serverPort, data));
        serverThread.start();
        serverThread.setName("TCPServer");

        serialReadThread = new Thread(new ReadSerialData(data, comPort, baudRate));
        serialReadThread.start();
        serialReadThread.setName("SerialReader");
    }

}
