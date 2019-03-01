/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package I2CCom;

import ROV.DataHandler;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author <Robin S. Thorholm>
 */
public class I2CRW implements Runnable
{

    protected static I2CHandler I2CH;
    protected static DataHandler dh;

    I2CDevice arduinoDummy;
    I2CDevice actuatorSB;
    I2CDevice actuatorPS;
    I2CDevice pressureSensor;

    private final static int ARDUINO_DUMMY_SIGNAL_ADDRESS = 0x09;
    private final static int pressureSensor_ADDRESS = 0x76;
    private final static int actuatorSB_ADDRESS = 11;
    private final static int actuatorPS_ADDRESS = 12;

    //Polulu JRK drive commands  
    private final static int setTarget = 0xC0;  //Sets the desired position
    private final static int motorOff = 0xFF;   //Stops the motor in its current pos

    String start_char = "<";
    String end_char = ">";
    String sep_char = ":";

    public I2CRW(I2CHandler I2CH, DataHandler dh)
    {

        this.I2CH = I2CH;
        this.dh = dh;

    }

    @Override
    public void run()
    {
        //Instanciating I2C bus and add all slaves
        try
        {
            //System.out.println("Creatingbus");
            I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);
            //System.out.println("Creatingdevices");
            arduinoDummy = bus.getDevice(ARDUINO_DUMMY_SIGNAL_ADDRESS);
            actuatorSB = bus.getDevice(actuatorSB_ADDRESS);
            actuatorPS = bus.getDevice(actuatorPS_ADDRESS);
            pressureSensor = bus.getDevice(pressureSensor_ADDRESS);

        } catch (Exception e)
        {
            System.out.println("ERROR: Failed to start I2C bus : " + e);

        }

        while (true)
        {
            try
            {
                if (I2CH.getI2cSendRequest())
                {
                    byte[] dataByteArray = I2CH.getCurrentDataByteArray();
                    String command = I2CH.getCommand();
                    int value = I2CH.getValue();
                    String key = command;

                    switch (key)
                    {
                        case "ActuatorSB_setTarget":
                            actuatorSB.write(setTarget, (byte)value);
                            break;
                        case "ActuatorSB_stopMotor":
                            actuatorSB.write(motorOff, (byte)0x00);
                            break;
                    }

//                    for (byte x : dataByteArray)
//                    {
//                        arduinoDummy.write((byte) x);
//                        //Thread.sleep(1);
//                    }

//                    device.write((byte) 0x03);
//                    Thread.sleep(200);
//                    //Thread.sleep(2000);
//                    System.out.println("Signal is sent");
//                    int r = device.read(inputData);
//                    System.out.println("Data recieved is: " + r);
//
//                    Thread.sleep(5000);
//                    device.write((byte) 0x00);
//                    System.out.println("Reset signal is sent");
//                    Thread.sleep(5000);
                    I2CH.setI2cSendRequest(false);
                }
            } catch (Exception e)
            {
                System.out.println(e + " : Unable to create I2C Bus");
            }

            try
            {
                if (I2CH.getDataRequest())
                {
                    byte[] inputDataRaw = new byte[32];
                    String key = I2CH.getDataRequestDevice();
                    String dataRecieved = "";
                    switch (key)
                    {
                        case "ActuatorSB_Feedback":
                            break;
                        case "ActuatorPS_Feedback":
                            break;
                        case "PressureSensor":
                            byte[] value = new byte[2];
                            pressureSensor.read(0xAA, value, 0, 2);

                            pressureSensor.write((byte) 0x01);
                            Thread.sleep(50);

                            break;
                        case "ArduinoIO":
                            arduinoDummy.read(inputDataRaw, 0, 32);
                            int sizeOfRecievedData = 0;

                            for (byte b : inputDataRaw)
                            {
                                if (b != -1)
                                {
                                    sizeOfRecievedData++;
                                }
                            }

                            byte[] inputData = new byte[sizeOfRecievedData];
                            System.arraycopy(inputDataRaw, 0, inputData, 0, sizeOfRecievedData);

                            dataRecieved = new String(inputData);

                            break;

                    }
                    dataRecieved = dataRecieved.substring(dataRecieved.indexOf(start_char) + 1);
                    dataRecieved = dataRecieved.substring(0, dataRecieved.indexOf(end_char));
                    String[] data = dataRecieved.split(sep_char);
                    String[] valueNames = new String[7];
                    valueNames[0] = "fb_depthToSeabed";
                    valueNames[1] = "fb_speedThroughWather";
                    valueNames[2] = "AuxIn1";
                    valueNames[3] = "AuxIn2";
                    valueNames[4] = "AuxIn3";
                    valueNames[5] = "AuxIn4";
                    valueNames[6] = "AuxIn5";

                    for (int i = 0; i < data.length; i++)
                    {

                        dh.data.put(valueNames[i], data[i]);
                        //SerialDataList.put(data[i], data[i + 1]);
                        //System.out.println("Key: " + data[i] + "     Value:" + data[i + 1]);                   
                    }

                    dh.handleDataFromI2C();
                    I2CH.setDataRequest(false);

                }
            } catch (IOException | InterruptedException e)
            {
                System.out.println("Exception" + e);
            }

        }
    }
}
