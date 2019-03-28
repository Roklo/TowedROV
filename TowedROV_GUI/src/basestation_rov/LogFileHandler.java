/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basestation_rov;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import java.text.SimpleDateFormat;
import java.util.Date;
import ntnusubsea.gui.Data;

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
    String photoLocationTrack = "null";

    int shipTrackPointNumb = 1;
    int photoLocationNumb = 1;
    String logStorageLocation = "C:\\Users\\rocio\\Google Drive\\NTNU\\3.År\\2.Semester\\Bachelor oppgave\\";
    String photoPosLog = "";
    String shipPosLog = "";
    boolean setupIsDone = false;

    public LogFileHandler(Data data)
    {
        this.data = data;
    }

    public void run()
    {

        while (true)
        {
            try
            {
                lastTime = System.currentTimeMillis();
                PrintWriter pw = new PrintWriter(shipPosLog);
            } catch (Exception e)
            {
                BufferedWriter writer = null;
                BufferedWriter writer2 = null;
                while (data.startLogging)
                {

                    if (!setupIsDone)
                    {
                        try
                        {
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
                            Date date = new Date(System.currentTimeMillis());
                            shipPosLog = logStorageLocation + "ShipPos_LOG_" + formatter.format(date) + ".csv";

                            photoPosLog = logStorageLocation + "PhotoPos_LOG_" + formatter.format(date) + ".csv";

                            writer = new BufferedWriter(new FileWriter(shipPosLog));
                            writer2 = new BufferedWriter(new FileWriter(photoPosLog));

                            //Setting up shiplog csv
                            writer.append("Point, Latitude, Longtitude, Speed, ROV Depth, Heading");
                            writer.append('\n');

                            setupIsDone = true;
                            lastTime = System.currentTimeMillis();

                        } catch (Exception ex)
                        {
                            System.out.println("ERROR: " + ex);
                        }
                    }

//                    timeDifference = System.currentTimeMillis() - lastTime;
//                    if (timeDifference >= pointFreqMillis)
//                    {
//                        logShipPosition(writer);
//                        lastTime = System.currentTimeMillis();
//                    }
                    logPhotoPosition(writer2);
//                    if(###PhotoIsTaken)
//                    {
//                        
//                    }

                }
            }

        }
    }

    private void logPhotoPosition(BufferedWriter bw)
    {
        try
        {
            double shipsLatitude = data.get_Latitude();
            double shipsLontitude = data.get_Longitude();
            double rovHeadingRelToShip = data.get_Heading() - 180;
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
                rovHeadingRelToShipSinRest =  1 + rovHeadingRelToShipSin ;

            }

            double rovLatitude = 0;
            double rovLongtitude = 0;

            rovLatitude = data.get_Latitude() - (adjustedCoordinateRovOffset * rovHeadingRelToShipSinRest);
            if (rovHeadingRelToShipSinRest != 0)
            {
                rovLongtitude = data.get_Longitude() + (adjustedCoordinateRovOffset * rovHeadingRelToShipSin);
            } else
            {
                rovLongtitude = data.get_Longitude();
            }

            photoLocationTrack = "";
            photoLocationTrack = photoLocationNumb + ","
                    + data.get_Latitude() + "," + data.get_Longitude() + ","
                    + data.get_Speed() + "," + data.rovDepth;

            bw.append(photoLocationTrack);
            bw.append('\n');
            bw.flush();

            photoLocationNumb++;
        } catch (Exception e)
        {
            System.out.println("Error: " + e);
        }

    }

    private void logShipPosition(BufferedWriter bw)
    {

        try
        {
            // PrintWriter pw = new PrintWriter(shipPosLog);

            // System.out.println("Waiting");
            shipTrack = "";
            shipTrack = shipTrackPointNumb + ","
                    + data.get_Latitude() + "," + data.get_Longitude() + ","
                    + data.get_Speed() + "," + data.rovDepth + "," + data.get_Heading();

            bw.append(shipTrack);
            bw.append('\n');
            bw.flush();
            //pw.close();

            shipTrackPointNumb++;

        } catch (Exception e)
        {
            System.out.println("Error: " + e);
        }

    }
}
