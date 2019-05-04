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
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * The data class is a storage box that let's the different threads change and
 * retrieve various data. The data class is a subclass of the java class
 * Observable, which makes it possible for observers to subscribe and update
 * their values whenever they change. Data is made thread safe by the use of
 * synchronized methods.
 *
 */
public final class Data extends Observable
{

    public HashMap<String, String> comPortList = new HashMap<>();
    public ConcurrentHashMap<String, Boolean> completeAlarmListDh = new ConcurrentHashMap<>();

    //------------------------
    //Do not change the times, this is measured movement time without oil
    private static final long actuatorPSInitialMovementTime = 8000;
    private static final long actuatorSBInitialMovementTime = 8000;
    //------------------------
    private static final int actuatorTolerableSpeedLoss = 10; //Percent
    private long PSActuatorMaxToMinTime;
    private long SBActuatorMaxToMinTime;

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
    public double voltage = (double) 40.00;

    // Feedback from IMU
    public double roll = 0;
    public double pitch = 0;
    public float heading = 100;

    // Feedback from the Camera RPi
    private boolean leakStatus = false;
    private double rovDepth;
    private double pressure = 0.0;
    private double outsideTemp = 0.0;
    private double insideTemp = 0.0;
    private double humidity = 0.0;

    // Feedback from ROV
    private boolean rovReady;
    private boolean i2cError;
    private int fb_actuatorPSPos;
    private int fb_actuatorSBPos;
    private int fb_actuatorPScmd;
    private int fb_actuatorSBcmd;

    private int fb_actuatorPSMinPos;
    private int fb_actuatorSBMinPos;
    private int fb_actuatorPSMaxPos;
    private int fb_actuatorSBMaxPos;

    // Feedback from GUI
    public boolean startLogging = true;

    public ConcurrentHashMap<String, String> data = new ConcurrentHashMap<>();
    public List<String> rovDepthDataList = new ArrayList<>();
    public List<String> depthBeneathBoatDataList = new ArrayList<>();

    private double timeBetweenBoatAndRov = 4.0;
    private float depthBeneathRov = 0;
    private float depthBeneathBoat = 0;
    private int pitchAngle = 0;
    private float wingAngle = 0;
    private int rollAngle = 0;
    private float channel1 = 0;
    private float channel2 = 0;
    private float channel3 = 0;
    private float channel4 = 0;
    private byte actuatorStatus = 0;
    private ArrayList<String> labels = new ArrayList();
    private float[] channelValues = new float[4];
    private String IP_Rov = "";
    private String IP_Camera = "";
    private BufferedImage videoImage;
    private String Kp;
    private String Ki;
    private String Kd;
    private long timer = System.currentTimeMillis();
    private boolean photoMode = false;
    private double photoModeDelay = 1.00;
    private double photoModeDelay_FB = 1.00;
    private int imageNumber = 0;
    private int cameraPitchValue = 0;
    private boolean doRovCalibration = false;
    private boolean emergencyMode = false;

    /**
     * Creates an object of the class Data.
     */
    public Data()
    {
        try
        {
            BufferedReader br = new BufferedReader(new FileReader("ROV Options.txt"));
            //this.updateRovDepthDataList();
            IP_Rov = br.readLine();
            IP_Camera = br.readLine();
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
            videoImage = ImageIO.read(getClass().getResource("/ntnusubsea/gui/Images/TowedROV.jpg"));
        } catch (IOException ex)
        {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Sets the default ROV IP
     *
     * @param ip The default ROV IP
     */
    public synchronized void setIP_Rov(String ip)
    {
        this.IP_Rov = ip;
        setChanged();
        notifyObservers();
    }

    /**
     * Returns the default ROV IP
     *
     * @return The default ROV IP
     */
    public synchronized String getIP_Rov()
    {
        return IP_Rov;
    }

    /**
     * Sets the default Camera IP
     *
     * @param ip The default Camera IP
     */
    public synchronized void setIP_Camera(String ip)
    {
        this.IP_Camera = ip;
        setChanged();
        notifyObservers();
    }

    /**
     * Returns the default Camera IP
     *
     * @return The default Camera IP
     */
    public synchronized String getIP_Camera()
    {
        return IP_Camera;
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
    public synchronized void setPitchAngle(int angle)
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
    public synchronized int getPitchAngle()
    {
        return pitchAngle;
    }

    /**
     * Updates the current roll angle of the ROV
     *
     * @param angle Current roll angle of the ROV
     */
    public synchronized void setRollAngle(int angle)
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
    public synchronized int getRollAngle()
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

    public double getTimeBetweenBoatAndRov()
    {
        return timeBetweenBoatAndRov;
    }

    public void setTimeBetweenBoatAndRov(double timeBetweenBoatAndRov)
    {
        this.timeBetweenBoatAndRov = timeBetweenBoatAndRov;
    }

    /**
     * Updates the current depth beneath the ROV
     *
     * @param depth Depth beneath the ROV
     */
    public synchronized void setDepthBeneathRov(float depth)
    {
        depthBeneathRov = depth;
        setChanged();
        notifyObservers();
    }

    /**
     * Returns the depth beneath the ROV
     *
     * @return Depth beneath the ROV
     */
    public synchronized float getDepthBeneathRov()
    {
        return depthBeneathRov;
    }

    /**
     * Updates the current depth beneath the vessel
     *
     * @param depth Depth beneath the vessel
     */
    public synchronized void setDepthBeneathBoat(float depth)
    {
        depthBeneathBoat = depth;
        setChanged();
        notifyObservers();
    }

    /**
     * Returns the depth beneath the vessel
     *
     * @return Depth beneath the vessel
     */
    public synchronized float getDepthBeneathBoat()
    {
        return depthBeneathBoat;
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
    public synchronized void setLeakStatus(boolean leak)
    {
        leakStatus = leak;
        if (!leak)
        {
            setEmergencyMode(false);
        }
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
        return leakStatus;
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
    public synchronized void setPressure(double pres)
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
    public synchronized double getPressure()
    {
        return pressure;
    }

    public double getOutsideTemp()
    {
        return outsideTemp;
    }

    public void setOutsideTemp(double outsideTemp)
    {
        this.outsideTemp = outsideTemp;
    }

    public double getInsideTemp()
    {
        return insideTemp;
    }

    public void setInsideTemp(double insideTemp)
    {
        this.insideTemp = insideTemp;
    }

    public double getHumidity()
    {
        return humidity;
    }

    public void setHumidity(double humidity)
    {
        this.humidity = humidity;
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

    public void setEmergencyMode(boolean status)
    {
        this.emergencyMode = status;
        setChanged();
        notifyObservers();
    }

    public boolean isEmergencyMode()
    {
        return this.emergencyMode;
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

    public synchronized double getRoll()
    {
        return roll;
    }

    public synchronized void setRoll(double roll)
    {
        this.roll = roll;
        setChanged();
        notifyObservers();
    }

    public synchronized double getPitch()
    {
        return pitch;
    }

    public synchronized void setPitch(double pitch)
    {
        this.pitch = pitch;
        setChanged();
        notifyObservers();
    }

    public synchronized double getVoltage()
    {
        return voltage;
    }

    public synchronized void setVoltage(double voltage)
    {
        this.voltage = voltage;
        setChanged();
        notifyObservers();
    }

    public boolean getStartLogging()
    {
        return startLogging;
    }

    public void setStartLogging(boolean startLogging)
    {
        this.startLogging = startLogging;
        setChanged();
        notifyObservers();
    }

    public boolean isRovReady()
    {
        return rovReady;
    }

    public void setRovReady(boolean rovReady)
    {
        this.rovReady = rovReady;
    }

    public boolean isI2cError()
    {
        return i2cError;
    }

    public void setI2cError(boolean i2cError)
    {
        this.i2cError = i2cError;
    }

    public Double getRovDepth()
    {
        return rovDepth;
    }

    public void setRovDepth(Double rovDepth)
    {
        this.rovDepth = rovDepth;
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

    public int getFb_actuatorPSMinPos()
    {
        return fb_actuatorPSMinPos;
    }

    public void setFb_actuatorPSMinPos(int fb_actuatorPSMinPos)
    {
        this.fb_actuatorPSMinPos = fb_actuatorPSMinPos;
    }

    public int getFb_actuatorSBMinPos()
    {
        return fb_actuatorSBMinPos;
    }

    public void setFb_actuatorSBMinPos(int fb_actuatorSBMinPos)
    {
        this.fb_actuatorSBMinPos = fb_actuatorSBMinPos;
    }

    public int getFb_actuatorPSMaxPos()
    {
        return fb_actuatorPSMaxPos;
    }

    public void setFb_actuatorPSMaxPos(int fb_actuatorPSMaxPos)
    {
        this.fb_actuatorPSMaxPos = fb_actuatorPSMaxPos;
    }

    public int getFb_actuatorSBMaxPos()
    {
        return fb_actuatorSBMaxPos;
    }

    public void setFb_actuatorSBMaxPos(int fb_actuatorSBMaxPos)
    {
        this.fb_actuatorSBMaxPos = fb_actuatorSBMaxPos;
    }

    public long getPSActuatorMaxToMinTime()
    {
        return PSActuatorMaxToMinTime;
    }

    public void setPSActuatorMaxToMinTime(long PSActuatorMaxToMinTime)
    {
        this.PSActuatorMaxToMinTime = PSActuatorMaxToMinTime;
    }

    public long getSBActuatorMaxToMinTime()
    {
        return SBActuatorMaxToMinTime;
    }

    public void setSBActuatorMaxToMinTime(long SBActuatorMaxToMinTime)
    {
        this.SBActuatorMaxToMinTime = SBActuatorMaxToMinTime;
    }

    public void updateRovDepthDataList(String time, String value)
    {
        if (rovDepthDataList.size() >= 300)
        {
            rovDepthDataList.remove(0);
        }
        this.rovDepthDataList.add(time + ":" + value);
    }

    public void updateDepthBeneathBoatDataList(String time, String value)
    {
        if (depthBeneathBoatDataList.size() >= 300)
        {
            depthBeneathBoatDataList.remove(0);
        }
        this.depthBeneathBoatDataList.add(time + ":" + value);
    }

    public int getFb_actuatorPScmd()
    {
        return fb_actuatorPScmd;
    }

    public void setFb_actuatorPScmd(int fb_actuatorPScmd)
    {
        this.fb_actuatorPScmd = fb_actuatorPScmd;
    }

    public int getFb_actuatorSBcmd()
    {
        return fb_actuatorSBcmd;
    }

    public void setFb_actuatorSBcmd(int fb_actuatorSBcmd)
    {
        this.fb_actuatorSBcmd = fb_actuatorSBcmd;
    }
    
    
}
