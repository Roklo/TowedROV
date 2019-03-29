/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ntnusubsea.gui;

import basestation_rov.LogFileHandler;
import basestation_rov.ReadSerialData;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.SwingUtilities;
import TCPCom.*;

/**
 * Main class that launches the application and schedules the different threads
 *
 * @author Marius Nonsvik
 */
public class NTNUSubseaGUI
{

    private static Thread readSerialData;
    private static Thread LogFileHandler;
    //private static ClientManualTest clientTest;
    protected static String ipAddress = "localHost";
    protected static int sendPort = 5057;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        ClientManualTest cmt = new ClientManualTest();

        Data data = new Data();
        EchoSounderFrame sonar = new EchoSounderFrame(data);
        DataLogger logger = new DataLogger(data);
        TCPClient client = new TCPClient(data);
        IOControlFrame io = new IOControlFrame(data, client);
        ROVFrame frame = new ROVFrame(sonar, data, client, io);
        VideoEncoder encoder = new VideoEncoder(data);
        NmeaReceiver nmea = new NmeaReceiver(data);
        UDPClient stream = new UDPClient(data);
        BufferedImage banan;
        ScheduledExecutorService executor
                = Executors.newScheduledThreadPool(8);
        SwingUtilities.invokeLater(frame);
        SwingUtilities.invokeLater(sonar);
        SwingUtilities.invokeLater(io);
        sonar.setVisible(false);
        data.addObserver(sonar);
        data.addObserver(logger);
        data.addObserver(frame);
        data.addObserver(encoder);
        data.addObserver(io);
        executor.scheduleAtFixedRate(logger,
                3000, 1000, TimeUnit.MILLISECONDS);
        executor.scheduleAtFixedRate(encoder,
                20, 40, TimeUnit.MILLISECONDS);
        executor.scheduleAtFixedRate(nmea,
                0, 1000, TimeUnit.MILLISECONDS);
        executor.scheduleAtFixedRate(client,
                0, 100, TimeUnit.MILLISECONDS);
        executor.scheduleAtFixedRate(stream,
                0, 20, TimeUnit.MILLISECONDS);

        executor.scheduleAtFixedRate(cmt,
                0, 100, TimeUnit.MILLISECONDS);
        Runtime.getRuntime()
                .addShutdownHook(new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        executor.shutdown();
                        encoder.finishVideo();
                    }
                },
                        "Shutdown-thread"));

        readSerialData = new Thread(new ReadSerialData(data, "COM9", 4800));
        LogFileHandler = new Thread(new LogFileHandler(data));

        readSerialData.start();
        LogFileHandler.start();

        long timeDifference = 0;
        long lastTime = 0;
        long timeDelay = 5000;

        while (true)
        {

            {
                timeDifference = System.currentTimeMillis() - lastTime;
            }
            if (timeDifference >= timeDelay)
            {
                try
                {
                    
                    System.out.println("kake");
                    //cmt.connect("localhost");
                    
                } catch (Exception e)
                {
                }

                lastTime = System.currentTimeMillis();
            }
        }
    }
}
