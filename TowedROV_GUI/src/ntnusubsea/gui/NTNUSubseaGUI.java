/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ntnusubsea.gui;

import InputController.InputController;
import basestation_rov.LogFileHandler;
import basestation_rov.ReadSerialData;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.SwingUtilities;
import TCPCom.*;
import basestation_rov.SerialDataHandler;
import java.util.Map.Entry;
import InputController.InputController;

/**
 * Main class that launches the application and schedules the different threads
 *
 */
public class NTNUSubseaGUI
{

    private static Thread readSerialData;
    //private static Thread LogFileHandler;
    private static Thread imuThread;
    private static Thread gpsThread;
    private static Thread echoSounderThread;
    private static Thread ROVDummyThread;
    private static Thread InputControllerThread;

    //private static ClientManualTest clientTest;
    protected static String ipAddress = "localHost";
    protected static int sendPort = 5057;
    protected static String IP_ROV = "192.168.0.101";
    protected static String IP_camera = "192.168.0.102";
    protected static int Port_ROV = 8080;
    protected static int Port_cameraStream = 8083;
    protected static int Port_cameraCom = 9006;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        ClientManualTest cmt = new ClientManualTest();

        Data data = new Data();
        Sounder sounder = new Sounder();
        SerialDataHandler sdh = new SerialDataHandler(data);
        EchoSounderFrame sonar = new EchoSounderFrame(data);

        //DataLogger logger = new DataLogger(data);
        LogFileHandler lgh = new LogFileHandler(data);
        TCPClient client_ROV = new TCPClient(IP_ROV, Port_ROV, data);
        TCPClient client_Camera = new TCPClient(IP_camera, Port_cameraCom, data);
        UDPClient stream = new UDPClient(Port_cameraStream, data);
        IOControlFrame io = new IOControlFrame(data, client_ROV);
        ROVFrame frame = new ROVFrame(sonar, data, io, client_ROV, client_Camera, stream, sounder);
        VideoEncoder encoder = new VideoEncoder(data);
        //NmeaReceiver nmea = new NmeaReceiver(data);
        DataUpdater dataUpdater = new DataUpdater(client_ROV, client_Camera, data);
        BufferedImage banan;
        ScheduledExecutorService executor
                = Executors.newScheduledThreadPool(8);
        SwingUtilities.invokeLater(frame);
        //SwingUtilities.invokeLater(sonar);
        SwingUtilities.invokeLater(io);
        sonar.setVisible(false);
        data.addObserver(sonar);
        // data.addObserver(logger);
        data.addObserver(frame);
        data.addObserver(encoder);
        data.addObserver(io);
        executor.scheduleAtFixedRate(lgh,
                0, 100, TimeUnit.MILLISECONDS);
        executor.scheduleAtFixedRate(encoder,
                5000, 40, TimeUnit.MILLISECONDS);
        InputControllerThread = new Thread(new InputController());
        InputControllerThread.start();
        InputControllerThread.setName("InputController");

        //executor.scheduleAtFixedRate(nmea,
        //      0, 1000, TimeUnit.MILLISECONDS);
//        executor.scheduleAtFixedRate(client_ROV,
//                0, 100, TimeUnit.MILLISECONDS);
//        executor.scheduleAtFixedRate(client_Camera,
//                0, 100, TimeUnit.MILLISECONDS);
//        executor.scheduleAtFixedRate(stream,
//                0, 20, TimeUnit.MILLISECONDS);
        executor.scheduleAtFixedRate(cmt,
                0, 100, TimeUnit.MILLISECONDS);
        executor.scheduleAtFixedRate(sonar,
                0, 100, TimeUnit.MILLISECONDS);
        executor.scheduleAtFixedRate(dataUpdater,
                1000, 1000, TimeUnit.MILLISECONDS);
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

//        LogFileHandler = new Thread(new LogFileHandler(data));
//
//        LogFileHandler.start();
        long timeDifference = 0;
        long lastTime = 0;
        long timeDelay = 5000;
        boolean connected = false;
        boolean foundComPort = false;
        boolean listedCom = false;

        while (true)
        {

            if (!foundComPort)

            {
                System.out.println("Searching for com ports...");
                sdh.findComPorts();
                foundComPort = true;
            }

            if (!listedCom)
            {
                System.out.println("Com ports found:");

                if (data.comPortList.isEmpty())
                {
                    System.out.println("None");
                } else
                {
                    for (Entry e : data.comPortList.entrySet())
                    {
                        String comPortKey = (String) e.getKey();
                        String comPortValue = (String) e.getValue();
                        System.out.println(comPortKey + " : " + comPortValue);

                    }
                }
                System.out.println("--End of com list--");
                listedCom = true;

                for (Entry e : data.comPortList.entrySet())
                {
                    String comPortKey = (String) e.getKey();
                    String comPortValue = (String) e.getValue();
                    if (comPortValue.contains("IMU"))
                    {
                        imuThread = new Thread(new ReadSerialData(data, comPortKey, 115200, comPortValue));
                        imuThread.start();
                        imuThread.setName(comPortValue);

                    }

                    if (comPortValue.contains("GPS"))
                    {
                        gpsThread = new Thread(new ReadSerialData(data, comPortKey, 115200, comPortValue));
                        gpsThread.start();
                        gpsThread.setName(comPortValue);

                    }

                    if (comPortValue.contains("EchoSounder"))
                    {
                        echoSounderThread = new Thread(new ReadSerialData(data, comPortKey, 4800, comPortValue));
                        echoSounderThread.start();
                        echoSounderThread.setName(comPortValue);
                    }

                    if (comPortValue.contains("ROVDummy"))
                    {
                        ROVDummyThread = new Thread(new ReadSerialData(data, comPortKey, 115200, comPortValue));
                        ROVDummyThread.start();
                        ROVDummyThread.setName(comPortValue);
                    }

                }

            }

            try
            {
                if (!connected)
                {
                    cmt.connect("localhost");
                    connected = true;

                }
                if (data.comPortList != null)
                {
                    System.out.println(data.comPortList);
                }
            } catch (Exception e)
            {
            }

            timeDifference = System.currentTimeMillis() - lastTime;

            if (timeDifference >= timeDelay)
            {
                try
                {
                    // System.out.println(cmt.sendData("ping"));
                } catch (Exception e)
                {
                }

                lastTime = System.currentTimeMillis();
            }

            System.out.println("Pitch: : " + data.getPitchAngle()
                    + "    TestDepth: " + data.getTestDepth());
        }
    }
}
