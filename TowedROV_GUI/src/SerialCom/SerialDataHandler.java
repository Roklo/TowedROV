/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SerialCom;

import ntnusubsea.gui.Data;
import java.util.HashMap;
import java.util.Map.Entry;
import jssc.SerialPort;
import jssc.SerialPortList;

/**
 *
 * @author <Robin S. Thorholm>
 */
public class SerialDataHandler
{

    private HashMap<String, String> portNamesList = new HashMap<>();

    String comPort = "";
    SerialPort serialPort;
    Data dh;

    String start_char = "<";
    String end_char = ">";
    String sep_char = ":";

    public SerialDataHandler(Data dh)
    {
        this.dh = dh;
    }

    public void initiateComPorts()
    {

    }

    private void saveUsableComPorts()
    {
        for (Entry e : portNamesList.entrySet())
        {
            String comPortKey = (String) e.getKey();
            String comPortValue = (String) e.getValue();
            if (!comPortValue.contains("Unknown"))
            {
                dh.comPortList.put(comPortKey, comPortValue);
            }
        }

    }

    public void findComPorts()
    {
        String[] portNames = getAvailableComPorts();
        for (int i = 0; i < portNames.length; i++)
        {
            portNamesList.put(portNames[i], "Unknown");
        }

        for (Entry e : portNamesList.entrySet())
        {
            String comPortKey = (String) e.getKey();
            serialPort = new SerialPort(comPortKey);
            try
            {
                serialPort.openPort();
                serialPort.setParams(9600, 8, 1, 0);
                String buffer = "";
                Thread.sleep(50);
                buffer = serialPort.readString();

                if (buffer != null)
                {

                    if (buffer.contains("<") && buffer.contains(">"))
                    {
                        buffer = buffer.substring(buffer.indexOf(start_char) + 1);
                        buffer = buffer.substring(0, buffer.indexOf(end_char));
                        buffer = buffer.replace("?", "");
                        String[] data = buffer.split(sep_char);

                        for (int i = 0; i < data.length; i = i + 2)
                        {
                            if (data[i].contains("Roll"))
                            {
                                String key = (String) e.getKey();
                                portNamesList.put(key, "IMU");
                            }
                            if (data[i].contains("Latitude"))
                            {
                                String key = (String) e.getKey();
                                portNamesList.put(key, "GPS");
                            }
                            if (data[i].contains("Depth"))
                            {
                                String key = (String) e.getKey();
                                portNamesList.put(key, "EchoSounder");
                            }
                        }
                        serialPort.closePort();

                    }
                }

            } catch (Exception ex)
            {

            }
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

//        for (int i = 0; i < portNames.length; i++)
//        {
//            System.out.println(portNames[i]);
//        }
        return portNames;
    }
}
