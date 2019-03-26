/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 
 */package ntnusubsea.gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
//import org.jcodec.api.awt.AWTSequenceEncoder;

/**
 * The class video encoder uses buffered image and encodes there images to a
 * video file.
 *
 * @author Marius Nonsvik
 *
 */
public class VideoEncoder implements Runnable, Observer {

    // private ArrayList<BufferedImage> list = new ArrayList();
    private BufferedImage videoImage;
    private Data data;
    //private AWTSequenceEncoder enc;
    private int frame = 0;
    private LocalDateTime startTime;

    /**
     * Creates an instance of video encoder and create the MP4 file and gives it
     * a unique name
     *
     * @param data Data containing the images
     */
    public VideoEncoder(Data data) {
        startTime = LocalDateTime.now();
        String minute, hour, day, month, year;
        if (startTime.getMinute() < 10) {
            minute = "0" + Integer.toString(startTime.getMinute());
        } else {
            minute = Integer.toString(startTime.getMinute());
        }
        if (startTime.getHour() < 10) {
            hour = "0" + Integer.toString(startTime.getHour());
        } else {
            hour = Integer.toString(startTime.getHour());
        }
        if (startTime.getDayOfMonth() < 10) {
            day = "0" + Integer.toString(startTime.getDayOfMonth());
        } else {
            day = Integer.toString(startTime.getDayOfMonth());
        }
        if (startTime.getMonthValue() < 10) {
            month = "0" + Integer.toString(startTime.getMonthValue());
        } else {
            month = Integer.toString(startTime.getMonthValue());
        }
        year = Integer.toString(startTime.getYear());
        String fileName = "ROV Video " + hour + minute + " " + day + "." + month + "." + year + ".mp4";
        //enc = AWTSequenceEncoder.create24Fps(new File(fileName));

        this.data = data;
    }

    @Override
    public void run() {
        // if (frame < list.size()) {
        // if (frame < list.size()) {
        try {
            //       BufferedImage image = list.get(frame);
            //enc.encodeImage(videoImage);
            //  frame++;
        } catch (Exception ex) {
            Logger.getLogger(VideoEncoder.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        videoImage = data.getVideoImage();
//        if(!list.contains(image)){
//        list.add(data.getVideoImage());
//        }
    }

    /**
     * Encodes the remaining images and finishes the video
     */
    public void finishVideo() {
//            for (int i = frame; i < list.size(); i++) {
//                enc.encodeImage(list.get(i));
//            }
        //enc.finish();
    }
}
