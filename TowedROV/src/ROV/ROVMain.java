/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ROV;

import ROV.*;
import I2CCom.*;
import SerialCom.*;
import ROV.AlarmSystem.AlarmHandler;

import ROV.TCPCom.Server;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 *
 * @author Robin S. Thorholm
 */
public class ROVMain
{

    private final static int serverPort = 8080;
    private static Thread serialRW;
    private static Thread I2CComHandler;

    static boolean dataIsRecieved = false;
    static boolean testIsDone = false;
    //static SerialPort serialPort;
    private static Thread I2CRW;

    protected static DataHandler dh;
    //protected static I2CHandler I2CH;

    private static Thread Server;
    private static Thread alarmHandler;

    private static Thread imuThread;
    private static Thread ArduinoIOThread;
    private static Thread ArduinoActuatorFBThread;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        boolean foundComPort = false;

        ScheduledExecutorService executor
                = Executors.newScheduledThreadPool(8);

        String osName = System.getProperty("os.name");

        String[] portNames = SerialPortList.getPortNames();
        for (int i = 0; i < portNames.length; i++)
        {
            System.out.println(portNames[i]);
        }
        if (!osName.contains("Windows"))
        {

            // I2CH = new I2CHandler(dh);
        } else
        {
            System.out.println("OS is windows, does not start raspberry libraries");
        }
        dh = new DataHandler();

        I2CRW I2CRW_this = new I2CRW(dh);
        Logic logic = new Logic(dh, I2CRW_this);
        
        PID pid = new PID (dh);
        dh.addObserver(logic);
        
        

        SerialDataHandler sdh = new SerialDataHandler(dh);

        executor.scheduleAtFixedRate(I2CRW_this,
                20, 40, TimeUnit.MILLISECONDS);

        executor.scheduleAtFixedRate(logic,
                20, 10, TimeUnit.MILLISECONDS);
        
        executor.scheduleAtFixedRate(pid,
                20, 10, TimeUnit.MILLISECONDS);
       

//        int b1 = 0xC0;//0xC0 + (commandValue & 0x1F);
//        int b2 = 0x7F; //(commandValue >> 5) & 0x7F;
//
//        String dataToSend = String.valueOf(b1) + String.valueOf(b2);
//        System.out.println(Byte.parseByte(dataToSend));
        //       I2CRW_this.readI2CData("ArduinoIO");
//
//        I2CRW_this.sendI2CData("ActuatorPS_setTarget", 0);
//        I2CRW_this.sendI2CData("ActuatorSB_setTarget", 0);
//        
//        alarmHandler = new Thread(new AlarmHandler(dh));
//        alarmHandler.start();
//        alarmHandler.setName("AlarmHandler");
//
        Server = new Thread(new Server(serverPort, dh));
        Server.start();
        Server.setName("Server");
//        I2CRW = new Thread(new I2CRW(dh));
//        I2CRW.start();
//        I2CRW.setName("I2C_RW");
//        Semaphore semSerial = new Semaphore(1);
//        serialRW = new Thread(new SerialRW(semSerial));
        int inputData = 0;
        if (!foundComPort)

        {
            System.out.println("Searching for com ports...");
            sdh.findComPorts();
            foundComPort = true;
        }

        for (Map.Entry e : dh.comPortList.entrySet())
        {
            String comPortKey = (String) e.getKey();
            String comPortValue = (String) e.getValue();
            if (comPortValue.contains("IMU"))
            {
                imuThread = new Thread(new ReadSerialData(dh, comPortKey, 115200, comPortValue));
                imuThread.start();
                imuThread.setName(comPortValue);
                System.out.println("IMU found");

            }

            if (comPortValue.contains("EchoSounder"))
            {
                ArduinoIOThread = new Thread(new ReadSerialData(dh, comPortKey, 4800, comPortValue));
                ArduinoIOThread.start();
                ArduinoIOThread.setName(comPortValue);
                System.out.println("EchoSounder found");

            }

            if (comPortValue.contains("ActuatorFBArduino"))
            {
                ArduinoActuatorFBThread = new Thread(new ReadSerialData(dh, comPortKey, 38400, comPortValue));
                ArduinoActuatorFBThread.start();
                ArduinoActuatorFBThread.setName(comPortValue);
                System.out.println("ActuatorFBArduino found");

            }

        }
        System.out.println("Done");
        
        dh.setFb_ROVReady(true);
        try
        {
            dh.setCmd_BlueLED(1);
            Thread.sleep(500);
            dh.setCmd_BlueLED(0);
        } catch (Exception e)
        {
        }

        while (true)
        {
            try
            {
//                System.out.println("Current depth: " + dh.getCmd_currentROVdepth());
                //Thread.sleep(500);
//                System.out.println("------------------------------------");
//                System.out.println("FeedbackPS: " + dh.getFb_actuatorPSPos());
//                System.out.println("FeedbackSB: " + dh.getFb_actuatorSBPos());
//                System.out.println("Temp1: " + dh.getFb_tempMainElBoxFront());
//                System.out.println("Temp2: " + dh.getFb_tempMainElBoxRear());

            } catch (Exception e)
            {
            }

            //  System.out.println("Roll: " + dh.getFb_roll());
            // System.out.println("Pitch: " + dh.getFb_pitch());
        }
        //serialRW.start();
//        // ////////////////////////Robin's test area//////////////////////
////        I2CH.requestDataFrom("ArduinoIO");
////        System.out.println("Data is gotten");
////
////        System.out.println("Depth to seabed: " + dh.getDepthToSeabed() + " m");
////        System.out.println("Speed through water: " + dh.getSpeedThroughWather() + " knots");
////        try
////        {
////            Thread.sleep(10000);
////        } catch (Exception e)
////        {
////        }
////
//        byte dataToSend[] = new byte[]
//        {
//            0x00
//        };
//
//        try
//        {
//            Thread.sleep(5000);
//            System.out.println(
//                    "///////////////////////////");
//            System.out.println(
//                    "Starting to send data");
//            I2CH.sendI2CCommand(dataToSend, "ActuatorSB_setTarget", 3200);
//            Thread.sleep(1000);
//            while (I2CH.getI2cSendRequest())
//            {
//                Thread.sleep(250);
//            }
//        } catch (Exception e)
//        {
//            System.out.println("Exception: " + e);
//        }
//
//        System.out.println(
//                "Data is sent");
//        System.out.println(
//                "///////////////////////////");
//
//        ////////////////////////End of Robin's test area//////////////////////
    }
}
