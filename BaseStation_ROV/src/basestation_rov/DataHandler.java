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

    private int arduinoBaudRate = 115200;
    private byte[] dataFromArduino = new byte[11];
    private boolean dataFromArduinoAvailable = false;
    private byte requestCodeFromArduino;
    private boolean threadStatus = true;
    private boolean dataUpdated = false;

    // Feedback from GPS
    public int satellites = 0;
    public float altitude = 0;
    public float angle = 0;
    public float speed = 0;
    public double latitude = 62.536819;
    public double longitude = 6.223951;
    public float depth = (float) 0.01;
    public float temperature = (float) 0.01;

    //Feedback from IMU
    public int roll = 0;
    public int pitch = 0;
    public int heading = 100;

    // Feedback from ROV
    public int rovDepth;

    // Feedback from GUI
    public boolean startLogging = true;

    public ConcurrentHashMap<String, String> data = new ConcurrentHashMap<>();

    public DataHandler()
    {
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

    public double get_Latitude()
    {
        set_Latitude(latitude + 0.0005);
        return latitude;
    }

    public void set_Latitude(double latitude)
    {
        this.latitude = latitude;
    }

    public double get_Longitude()
    {
        return longitude;
    }

    public void set_Longitude(double longitude)
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

    public int get_Roll()
    {
        return roll;
    }

    public void set_Roll(int roll)
    {
        this.roll = roll;
    }

    public int get_Pitch()
    {
        return pitch;
    }

    public void set_Pitch(int pitch)
    {
        this.pitch = pitch;
    }

    public int get_Heading()
    {
        return heading;
    }

    public void get_Heading(int heading)
    {
        this.heading = heading;
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
                    this.roll = Integer.parseInt(value);
                    break;
                case "Pitch":
                    this.pitch = Integer.parseInt(value);
                    break;
                case "Heading":
                    this.heading = Integer.parseInt(value);
                    break;
            }
        }
    }

}
