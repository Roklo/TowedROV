/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ntnusubsea.gui;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Detects a connected com-port and reads incoming Nmea data.
 * @author Kristian Homdrom and Marius Nonsvik
 */
public class NmeaReceiver implements Runnable {

    private boolean connection;
    private SerialPort port;
    private Data data;

    public NmeaReceiver(Data data) {
        // Create new Arduino Serial object, used to cennect to arduino with comport and baudrate set.
        //  ardConn = new Arduino(wantedPort, BAUD_RATE);
        if (SerialPort.getCommPorts().length > 0) {
            port = SerialPort.getCommPorts()[0];
            port.setBaudRate(115200);
        }
        this.data = data;
    }

    /**
     * open the serial connection to the arduino
     */
    public synchronized void openConnection() {
        try {
            if (!this.port.isOpen()) {
                this.port.openPort();
                connection = true;
                // if port failed to open get message
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * check if Serial port is open
     *
     * @return state of the serialport
     */
    public boolean getOpen() {
        return this.port.isOpen();
    }

    @Override
    public void run() {
        openConnection();
        if (getOpen());
        {
            //   port.getFlowControlSettings()
            port.addDataListener(new SerialPortDataListener() {
                @Override
                public int getListeningEvents() {
                    return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
                }

                @Override
                public void serialEvent(SerialPortEvent event) {
                    if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
                        return;
                    }
                    //    System.out.println("bytesav " + port.bytesAvailable() ); 
                    if (port.bytesAvailable() > 6) {
                        byte[] newData = new byte[port.bytesAvailable()];
                        int numRead = port.readBytes(newData, newData.length);
                        System.out.println("numread " + numRead);
                        if (numRead == 24) {
                            getValues(newData);
                            System.out.println("tt");
                        } else {
                            port.readBytes(newData, port.bytesAvailable());
                        }

                    }

                }
            });

        }
    }

    public void getValues(byte[] b) {
        ByteBuffer bb = ByteBuffer.wrap(b);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        int index = 0;
        try {
            data.setLatitude(roundToDecimalPlaces(bb.getFloat(4), 2));
            data.setLongitude(roundToDecimalPlaces(bb.getFloat(8), 2));
            data.setHeading(roundToDecimalPlaces(bb.getFloat(12), 2));
            data.setSeafloorRov(roundToDecimalPlaces(bb.getFloat(16), 2));
            data.setSpeed(roundToDecimalPlaces(bb.getFloat(20), 2));
        } catch (Exception e) {

        }
    }

    private float roundToDecimalPlaces(float value, int decimalPlaces) {
        float shift = (float) Math.pow(10, decimalPlaces);
        return Math.round(value * shift) / shift;
    }
}
