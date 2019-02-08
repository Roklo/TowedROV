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

/**
 *
 * @author <Robin S. Thorholm>
 */
public class I2CRW implements Runnable
{

    
    protected static I2CHandler I2CH;

    I2CDevice arduinoDummy;

    private final static int ARDUINO_DUMMY_SIGNAL_ADDRESS = 9;

    public I2CRW(I2CHandler I2CH)
    {
        
        this.I2CH = I2CH;

    }

    @Override
    public void run()
    {
        //Instanciating I2C bus and add all slaves
        try
        {
            System.out.println("Creatingbus");
            I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);
            System.out.println("Creatingdevice");
            arduinoDummy = bus.getDevice(ARDUINO_DUMMY_SIGNAL_ADDRESS);

        } catch (Exception e)
        {
            System.out.println("ERROR: Failed to start I2C bus : " + e);
        }

        while (true)
        {
            try
            {
                if (I2CH.getI2cRequest())
                {
                    byte[] dataByteArray = I2CH.getCurrentDataByteArray();
                    for (byte x : dataByteArray)
                    {
                        arduinoDummy.write((byte) x);
                        //Thread.sleep(1);
                    }

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
                    I2CH.setI2cRequest(false);
                }
            } catch (Exception e)
            {
                System.out.println(e + " : Unable to create I2C Bus");
            }

        }
    }
}
