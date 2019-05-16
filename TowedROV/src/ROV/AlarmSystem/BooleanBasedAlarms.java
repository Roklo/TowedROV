/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ROV.AlarmSystem;

import ROV.Data;

/**
 * Creates boolean alarms for the ROV
 * 
 * 
 * Not yet implemented
 */
public class BooleanBasedAlarms implements Runnable
{

//    long currentTime = 0;
//    long lastTime = 0;
//
//    String input;
//    int setPoint;
//    Boolean alarm;
//    boolean HAlarm;
//    boolean ack;
//    boolean inhibit;
//    String alarmName;
//    Data dh;
//    AlarmHandler alarmHandler;
//    
//
//    public BooleanBasedAlarms(Data dh, AlarmHandler alarmHandler, String input, int setPoint, String alarmName,
//            boolean HAlarm, boolean ack, boolean inhibit)
//    {
//        this.alarmHandler = alarmHandler;
//        this.dh = dh;
//        this.input = input;
//        this.setPoint = setPoint;
//
//        this.alarmName = alarmName;
//        this.HAlarm = HAlarm;
//        this.ack = ack;
//        this.inhibit = inhibit;
//
//        currentTime = System.nanoTime();
//
//    }

    public void run()
    {
//
//        while (true)
//        {
//            while (!alarmHandler.inhibitedAlarms.get(alarmName))
//            {
//                if (HAlarm && alarmHandler.alarmDataList.get(input) >= setPoint)
//                {
//                    // High Alarm
//                    alarm = true;
//
//                    alarmHandler.completeAlarmList.put(alarmName, alarm);
//                    //dh.handleDataFromAlarmList(alarmName, alarm);
//                    while (alarm)
//                    {
//                        try
//                        {
//                            Thread.sleep(10);
//                        } catch (Exception e)
//                        {
//                        }
//                        //System.out.println("waiting...");
//                        if (alarmHandler.ack)
//                        {
//                            //System.out.println("test");
//                            alarm = false;
//                            alarmHandler.completeAlarmList.put(alarmName, alarm);
//                            System.out.println("Alarm is acked");
//                            //dh.handleDataFromAlarmList(alarmName, alarm);
//                        }
//                    }
//                }
//                if (!HAlarm && alarmHandler.alarmDataList.get(input) <= setPoint)
//                {
//                    // Low Alarm
//                    alarm = true;
//                    alarmHandler.completeAlarmList.put(alarmName, alarm);
//                    //dh.handleDataFromAlarmList(alarmName, alarm);
//                    while (alarm)
//                    {
//                        if (alarmHandler.ack)
//                        {
//                            alarm = false;
//                            alarmHandler.completeAlarmList.put(alarmName, alarm);
//                            System.out.println("Alarm is acked");
//                            //dh.handleDataFromAlarmList(alarmName, alarm);
//                        }
//                    }
//                }
//            }
//
//        }
    }

}
