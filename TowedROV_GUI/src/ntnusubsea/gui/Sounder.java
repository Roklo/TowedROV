/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ntnusubsea.gui;

import java.io.FileInputStream;
import java.io.InputStream;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 *
 */
public class Sounder implements Runnable
{

    private boolean isAlive = false;

    public Sounder()
    {
        this.isAlive = true;
    }

    @Override
    public void run()
    {
        try
        {
            String gongFile = "C:\\Emergency_Warning_06.wav";
            InputStream in = new FileInputStream(gongFile);
            AudioStream audioStream = new AudioStream(in);
            AudioPlayer.player.start(audioStream);
            Thread.sleep(15000);

        } catch (Exception e)
        {
            System.out.println("Error while trying to play an audio file: " + e.getMessage());
        }
        this.setAlive(false);
    }

    public void setAlive(boolean bool)
    {
        this.isAlive = bool;
    }

    public boolean isAlive()
    {
        return this.isAlive;
    }

}
