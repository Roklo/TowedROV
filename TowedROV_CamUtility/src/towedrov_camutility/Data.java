/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package towedrov_camutility;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The data class is a storage box that let's the different threads change and
 * retrieve various data. The data class is a subclass of the java class
 * Observable, which makes it possible for observers to subscribe and update
 * their values whenever they change. Data is made thread safe by the use of
 * synchronized methods.
 *
 * @author Marius Nonsvik
 */
public final class Data //extends Observable
{

    public HashMap<String, String> comPortList = new HashMap<>();
    public ConcurrentHashMap<String, Boolean> completeAlarmListDh = new ConcurrentHashMap<>();

    private int arduinoBaudRate = 115200;
    private byte[] dataFromArduino = new byte[11];
    private boolean dataFromArduinoAvailable = false;
    private byte requestCodeFromArduino;
    private boolean threadStatus = true;
    private boolean dataUpdated = false;

    private boolean fanRunning = false;
    private boolean heaterRunning = false;
    private int cameraPitchValue = 0;
    private int pressureValue = 0;

    public ConcurrentHashMap<String, String> data = new ConcurrentHashMap<>();
    private byte leakStatus = 0;
    private ArrayList<String> labels = new ArrayList();
    private float[] channelValues = new float[4];
    private String defaultIP = "";
    private long timer = System.currentTimeMillis();

    /**
     * Creates an object of the class Data.
     */
    public Data()
    {
     
    }

    /**
     * Sets the default connection IP
     *
     * @param ip The default connection IP
     */
    public synchronized void setDefaultIP(String ip)
    {
        defaultIP = ip;
//        setChanged();
//        notifyObservers();
    }

    /**
     * Returns the default connection IP
     *
     * @return The default connection IP
     */
    public synchronized String getDefaultIP()
    {
        return defaultIP;
    }


    /**
     * Sets the label of all the different I/O channels and notifies observers
     *
     * @param c1 Channel 1 label
     * @param c2 Channel 2 label
     * @param c3 Channel 3 label
     * @param c4 Channel 4 label
     * @param c5 Channel 5 label
     * @param c6 Channel 6 label
     * @param c7 Channel 7 label
     * @param c8 Channel 8 label
     */
    public synchronized void setIOLabels(String c1, String c2, String c3, String c4, String c5, String c6, String c7, String c8)
    {
        labels.set(0, c1);
        labels.set(1, c2);
        labels.set(2, c3);
        labels.set(3, c4);
        labels.set(4, c5);
        labels.set(5, c6);
        labels.set(6, c7);
        labels.set(7, c8);
//        setChanged();
//        notifyObservers();
    }

    /**
     * Returns a string containing the label of the channel. If the channel is
     * an input, the string also contains its measure value. (Index 1-8)
     *
     * @param channel Index of channel to return
     * @return String containing label and value
     */
    public synchronized String getChannel(int channel)
    {
        if (channel > 0 && channel < 9)
        {
            if (channel < 5)
            {
                String channelString = labels.get(channel - 1) + ": ";
                channelString += channelValues[channel - 1];
                return channelString;
            } else
            {
                return labels.get(channel - 1);
            }
        } else
        {
            return null;
        }
    }

    /**
     * Returns the value of the channel as a float. (Index 1-4)
     *
     * @param channel Index of channel
     * @return Value of the channel as float
     */
    public synchronized float getChannelValue(int channel)
    {
        if (channel < 0 && channel > 5)
        {
            return channelValues[channel - 1];
        } else
        {
            return (float) 0.001;
        }
    }

    /**
     * Sets the value of one of the inputs and notifies observers (Index 1-4).
     *
     * @param value Value of the channel
     * @param channel Index of the channel
     */
    public synchronized void setChannel(float value, int channel)
    {
        if (channel < 0 && channel > 5)
        {
            channelValues[channel - 1] = value;
        }
//        setChanged();
//        notifyObservers();
    }

    public boolean isFanRunning()
    {
        return fanRunning;
    }

    public void setFanRunning(boolean fanRunning)
    {
        this.fanRunning = fanRunning;
    }

    public boolean isHeaterRunning()
    {
        return heaterRunning;
    }

    public void setHeaterRunning(boolean heaterRunning)
    {
        this.heaterRunning = heaterRunning;
    }

    public int getPressureValue()
    {
        return pressureValue;
    }

    public void setPressureValue(int pressureValue)
    {
        this.pressureValue = pressureValue;
    }



    /**
     * Updates the leak status in the ROV. 1 if a leak is detected, 0 if no leak
     * is detected.
     *
     * @param leak Current leak status of the ROV
     */
    public synchronized void setLeakStatus(byte leak)
    {
        leakStatus = leak;
//        setChanged();
//        notifyObservers();
    }

    /**
     * Returns the leak status of the ROV. Returns true if a leak is detected,
     * false if no leak is detected
     *
     * @return Current leak status of the ROV
     */
    public synchronized boolean getLeakStatus()
    {
        if (leakStatus == 1)
        {
            return true;
        } else
        {
            return false;
        }
    }

    /**
     * Returns the camera pitch value
     *
     * @return the camera pitch value
     */
    public int getCameraPitchValue()
    {
        return cameraPitchValue;
    }

    /**
     * Sets the camera pitch value
     *
     * @param photoMode the camera pitch value
     */
    public void setCameraPitchValue(int cameraPitchValue)
    {
        this.cameraPitchValue = cameraPitchValue;
    }

    // CODE BELOW ADDED FROM THE BASESTATION PROJECT
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

    /**
     * Compare keys to controll values coming in from arduino, and puts correct
     * value to correct variable.
     */
    public synchronized void handleDataFromArduino()
    {
        for (Map.Entry e : data.entrySet())
        {
            String key = (String) e.getKey();
            String value = (String) e.getValue();

            switch (key)
            {
                case "Pressure":
                    this.pressureValue = Integer.parseInt(value);
                    break;
            }
        }
    }

}
