/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basestation_rov;


/**
 * Main class responsible for reading arduino values, and send necessary 
 * data to ROV and GUI.
 * @author Bjørnar
 */
public class BaseStation_ROV
{
    
    /**
     * Main class of the ship system
     */
    protected static DataHandler dh;
    private static Thread readSerialData;
    
    protected static String ipAddress = "localHost";
    protected static int sendPort = 5057;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
       dh = new DataHandler();
       dh.setThreadStatus(true);
       readSerialData = new Thread(new ReadSerialData(dh, "COM9", 4800));
       
       readSerialData.start();
       
       while (true)
       {
           try
           {
               Thread.sleep(425);
               System.out.println("GPS Values: ");
               System.out.println("Satellites: " + dh.get_Satellites());
               System.out.println("Altitude: " + dh.get_Altitude());
               System.out.println("Angle: " + dh.get_Angle());
               System.out.println("Speed: " + dh.get_Speed());
               System.out.println("Latitude: " + dh.get_Latitude());
               System.out.println("Longitude: " + dh.get_Longitude());
               System.out.println("");
               System.out.println("Echo sounder values: ");
               System.out.println("Depth: " + dh.get_Depth());
               System.out.println("Temperature: " + dh.get_Temperature());
               System.out.println("");
               System.out.println("IMU values:");
               System.out.println("Roll: " + dh.get_Roll());
               System.out.println("Pitch: " + dh.get_Pitch());
               System.out.println("Yaw: " + dh.get_Pitch());
               
           }
           catch (Exception e)
           {
           }
       }
    }
}
