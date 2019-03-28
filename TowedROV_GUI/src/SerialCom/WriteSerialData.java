/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SerialCom;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import jssc.SerialPort;
import jssc.SerialPortException;
import ntnusubsea.gui.Data;

/**
 *
 * @author rocio
 */
public class WriteSerialData implements Runnable
{

    Data dh;
    //SerialDataHandler sdh;
    String comPort;
    int baudRate;
    String data;
    SerialPort serialPort;

    /**
     *
     * @param dh
     * @param sdh
     * @param comPort
     * @param baudRate
     */
//    public WriteSerialData(DataHandler dh, SerialDataHandler sdh, String comPort, int baudRate)
//    {
//        this.dh = dh;
//        this.sdh = sdh;
//        this.comPort = comPort;
//        this.baudRate = baudRate;
//        this.data = data;
//
//    }

    @Override
    public void run()
    {
        serialPort = new SerialPort(comPort);
        try
        {
            serialPort.openPort();
            serialPort.setParams(baudRate, 8, 1, 0);
            try
            {
                Thread.sleep(500);
            } catch (Exception e)
            {
            }

        } catch (SerialPortException ex)
        {
            System.out.println(ex);
        }
        while (true)
        {

//            if (dh.isDataToRemoteUpdated())
//            {
//                ConcurrentHashMap<String, String> tempList = new ConcurrentHashMap<>();
//                tempList.entrySet().addAll(dh.dataToRemote.entrySet());
//
//                for (Map.Entry e : tempList.entrySet())
//                {
//                    String key = (String) e.getKey();
//                    String value = (String) e.getValue();
//                    String data = ("<" + key + ":" + value + ">");
//                    try
//                    {
//                        Thread.sleep(10);
//                    } catch (Exception x)
//                    {
//                    }
//                    this.write(comPort, 9600, data);
//                    //wsd.writeData("Com3", arduinoBaudRate, data);
//                }
//                dh.setDataToRemoteUpdated(false);
//            }
        }
    }

    /**
     *
     * @param comPort
     * @param baudRate
     * @param data
     */
    public void write(String comPort, int baudRate, String data)
    {
        //Declare Special Symbol Used in Serial Data Stream from Arduino
        String start_char = "<";
        String end_char = ">";
        String sep_char = ":";
        //Define Serial Port # -- can be found in Device Manager or Arduino IDE

        try
        {

            String stringData = data;

//            for (int i = 0; i <= serialDataList.size(); i++)
//            {
//                stringData = stringData + serialDataList.
//            }
            serialPort.writeString(stringData);
            System.out.println("Data is sent");

        } catch (Exception ex)
        {
            System.out.println(ex + "Error while sending data over serial");
        }

        try
        {
            //serialPort.closePort();
        } catch (Exception e)
        {
            System.err.println(e);
        }
    }
}
