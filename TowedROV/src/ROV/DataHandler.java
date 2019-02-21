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

    //Sensor values
    int depthToSeabed = 0;
    int speedThroughWather = 0;
    int waterTemperature = 0;

    private int counter = 0;
    private boolean i2cRequest = false;

    public HashMap<String, String> data = new HashMap<>();

    //Sensor values getters and setters
    public int getDepthToSeabed()
    {
        return depthToSeabed;
    }

    public void setDepthToSeabed(int depthToSeabed)
    {
        this.depthToSeabed = depthToSeabed;
    }

    public int getSpeedThroughWather()
    {
        return speedThroughWather;
    }

    public void setSpeedThroughWather(int speedThroughWather)
    {
        this.speedThroughWather = speedThroughWather;
    }

    public int getWaterTemperature()
    {
        return waterTemperature;
    }

    public void setWaterTemperature(int waterTemperature)
    {
        this.waterTemperature = waterTemperature;
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
                        this.depthToSeabed = Integer.parseInt(value);
                        break;
                    case "fb_speedThroughWather":
                        this.speedThroughWather = Integer.parseInt(value);
                        break;
                    case "fb_waterTemperature":
                        this.waterTemperature = Integer.parseInt(value);
                        break;
                }

            }
        }
    }
}
