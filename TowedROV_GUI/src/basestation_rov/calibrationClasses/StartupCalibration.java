/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basestation_rov.calibrationClasses;

import ntnusubsea.gui.*;

/**
 *
 * @author <Robin S. Thorholm>
 */
public class StartupCalibration
{

    private static Thread actuatorCalibrationThread;
    Data data = null;
    TCPClient client_ROV = null;
    TCPClient client_Camera = null;
    long currentTime = 0;
    long lastTimePS = 0;
    long lastTimeSB = 0;

    public StartupCalibration(Data data, TCPClient client_ROV)
    {
        this.data = data;
        this.client_ROV = client_ROV;
        this.client_Camera = client_Camera;

    }

    public String doStartupCalibration()
    {
        calibrateActuators();
        //testLights();

        return "Calibration complete...";
    }

    public void setupComPorts()
    {

    }

    public void calibrateActuators()
    {
        actuatorCalibrationThread = new Thread(new ActuatorCalibration(data, client_ROV));
        actuatorCalibrationThread.start();
        actuatorCalibrationThread.setName("actuatorCalibrationThread");
    }

    //Test lights
//    public void testLights()
//    {
//        boolean testingLights = true;
//        long lastTime = System.nanoTime();
//        int lightIntensity = 1;
//        while (testingLights && lightIntensity < 100)
//        {
//            if (System.nanoTime() - lastTime >= 250000000)
//            {
//                dh.cmd_lightIntensity = lightIntensity + 1;
//                lastTime = System.nanoTime();
//            }
//
//        }
//        try
//        {
//            Thread.sleep(5000);
//            dh.cmd_lightIntensity = 0;
//        } catch (Exception e)
//        {
//        }
//
//    }
//Calibrate depth sensor here:
}
