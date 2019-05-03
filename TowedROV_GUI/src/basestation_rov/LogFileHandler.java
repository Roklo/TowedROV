/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basestation_rov;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import static java.lang.Math.PI;
import static java.lang.Math.sin;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import ntnusubsea.gui.Data;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author <Robin S. Thorholm>
 */
public class LogFileHandler implements Runnable
{

    //User settings
    int pointFreqMillis = 5000;
    int lenghtOfUmbillicalCord = 500;
    //End of user settings

    Data data;

    long lastTime = 0;
    long timeDifference = 0;

    double adjustedCoordinateRovOffset = ((lenghtOfUmbillicalCord / 100) * 0.000892);

    String shipTrack = "null";
    String Data = "null";
    String photoLocationTrack = "null";

    int shipTrackPointNumb = 1;
    int DataPointNumb = 1;
    int photoLocationNumb = 1;

    String timeStampString = "";

    String logStorageLocation = "C:\\TowedROV\\Log\\";

    String photoPosLog = "";
    String shipPosLog = "";
    String dataLog = "";
    boolean setupIsDone = false;

    File shipPosLogFile = null;
    File dataLogFile = null;

    BufferedWriter outputWriterShipPos = null;
    BufferedWriter outputWriterData = null;

    public LogFileHandler(Data data)
    {
        this.data = data;
    }

    public void run()
    {
        if (!setupIsDone)
        {
            try
            {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
                Date date = new Date(System.currentTimeMillis());

                shipPosLogFile = new File(logStorageLocation + "ShipPos_LOG_" + formatter.format(date) + ".csv");
                FileUtils.touch(shipPosLogFile);

//                File photoPosLog = new File(logStorageLocation + "PhotoPos_LOG_" + formatter.format(date) + ".csv");
//                FileUtils.touch(photoPosLog);
                dataLogFile = new File(logStorageLocation + "Data_LOG_" + formatter.format(date) + ".csv");
                FileUtils.touch(dataLogFile);
//                         
                outputWriterShipPos = new BufferedWriter(new FileWriter(shipPosLogFile));

                outputWriterData = new BufferedWriter(new FileWriter(dataLogFile));

                outputWriterShipPos.append("Point, Time, Latitude, Longtitude, Speed, ROV Depth, Heading");
                outputWriterShipPos.flush();

                outputWriterData.append("Point, Time, Roll, Pitch, Depth,"
                        + " DepthToSeaFloor, ROV Depth, ActuatorPS_feedback,"
                        + "ActuatorSB_feedback, ActuatorPS_command,"
                        + "ActuatorSB_command, Emergency");
                outputWriterData.flush();

                setupIsDone = true;

            } catch (Exception ex)
            {
                System.out.println("ERROR: " + ex);
            }
        }
        if (data.startLogging)
        {
            
            timeStampString = String.valueOf(java.time.LocalTime.now());
            logShipPosition();
            logData();

        }

    }

    private void logPhotoPosition(BufferedWriter bw)
    {
        try
        {
            double shipsLatitude = data.getLatitude();
            double shipsLontitude = data.getLongitude();
            double rovHeadingRelToShip = data.getHeading() - 180;
            if (rovHeadingRelToShip < 0)
            {
                rovHeadingRelToShip = rovHeadingRelToShip + 360;
            }
            //convert to radians

            double normalizedHeading = Math.atan2(Math.sin(rovHeadingRelToShip), Math.cos(rovHeadingRelToShip));
            double rovHeadingRelToShipSin = sin(normalizedHeading * PI / 180);
            double rovHeadingRelToShipSinRest = 0;

            if (rovHeadingRelToShipSin < 0)
            {
                rovHeadingRelToShipSinRest = 1 + rovHeadingRelToShipSin;

            }

            double rovLatitude = 0;
            double rovLongtitude = 0;

            rovLatitude = data.getLatitude() - (adjustedCoordinateRovOffset * rovHeadingRelToShipSinRest);
            if (rovHeadingRelToShipSinRest != 0)
            {
                rovLongtitude = data.getLongitude() + (adjustedCoordinateRovOffset * rovHeadingRelToShipSin);
            } else
            {
                rovLongtitude = data.getLongitude();
            }

            photoLocationTrack = "";
            photoLocationTrack = photoLocationNumb + ","
                    + data.getLatitude() + "," + data.getLongitude() + ","
                    + data.getSpeed() + "," + data.rovDepth;

            bw.append(photoLocationTrack);
            bw.append('\n');
            bw.flush();

            photoLocationNumb++;
        } catch (Exception e)
        {
            System.out.println("Error: " + e);
        }

    }

    public void closeLog()
    {
        try
        {
            outputWriterShipPos.close();
            outputWriterData.close();
        } catch (Exception e)
        {
            System.out.println("Problem closing log file");
        }

    }

    private void logData()
    {
        try
        {
            dataLog = "";
            dataLog = DataPointNumb + "," + timeStampString + ","
                    + data.getRollAngle() + "," + data.getPitchAngle() + ","
                    + data.getDepth() + "," + data.getDepthBeneathRov() + ","
                    + data.getRovDepth() + "," + data.getFb_actuatorPSPos() + ","
                    + data.getFb_actuatorSBPos() + data.getFb_actuatorPScmd() + ","
                    + data.getFb_actuatorSBcmd();
            outputWriterData.append('\n');
            outputWriterData.append(shipTrack);
            outputWriterData.flush();
            shipTrackPointNumb++;
        } catch (Exception e)
        {
        }
    }

    private void logErrorMessages()
    {

    }

    private void logShipPosition()
    {
        try
        {
            shipTrack = "";
            shipTrack = shipTrackPointNumb + "," + timeStampString + ","
                    + data.getLatitude() + "," + data.getLongitude() + ","
                    + data.getSpeed() + "," + data.rovDepth + "," + data.getHeading();
            outputWriterShipPos.append('\n');
            outputWriterShipPos.append(shipTrack);
            outputWriterShipPos.flush();
            shipTrackPointNumb++;

        } catch (Exception e)
        {
            System.out.println("Error: " + e);
        }
    }
}
