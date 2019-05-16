/*
 * This code is for the bachelor thesis named "Towed-ROV".
 * The purpose is to build a ROV which will be towed behind a surface vessel
 * and act as a multi-sensor platform, were it shall be easy to place new 
 * sensors. There will also be a video stream from the ROV.
 * 
 * The system consists of two Raspberry Pis in the ROV that is connected to
 * several Arduino micro controllers. These micro controllers are connected to
 * feedback from the actuators, the echo sounder and extra optional sensors.
 * The external computer which is on the surface vessel is connected to a GPS,
 * echo sounder over USB, and the ROV over ethernet. It will present and
 * log data in addition to handle user commands for controlling the ROV.
 */
package basestation_rov.calibrationClasses;

import ntnusubsea.gui.*;

/**
 * Responsible for calibrating the actuators. Not currently in use/finished.
 */
public class ActuatorCalibration implements Runnable {

    private final static String actuatorEndPos = "254";
    private final static String actuatorMiddlePos = "127";
    private final static String actuatorStartPos = "1";
    private final static int accuracy = 4;
    private int posChangeTime = 8000;  // millisecond
    private int lastActuatorPSPos = 0;
    private int lastActuatorSBPos = 0;
    private boolean findingMinPS = true;
    private boolean findingMinSB = true;
    private boolean findingMaxPS = true;
    private boolean findingMaxSB = true;
    private long currentTime = 0;
    private long lastTimePS = 0;
    private long lastTimeSB = 0;
    private long PSActuatorMaxToMinTime = 0;
    private long SBActuatorMaxToMinTime = 0;

    //Error List
    //PS actuator
    private boolean Error_PSActuatorNotInMinPos = false;
    private boolean Error_PSActuatorNotInMaxPos = false;
    private boolean Error_PSActuatorTooSlow = false;

    //SB actuator
    private boolean Error_SBActuatorNotInMinPos = false;
    private boolean Error_SBActuatorNotInMaxPos = false;
    private boolean Error_SBActuatorTooSlow = false;

    private Data data;
    private TCPClient client_ROV;

    /**
     * The constructor of the ActuatorCalibration class.
     *
     * @param data the shared resource Data class
     * @param client_ROV The ROV TCP client
     */
    public ActuatorCalibration(Data data, TCPClient client_ROV) {
        this.data = data;
        this.client_ROV = client_ROV;
    }

    /**
     * Runs the ActuatorCalibration thread.
     */
    @Override
    public void run() {
        calibrateMinPos();
        speedFromMinToMax();
    }

    /**
     * Calibrates the minimum position of the actuator.
     */
    private void calibrateMinPos() {
        try {
            client_ROV.sendCommand("<cmd_actuatorPS:" + actuatorStartPos + ">");
            client_ROV.sendCommand("<cmd_actuatorSB:" + actuatorStartPos + ">");
            boolean waitingForPSPosChange = true;
            boolean waitingForSBPosChange = true;
            lastTimePS = System.currentTimeMillis();
            lastTimeSB = System.currentTimeMillis();
            while (waitingForPSPosChange || waitingForSBPosChange) {
                if (System.currentTimeMillis() - lastTimePS >= posChangeTime || !waitingForPSPosChange) {
                    client_ROV.sendCommand("<cmd_actuatorPS:" + data.getFb_actuatorPSPos() + ">");
                    System.out.println("Error: PS_Actuator did not reach min pos in time");
                    waitingForPSPosChange = false;
                    Error_PSActuatorNotInMinPos = true;
                }
                if (data.getFb_actuatorPSPos() < 3) {
                    System.out.println("PS actuator in position");
                    waitingForPSPosChange = false;
                }

                if (System.currentTimeMillis() - lastTimeSB >= posChangeTime || !waitingForPSPosChange) {
                    client_ROV.sendCommand("<cmd_actuatorSB:" + data.getFb_actuatorSBPos() + ">");
                    System.out.println("Error: SB_Actuator did not reach min pos in time");
                    waitingForSBPosChange = false;
                    Error_PSActuatorNotInMinPos = true;
                }
                if (data.getFb_actuatorSBPos() < 3) {
                    System.out.println("SB actuator in position");
                    waitingForSBPosChange = false;
                }
            }

        } catch (Exception e) {
        }
    }

    /**
     * Finds the speed from minimum to maximum position.
     */
    private void speedFromMinToMax() {
        try {

            client_ROV.sendCommand("<cmd_actuatorPS:" + actuatorEndPos + ">");
            client_ROV.sendCommand("<cmd_actuatorSB:" + actuatorEndPos + ">");

            boolean waitingForPSPosChange = true;
            boolean waitingForSBPosChange = true;
            lastTimePS = System.currentTimeMillis();
            lastTimeSB = System.currentTimeMillis();
            while (waitingForPSPosChange || waitingForSBPosChange) {
                if (System.currentTimeMillis() - lastTimePS >= posChangeTime || !waitingForPSPosChange) {
                    client_ROV.sendCommand("<cmd_actuatorPS:" + data.getFb_actuatorPSPos() + ">");
                    System.out.println("Error: PS_Actuator did not reach max pos in time");
                    waitingForPSPosChange = false;
                    Error_PSActuatorNotInMaxPos = true;
                }
                if (data.getFb_actuatorPSPos() > 250) {
                    data.setPSActuatorMaxToMinTime(System.currentTimeMillis() - lastTimePS);
                    System.out.println("PS actuator in position");
                    waitingForPSPosChange = false;
                }

                if (System.currentTimeMillis() - lastTimeSB >= posChangeTime || !waitingForSBPosChange) {
                    client_ROV.sendCommand("<cmd_actuatorSB:" + data.getFb_actuatorSBPos() + ">");
                    System.out.println("Error: SB_Actuator did not reach max pos in time");
                    waitingForSBPosChange = false;
                    Error_SBActuatorNotInMaxPos = true;
                }
                if (data.getFb_actuatorSBPos() > 250) {
                    data.setSBActuatorMaxToMinTime(System.currentTimeMillis() - lastTimeSB);
                    System.out.println("SB actuator in position");
                    waitingForPSPosChange = false;
                }
            }
        } catch (Exception e) {
        }
    }
}
