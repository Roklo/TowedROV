/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ROV;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author <Robin S. Thorholm>
 */
public class DataHandler extends Observable
{

    // Calibration values
    int pressureSensorOffset = 0;

    // Command values
    int cmd_lightIntensity = 0;
    int cmd_actuatorPS = 0;
    int cmd_actuatorSB = 0;
    int cmd_actuatorPSMaxPos = 0;
    int cmd_actuatorPSMinPos = 0;
    int cmd_actuatorSBMaxPos = 0;
    int cmd_actuatorSBMinPos = 0;
    int cmd_BlueLED = 0;

    int cmd_pressureAtSeaLevel = 0;

    byte cmd_mode = 0; // Mode 0 = depth, 1 = seafloor, 2 = manual
    int cmd_depth = 0;
    int cmd_cameraPitch = 0;
    int cmd_cameraRoll = 0;
    byte cmd_cameraMode = 0;

    double cmd_pid_p = 0;
    double cmd_pid_i = 0;
    double cmd_pid_d = 0;
    double cmd_pid_gain = 0;

    boolean cmd_emergencySurface = false;
    boolean cmd_ack = false;
    boolean cmd_manualWingControl = false;

    int cmd_imuCalibrateRoll = 0;
    int cmd_imuCalibratePitch = 0;

    // Sensor values
    int fb_depthToSeabedEcho = 12;
    int fb_depthFromPressure = 0;
    int fb_depthBelowTransduser = 0;
    int fb_depth = 0;

    int fb_speedThroughWather = 0;
    int fb_waterTemperature = 0;
    int fb_pressure = 0;
    int fb_actuatorPSPos = 0;
    int fb_actuatorSBPos = 0;
    int fb_tempInternalCamera = 0;
    int fb_humidityInternalCamera = 0;
    int fb_tempPSactuatorBox = 0;
    int fb_tempSBactuatorBox = 0;
    double fb_tempMainElBoxFront = 0;
    double fb_tempMainElBoxRear = 0;
    int fb_tempEchoBox = 0;
    int fb_currentDraw = 0;
    int fb_pitch = -1;
    int fb_roll = -3;
    int fb_yaw = 0;
    int fb_heading = 0;

    boolean fb_waterLeakChannel_1 = false;
    boolean fb_waterLeakChannel_2 = false;
    boolean fb_waterLeakChannel_3 = false;
    boolean fb_waterLeakChannel_4 = false;

    //Input channels
    double analogInputChannel_1 = 0.00;
    double analogInputChannel_2 = 0.00;
    double analogInputChannel_3 = 0.00;
    double analogInputChannel_4 = 0.00;
    boolean digitalInputChannel_5 = false;
    boolean digitalInputChannel_6 = false;

    //Class variables
    private int counter = 0;
    private boolean i2cRequest = false;

    public HashMap<String, String> comPortList = new HashMap<>();

    public HashMap<String, String> data = new HashMap<>();

    public ConcurrentHashMap<String, Boolean> completeAlarmListDh = new ConcurrentHashMap<>();

    public int getCmd_actuatorPSMaxPos()
    {
        return cmd_actuatorPSMaxPos;
    }

    public void setCmd_actuatorPSMaxPos(int cmd_actuatorPSMaxPos)
    {
        this.cmd_actuatorPSMaxPos = cmd_actuatorPSMaxPos;
    }

    public int getCmd_actuatorPSMinPos()
    {
        return cmd_actuatorPSMinPos;
    }

    public void setCmd_actuatorPSMinPos(int cmd_actuatorPSMinPos)
    {
        this.cmd_actuatorPSMinPos = cmd_actuatorPSMinPos;
    }

    public int getCmd_actuatorSBMaxPos()
    {
        return cmd_actuatorSBMaxPos;
    }

    public void setCmd_actuatorSBMaxPos(int cmd_actuatorSBMaxPos)
    {
        this.cmd_actuatorSBMaxPos = cmd_actuatorSBMaxPos;
    }

    public int getCmd_actuatorSBMinPos()
    {
        return cmd_actuatorSBMinPos;
    }

    public void setCmd_actuatorSBMinPos(int cmd_actuatorSVMinPos)
    {
        this.cmd_actuatorSBMinPos = cmd_actuatorSVMinPos;
    }

    public boolean isCmd_ack()
    {
        return cmd_ack;
    }

    public void setCmd_ack(boolean cmd_ack)
    {
        this.cmd_ack = cmd_ack;
    }

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

    public double getFb_tempMainElBoxFront()
    {
        return fb_tempMainElBoxFront;
    }

    public void setFb_tempMainElBoxFront(double fb_tempMainElBoxFront)
    {
        this.fb_tempMainElBoxFront = fb_tempMainElBoxFront;
    }

    public double getFb_tempMainElBoxRear()
    {
        return fb_tempMainElBoxRear;
    }

    public void setFb_tempMainElBoxRear(double fb_tempMainElBoxRear)
    {
        this.fb_tempMainElBoxRear = fb_tempMainElBoxRear;
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
        return fb_pitch + getCmd_imuCalibratePitch();
    }

    public void setFb_pitch(int fb_pitch)
    {
        this.fb_pitch = fb_pitch;
    }

    public int getFb_roll()
    {
        return fb_roll + getCmd_imuCalibrateRoll();
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

    public int getCmd_lightIntensity()
    {
        return cmd_lightIntensity;
    }

    public void setCmd_lightIntensity(int cmd_lightIntensity)
    {
        this.cmd_lightIntensity = cmd_lightIntensity;
    }

    public int getCmd_actuatorPS()
    {
        return cmd_actuatorPS;
    }

    public void setCmd_actuatorPS(int cmd_actuatorPS)
    {
        this.cmd_actuatorPS = cmd_actuatorPS;
        setChanged();
        notifyObservers();

    }

    public int getCmd_actuatorSB()
    {
        return cmd_actuatorSB;
    }

    public void setCmd_actuatorSB(int cmd_actuatorSB)
    {
        this.cmd_actuatorSB = cmd_actuatorSB;
        setChanged();
        notifyObservers();
    }

    public byte getCmd_mode()
    {
        return cmd_mode;
    }

    public void setCmd_mode(byte cmd_mode)
    {
        this.cmd_mode = cmd_mode;
    }

    public int getCmd_depth()
    {
        return cmd_depth;
    }

    public void setCmd_depth(int cmd_depth)
    {
        this.cmd_depth = cmd_depth;
    }

    public int getCmd_cameraPitch()
    {
        return cmd_cameraPitch;
    }

    public void setCmd_cameraPitch(int cmd_cameraPitch)
    {
        this.cmd_cameraPitch = cmd_cameraPitch;
    }

    public int getCmd_cameraRoll()
    {
        return cmd_cameraRoll;
    }

    public void setCmd_cameraRoll(int cmd_cameraRoll)
    {
        this.cmd_cameraRoll = cmd_cameraRoll;
    }

    public byte getCmd_cameraMode()
    {
        return cmd_cameraMode;
    }

    public void setCmd_cameraMode(byte cmd_cameraMode)
    {
        this.cmd_cameraMode = cmd_cameraMode;
    }

    public double getCmd_pid_p()
    {
        return cmd_pid_p;
    }

    public void setCmd_pid_p(double cmd_pid_p)
    {
        this.cmd_pid_p = cmd_pid_p;
    }

    public double getCmd_pid_i()
    {
        return cmd_pid_i;
    }

    public void setCmd_pid_i(double cmd_pid_i)
    {
        this.cmd_pid_i = cmd_pid_i;
    }

    public double getCmd_pid_d()
    {
        return cmd_pid_d;
    }

    public void setCmd_pid_d(double cmd_pid_d)
    {
        this.cmd_pid_d = cmd_pid_d;
    }

    public double getCmd_pid_gain()
    {
        return cmd_pid_gain;
    }

    public void setCmd_pid_gain(double cmd_pid_gain)
    {
        this.cmd_pid_gain = cmd_pid_gain;
    }

    public boolean isCmd_emergencySurface()
    {
        return cmd_emergencySurface;
    }

    public void setCmd_emergencySurface(boolean cmd_emergencySurface)
    {
        this.cmd_emergencySurface = cmd_emergencySurface;
    }

    public boolean isCmd_manualWingControl()
    {
        return cmd_manualWingControl;
    }

    public void setCmd_manualWingControl(boolean cmd_manualWingControl)
    {
        this.cmd_manualWingControl = cmd_manualWingControl;
    }

    public int getFb_depthBelowTransduser()
    {
        return fb_depthBelowTransduser;
    }

    public void setFb_depthBelowTransduser(int fb_depthBelowTransduser)
    {
        this.fb_depthBelowTransduser = fb_depthBelowTransduser;

    }

    public int getFb_depth()
    {
        return fb_depth;
    }

    public void setFb_depth(int fb_depth)
    {
        this.fb_depth = fb_depth;
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

    public double getAnalogInputChannel_1()
    {
        return analogInputChannel_1;
    }

    public void setAnalogInputChannel_1(double analogInputChannel_1)
    {
        this.analogInputChannel_1 = analogInputChannel_1;
    }

    public double getAnalogInputChannel_2()
    {
        return analogInputChannel_2;
    }

    public void setAnalogInputChannel_2(double analogInputChannel_2)
    {
        this.analogInputChannel_2 = analogInputChannel_2;
    }

    public double getAnalogInputChannel_3()
    {
        return analogInputChannel_3;
    }

    public void setAnalogInputChannel_3(double analogInputChannel_3)
    {
        this.analogInputChannel_3 = analogInputChannel_3;
    }

    public double getAnalogInputChannel_4()
    {
        return analogInputChannel_4;
    }

    public void setAnalogInputChannel_4(double analogInputChannel_4)
    {
        this.analogInputChannel_4 = analogInputChannel_4;
    }

    public boolean isDigitalInputChannel_5()
    {
        return digitalInputChannel_5;
    }

    public void setDigitalInputChannel_5(boolean digitalInputChannel_5)
    {
        this.digitalInputChannel_5 = digitalInputChannel_5;
    }

    public boolean isDigitalInputChannel_6()
    {
        return digitalInputChannel_6;
    }

    public void setDigitalInputChannel_6(boolean digitalInputChannel_6)
    {
        this.digitalInputChannel_6 = digitalInputChannel_6;
    }

    public int getCmd_imuCalibrateRoll()
    {
        return cmd_imuCalibrateRoll;
    }

    public void setCmd_imuCalibrateRoll(int cmd_imuCalibrateRoll)
    {
        this.cmd_imuCalibrateRoll = cmd_imuCalibrateRoll;
    }

    public int getCmd_imuCalibratePitch()
    {
        return cmd_imuCalibratePitch;
    }

    public void setCmd_imuCalibratePitch(int cmd_imuCalibratePitch)
    {
        this.cmd_imuCalibratePitch = cmd_imuCalibratePitch;
    }

    public int getCmd_BlueLED()
    {
        return cmd_BlueLED;
    }

    public void setCmd_BlueLED(int cmd_BlueLED)
    {
        this.cmd_BlueLED = cmd_BlueLED;
        setChanged();
        notifyObservers();
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

    public void handleDataFromTCP(String data)
    {

    }

}
