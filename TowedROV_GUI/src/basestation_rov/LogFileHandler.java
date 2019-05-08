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
    String telementry = "null";
    String photoLocationTrack = "null";
    String exif = "null";

    int shipTrackPointNumb = 1;
    int DataPointNumb = 1;
    int photoLocationNumb = 1;

    String timeStampString = "";
    SimpleDateFormat timeAndDateCSV;
    SimpleDateFormat exifDateAndTime;

    String logStorageLocation = "C:\\TowedROV\\Log\\";

    String photoPosLog = "";
    String shipPosLog = "";
    String dataLog = "";
    String telementryLog = "";
    String exifLog = "";
    boolean setupIsDone = false;

    File shipPosLogFile = null;
    File dataLogFile = null;
    File telementryLogFile = null;
    File exifLogFile = null;

    Date date;
    Date dateCSV;
    Date dateExif;

    BufferedWriter outputWriterShipPos = null;
    BufferedWriter outputWriterData = null;
    BufferedWriter outputWriterTelementry = null;
    BufferedWriter outputWriterExif = null;

    int lastImageNumber = 0;
    boolean exifSetup = false;

    public LogFileHandler(Data data)
    {
        this.data = data;
    }

    public void run()
    {
        if (!exifSetup || data.isImagesCleared())
        {
            try
            {
                lastImageNumber = 0;
                //SimpleDateFormat exifTime = new SimpleDateFormat("yyyyMMddHHmmss");
                exifDateAndTime = new SimpleDateFormat("yyyyMMddHHmmss");
                dateExif = new Date(System.currentTimeMillis());
                exifLogFile = new File(logStorageLocation + "EXIF_LOG_" + exifDateAndTime.format(dateExif) + ".csv");
                FileUtils.touch(exifLogFile);

                outputWriterExif = new BufferedWriter(new FileWriter(exifLogFile));
                outputWriterExif.append("Latitude,Longtitude,Time");
                outputWriterExif.flush();
                exifSetup = true;
                data.setImagesCleared(false);

            } catch (Exception e)
            {
            }

        }
        if (data.getImageNumber() != lastImageNumber)
        {
            exifDateAndTime = new SimpleDateFormat("yyyyMMddHHmmss");
            dateExif = new Date(System.currentTimeMillis());
            exifDateAndTime.format(dateExif);
            lastImageNumber++;
            logExifData();

        }

        if (data.getStartLogging())
        {

            if (!setupIsDone)
            {
                try
                {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
                    date = new Date(System.currentTimeMillis());

                    shipPosLogFile = new File(logStorageLocation + "ShipPos_LOG_" + formatter.format(date) + ".csv");
                    FileUtils.touch(shipPosLogFile);

//                File photoPosLog = new File(logStorageLocation + "PhotoPos_LOG_" + formatter.format(date) + ".csv");
//                FileUtils.touch(photoPosLog);
                    dataLogFile = new File(logStorageLocation + "Data_LOG_" + formatter.format(date) + ".csv");
                    FileUtils.touch(dataLogFile);

                    telementryLogFile = new File(logStorageLocation + "Telementry_LOG_" + formatter.format(date) + ".csv");
                    FileUtils.touch(telementryLogFile);

                    outputWriterShipPos = new BufferedWriter(new FileWriter(shipPosLogFile));

                    outputWriterData = new BufferedWriter(new FileWriter(dataLogFile));

                    outputWriterTelementry = new BufferedWriter(new FileWriter(telementryLogFile));

                    outputWriterShipPos.append("Point,Time,Latitude,Longtitude,Speed,ROV Depth,GPSHeading");
                    outputWriterShipPos.flush();

                    outputWriterData.append("Point,Time,Roll,Pitch,Depth,"
                            + "DepthToSeaFloor,ROV_Depth,ActuatorPS_feedback,"
                            + "ActuatorSB_feedback,ActuatorPS_command,"
                            + "ActuatorSB_command,Voltage,Emergency, outsideTemp,"
                            + "insideTempCameraHouse, humidity, tempElBoxFromt,"
                            + "tempElBoxRear, I2CError, LeakDetection");
                    outputWriterData.flush();

                    outputWriterTelementry.append("Latitude,Longtitude, Elevation, Time");
//                        + "Elevation,Heading,Time");
                    outputWriterTelementry.flush();

                    setupIsDone = true;

                } catch (Exception ex)
                {
                    System.out.println("ERROR: " + ex);
                }
            }

            dateCSV = new Date(System.currentTimeMillis());
            timeStampString = String.valueOf(java.time.LocalTime.now());
            timeAndDateCSV = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

            logShipPosition();
            logData();
            logTelementry();

        } else
        {
            setupIsDone = false;
        }
    }

//    private void logPhotoPosition(BufferedWriter bw)
//    {
//        try
//        {
//            double shipsLatitude = data.getLatitude();
//            double shipsLontitude = data.getLongitude();
//            double rovHeadingRelToShip = data.getHeading() - 180;
//            if (rovHeadingRelToShip < 0)
//            {
//                rovHeadingRelToShip = rovHeadingRelToShip + 360;
//            }
//            //convert to radians
//
//            double normalizedHeading = Math.atan2(Math.sin(rovHeadingRelToShip), Math.cos(rovHeadingRelToShip));
//            double rovHeadingRelToShipSin = sin(normalizedHeading * PI / 180);
//            double rovHeadingRelToShipSinRest = 0;
//
//            if (rovHeadingRelToShipSin < 0)
//            {
//                rovHeadingRelToShipSinRest = 1 + rovHeadingRelToShipSin;
//
//            }
//
//            double rovLatitude = 0;
//            double rovLongtitude = 0;
//
//            rovLatitude = data.getLatitude() - (adjustedCoordinateRovOffset * rovHeadingRelToShipSinRest);
//            if (rovHeadingRelToShipSinRest != 0)
//            {
//                rovLongtitude = data.getLongitude() + (adjustedCoordinateRovOffset * rovHeadingRelToShipSin);
//            } else
//            {
//                rovLongtitude = data.getLongitude();
//            }
//
//            photoLocationTrack = "";
//            photoLocationTrack = photoLocationNumb + ","
//                    + data.getLatitude() + "," + data.getLongitude() + ","
//                    + data.getSpeed() + "," + data.getRovDepth();
//
//            bw.append(photoLocationTrack);
//            bw.append('\n');
//            bw.flush();
//
//            photoLocationNumb++;
//        } catch (Exception e)
//        {
//            System.out.println("Error: " + e);
//        }
//
//    }
    public void closeLog()
    {
        try
        {
            outputWriterShipPos.close();
            outputWriterData.close();
            outputWriterExif.close();
            outputWriterTelementry.close();
        } catch (Exception e)
        {
            System.out.println("Problem closing log file");
        }

    }

    private void logExifData()
    {
        try
        {
            exifLog = "";
            exifLog = data.getLatitude() + ","
                    + data.getLongitude();

            outputWriterExif.append('\n');
            outputWriterExif.append(telementryLog);
            outputWriterExif.flush();
        } catch (Exception e)
        {
        }

    }

    private void logTelementry()
    {
        try
        {
            telementryLog = "";
            telementryLog = data.getLatitude() + ","
                    + data.getLongitude() + ","
                    + data.getDepth() + ","
                    + timeAndDateCSV.format(dateCSV);

            outputWriterTelementry.append('\n');
            outputWriterTelementry.append(telementryLog);
            outputWriterTelementry.flush();

        } catch (Exception e)
        {
            System.out.println("Error writing telementry...");
        }

    }

    private void logData()
    {
        try
        {
            dataLog = "";

            dataLog = DataPointNumb + ","
                    + timeStampString + ","
                    + String.valueOf(data.getRollAngle()) + ","
                    + String.valueOf(data.getPitchAngle()) + ","
                    + String.valueOf(data.getDepthBeneathBoat()) + ","
                    + String.valueOf(data.getDepthBeneathRov()) + ","
                    + String.valueOf(data.getRovDepth()) + ","
                    + String.valueOf(data.getFb_actuatorPSPos()) + ","
                    + String.valueOf(data.getFb_actuatorSBPos()) + ","
                    + String.valueOf(data.getFb_actuatorPScmd()) + ","
                    + String.valueOf(data.getFb_actuatorSBcmd()) + ","
                    + String.valueOf(data.getVoltage()) + ","
                    + String.valueOf(data.getOutsideTemp()) + ","
                    + String.valueOf(data.getInsideTemp()) + ","
                    + String.valueOf(data.getHumidity()) + ","
                    + String.valueOf(data.getFb_tempElBoxFront()) + ","
                    + String.valueOf(data.getFb_tempElBoxRear()) + ","
                    + String.valueOf(data.isI2cError()) + ","
                    + String.valueOf(data.getLeakStatus()) + ",";

//            outputWriterData.append(String.valueOf(DataPointNumb));
//            outputWriterData.append(',');
//            outputWriterData.append(String.valueOf(timeStampString));
//            outputWriterData.append(',');
//            outputWriterData.append(String.valueOf(data.getRollAngle()));
//            outputWriterData.append(',');
//            outputWriterData.append(String.valueOf(data.getPitchAngle()));
//            outputWriterData.append(',');
//            outputWriterData.append(String.valueOf(data.getDepth()));
//            outputWriterData.append(',');
//            outputWriterData.append(String.valueOf(data.getDepthBeneathRov()));
//            outputWriterData.append(',');
//            outputWriterData.append(String.valueOf(data.getRovDepth()));
//            outputWriterData.append(',');
//            outputWriterData.append(String.valueOf(data.getFb_actuatorPSPos()));
//            outputWriterData.append(',');
//            outputWriterData.append(String.valueOf(data.getFb_actuatorSBPos()));
//            outputWriterData.append(',');
//            outputWriterData.append(String.valueOf(data.getFb_actuatorPScmd()));
//            outputWriterData.append(',');
//            outputWriterData.append(String.valueOf(data.getFb_actuatorSBcmd()));
//            outputWriterData.append(',');
//            outputWriterData.append(String.valueOf(data.getVoltage()));
            outputWriterData.append('\n');
            outputWriterData.append(dataLog);
            outputWriterData.flush();
            DataPointNumb++;
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
                    + data.getSpeed() + "," + data.getRovDepth() + "," + data.getGPSAngle();
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
