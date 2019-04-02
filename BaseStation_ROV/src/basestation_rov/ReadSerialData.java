/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basestation_rov;

import java.util.concurrent.ConcurrentHashMap;
import jssc.SerialPort;
import jssc.SerialPortList;
import jssc.SerialPortException;

/**
 * Respnsible for reading serial data from the GPS, Sonar and IMU values on the
 * base station
 *
 * @author <Bjørnar M. Tennfjord>
 */
public class ReadSerialData implements Runnable
{

    boolean portIsOpen = false;
    String comPort = "";
    int baudRate = 0;
    DataHandler dh = null;

    private static volatile double depth;
    private static volatile double tempC;

    public ReadSerialData(DataHandler dh, String comPort, int baudRate)
    {
        this.comPort = comPort;
        this.baudRate = baudRate;
        this.dh = dh;
    }

    @Override
    public void run()
    {
        while (true)
        {
            readData(comPort, baudRate);
        }
    }

    public String[] getAvailableComPorts()
    {
        String[] portNames = SerialPortList.getPortNames();

        if (portNames.length == 0)
        {
            System.out.println("There are no serial-ports available!");
            System.out.println("Press enter to exit...");

            try
            {
                System.in.read();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < portNames.length; i++)
        {
            System.out.println(portNames[i]);
        }
        return portNames;
    }

    public void sendDepth()
    {
        dh.set_Depth((float) depth);
    }

    public void sendTempC()
    {
        dh.set_Temperature((float) tempC);
    }

    public void readData(String comPort, int baudRate)
    {

        // long lastTime = System.nanoTime();
        ConcurrentHashMap<String, String> SerialDataList = new ConcurrentHashMap<>();

        boolean recievedData = false;
        //Declare special symbol used in serial data stream from Arduino
        String startChar = "<";
        String endChar = ">";
        String seperationChar = ":";

        SerialPort serialPort = new SerialPort(comPort);

        if (!portIsOpen)
        {
            try
            {
                serialPort.openPort();
                portIsOpen = true;
                System.out.println(comPort + " is open");
            } catch (SerialPortException ex)
            {
                System.out.println(ex);
            }
        }

        while (recievedData == false)
        {
            try
            {
                Thread.sleep(400);
            } catch (Exception ex)
            {

            }
            String buffer;

            try
            {
                serialPort.setParams(baudRate, 8, 1, 0);
                buffer = serialPort.readString();
               


                System.out.println(buffer);

                boolean dataNotNull = false;
                boolean dataHasFormat = false;
                
                if ((buffer != null))
                {
                    dataHasFormat = true;
                }
                else
                {
                    dataHasFormat = false;
                    dataNotNull = false;
         

                }

                if (dataHasFormat)
                {
                    String dataStream = buffer;

                    dataStream = dataStream.substring(dataStream.indexOf(startChar) + 1);
                    dataStream = dataStream.substring(0, dataStream.indexOf(endChar));
                    dataStream = dataStream.replace("?", "");
                    String[] data = dataStream.split(seperationChar);

                    for (int i = 0; i < data.length; i = i + 2)
                    {
                        dh.data.put(data[i], data[i + 1]);
                    }

                    recievedData = true;
                    dh.handleDataFromRemote();
                }
                
//            if (elapsedTimer != 0)
//            {
//                
//                System.out.println("Data is recieved in: " + elapsedTimer + " millis"
//                        + " or with: " + 1000 / elapsedTimer + " Hz");
//            } else
//            {
//                System.out.println("Data is recieved in: " + elapsedTimer + " millis"
//                        + " or with: unlimited Hz!");
//            }
            } catch (Exception ex)
            {

            }

        }

    }

}
