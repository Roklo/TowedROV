/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SerialCom;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import jssc.SerialPort;
import jssc.SerialPortList;
import jssc.SerialPortException;
import ROV.DataHandler;

/**
 * Respnsible for reading serial data from the GPS, Sonar and IMU values on the
 * base station
 *
 * @author <Bjørnar M. Tennfjord>
 */
public class ReadSerialData implements Runnable
{

    // Filter values
    int actuatorFbFilter = 1; // 1 equals off

    int actuatorPSFbFilterStorage = 0;
    int actuatorSBFbFilterStorage = 0;

    int actuatorPSFbFilterCounter = 0;
    int actuatorSBFbFilterCounter = 0;

    boolean portIsOpen = false;
    String comPort = "";
    int baudRate = 0;
    DataHandler data = null;

    public HashMap<String, String> incommingData = new HashMap<>();

    private static volatile double depth;
    private static volatile double tempC;

    public ReadSerialData(DataHandler data, String comPort, int baudRate)
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
        // data.setDepth((float) depth);
    }

    public void sendTempC()
    {
        //data.setTemperature((float) tempC);
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
                System.out.println(ex);
            }
        }

        while (recievedData == false)
        {
            try
            {
                Thread.sleep(50);
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

                case "D":
                    data.setFb_depthToSeabedEcho(Double.parseDouble(value));
                    break;
                case "DBT":
                    data.setFb_depthBelowTransduser(Double.parseDouble(value));
                    break;
                case "ch1":
                    data.setAnalogInputChannel_1(Double.parseDouble(value));
                    break;
                case "ch2":
                    data.setAnalogInputChannel_2(Double.parseDouble(value));
                    break;

                case "ch3":
                    if (value.equals("1.00"))
                    {
                        data.setDigitalInputChannel_3(true);
                    } else
                    {
                        data.setDigitalInputChannel_3(false);
                    }
                    break;
                case "ch4":
                    if (value.equals("1.00"))
                    {
                        data.setDigitalInputChannel_4(true);
                    } else
                    {
                        data.setDigitalInputChannel_4(false);
                    }
                    break;

                case "PsActuatorFb":
                    int tempValuePs = Integer.parseInt(value);
                    if (tempValuePs < 0)
                    {
                        tempValuePs = 0;
                    }
                    if (tempValuePs > 254)
                    {
                        tempValuePs = 254;
                    }

                    if (actuatorPSFbFilterCounter <= actuatorFbFilter)
                    {
                        actuatorPSFbFilterStorage = actuatorPSFbFilterStorage + tempValuePs;
                        actuatorPSFbFilterCounter++;

                    } else
                    {
                        actuatorPSFbFilterStorage = actuatorPSFbFilterStorage / actuatorFbFilter;
                        actuatorPSFbFilterCounter = 0;

                        data.setFb_actuatorPSPos(tempValuePs);
                    }
                    break;

                case "SbActuatorFb":
                    int tempValueSb = Integer.parseInt(value);
                    if (tempValueSb < 0)
                    {
                        tempValueSb = 0;
                    }
                    if (tempValueSb > 254)
                    {
                        tempValueSb = 254;
                    }

                    if (actuatorSBFbFilterCounter <= actuatorFbFilter)
                    {
                        actuatorSBFbFilterStorage = actuatorSBFbFilterStorage + tempValueSb;
                        actuatorSBFbFilterCounter++;

                    } else
                    {
                        actuatorSBFbFilterStorage = actuatorSBFbFilterStorage / actuatorFbFilter;
                        actuatorSBFbFilterCounter = 0;

                        data.setFb_actuatorSBPos(tempValueSb);
                    }
                    break;

                case "Roll":
                    data.setFb_roll(Integer.parseInt(value));
                    //setRoll(Integer.parseInt(value));
                    break;
                case "Pitch":
                    data.setFb_pitch(Integer.parseInt(value));
                    //setPitch(Integer.parseInt(value));
                    break;
                case "Heading":
                    data.setFb_heading(Integer.parseInt(value));
                    //setHeading(Integer.parseInt(value));
                    break;

                case "tmp1":
                    data.setFb_tempMainElBoxFront(Double.parseDouble(value));
                    break;

                case "tmp2":
                    data.setFb_tempMainElBoxRear(Double.parseDouble(value));
                    break;


                /*
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
                 */
            }
        }

    }
}
