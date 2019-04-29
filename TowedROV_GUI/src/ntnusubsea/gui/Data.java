/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ntnusubsea.gui;

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
public final class Data extends Observable
{

    public HashMap<String, String> comPortList = new HashMap<>();
    public ConcurrentHashMap<String, Boolean> completeAlarmListDh = new ConcurrentHashMap<>();

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
    public float latitude = (float) 0;
    public float longitude = (float) 0;
    public float depth = (float) 0.01;
    public float temperature = (float) 0.01;
    public float voltage = (float) 0.00;

    //Feedback from IMU
    public float roll = 0;
    public float pitch = 0;
    public float heading = 100;

    // Feedback from ROV
    public int rovDepth;

    // Feedback from GUI
    public boolean startLogging = true;

    public ConcurrentHashMap<String, String> data = new ConcurrentHashMap<>();

    private float seafloorRov = 0;
    private float seafloorBoat = 0;
    private float pitchAngle = 0;
    private float wingAngle = 0;
    private float rollAngle = 0;
    private float pressure = 0;
    private float channel1 = 0;
    private float channel2 = 0;
    private float channel3 = 0;
    private float channel4 = 0;
    private byte actuatorStatus = 0;
    private byte leakStatus = 0;
    private ArrayList<String> labels = new ArrayList();
    private float[] channelValues = new float[4];
    private String defaultIP = "";
    private BufferedImage videoImage;
    private String Kp;
    private String Ki;
    private String Kd;
    private long timer = System.currentTimeMillis();
    private boolean photoMode = false;
    private double photoModeDelay = 1;
    private double photoModeDelay_FB = 1;
    private int imageNumber = 0;
    private int cameraPitchValue = 0;

    /**
     * Creates an object of the class Data.
     */
    public Data()
    {
        try
        {
            BufferedReader br = new BufferedReader(new FileReader("ROV Options.txt"));
            defaultIP = br.readLine();
            labels.add(0, br.readLine());
            labels.add(1, br.readLine());
            labels.add(2, br.readLine());
            labels.add(3, br.readLine());
            labels.add(4, br.readLine());
            labels.add(5, br.readLine());
            labels.add(6, br.readLine());
            labels.add(7, br.readLine());
            Kp = br.readLine();
            Ki = br.readLine();
            Kd = br.readLine();
            channelValues[0] = channel1;
            channelValues[1] = channel2;
            channelValues[2] = channel3;
            channelValues[3] = channel4;
//            videoImage = ImageIO.read(new File("C:\\Users\\marno\\OneDrive\\Documents\\NetBeansProjects\\NTNUSubsea GUI\\src\\ntnusubsea\\gui\\Images\\rsz_rovside.png"));
        } catch (IOException ex)
        {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Sets the default connection IP
     *
     * @param ip The default connection IP
     */
    public synchronized void setDefaultIP(String ip)
    {
        defaultIP = ip;
        setChanged();
        notifyObservers();
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

    public synchronized String getKp()
    {
        return Kp;
    }

    public synchronized String getKi()
    {
        return Ki;
    }

    public synchronized String getKd()
    {
        return Kd;
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
        setChanged();
        notifyObservers();
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
        setChanged();
        notifyObservers();
    }

    /**
     * Updates the current pitch angle of the ROV and notifies observers
     *
     * @param angle Current pitch angle of the ROV
     */
    public synchronized void setPitchAngle(float angle)
    {
        pitchAngle = angle;
        setChanged();
        notifyObservers();
    }

    /**
     * Retrieves the current pitch angle
     *
     * @return Current pitch angle of the ROV
     */
    public synchronized float getPitchAngle()
    {
        return pitchAngle;
    }

    /**
     * Updates the current roll angle of the ROV
     *
     * @param angle Current roll angle of the ROV
     */
    public synchronized void setRollAngle(float angle)
    {
        rollAngle = angle;
        setChanged();
        notifyObservers();
    }

    /**
     * Retrieves the current roll angle
     *
     * @return Current roll angle of the ROV
     */
    public synchronized float getRollAngle()
    {
        return rollAngle;
    }

    /**
     * Updates the current wing angle of the ROV and notifies observers
     *
     * @param angle Current wing angle of the ROV
     */
    public synchronized void setWingAngle(float angle)
    {
        wingAngle = angle;
        setChanged();
        notifyObservers();
    }

    /**
     * Retrieves the current pitch angle
     *
     * @return Current wing angle of the ROV
     */
    public synchronized float getWingAngle()
    {
        return wingAngle;
    }

    /**
     * Updates the current heading of the ROV and notifies observers
     *
     * @param heading Current heading of the ROV
     */
    public synchronized void setHeading(float heading)
    {
        this.heading = heading;
        setChanged();
        notifyObservers();
    }

    /**
     * Retrieves the current heading
     *
     * @return Current heading of the ROV
     */
    public synchronized float getHeading()
    {
        return heading;
    }

    /**
     * Updates the current latitude of the ROV and notifies observers
     *
     * @param latitude Current latitude of the ROV
     */
    public synchronized void setLatitude(float latitude)
    {
        this.latitude = latitude;
        setChanged();
        notifyObservers();
    }

    /**
     * Retrieves the current latitude
     *
     * @return Current latitude of the ROV
     */
    public synchronized float getLatitude()
    {
        return latitude;
    }

    /**
     * Updates the current longitude of the ROV and notifies observers
     *
     * @param longitude Current longitude of the ROV
     */
    public synchronized void setLongitude(float longitude)
    {
        this.longitude = longitude;
        setChanged();
        notifyObservers();
    }

    /**
     * Retrieves the current longitude
     *
     * @return Current longitude of the ROV
     */
    public synchronized float getLongitude()
    {
        return longitude;
    }

    /**
     * Updates the current depth of the ROV and notifies observers
     *
     * @param depth Current depth of the ROV
     */
    public synchronized void setDepth(float depth)
    {
        this.depth = depth;
        setChanged();
        notifyObservers();
    }

    /**
     * Retrieves the current depth
     *
     * @return Current depth of the ROV
     */
    public synchronized float getDepth()
    {
        return depth;
    }

    /**
     * Updates the current depth beneath the ROV
     *
     * @param depth Depth beneath the ROV
     */
    public synchronized void setSeafloorRov(float depth)
    {
        seafloorRov = depth;
        setChanged();
        notifyObservers();
    }

    /**
     * Returns the depth beneath the ROV
     *
     * @return Depth beneath the ROV
     */
    public synchronized float getSeafloorRov()
    {
        return seafloorRov;
    }

    /**
     * Updates the current depth beneath the vessel
     *
     * @param depth Depth beneath the vessel
     */
    public synchronized void setSeafloorBoat(float depth)
    {
        seafloorBoat = depth;
        setChanged();
        notifyObservers();
    }

    /**
     * Returns the depth beneath the vessel
     *
     * @return Depth beneath the vessel
     */
    public synchronized float getSeafloorBoat()
    {
        return seafloorBoat;
    }

    /**
     * Updates the image of the video stream and notifies observers
     *
     * @param image New image in the video stream
     */
    public synchronized void setVideoImage(BufferedImage image)
    {
        videoImage = null;
        videoImage = image;
        setChanged();
        notifyObservers();
    }

    /**
     * Updates the status of the actuators. 1 if they are currently running and
     * 0 if they are currently idle.
     *
     * @param status Current status of the actuators
     */
    public synchronized void setActuatorStatus(byte status)
    {
        this.actuatorStatus = status;
        setChanged();
        notifyObservers();
    }

    /**
     * Returns the status of the actuators. true if they are currently running
     * and false if they are currently idle.
     *
     * @return Current status of the actuators
     */
    public synchronized boolean getActuatorStatus()
    {
        if (actuatorStatus == 1)
        {
            return true;
        } else
        {
            return false;
        }
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
        setChanged();
        notifyObservers();
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
     * Updates the temperature of the water
     *
     * @param temp Temperature of the water
     */
    public synchronized void setTemperature(float temp)
    {
        temperature = temp;
        setChanged();
        notifyObservers();
    }

    /**
     * Returns the current temperature of the water
     *
     * @return Temperature of the water
     */
    public synchronized float getTemperature()
    {
        return temperature;
    }

    /**
     * Updates the pressure surrounding the ROV
     *
     * @param pres Pressure surrounding the ROV
     */
    public synchronized void setPressure(float pres)
    {
        pressure = pres;
        setChanged();
        notifyObservers();
    }

    /**
     * Returns the current pressure around the ROV
     *
     * @return Current pressure around the ROV
     */
    public synchronized float getPressure()
    {
        return pressure;
    }

    /**
     * Sets the current speed of the vessel
     *
     * @param speed Current speed of the vessel
     */
    public synchronized void setSpeed(float speed)
    {
        this.speed = speed;
        setChanged();
        notifyObservers();
    }

    /**
     * Returns the current speed of the vessel
     *
     * @return Current speed of the vessel
     */
    public synchronized float getSpeed()
    {
        return speed;
    }

    /**
     * Returns the current image in the video stream
     *
     * @return Current image in the video stream
     */
    public synchronized BufferedImage getVideoImage()
    {
        return videoImage;
    }

    /**
     * Returns the state of the photoMode variable
     *
     * @return the state of the photoMode variable, true or false
     */
    public boolean isPhotoMode()
    {
        return photoMode;
    }

    /**
     * Sets the state of the photoMode variable
     *
     * @param photoMode the state of the photoMode variable, true or false
     */
    public void setPhotoMode(boolean photoMode)
    {
        this.photoMode = photoMode;
    }

    /**
     * Returns the photo mode delay
     *
     * @return the photo mode delay
     */
    public double getPhotoModeDelay()
    {
        return photoModeDelay;
    }

    /**
     * Sets the photo mode delay
     *
     * @param photoMode the photo mode delay
     */
    public void setPhotoModeDelay(double photoModeDelay)
    {
        this.photoModeDelay = photoModeDelay;
        setChanged();
        notifyObservers();
    }

    public double getPhotoModeDelay_FB()
    {
        return photoModeDelay_FB;
    }

    public void setPhotoModeDelay_FB(double photoModeDelay_FB)
    {
        this.photoModeDelay_FB = photoModeDelay_FB;
        setChanged();
        notifyObservers();
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

    /**
     * Returns the image number value
     *
     * @return the image number value
     */
    public int getImageNumber()
    {
        return imageNumber;
    }

    /**
     * Sets the image number value
     *
     * @param imageNumber the image number value
     */
    public void setImageNumber(int imageNumber)
    {
        this.imageNumber = imageNumber;
        setChanged();
        notifyObservers();
    }

    /**
     * Increases the image number by one
     */
    public void increaseImageNumberByOne()
    {
        this.imageNumber++;
        setChanged();
        notifyObservers();
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

    public synchronized int getSatellites()
    {
        return satellites;
    }

    public synchronized void setSatellites(int satellites)
    {
        this.satellites = satellites;
        setChanged();
        notifyObservers();
    }

    public synchronized float getAltitude()
    {
        return altitude;
    }

    public synchronized void setAltitude(float altitude)
    {
        this.altitude = altitude;
        setChanged();
        notifyObservers();
    }

    public synchronized float getAngle()
    {
        return angle;
    }

    public synchronized void setAngle(float angle)
    {
        this.angle = angle;
        setChanged();
        notifyObservers();
    }

    public synchronized float getRoll()
    {
        return roll;
    }

    public synchronized void setRoll(int roll)
    {
        this.roll = roll;
        setChanged();
        notifyObservers();
    }

    public synchronized float getPitch()
    {
        return pitch;
    }

    public synchronized void setPitch(int pitch)
    {
        this.pitch = pitch;
        setChanged();
        notifyObservers();
    }

    public synchronized float getVoltage()
    {
        return voltage;
    }

    public synchronized void setVoltage(float voltage)
    {
        this.voltage = voltage;
        setChanged();
        notifyObservers();
    }

    /**
     * Compare keys to controll values coming in from arduino, and puts correct
     * value to correct variable.
     */
//    public synchronized void handleDataFromRemote()
//    {
//        for (Map.Entry e : data.entrySet())
//        {
//            String key = (String) e.getKey();
//            String value = (String) e.getValue();
//
//            switch (key)
//            {
//                case "Satellites":
//                    setSatellites(Integer.parseInt(value));
//                    break;
//                case "Altitude":
//                    setAltitude(Float.parseFloat(value));
//                    break;
//                case "Angle":
//                    setAngle(Float.parseFloat(value));
//                    break;
//                case "Speed":
//                    setSpeed(Float.parseFloat(value));
//                    break;
//                case "Latitude":
//                    setLatitude(Float.parseFloat(value));
//                    break;
//                case "Longitude":
//                    setLongitude(Float.parseFloat(value));
//                    break;
//                case "Depth":
//                    setDepth(Float.parseFloat(value));
//                    break;
//                case "Temp":
//                    setTemperature(Float.parseFloat(value));
//                    break;
//                case "Roll":
//                    setRoll(Integer.parseInt(value));
//                    break;
//                case "Pitch":
//                    setPitch(Integer.parseInt(value));
//                    break;
//                case "Heading":
//                    setHeading(Integer.parseInt(value));
//                    break;
//            }
}
