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
import ROV.Data;

/**
 * This class is eespnsible for reading serial data from the IMU, echo sounder
 * and the Arduino I/O
 *
 *
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
    String myName = "";
    int baudRate = 0;
    Data data = null;

    /**
     * The complete list of alla incomming data and its values
     */
    public HashMap<String, String> incommingData = new HashMap<>();

    private static volatile double depth;
    private static volatile double tempC;

    /**
     *
     * @param data the shared recource data class
     * @param comPort the com port it should use
     * @param baudRate the baud rate of the com port
     * @param myName the name of the com device it should connect to
     */
    public ReadSerialData(Data data, String comPort, int baudRate, String myName)
    {
        this.comPort = comPort;
        this.myName = myName;
        this.baudRate = baudRate;
        this.data = data;
    }

    /**
     * Run command loops through the readData
     */
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

    /**
     * readData is responsible for gathering data from the serial devices
     *
     * @param comPort the com port it should connect to
     * @param baudRate the boud rate of the com port
     */
    public void readData(String comPort, int baudRate)
    {

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
                // System.out.println("Lost connection to " + myName);
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
                    double doubleValue = Double.parseDouble(value) * -1;
                    data.setFb_depthBeneathROV(doubleValue);
                    break;
//                case "DBT":
//                    data.setFb_depthBelowTransduser(Double.parseDouble(value));
//                    break;
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
                    data.setFb_rollAngle(Double.parseDouble(value));
                    //setRoll(Integer.parseInt(value));
                    break;
                case "Pitch":
                    data.setFb_pitchAngle(Double.parseDouble(value));
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
            }
        }
    }
}
