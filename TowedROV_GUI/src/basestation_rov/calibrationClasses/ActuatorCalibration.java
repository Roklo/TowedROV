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
public class ActuatorCalibration implements Runnable
{

    final static String actuatorEndPos = "254";
    final static String actuatorMiddlePos = "127";
    final static String actuatorStartPos = "1";
    final static int accuracy = 4;
    int posChangeTime = 8000;  // millisecond

    int lastActuatorPSPos = 0;
    int lastActuatorSBPos = 0;
    boolean findingMinPS = true;
    boolean findingMinSB = true;
    boolean findingMaxPS = true;
    boolean findingMaxSB = true;

    long currentTime = 0;
    long lastTimePS = 0;
    long lastTimeSB = 0;

    long PSActuatorMaxToMinTime = 0;
    long SBActuatorMaxToMinTime = 0;

    //Error List
    //PS actuator
    boolean Error_PSActuatorNotInMinPos = false;
    boolean Error_PSActuatorNotInMaxPos = false;
    boolean Error_PSActuatorTooSlow = false;

    //SB actuator
    boolean Error_SBActuatorNotInMinPos = false;
    boolean Error_SBActuatorNotInMaxPos = false;
    boolean Error_SBActuatorTooSlow = false;

    Data data;
    TCPClient client_ROV;

    public ActuatorCalibration(Data data, TCPClient client_ROV)
    {
        this.data = data;
        this.client_ROV = client_ROV;
    }

    @Override
    public void run()
    {
        calibrateMinPos();
        speedFromMinToMax();
    }

    private void calibrateMinPos()
    {
        try
        {
            client_ROV.sendCommand("<cmd_actuatorPS:" + actuatorStartPos + ">");
            client_ROV.sendCommand("<cmd_actuatorSB:" + actuatorStartPos + ">");
            boolean waitingForPSPosChange = true;
            boolean waitingForSBPosChange = true;
            lastTimePS = System.currentTimeMillis();
            lastTimeSB = System.currentTimeMillis();
            while (waitingForPSPosChange || waitingForSBPosChange)
            {
                if (System.currentTimeMillis() - lastTimePS >= posChangeTime || !waitingForPSPosChange)
                {
                    client_ROV.sendCommand("<cmd_actuatorPS:" + data.getFb_actuatorPSPos() + ">");
                    System.out.println("Error: PS_Actuator did not reach min pos in time");
                    waitingForPSPosChange = false;
                    Error_PSActuatorNotInMinPos = true;
                }
                if (data.getFb_actuatorPSPos() < 3)
                {
                    System.out.println("PS actuator in position");
                    waitingForPSPosChange = false;
                }

                if (System.currentTimeMillis() - lastTimeSB >= posChangeTime || !waitingForPSPosChange)
                {
                    client_ROV.sendCommand("<cmd_actuatorSB:" + data.getFb_actuatorSBPos() + ">");
                    System.out.println("Error: SB_Actuator did not reach min pos in time");
                    waitingForSBPosChange = false;
                    Error_PSActuatorNotInMinPos = true;
                }
                if (data.getFb_actuatorSBPos() < 3)
                {
                    System.out.println("SB actuator in position");
                    waitingForSBPosChange = false;
                }
            }

        } catch (Exception e)
        {
        }
    }

    private void speedFromMinToMax()
    {
        try
        {

            client_ROV.sendCommand("<cmd_actuatorPS:" + actuatorEndPos + ">");
            client_ROV.sendCommand("<cmd_actuatorSB:" + actuatorEndPos + ">");

            boolean waitingForPSPosChange = true;
            boolean waitingForSBPosChange = true;
            lastTimePS = System.currentTimeMillis();
            lastTimeSB = System.currentTimeMillis();
            while (waitingForPSPosChange || waitingForSBPosChange)
            {
                if (System.currentTimeMillis() - lastTimePS >= posChangeTime || !waitingForPSPosChange)
                {
                    client_ROV.sendCommand("<cmd_actuatorPS:" + data.getFb_actuatorPSPos() + ">");
                    System.out.println("Error: PS_Actuator did not reach max pos in time");
                    waitingForPSPosChange = false;
                    Error_PSActuatorNotInMaxPos = true;
                }
                if (data.getFb_actuatorPSPos() > 250)
                {
                    data.setPSActuatorMaxToMinTime(System.currentTimeMillis() - lastTimePS);
                    System.out.println("PS actuator in position");
                    waitingForPSPosChange = false;
                }

                if (System.currentTimeMillis() - lastTimeSB >= posChangeTime || !waitingForSBPosChange)
                {
                    client_ROV.sendCommand("<cmd_actuatorSB:" + data.getFb_actuatorSBPos() + ">");
                    System.out.println("Error: SB_Actuator did not reach max pos in time");
                    waitingForSBPosChange = false;
                    Error_SBActuatorNotInMaxPos = true;
                }
                if (data.getFb_actuatorSBPos() > 250)
                {
                    data.setSBActuatorMaxToMinTime(System.currentTimeMillis() - lastTimeSB);
                    System.out.println("SB actuator in position");
                    waitingForPSPosChange = false;
                }
            }
        } catch (Exception e)
        {
        }
    }
}
