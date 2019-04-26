/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basestation_rov;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import jssc.SerialPort;
import jssc.SerialPortList;
import jssc.SerialPortException;
import ntnusubsea.gui.Data;

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
    Data data = null;

    public HashMap<String, String> incommingData = new HashMap<>();

    private static volatile double depth;
    private static volatile double tempC;

    public ReadSerialData(Data data, String comPort, int baudRate)
    {
        this.comPort = comPort;
        this.baudRate = baudRate;
        this.data = data;
    }

    @Override
    public void run()
    {
        while (true)
        {
            try
            {

                readData(comPort, baudRate);
            } catch (Exception e)
            {
            }

        }
    }

    public String[] getAvailableComPorts()
    {
        String[] portNames = SerialPortList.getPortNames();

        if (portNames.length == 0)
        {
            // System.out.println("There are no serial-ports available!");
            // System.out.println("Press enter to exit...");

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
            //System.out.println(portNames[i]);
        }
        return portNames;
    }

    public void sendDepth()
    {
        data.setDepth((float) depth);
    }

    public void sendTempC()
    {
        data.setTemperature((float) tempC);
    }

    public void readData(String comPort, int baudRate)
    {

        // long lastTime = System.nanoTime();
//        ConcurrentHashMap<String, String> SerialDataList = new ConcurrentHashMap<>();
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
                // System.out.println(comPort + " is open");
            } catch (SerialPortException ex)
            {
                System.out.println(ex.getMessage());
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

                // System.out.println(buffer);
                boolean dataNotNull = false;
                boolean dataHasFormat = false;

                if ((buffer != null))
                {
                    dataHasFormat = true;
                } else
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
                        //this.data.data.put(data[i], data[i + 1]);
                        incommingData.put(data[i], data[i + 1]);

                    }

                    //recievedData = true;
                    //this.data.handleDataFromRemote();
                    sendIncommingDataToDataHandler();
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

    private void sendIncommingDataToDataHandler()
    {
        for (Map.Entry e : incommingData.entrySet())
        {
            String key = (String) e.getKey();
            String value = (String) e.getValue();

            switch (key)
            {
                case "Satellites":
                    data.setSatellites(Integer.parseInt(value));
                    // setSatellites(Integer.parseInt(value));
                    break;
                case "Altitude":
                    data.setAltitude(Float.parseFloat(value));
                    //setAltitude(Float.parseFloat(value));
                    break;
                case "Angle":
                    data.setAngle(Float.parseFloat(value));
                    //setAngle(Float.parseFloat(value));
                    break;
                case "Speed":
                    data.setSpeed(Float.parseFloat(value));
                    //setSpeed(Float.parseFloat(value));
                    break;
                case "Latitude":
                    data.setLatitude(Float.parseFloat(value));
                    //setLatitude(Float.parseFloat(value));
                    break;
                case "Longitude":
                    data.setLongitude(Float.parseFloat(value));
                    //setLongitude(Float.parseFloat(value));
                    break;
                case "Depth":
                    data.setDepth(Float.parseFloat(value));
                    //setDepth(Float.parseFloat(value));
                    break;
                case "Temp":
                    data.setTemperature(Float.parseFloat(value));
                    //setTemperature(Float.parseFloat(value));
                    break;
                case "Roll":
                    data.setRoll(Integer.parseInt(value));
                    //setRoll(Integer.parseInt(value));
                    break;
                case "Pitch":
                    data.setPitch(Integer.parseInt(value));
                    //setPitch(Integer.parseInt(value));
                    break;
                case "Heading":
                    data.setHeading(Integer.parseInt(value));
                    //setHeading(Integer.parseInt(value));
                    break;
                case "Volt":
                    data.setVoltage(Float.parseFloat(value));
                    break;

            }
        }

    }
}
