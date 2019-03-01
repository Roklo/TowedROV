/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ROV;

import ROV.SerialCom.SerialRW;
import I2CCom.*;
import ROV.TCPCom.Server;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;

/**
 *
 * @author Robin S. Thorholm
 */
public class ROVMain
{

    private final static int serverAddress = 9000;
    private static Thread serialRW;
    private static Thread I2CComHandler;
    static boolean dataIsRecieved = false;
    static boolean testIsDone = false;
    static SerialPort serialPort;
    protected static DataHandler dh;
    protected static I2CHandler I2CH;

    private static Thread Server;

    public final static int ARDUINO_DUMMY_SIGNAL_ADDRESS = 9;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        dh = new DataHandler();
        I2CH = new I2CHandler(dh);

        Server = new Thread(new Server(serverAddress));
        Server.start();
        Server.setName("Server");

//        Semaphore semSerial = new Semaphore(1);
//        serialRW = new Thread(new SerialRW(semSerial));
        int inputData = 0;

        //serialRW.start();
        System.out.println(System.getProperty("os.name"));
        String[] portNames = SerialPortList.getPortNames();
        for (int i = 0; i < portNames.length; i++)
        {
            System.out.println(portNames[i]);

        }
        // ////////////////////////Robin's test area//////////////////////
//        I2CH.requestDataFrom("ArduinoIO");
//        System.out.println("Data is gotten");
//
//        System.out.println("Depth to seabed: " + dh.getDepthToSeabed() + " m");
//        System.out.println("Speed through water: " + dh.getSpeedThroughWather() + " knots");
//        try
//        {
//            Thread.sleep(10000);
//        } catch (Exception e)
//        {
//        }
//
        byte dataToSend[] = new byte[]
        {
            0x00
        };

        try
        {
            Thread.sleep(5000);
            System.out.println(
                    "///////////////////////////");
            System.out.println(
                    "Starting to send data");
            I2CH.sendI2CCommand(dataToSend, "ActuatorSB_setTarget", 3200);
            Thread.sleep(1000);
            while (I2CH.getI2cSendRequest())
            {
                Thread.sleep(250);
            }
        } catch (Exception e)
        {
            System.out.println("Exception: " + e);
        }

        System.out.println(
                "Data is sent");
        System.out.println(
                "///////////////////////////");

        ////////////////////////End of Robin's test area//////////////////////
    }
}
