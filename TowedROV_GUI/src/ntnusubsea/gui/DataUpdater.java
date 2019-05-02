/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ntnusubsea.gui;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hakon
 */
public class DataUpdater implements Runnable
{

    private TCPClient client_Rov;
    private TCPClient client_Camera;

    public DataUpdater(TCPClient client_Rov, TCPClient client_Camera)
    {
        this.client_Rov = client_Rov;
        this.client_Camera = client_Camera;
    }

    @Override
    public void run()
    {
        if (client_Rov.isConnected())
        {
            try
            {
                client_Rov.sendCommand("fb_allData");
            } catch (IOException ex)
            {
                System.out.println("Error while getting data from remote: " + ex.getMessage());
            }

        }
        if (client_Camera.isConnected())
        {
            try
            {
                client_Camera.sendCommand("getData");
            } catch (IOException ex)
            {
                System.out.println("Error while getting data from remote: " + ex.getMessage());
            }

        }

    }

}
