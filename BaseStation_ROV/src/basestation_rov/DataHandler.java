/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basestation_rov;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Shared recource for the base station program, responsible for handling all
 * data used in the application.
 *
 * @author Bjørnar
 */
public class DataHandler
{

    private int arduinoBaudRate;

    private byte[] dataFromArduino;
    private boolean dataFromArduinoAvailable = false;
    private byte requestCodeFromArduino;
    private boolean threadStatus = true;

    private boolean dataUpdated = false;

    // Feedback from GPS
    public int satellites;
    public float altitude;
    public float angle;
    public float speed;
    public float latitude;
    public float longitude;
    public float depth;
    public float temperature;
    public float roll;
    public float pitch;
    public float yaw;

    public ConcurrentHashMap<String, String> data = new ConcurrentHashMap<>();

    public DataHandler()
    {

        this.dataFromArduino = new byte[11];
        dataFromArduinoAvailable = false;

        arduinoBaudRate = 115200;

        satellites = 0;
        altitude = (float) 0.00;
        angle = (float) 0.00;
        speed = (float) 0.00;
        latitude = (float) 0.00;
        longitude = (float) 0.00;
        depth = (float) 0.01;
        temperature = (float) 0.01;
        roll = (float) 0.00;
        pitch = (float) 0.00;
        yaw = (float) 0.00;
        

    }

    /**
     * Checks status of thread
     *
     * @return thread status
     */
    public boolean shouldThreadRun()
    {
        return threadStatus;
    }

    /**
     * Sets the thread status
     *
     * @param threadStatus
     */
    public void setThreadStatus(boolean threadStatus)
    {
        this.threadStatus = threadStatus;
    }

    public byte[] getDataFromArduino()
    {
        return dataFromArduino;
    }

    /**
     *
     * @return true if new data available, false if not
     */
    public synchronized boolean isDataFromArduinoAvailable()
    {
        return this.dataFromArduinoAvailable;
    }

    public synchronized boolean isDataUpdated()
    {
        return dataUpdated;
    }

    public synchronized void setDataUpdated(boolean dataUpdated)
    {
        this.dataUpdated = dataUpdated;
    }

    public int get_Satellites()
    {
        return satellites;
    }

    public void set_Satellites(int satellites)
    {
        this.satellites = satellites;
    }

    public float get_Altitude()
    {
        return altitude;
    }

    public void set_Altitude(float altitude)
    {
        this.altitude = altitude;
    }

    public float get_Angle()
    {
        return angle;
    }

    public void set_Angle(float angle)
    {
        this.angle = angle;
    }

    public float get_Speed()
    {
        return speed;
    }

    public void set_Speed(float speed)
    {
        this.speed = speed;
    }

    public float get_Latitude()
    {
        return latitude;
    }

    public void set_Latitude(float latitude)
    {
        this.latitude = latitude;
    }

    public float get_Longitude()
    {
        return longitude;
    }

    public void set_Longitude(float longitude)
    {
        this.longitude = longitude;
    }

    public Float get_Depth()
    {
        return depth;
    }

    public void set_Depth(Float depth)
    {
        this.depth = depth;
    }

    public Float get_Temperature()
    {
        return temperature;
    }

    public void set_Temperature(Float temperature)
    {
        this.temperature = temperature;
    }
    
    public Float get_Roll()
    {
        return roll;
    }
    
    public void set_Roll(Float roll)
    {
        this.roll = roll;
    }
    
    public Float get_Pitch()
    {
        return pitch;
    }
    
    public void set_Pitch(Float pitch)
    {
        this.pitch = pitch;
    }
    
    public Float get_Yaw()
    {
        return yaw;
    }
    
    public void set_Yaw(Float yaw)
    {
        this.yaw = yaw;
    }

    /**
     * Compare keys to controll values coming in from arduino, and puts correct
     * value to correct variable.
     */
    public synchronized void handleDataFromRemote()
    {
        for (Entry e : data.entrySet())
        {
            String key = (String) e.getKey();
            String value = (String) e.getValue();

            switch (key)
            {
                case "Satellites":
                    this.satellites = Integer.parseInt(value);
                    break;
                case "Altitude":
                    this.altitude = Float.parseFloat(value);
                    break;
                case "Angle":
                    this.angle = Float.parseFloat(value);
                    break;
                case "Speed":
                    this.speed = Float.parseFloat(value);
                    break;
                case "Latitude":
                    this.latitude = Float.parseFloat(value);
                    break;
                case "Longitude":
                    this.longitude = Float.parseFloat(value);
                    break;
                case "Depth":
                    this.depth = Float.parseFloat(value);
                    break;
                case "Temp":
                    this.temperature = Float.parseFloat(value);
                    break;
                case "Roll":
                    this.roll = Float.parseFloat(value);
                    break;
                case "Pitch":
                    this.pitch = Float.parseFloat(value);
                break;
                case "Yaw":
                    this.yaw = Float.parseFloat(value);
                    break;
            }
        }
    }

}
