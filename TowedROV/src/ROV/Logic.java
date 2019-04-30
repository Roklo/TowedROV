/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ROV;

import I2CCom.*;
import ROV.TCPCom.*;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author <Robin S. Thorholm>
 */
public class Logic implements Runnable, Observer
{

    DataHandler data = null;
    I2CRW i2cRw = null;
    int old_cmd_actuatorPS = -1;
    int old_cmd_actuatorSB = -1;

    int actuatorPShysteresis = 10;
    int actuatorSBhysteresis = 10;

    final GpioController gpio = GpioFactory.getInstance();

    final GpioPinDigitalOutput BlueLED_PIN = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "BlueLED", PinState.LOW);

    public Logic(DataHandler data, I2CRW i2cRw)
    {
        this.data = data;
        this.i2cRw = i2cRw;

        int old_cmd_actuatorPS = -1;
        int old_cmd_actuatorSB = -1;

    }

    @Override
    public void run()
    {
        try
        {
            if (data.fb_actuatorPSPos >= data.getCmd_actuatorPS() - actuatorPShysteresis
                    && data.fb_actuatorPSPos <= data.getCmd_actuatorPS() + actuatorPShysteresis)
            {
                i2cRw.sendI2CData("ActuatorPS_stopMotor", 0);
            }

            if (data.fb_actuatorSBPos >= data.getCmd_actuatorSB() - actuatorSBhysteresis
                    && data.fb_actuatorSBPos <= data.getCmd_actuatorSB() + actuatorSBhysteresis)
            {
                i2cRw.sendI2CData("ActuatorSB_stopMotor", 0);
            }

//            Thread.sleep(1000);
//            i2cRw.readI2CData("ActuatorPS_Feedback");
//            Thread.sleep(1000);
//            i2cRw.readI2CData("ActuatorSB_Feedback");
        } catch (Exception e)
        {
        }

    }

    @Override
    public void update(Observable o, Object arg)
    {
        //Commands
        if (old_cmd_actuatorPS != data.cmd_actuatorPS)
        {
            i2cRw.sendI2CData("ActuatorPS_setTarget", data.cmd_actuatorPS);
        }

        if (old_cmd_actuatorSB != data.cmd_actuatorSB)
        {
            i2cRw.sendI2CData("ActuatorSB_setTarget", data.cmd_actuatorSB);
        }

        if (data.getCmd_BlueLED() == 1)
        {
            BlueLED_PIN.high();
        }
        else
        {
            BlueLED_PIN.low();
        }       
    }
}
