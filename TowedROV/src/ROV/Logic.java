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
import java.util.HashMap;
import java.util.Map;

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
    int old_cmd_actuatorPS = 0;
    int old_cmd_actuatorSB = 0;
    int old_cmd_bothActuators = 0;

    int old_cmd_BlueLED = 0;

    int old_cmd_actuatorPS_2 = 0;
    int old_cmd_actuatorSB_2 = 0;

    private final static int actuatorPShysteresis = 5;
    private final static int actuatorSBhysteresis = 5;
    private HashMap<String, String> newDataToSend = new HashMap<>();

    final GpioController gpio = GpioFactory.getInstance();

    final GpioPinDigitalOutput BlueLED_PIN = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "BlueLED", PinState.LOW);

    public Logic(DataHandler data, I2CRW i2cRw)
    {
        this.data = data;
        this.i2cRw = i2cRw;

    }

    @Override
    public void run()
    {
        try
        {
            if (old_cmd_actuatorPS_2 != data.getCmd_actuatorPS())
            {
                System.out.println("Distance to target(PS): " + (data.getFb_actuatorPSPos() - data.getCmd_actuatorPS()));
                if (data.fb_actuatorPSPos >= data.getCmd_actuatorPS() - actuatorPShysteresis
                        && data.fb_actuatorPSPos <= data.getCmd_actuatorPS() + actuatorPShysteresis)
                {
                    i2cRw.sendI2CData("ActuatorPS_stopMotor", (byte) 0);
                    old_cmd_actuatorPS_2 = data.getCmd_actuatorPS();
                }
            }
            if (old_cmd_actuatorSB_2 != data.getCmd_actuatorSB())
            {
                System.out.println("Distance to target(SB): " + (data.getFb_actuatorSBPos() - data.getCmd_actuatorSB()));
                if (data.fb_actuatorSBPos >= data.getCmd_actuatorSB() - actuatorSBhysteresis
                        && data.fb_actuatorSBPos <= data.getCmd_actuatorSB() + actuatorSBhysteresis)
                {
                    i2cRw.sendI2CData("ActuatorSB_stopMotor", (byte) 0);
                    old_cmd_actuatorSB_2 = data.getCmd_actuatorSB();
                }
            }
            gatherFbData();

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
        if (data.getcmd_targetMode() == 3)
        {
            if (old_cmd_actuatorPS != data.getCmd_actuatorPS())
            {
                old_cmd_actuatorPS = data.getCmd_actuatorPS();
                i2cRw.sendI2CData("ActuatorPS_setTarget", data.cmd_actuatorPS);
            }

            if (old_cmd_actuatorSB != data.getCmd_actuatorSB())
            {
                old_cmd_actuatorSB = data.getCmd_actuatorSB();
                i2cRw.sendI2CData("ActuatorSB_setTarget", data.cmd_actuatorSB);
            }
        } else
        {
            if (old_cmd_bothActuators != data.getCmd_bothActuators())
            {
                old_cmd_bothActuators = data.getCmd_bothActuators();
                i2cRw.sendI2CData("ActuatorSB_setTarget", data.getCmd_bothActuators());
                i2cRw.sendI2CData("ActuatorPS_setTarget", data.getCmd_bothActuators());
            }

        }
        if (old_cmd_BlueLED != data.getCmd_BlueLED())
        {
            BlueLED_PIN.toggle();
            old_cmd_BlueLED = data.getCmd_BlueLED();
        }
//         gatherFbData();
    }

    public void gatherFbData()
    {

        newDataToSend.put("Fb_actuatorPSPos", String.valueOf(data.getFb_actuatorPSPos()));
        newDataToSend.put("Fb_actuatorSBPos", String.valueOf(data.getFb_actuatorSBPos()));
        newDataToSend.put("Fb_rollAngle", String.valueOf(data.getFb_rollAngle()));
        newDataToSend.put("Fb_pitchAngle", String.valueOf(data.getFb_pitchAngle()));
        newDataToSend.put("Fb_ROVdepth", String.valueOf(data.getCmd_currentROVdepth()));
        

        newDataToSend.put("Fb_ROVReady", String.valueOf(data.getFb_ROVReady()));
        newDataToSend.put("ERROR_I2C", String.valueOf(data.ERROR_I2C));

        String dataToSend = "<";
        for (Map.Entry e : newDataToSend.entrySet())
        {
            String key = (String) e.getKey();
            String value = (String) e.getValue();
            dataToSend = dataToSend + key + ":" + value + ":";
        }
        dataToSend = dataToSend.substring(0, dataToSend.length() - 1);
        dataToSend = dataToSend + ">";

        data.setDataToSend(dataToSend);

    }
}
