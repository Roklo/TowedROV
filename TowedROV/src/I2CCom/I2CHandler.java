/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package I2CCom;

import ROV.DataHandler;

/**
 *
 * @author <Robin S. Thorholm>
 */
public class I2CHandler
{

    boolean I2cSendRequest = false;
    boolean dataRequest = false;
    String dataRequestDevice;

    private static Thread I2CRW;
    protected static DataHandler dh;

    private byte[] recievedDataArray;

    //Send variables
    private byte[] dataByteArray;
    String command;
    int value = 0;

    public I2CHandler(DataHandler dh)
    {
        this.dh = dh;
        I2CRW = new Thread(new I2CRW(this, dh));
        I2CRW.start();
        I2CRW.setName("I2C_RW");
    }

    public boolean getDataRequest()
    {
        return dataRequest;
    }

    public void setDataRequest(boolean dataRequest)
    {
        this.dataRequest = dataRequest;
    }

    public String getDataRequestDevice()
    {
        return dataRequestDevice;
    }

    public void setDataRequestDevice(String dataRequestDevice)
    {
        this.dataRequestDevice = dataRequestDevice;
    }

    public boolean getI2cSendRequest()
    {

        return I2cSendRequest;
    }

    public void setI2cSendRequest(boolean i2cRequest)
    {
        this.I2cSendRequest = i2cRequest;
    }

    public byte[] getCurrentDataByteArray()
    {
        return dataByteArray;
    }

    public String getCommand()
    {
        return command;
    }

    public void setCommand(String Command)
    {
        this.command = Command;
    }

    public int getValue()
    {
        return value;
    }

    public void setValue(int value)
    {
        this.value = value;
    }

    public synchronized void sendI2CCommand(byte[] dataByteArray,
            String command, int value)
    {
        
        this.dataByteArray = dataByteArray;
        this.command = command;
        this.value = value;
        

        setI2cSendRequest(true);
        while (getI2cSendRequest())
        {
            System.out.println("Waiting...");
            //Waiting for all data to be sent
        }

    }

    public synchronized byte[] requestDataFrom(String device)
    {
        byte[] dataRecieved = null;

        setDataRequestDevice(device);
        setDataRequest(true);

        while (getDataRequest())
        {
            System.out.println("Waiting for data...");
        }

        return recievedDataArray;
    }
}
