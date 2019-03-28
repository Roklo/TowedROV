/*
 * This code is for the bachelor thesis named "Towed-ROV".
 * The purpose is to build a ROV which will be towed behind a surface vessel
 * and act as a multi-sensor platform, were it shall be easy to place new 
 * sesnors. There will also be a video stream from the ROV.
 * 
 * The system consists of a Raspberry Pi on the ROV that is connected to several
 * Arduino microcontrollers that are connected to sensors and and programed with
 * the control system.
 * The external computer which is on the surface vessel is connected to a GPS,
 * echo sounder and the RBPi. It will present and save data in addition to
 * handle user commands.
 */
package ntnusubsea.gui;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import java.io.File;
import java.net.InetAddress;

/**
 * This class handles incoming images from a DatagramPacket. It receives the
 * image on a DatagramSocket and returns it as a BufferedImage.
 *
 * @author MorSol and Marius Nonsvik
 */
public class UDPClient implements Runnable {

    static BufferedImage videoImage;
    //static Socket videoSocket;
    private int photoNumber = 1;
    boolean lastPhotoMode = false;
    private boolean debug = false;
    private Data data;
    private int test = 0;
    private DatagramSocket videoSocket;
    private long timer = System.currentTimeMillis();
    private File photoDirectory;

    public UDPClient(Data data) {
        try {
            this.data = data;
            videoSocket = new DatagramSocket(8083);
            photoDirectory = new File("C://ROV_Photos/");
        } catch (Exception e) {

        }

    }

    @Override
    public void run() {
        try {
            if (System.currentTimeMillis() - timer > 60000) {
                videoSocket.close();
                videoSocket = new DatagramSocket(8083);
                timer = System.currentTimeMillis();
                System.out.println("Reconnected");
            }
            //Createss new DatagramSocket for reciving DatagramPackets

            //Creating new DatagramPacket form the packet recived on the videoSocket
            byte[] receivedData = new byte[60000];
            DatagramPacket receivePacket = new DatagramPacket(receivedData,
                    receivedData.length);
            if (receivePacket.getLength() > 0) {
                long startTime = System.currentTimeMillis();
                //Updates the videoImage from the received DatagramPacket
                videoSocket.receive(receivePacket);
                long endTime = System.currentTimeMillis();
                if (debug) {
                    System.out.println("Videopackage received");
                }
                //Reads incomming byte array into a BufferedImage
                ByteArrayInputStream bais = new ByteArrayInputStream(receivedData);
                videoImage = ImageIO.read(bais);
                data.setVideoImage(videoImage);

                // Saves the photo to disk if photo mode is true
                if (data.isPhotoMode()) {
                    try {
                        if (this.photoDirectory.exists() && this.photoDirectory.isDirectory()) {
                            ImageIO.write(videoImage, "jpg", new File(this.photoDirectory.toString() + "/image" + this.photoNumber + ".png"));
                            this.photoNumber++;
                        } else {
                            System.out.println("No directory found, creating a new one at C://ROV_Photos/");
                            this.photoDirectory = new File("C://ROV_Photos/");
                        }

                    } catch (Exception e) {
                        System.out.println("Exception occured :" + e.getMessage());
                    }
                    System.out.println("Image were saved to disk succesfully at C:/ROV_Photos");
                }

                if (data.isPhotoMode() != lastPhotoMode) {
                    InetAddress returnIP = receivePacket.getAddress();
                    int returnPort = receivePacket.getPort();
                    String s1 = "photoMode:" + String.valueOf(data.isPhotoMode());
                    byte arr[] = s1.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(arr, arr.length, returnIP, returnPort);
                    videoSocket.send(sendPacket);
                    lastPhotoMode = data.isPhotoMode();
                }

                receivedData = null;
                bais = null;
                test++;
                System.out.println(endTime - startTime);
            }

        } catch (SocketException ex) {
            Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
