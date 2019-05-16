/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SerialCom;

import ROV.Data;
import java.util.HashMap;
import java.util.Map.Entry;
import jssc.SerialPort;
import jssc.SerialPortList;

/**
 * This class is resposible for handling the search for com ports and store
 * a list over them. 
 */
public class SerialDataHandler
{

    private HashMap<String, String> portNamesList = new HashMap<>();

    String comPort = "";
    SerialPort serialPort;
    Data data;

    String start_char = "<";
    String end_char = ">";
    String sep_char = ":";

    /**
     * The constructor for the SerialDataHandler class
     * @param data the shared recource data class
     */
    public SerialDataHandler(Data data)
    {
        this.data = data;
    }


   

    private void saveUsableComPorts()
    {
        int comCheck = 0;
        for (Entry e : portNamesList.entrySet())
        {
            String comPortKey = (String) e.getKey();
            String comPortValue = (String) e.getValue();
            if (!comPortValue.contains("Unknown"))
            {
                data.comPortList.put(comPortKey, comPortValue);
//                comCheck++;
            }
        }
//        if (comCheck < 3)
//        {
//            //Not all comports was found
//            System.out.println("ERROR: Not all com ports was found, trying again...");
//            findComPorts();
//        }

    }

    /**
     * This method is responsible or finding the desired com ports.
     * Since the com ports may change we have to search and store which com
     * port is connected to which device
     */
    public void findComPorts()
    {
        int baudrate = 0;
        int searchRuns = 0;

        String[] portNames = getAvailableComPorts();
        for (int i = 0; i < portNames.length; i++)
        {
            // 
            if (portNames[i].contains("dev") && !portNames[i].contains("AMA0"))
            {
                portNamesList.put(portNames[i], "Unknown");
            }
        }
        while (searchRuns != 3)
        {

            if (searchRuns == 0)
            {

                baudrate = 38400;
            }
            if (searchRuns == 1)
            {
                baudrate = 4800;
            }
            if (searchRuns == 2)
            {
                baudrate = 115200;
            }

            for (Entry e : portNamesList.entrySet())
            {

                String comPortKey = (String) e.getKey();
                String comPortValue = (String) e.getValue();
                if (comPortValue.contains("Unknown"))
                {
                    serialPort = new SerialPort(comPortKey);

                    try
                    {
                        serialPort.openPort();
                        serialPort.setParams(baudrate, 8, 1, 0);
                        String buffer = "";
                        Thread.sleep(5000);
                        buffer = serialPort.readString();

                        if (buffer != null)
                        {

                            if (buffer.contains("<") && buffer.contains(">"))
                            {
                                buffer = buffer.substring(buffer.indexOf(start_char) + 1);
                                buffer = buffer.substring(0, buffer.indexOf(end_char));
                                //  buffer = buffer.replace("?", "");
                                String[] data = buffer.split(sep_char);

                                for (int i = 0; i < data.length; i = i + 2)
                                {
                                    if (data[i].contains("Roll"))
                                    {
                                        String key = (String) e.getKey();
                                        portNamesList.put(key, "IMU");
                                    }
                                    if (data[i].contains("EchoSounder"))
                                    {
                                        String key = (String) e.getKey();
                                        portNamesList.put(key, "EchoSounder");
                                    }
                                    if (data[i].contains("ActuatorFBArduino")
                                            || data[i].contains("PsActuatorFb"))
                                    {
                                        String key = (String) e.getKey();
                                        portNamesList.put(key, "ActuatorFBArduino");
                                    }
//                                if (data[i].contains("EchoSounder"))
//                                {
//                                    String key = (String) e.getKey();
//                                    portNamesList.put(key, "EchoSounder");
//                                }
                                }

                            }
                        }
                        serialPort.closePort();

                    } catch (Exception ex)
                    {
                        try
                        {
                            serialPort.closePort();
                        } catch (Exception exep)
                        {
                        }

                    }
                }
            }
            searchRuns++;
        }
        saveUsableComPorts();

    }

    private String[] getAvailableComPorts()
    {
        // getting serial ports list into the array

        String[] portNames = SerialPortList.getPortNames();

        if (portNames.length == 0)
        {
            System.out.println("There are no serial-ports :( You can use an emulator, such ad VSPE, to create a virtual serial port.");
            System.out.println("Press Enter to exit...");
            try
            {
                System.in.read();
            } catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return portNames;
    }
}
