/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ntnusubsea.gui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.Observable;
import java.util.Observer;

/**
 * This class creates a text file and logs the data every second. The text file
 * contains time stamps and the desired data.
 *
 */
public class DataLogger implements Observer, Runnable {

    private String dataString = " NaN";
    private Data data;
    private LocalDateTime now;
    File file = new File("ROV Data Log.txt");

    /**
     * Creates an instance of DataLogger and saves the current timestamp
     *
     * @param data The Data object containing the data to be logged
     */
    public DataLogger(Data data) {
        now = LocalDateTime.now();
        this.data = data;
        newSession();
    }

    /**
     * Writes a string the the text file
     *
     * @param data String to write to the text file
     */
    public void writeToFile(String data) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file, true));
            writer.write(data + System.getProperty("line.separator"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Writes a header in the text file to indicate a new logging session
     */
    public void newSession() {
        String seperator = System.getProperty("line.separator");
        String startString = seperator + seperator + "----- New session! -----" + seperator;
        writeToFile(startString);
        String day, month, year;
        if (now.getDayOfMonth() < 10) {
            day = "0" + Integer.toString(now.getDayOfMonth());
        } else {
            day = Integer.toString(now.getDayOfMonth());
        }
        if (now.getMonthValue() < 10) {
            month = "0" + Integer.toString(now.getMonthValue());
        } else {
            month = Integer.toString(now.getMonthValue());
        }
        year = Integer.toString(now.getYear());
        String date = day + "." + month + "." + year;
        writeToFile("-----" + date + "-----" + seperator);
        String channelLabels = "               ";
        dataString = " | Depth: " + String.valueOf(data.getDepth());
        channelLabels += fixedLengthString("| Depth", dataString.length());
        dataString += " | Heading: " + String.valueOf(data.getHeading());
        channelLabels += fixedLengthString("| Heading", dataString.length() + 15 - channelLabels.length());
        dataString += " | Latitude: " + String.valueOf(data.getLatitude());
        channelLabels += fixedLengthString("| Latitude", dataString.length() + 15 - channelLabels.length());
        dataString += " | Longitude: " + String.valueOf(data.getLongitude());
        channelLabels += fixedLengthString("| Longitude", dataString.length() + 15 - channelLabels.length());
        dataString += " | " + data.getChannel(1);
        channelLabels += fixedLengthString("| Channel 1", dataString.length() + 15 - channelLabels.length());
        dataString += " | " + data.getChannel(2);
        channelLabels += fixedLengthString("| Channel 2", dataString.length() + 15 - channelLabels.length());
        dataString += " | " + data.getChannel(3);
        channelLabels += fixedLengthString("| Channel 3", dataString.length() + 15 - channelLabels.length());
        dataString += " | " + data.getChannel(4);
        channelLabels += fixedLengthString("| Channel 4", dataString.length() + 15 - channelLabels.length());
        writeToFile(channelLabels);
    }

    @Override
    public void update(Observable o, Object arg) {
        dataString = " | Depth: " + String.valueOf(data.getDepth());
        dataString += " | Heading: " + String.valueOf(data.getHeading());
        dataString += " | Latitude: " + String.valueOf(data.getLatitude());
        dataString += " | Longitude: " + String.valueOf(data.getLongitude());
        dataString += " | " + data.getChannel(1);
        dataString += " | " + data.getChannel(2);
        dataString += " | " + data.getChannel(3);
        dataString += " | " + data.getChannel(4);
    }

    @Override
    public void run() {
        now = LocalDateTime.now();
        String second, minute, hour;
        if (now.getSecond() < 10) {
            second = "0" + Integer.toString(now.getSecond());
        } else {
            second = Integer.toString(now.getSecond());
        }
        if (now.getMinute() < 10) {
            minute = "0" + Integer.toString(now.getMinute());
        } else {
            minute = Integer.toString(now.getMinute());
        }
        if (now.getHour() < 10) {
            hour = "0" + Integer.toString(now.getHour());
        } else {
            hour = Integer.toString(now.getHour());
        }
        String time = hour + ":" + minute + ":" + second;
        writeToFile("Time: " + time + dataString);
    }

    /**
     * Returns a string at a fixed length
     * @param string String to change length of
     * @param length Wanted length of string
     * @return String at new fixed length
     */
    public static String fixedLengthString(String string, int length) {
        return String.format("%-" + length + "s", string);
    }

}
