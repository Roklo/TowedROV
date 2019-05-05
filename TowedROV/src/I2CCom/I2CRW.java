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
    protected static DataHandler data;

    I2CDevice arduinoIO;
    I2CDevice actuatorSB;
    I2CDevice actuatorPS;

    //User settings
    //SB and PS 180 target equals 0 degrees wing pos
    private final static int PS_ACTUATOR_SPEED = 125;
    private final static int SB_ACTUATOR_SPEED = 50;

    //Polulu JRK drive commands 
    private final static int PS_ACTUATOR_ADDRESS = 0x10;
    private final static int SB_ACTUATOR_ADDRESS = 0x0F;
    private final static int ACTUATOR_STOP = 0xFF;

    //Arduino Address
    private final static int ARDUINO_IO_ADDRESS = 0x0B;

    //JRK commands 
    int JRK_setTargetLowResRev = 0xE0;
    int JRK_setTargetLowResFwd = 0xE1;
    int JRK_getScaledFeedback = 0xA7; //The low byte of “Feedback”

    String start_char = "<";
    String end_char = ">";
    String sep_char = ":";

    public I2CRW(DataHandler data)
    {

        this.I2CH = I2CH;

        this.data = data;

        try
        {
            //System.out.println("Creatingbus");
            I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_3);
            //System.out.println("Creatingdevices");
            arduinoIO = bus.getDevice(ARDUINO_IO_ADDRESS);
            actuatorSB = bus.getDevice(SB_ACTUATOR_ADDRESS);
            actuatorPS = bus.getDevice(PS_ACTUATOR_ADDRESS);

        } catch (Exception e)
        {
            System.out.println("Failed to instansiate I2 Bus");
        }

    }

    private void initiateI2Cbus()
    {
        try
        {
            //System.out.println("Creatingbus");
            I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_3);
            //System.out.println("Creatingdevices");
            arduinoIO = bus.getDevice(ARDUINO_IO_ADDRESS);
            actuatorSB = bus.getDevice(SB_ACTUATOR_ADDRESS);
            actuatorPS = bus.getDevice(PS_ACTUATOR_ADDRESS);
        } catch (Exception e)
        {
        }

    }

    @Override
    public void run()
    {
        while (true)
        {

        }
    }

//    @Override
//    public void run()
//    {
//        //Instanciating I2C bus and add all slaves
//        try
//        {
//            //System.out.println("Creatingbus");
//            I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);
//            //System.out.println("Creatingdevices");
//            arduinoIO = bus.getDevice(ARDUINO_IO_ADDRESS);
//            actuatorSB = bus.getDevice(SB_ACTUATOR_ADDRESS);
//            actuatorPS = bus.getDevice(PS_ACTUATOR_ADDRESS);
//
//        } catch (Exception e)
//        {
//            System.out.println("ERROR: Failed to start I2C bus : " + e);
//
//        }
//        while (true)
//        {
//            try
//            {
//                if (I2CH.getI2cSendRequest())
//                {
//                    byte[] dataByteArray = I2CH.getCurrentDataByteArray();
//                    String command = I2CH.getCommand();
//                    int value = I2CH.getValue();
//                    String key = command;
//                    int magnitude = 0;
//                    int target = 1;
//
//                    switch (key)
//                    {
//                        case "ActuatorSB_setTarget":
//
//                            actuatorSB.write(SB_ACTUATOR_ADDRESS, command2);
//                            actuatorSB.write(setTarget, (byte) value);
//                            break;
//                        case "ActuatorSB_stopMotor":
//                            actuatorSB.write(motorOff, (byte) 0x00);
//                            break;
//                    }
//
////                    for (byte x : dataByteArray)
////                    {
////                        arduinoDummy.write((byte) x);
////                        //Thread.sleep(1);
////                    }
////                    device.write((byte) 0x03);
////                    Thread.sleep(200);
////                    //Thread.sleep(2000);
////                    System.out.println("Signal is sent");
////                    int r = device.read(inputData);
////                    System.out.println("Data recieved is: " + r);
////
////                    Thread.sleep(5000);
////                    device.write((byte) 0x00);
////                    System.out.println("Reset signal is sent");
////                    Thread.sleep(5000);
//                    I2CH.setI2cSendRequest(false);
//                }
//            } catch (Exception e)
//            {
//                System.out.println(e + " : Unable to create I2C Bus");
//            }
//
//            try
//            {
//                if (I2CH.getDataRequest())
//                {
//                    byte[] inputDataRaw = new byte[32];
//                    String key = I2CH.getDataRequestDevice();
//                    String dataRecieved = "";
//                    switch (key)
//                    {
//                        case "ActuatorSB_Feedback":
//                            break;
//                        case "ActuatorPS_Feedback":
//                            break;
//
//                        case "ArduinoIO":
//                            arduinoIO.read(inputDataRaw, 0, 32);
//                            int sizeOfRecievedData = 0;
//
//                            for (byte b : inputDataRaw)
//                            {
//                                if (b != -1)
//                                {
//                                    sizeOfRecievedData++;
//                                }
//                            }
//
//                            byte[] inputData = new byte[sizeOfRecievedData];
//                            System.arraycopy(inputDataRaw, 0, inputData, 0, sizeOfRecievedData);
//
//                            dataRecieved = new String(inputData);
//
//                            break;
//
//                    }
//                    dataRecieved = dataRecieved.substring(dataRecieved.indexOf(start_char) + 1);
//                    dataRecieved = dataRecieved.substring(0, dataRecieved.indexOf(end_char));
//                    String[] data = dataRecieved.split(sep_char);
//                    String[] valueNames = new String[7];
//                    valueNames[0] = "fb_depthToSeabed";
//                    valueNames[1] = "fb_speedThroughWather";
//                    valueNames[2] = "AuxIn1";
//                    valueNames[3] = "AuxIn2";
//                    valueNames[4] = "AuxIn3";
//                    valueNames[5] = "AuxIn4";
//                    valueNames[6] = "AuxIn5";
//
//                    for (int i = 0; i < data.length; i++)
//                    {
//
//                        dh.data.put(valueNames[i], data[i]);
//                        //SerialDataList.put(data[i], data[i + 1]);
//                        //System.out.println("Key: " + data[i] + "     Value:" + data[i + 1]);                   
//                    }
//
//                    dh.handleDataFromI2C();
//                    I2CH.setDataRequest(false);
//
//                }
//            } catch (IOException | InterruptedException e)
//            {
//                System.out.println("Exception" + e);
//            }
//
//        }
//    }
    public void sendI2CData(String device, int commandValue)
    {
        if (!data.getCmd_disableMotors())
        {
            try
            {
                switch (device)
                {
                    case "ActuatorPS_setTarget":

                        if (commandValue > data.getFb_actuatorPSPos()
                                && commandValue > 0
                                && commandValue <= 254)
                        {
                            actuatorPS.write(JRK_setTargetLowResFwd, (byte) PS_ACTUATOR_SPEED);
                        }
//                    if (commandValue >= 127 && commandValue <= 254)
//                    {
//                        if (commandValue == 127)
//                        {
//                            // commandValue = commandValue - 1;
//                        }
////                        commandValue = (byte) (commandValue);
//                        actuatorPS.write(JRK_setTargetLowResFwd, (byte) PS_ACTUATOR_SPEED);
//                    }

                        if (commandValue < data.getFb_actuatorPSPos()
                                && commandValue > 0
                                && commandValue <= 254)
                        {
                            actuatorPS.write(JRK_setTargetLowResRev, (byte) PS_ACTUATOR_SPEED);
                        }

//                    if (commandValue > 0 && commandValue < 127)
//                    {
//                        if (commandValue == 1)
//                        {
//                            commandValue = (commandValue + 1);
//                        }
////                        commandValue = (byte) (commandValue); // - 127
//                        actuatorPS.write(JRK_setTargetLowResRev, (byte) PS_ACTUATOR_SPEED);
//                    }
                        if (commandValue == 0)
                        {
                            actuatorPS.write(JRK_setTargetLowResRev, (byte) 0);
                        }
                        break;
                    case "ActuatorSB_setTarget":

                        if (commandValue > data.getFb_actuatorSBPos()
                                && commandValue > 0
                                && commandValue <= 254)
                        {
                            actuatorSB.write(JRK_setTargetLowResFwd, (byte) SB_ACTUATOR_SPEED);
                        }

                        if (commandValue < data.getFb_actuatorSBPos()
                                && commandValue > 0
                                && commandValue <= 254)
                        {
                            actuatorSB.write(JRK_setTargetLowResRev, (byte) SB_ACTUATOR_SPEED);
                        }

//                    //Wing up
//                    if (commandValue >= 127 && commandValue < 255)
//                    {
//                        if (commandValue == 127)
//                        {
//                            commandValue = (commandValue + 1);
//                        }
//                        //commandValue =  (commandValue - 127);
//                        actuatorSB.write(JRK_setTargetLowResFwd, (byte) SB_ACTUATOR_SPEED);
//                    }
//                    //Wing down
//                    if (commandValue > 0 && commandValue < 127)
//                    {
//                        if (commandValue == 1)
//                        {
//                            commandValue = (commandValue - 1);
//                        }
////                        commandValue = (byte) (127 - commandValue);
//                        actuatorSB.write(JRK_setTargetLowResRev, (byte) SB_ACTUATOR_SPEED);
//                    }
                        if (commandValue == 0)
                        {
                            actuatorSB.write(JRK_setTargetLowResRev, (byte) 0);
                        }
                        break;
                    case "ActuatorSB_stopMotor":
                        //actuatorSB.write((byte) ACTUATOR_STOP);
                        try
                        {
                            Thread.sleep(25);
                        } catch (Exception e)
                        {
                        }

                        actuatorSB.write(JRK_setTargetLowResRev, (byte) 0);
                        break;
                    case "ActuatorPS_stopMotor":
                        try
                        {
                            Thread.sleep(25);
                        } catch (Exception e)
                        {
                        }
                        //actuatorPS.write((byte) ACTUATOR_STOP);
                        actuatorPS.write(JRK_setTargetLowResRev, (byte) 0);
                        break;

                }
            } catch (Exception e)
            {
                data.setERROR_I2C(true);
                System.out.println("Error at I2C read write");
                System.out.println(e);
                //Error writing to i2c
            }
        }
    }

    public void readI2CData(String device)
    {
        byte[] inputDataRaw = new byte[32];

        String dataRecieved = "";
        try
        {
            switch (device)
            {

                case "ActuatorSB_Feedback":

                    // int test = actuatorSB.read(JRK_getScaledFeedback);
                    byte[] byteArraySB = new byte[2];
                    actuatorSB.read(0xA7, byteArraySB, 0, 2);
                    int posSB = byteArraySB[0] + 256 * byteArraySB[1];

//                    int posSB = actuatorSB.read(JRK_getScaledFeedback);
                    data.setFb_actuatorSBPos(posSB);
                    break;

                case "ActuatorPS_Feedback":
                    byte[] byteArrayPS = new byte[2];
                    actuatorPS.read(0xA7, byteArrayPS, 0, 2);
                    int posPS = byteArrayPS[0] + 256 * byteArrayPS[1];
                    data.setFb_actuatorPSPos(posPS);
                    break;

                case "ArduinoIO":
                    byte[] buffer = new byte[20];
                    arduinoIO.read(buffer, 0, 20);
                    String bufferData = new String(buffer);
                    //arduinoIO.read(inputDataRaw, 0, 6);
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
        } catch (Exception e)
        {
            System.out.println(e);
        }

    }
}
