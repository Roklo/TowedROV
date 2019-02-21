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

    boolean i2cRequest = false;
    boolean dataRequest = false;
    String dataRequestDevice;

    private static Thread I2CRW;
    protected static DataHandler dh;

    private byte[] recievedDataArray;
    private byte[] dataByteArray;
    private byte address;

    public I2CHandler(DataHandler dh)
    {
        this.dh = dh;
        I2CRW = new Thread(new I2CRW(this, dh));
        I2CRW.start();
        I2CRW.setName("I2CComHandler");
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

   
    
    
    
    

    public boolean getI2cRequest()
    {

        return i2cRequest;
    }

    public void setI2cRequest(boolean i2cRequest)
    {
        this.i2cRequest = i2cRequest;
    }

    public byte[] getCurrentDataByteArray()
    {
        return dataByteArray;
    }

    public synchronized void sendI2CData(byte[] dataByteArray)
    {
        this.dataByteArray = dataByteArray;
        setI2cRequest(true);
        while (getI2cRequest())
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
        
        while(getDataRequest())
        {
            System.out.println("Waiting for data...");
        }
        
        return recievedDataArray;
    }
}
