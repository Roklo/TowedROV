/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ROV;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author <Robin S. Thorholm>
 */
public class DataHandler
{
    // Calibration values
    int pressureSensorOffset = 0;
    

    // Command values
    int cmd_lightIntensity = 0;
    int cmd_actuatorPS = 0;
    int cmd_actuatorSB = 0;
    int cmd_depth = 0;
    int cmd_cameraPitch = 0;
    int cmd_cameraRoll = 0;
    byte cmd_cameraMode = 0;
    
    int cmd_pid_p = 0;
    int cmd_pid_i = 0;
    int cmd_pid_d = 0;
    int cmd_pid_gain = 0;
    
    boolean cmd_emergencySurface = false;

    // Sensor values
    int fb_depthToSeabedEcho = 12;
    int fb_depthFromPressure = 0;
    int fb_speedThroughWather = 0;
    int fb_waterTemperature = 0;
    int fb_pressure = 0;
    int fb_actuatorPSPos = 0;
    int fb_actuatorSBPos = 0;
    int fb_tempInternalCamera = 0;
    int fb_humidityInternalCamera = 0;
    int fb_tempPSactuatorBox = 0;
    int fb_tempSBactuatorBox = 0;
    int fb_tempMainElBox = 0;
    int fb_tempEchoBox = 0;
    int fb_currentDraw = 0;
    int fb_pitch = 0;
    int fb_roll = 0;
    int fb_yaw = 0;
    int fb_heading = 0;
    
    boolean fb_waterLeakChannel_1 = false;
    boolean fb_waterLeakChannel_2 = false;
    boolean fb_waterLeakChannel_3 = false;
    boolean fb_waterLeakChannel_4 = false;
    
    //Class variables

    private int counter = 0;
    private boolean i2cRequest = false;

    public HashMap<String, String> data = new HashMap<>();

    //Sensor values getters and setters

    public int getFb_depthToSeabedEcho()
    {
        return fb_depthToSeabedEcho;
    }

    public void setFb_depthToSeabedEcho(int fb_depthToSeabedEcho)
    {
        this.fb_depthToSeabedEcho = fb_depthToSeabedEcho;
    }

    public int getFb_depthFromPressure()
    {
        return fb_depthFromPressure;
    }

    public void setFb_depthFromPressure(int fb_depthFromPressure)
    {
        this.fb_depthFromPressure = fb_depthFromPressure;
    }

    public int getFb_speedThroughWather()
    {
        return fb_speedThroughWather;
    }

    public void setFb_speedThroughWather(int fb_speedThroughWather)
    {
        this.fb_speedThroughWather = fb_speedThroughWather;
    }

    public int getFb_waterTemperature()
    {
        return fb_waterTemperature;
    }

    public void setFb_waterTemperature(int fb_waterTemperature)
    {
        this.fb_waterTemperature = fb_waterTemperature;
    }

    public int getFb_pressure()
    {
        return fb_pressure;
    }

    public void setFb_pressure(int fb_pressure)
    {
        this.fb_pressure = fb_pressure;
    }

    public int getFb_actuatorPSPos()
    {
        return fb_actuatorPSPos;
    }

    public void setFb_actuatorPSPos(int fb_actuatorPSPos)
    {
        this.fb_actuatorPSPos = fb_actuatorPSPos;
    }

    public int getFb_actuatorSBPos()
    {
        return fb_actuatorSBPos;
    }

    public void setFb_actuatorSBPos(int fb_actuatorSBPos)
    {
        this.fb_actuatorSBPos = fb_actuatorSBPos;
    }

    public int getFb_tempInternalCamera()
    {
        return fb_tempInternalCamera;
    }

    public void setFb_tempInternalCamera(int fb_tempInternalCamera)
    {
        this.fb_tempInternalCamera = fb_tempInternalCamera;
    }

    public int getFb_humidityInternalCamera()
    {
        return fb_humidityInternalCamera;
    }

    public void setFb_humidityInternalCamera(int fb_humidityInternalCamera)
    {
        this.fb_humidityInternalCamera = fb_humidityInternalCamera;
    }

    public int getFb_tempPSactuatorBox()
    {
        return fb_tempPSactuatorBox;
    }

    public void setFb_tempPSactuatorBox(int fb_tempPSactuatorBox)
    {
        this.fb_tempPSactuatorBox = fb_tempPSactuatorBox;
    }

    public int getFb_tempSBactuatorBox()
    {
        return fb_tempSBactuatorBox;
    }

    public void setFb_tempSBactuatorBox(int fb_tempSBactuatorBox)
    {
        this.fb_tempSBactuatorBox = fb_tempSBactuatorBox;
    }

    public int getFb_tempMainElBox()
    {
        return fb_tempMainElBox;
    }

    public void setFb_tempMainElBox(int fb_tempMainElBox)
    {
        this.fb_tempMainElBox = fb_tempMainElBox;
    }

    public int getFb_tempEchoBox()
    {
        return fb_tempEchoBox;
    }

    public void setFb_tempEchoBox(int fb_tempEchoBox)
    {
        this.fb_tempEchoBox = fb_tempEchoBox;
    }

    public int getFb_currentDraw()
    {
        return fb_currentDraw;
    }

    public void setFb_currentDraw(int fb_currentDraw)
    {
        this.fb_currentDraw = fb_currentDraw;
    }

    public int getFb_pitch()
    {
        return fb_pitch;
    }

    public void setFb_pitch(int fb_pitch)
    {
        this.fb_pitch = fb_pitch;
    }

    public int getFb_roll()
    {
        return fb_roll;
    }

    public void setFb_roll(int fb_roll)
    {
        this.fb_roll = fb_roll;
    }

    public int getFb_yaw()
    {
        return fb_yaw;
    }

    public void setFb_yaw(int fb_yaw)
    {
        this.fb_yaw = fb_yaw;
    }

    public int getFb_heading()
    {
        return fb_heading;
    }

    public void setFb_heading(int fb_heading)
    {
        this.fb_heading = fb_heading;
    }

    public boolean isFb_waterLeakChannel_1()
    {
        return fb_waterLeakChannel_1;
    }

    public void setFb_waterLeakChannel_1(boolean fb_waterLeakChannel_1)
    {
        this.fb_waterLeakChannel_1 = fb_waterLeakChannel_1;
    }

    public boolean isFb_waterLeakChannel_2()
    {
        return fb_waterLeakChannel_2;
    }

    public void setFb_waterLeakChannel_2(boolean fb_waterLeakChannel_2)
    {
        this.fb_waterLeakChannel_2 = fb_waterLeakChannel_2;
    }

    public boolean isFb_waterLeakChannel_3()
    {
        return fb_waterLeakChannel_3;
    }

    public void setFb_waterLeakChannel_3(boolean fb_waterLeakChannel_3)
    {
        this.fb_waterLeakChannel_3 = fb_waterLeakChannel_3;
    }

    public boolean isFb_waterLeakChannel_4()
    {
        return fb_waterLeakChannel_4;
    }

    public void setFb_waterLeakChannel_4(boolean fb_waterLeakChannel_4)
    {
        this.fb_waterLeakChannel_4 = fb_waterLeakChannel_4;
    }
    

    //Alarm flags
    //Alarm getters and setters
    public int getCounter()
    {
        return counter;
    }

    public void setCounter(int counter)
    {
        this.counter = counter;
    }

    public void handleDataFromI2C()
    {

        for (Map.Entry e : data.entrySet())
        {

            {
                String key = (String) e.getKey();
                String value = (String) e.getValue();

                switch (key)
                {
                    case "fb_depthToSeabed":
                        this.fb_depthToSeabedEcho = Integer.parseInt(value);
                        break;
                    case "fb_speedThroughWather":
                        this.fb_speedThroughWather = Integer.parseInt(value);
                        break;
                    case "fb_waterTemperature":
                        this.fb_waterTemperature = Integer.parseInt(value);
                        break;
                }

            }
        }
    }
}
