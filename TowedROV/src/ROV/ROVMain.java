/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ROV;

import ROV.SerialCom.SerialRW;
import I2CCom.*;
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

    private static Thread serialRW;
    private static Thread I2CComHandler;
    static boolean dataIsRecieved = false;
    static boolean testIsDone = false;
    static SerialPort serialPort;
    protected static DataHandler dh;
    protected static I2CHandler I2CH;

    public final static int ARDUINO_DUMMY_SIGNAL_ADDRESS = 9;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        dh = new DataHandler();
        I2CH = new I2CHandler(dh);

        Semaphore semSerial = new Semaphore(1);
        serialRW = new Thread(new SerialRW(semSerial));

        int inputData = 0;

        serialRW.start();

        System.out.println(System.getProperty("os.name"));
        String[] portNames = SerialPortList.getPortNames();
        for (int i = 0; i < portNames.length; i++)
        {
            System.out.println(portNames[i]);

//        serialPort = new SerialPort("COM3");
//        try
//        {
//            serialPort.openPort();//Open port
//            serialPort.setParams(115200, 8, 1, 0);//Set params
//            int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;//Prepare mask
//            serialPort.setEventsMask(mask);//Set mask
//            serialPort.addEventListener(new SerialPortReader());//Add SerialPortEventListener
//        } catch (SerialPortException ex)
//        {
//            System.out.println(ex);
//        }
        }

//        // ////////////////////////Robin's test area//////////////////////
//        byte dataToSend[] = new byte[]
//        {
//            0x03
//        };
//        System.out.println("///////////////////////////");
//        System.out.println("Starting to send data");
//        I2CH.sendI2CData(dataToSend);
//        System.out.println("Data is sent");
//        System.out.println("///////////////////////////");
////        while (true)
////        {
//
////            try
////            {
////                System.out.println("Creatingbus");
////                I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);
////                System.out.println("Creatingdevice");
////                I2CDevice device = bus.getDevice(ARDUINO_DUMMY_SIGNAL_ADDRESS);
////
////                device.write((byte) 0x03);
////                 Thread.sleep(200);
////                //Thread.sleep(2000);
////                System.out.println("Signal is sent");
////                int r = device.read(inputData);
////                System.out.println("Data recieved is: " + r);
//////                if (r == 0x09)
//////                {
//////                    System.out.println("Data recieved is: " + r);
//////                } else
//////                {
//////                    System.out.println("Data was not 0x09");
//////                }
////
////                Thread.sleep(5000);
////                device.write((byte) 0x00);
////                System.out.println("Reset signal is sent");
////                Thread.sleep(5000);
////
////            } catch (Exception e)
////            {
////            }
////        }
//////        while (!testIsDone)
//////        {
//////            System.out.println("Acquire access to SerialRW");
//////            try
//////            {
//////               semSerial.acquire();
//////            } catch (Exception e)
//////            {
//////                System.out.println(e + "Failed to acquire access to SerialRW semaphore");
//////            }
//////            System.out.println("Reading counter = " + dh.getCounter());
//////            System.out.println("Seting counter to: 5" );
//////            dh.setCounter(5);
//////            System.out.println("Reading counter = " + dh.getCounter());
//////            
//////            System.out.println("Releasing access to SerialRW");
//////            semSerial.release();
//////            System.out.println("Done releasing");
//////            testIsDone = true;
//////        }
//    
//
//    // ////////////////////////End of Robin's test area//////////////////////
    }

    /*
 * In this class must implement the method serialEvent, through it we learn about 
 * events that happened to our port. But we will not report on all events but only 
 * those that we put in the mask. In this case the arrival of the data and change the 
 * status lines CTS and DSR
     */
    static class SerialPortReader implements SerialPortEventListener
    {

        @Override
        public void serialEvent(SerialPortEvent event)
        {
            if (event.isRXCHAR())
            {//If data is available
                if (event.getEventValue() >= 10)
                {
//Check bytes count in the input buffer
//Read data, if 10 bytes available 
                    try
                    {
                        System.out.println("Resiving data");
                        String buffer = serialPort.readString();
                        String dataStream = buffer;
                        System.out.println("Data is:" + dataStream);
                        dataIsRecieved = true;
                    } catch (SerialPortException ex)
                    {
                        System.out.println(ex);
                    }
                }

            }
        }
    }
}
