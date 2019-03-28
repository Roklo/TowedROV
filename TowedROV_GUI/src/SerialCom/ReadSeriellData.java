/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SerialCom;

import ntnusubsea.gui.Data;
import java.util.concurrent.ConcurrentHashMap;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

/**
 *
 * @author rocio
 */
public class ReadSeriellData implements Runnable
{

    boolean portIsOpen = false;
    String comPort = "";
    int baudRate = 0;
    SerialPort serialPort;

    Data dh = null;

    /**
     *
     * @param dh
     * @param sdh
     * @param comPort
     * @param baudRate
     */
    public ReadSeriellData(Data dh, String comPort, int baudRate)
    {
        this.comPort = comPort;
        this.baudRate = baudRate;
        this.dh = dh;
       
    }

    @Override
    public void run()
    {
        serialPort = new SerialPort(comPort);
        while (true)
        {
            readData(comPort, baudRate);
        }

    }

    /**
     *
     * @return
     */
    public String[] getAvailableComPorts()
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

        for (int i = 0; i < portNames.length; i++)
        {
            System.out.println(portNames[i]);
        }
        return portNames;
    }

    /**
     *
     * @param comPort
     * @param baudRate
     */
    public void readData(String comPort, int baudRate)
    //ConcurrentHashMap
    {
        long lastTime = System.nanoTime();
        ConcurrentHashMap<String, String> SerialDataList;
        SerialDataList = new ConcurrentHashMap<String, String>();

        boolean receivedData = false;
        //Declare Special Symbol Used in Serial Data Stream from Arduino
        String start_char = "<";
        String end_char = ">";
        String sep_char = ":";
        //Define Serial Port # -- can be found in Device Manager or Arduino IDE

        if (!portIsOpen)
        {
            try
            {
                serialPort.openPort();
                serialPort.setParams(baudRate, 8, 1, 0);
                portIsOpen = true;
                System.out.println(comPort + " is open");
            } catch (SerialPortException ex)
            {
                System.out.println(ex);
            }
        }

        while (receivedData == false)
        {
            // sdh.comPorts.put(comPort, true);
            //byte[] buffer = null;
            String buffer = "";

            try
            {
                //serialPort.setParams(9600, 8, 1, 0);

                //serialPort.setParams(baudRate, 8, 1, 0);
                buffer = serialPort.readString();
                buffer = "";
                try
                {
                    Thread.sleep(200);
                } catch (InterruptedException ex)
                {
                    System.out.println("Error insomnia");
                    // Thread.currentThread().interrupt();
                }

                buffer = serialPort.readString();

                boolean dataNotNull = false;
                boolean dataHasFormat = false;

                while (!dataHasFormat && !dataNotNull)
                {

                    while (buffer == null)
                    {
                        dataNotNull = false;
                        buffer = "";
                        try
                        {
                            buffer = serialPort.readString();
                        } catch (Exception e)
                        {
                            System.out.println("error");
                        }
                    }

                    dataNotNull = true;
                    boolean endWhile = false;
                    while (buffer != null && !endWhile)
                    {
                        if (!buffer.contains("<") || !buffer.contains(">"))
                        {
                            dataHasFormat = false;
                            buffer = "";
                            try
                            {
                                try
                                {
                                    Thread.sleep(10);
                                } catch (Exception e)
                                {
                                    System.out.println("Exception: insomnia");
                                }

                                buffer = serialPort.readString();

                            } catch (Exception e)
                            {
                                System.out.println("error");
                            }
                        }
                        endWhile = true;
                    }

                    if (buffer != null)
                    {
                        dataHasFormat = true;
                    } else
                    {
                        dataHasFormat = false;
                        dataNotNull = false;
                    }

                }

                dataNotNull = false;
                dataHasFormat = false;

                String dataStream = buffer;
//                String dataStream = new String(buffer);

                dataStream = dataStream.substring(dataStream.indexOf(start_char) + 1);
                dataStream = dataStream.substring(0, dataStream.indexOf(end_char));
                dataStream = dataStream.replace("?", "");
                String[] data = dataStream.split(sep_char);

                for (int i = 0; i < data.length; i = i + 2)
                {

                    dh.data.put(data[i], data[i + 1]);
                    //SerialDataList.put(data[i], data[i + 1]);
                    //System.out.println("Key: " + data[i] + "     Value:" + data[i + 1]);                   
                }
                receivedData = true;
                //dh.handleDataFromRemote();

                long elapsedTimer = (System.nanoTime() - lastTime) / 1000000;
                if (elapsedTimer != 0)
                {
                   // dh.setComResponseTime(elapsedTimer);
                    System.out.println("Data is gøtt'n in: " + elapsedTimer + " millis"
                            + " or with: " + 1000 / elapsedTimer + " Hz");
                } else
                {
                    System.out.println("Data is gøtt'n in: " + elapsedTimer + " millis"
                            + " or with: unlimited Hz!");
                }
                //sdh.comPorts.put(comPort, true);
            } catch (Exception ex)
            {
                System.out.println("1 " + ex);
            }

        }

//        try
//        {
//            serialPort.closePort();
//        } catch (Exception e)
//        {
//            System.err.println(e);
//            System.out.println("Error");
//        }
        // return SerialDataList;
    }
}
