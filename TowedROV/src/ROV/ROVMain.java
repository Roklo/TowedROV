/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ROV;

import ROV.SerialCom.SerialRW;
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
    static boolean dataIsRecieved = false;
    static boolean testIsDone = false;
    static SerialPort serialPort;
    protected static DataHandler dh;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        dh = new DataHandler();
        Semaphore semSerial = new Semaphore(1);
        serialRW = new Thread(new SerialRW(semSerial));

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

        while (!testIsDone)
        {
            System.out.println("Acquire access to SerialRW");
            try
            {
               semSerial.acquire();
            } catch (Exception e)
            {
                System.out.println(e + "Failed to acquire access to SerialRW semaphore");
            }
            System.out.println("Reading counter = " + dh.getCounter());
            System.out.println("Seting counter to: 5" );
            dh.setCounter(5);
            System.out.println("Reading counter = " + dh.getCounter());
            
            System.out.println("Releasing access to SerialRW");
            semSerial.release();
            System.out.println("Done releasing");
            testIsDone = true;
        }
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

//    public void writeToSeriell()
//    {
//        if (dataIsRecieved)
//        {
//            ConcurrentHashMap<String, String> tempList = new ConcurrentHashMap<>();
//            tempList.entrySet().addAll(dh.dataToRemote.entrySet());
//
//            for (Map.Entry e : tempList.entrySet())
//            {
//                String key = (String) e.getKey();
//                String value = (String) e.getValue();
//                String data = ("<" + key + ":" + value + ">");
//                try
//                {
//                    Thread.sleep(10);
//                } catch (Exception x)
//                {
//                }
//                this.write(comPort, 9600, data);
//                //wsd.writeData("Com3", arduinoBaudRate, data);
//
//            }
//        }
//    }
}
