/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InputController;

import com.exlumina.j360.Controller;
import com.exlumina.j360.ValueListener;
import java.io.IOException;
import ntnusubsea.gui.Data;
import ntnusubsea.gui.TCPClient;

/**
 *
 * @author Haakon
 */
public class InputController implements Runnable
{

    private double btnLyGUI = 0;
    private int btnLy = 0;
    private int btnLx = 0;
    private int btnRy = 0;
    private int btnRx = 0;
    private int angle = 0;

    private static int lastAngle = 0;
    private static float lastVal = 0;
    private int oldPS = 0;
    private int oldSB = 0;

    private String ipAddress = "";
    private int sendPort = 0;
    private Data data;
    private TCPClient client_ROV;

    public InputController(Data data, TCPClient client_ROV)
    {
        this.data = data;
        this.client_ROV = client_ROV;
    }

    @Override
    public void run()
    {
        ValueListener Ly = new LeftThumbYListener(this);
        //ValueListener Lx = new LeftThumbXListener(this);
        //ValueListener Ry = new RightThumbYListener(this);
        ValueListener Rx = new RightThumbXListener(this);
        Controller c1 = Controller.C1;

        c1.leftThumbY.addValueChangedListener(Ly);
        //c1.leftThumbX.addValueChangedListener(Lx);
        //c1.rightThumbY.addValueChangedListener(Ry);
        c1.rightThumbX.addValueChangedListener(Rx);

        for (;;)
        {
            try
            {
                if (data.isControllerEnabled() && data.isManualMode())
                {
//                    int ps = 0;
//                    int sb = 0;
//                    if (btnLy <= 127)
//                    {
//                        ps = btnLy + btnRx;
//                        sb = btnLy - btnRx;
//                    }
//                    if (btnLy > 127)
//                    {
//                        ps = btnLy - (btnRx);
//                        sb = btnLy;
//                    }

                    int ps = btnLy;
                    int sb = btnLy;
                    if (ps != oldPS)
                    {
                        this.client_ROV.sendCommand("cmd_actuatorPS:" + String.valueOf(ps));
                        oldPS = ps;
                    }
                    if (sb != oldSB)
                    {
                        this.client_ROV.sendCommand("cmd_actuatorSB:" + String.valueOf(sb));
                        oldSB = sb;
                    }

                }
                Thread.sleep(10);
            } catch (InterruptedException ex)
            {
                System.out.println("InterruptedException: " + ex.getMessage());
            } catch (IOException ex)
            {
                System.out.println("IOException: " + ex.getMessage());
            } catch (Exception ex)
            {
                System.out.println("Exception: " + ex.getMessage());
            }
        }
    }

    public void getDegree()
    {
        angle = this.FindDegree(btnRx, btnRy);
        if (angle != lastAngle)
        {
//            System.out.println(angle);
            this.lastAngle = angle;
        }
    }

    public static int FindDegree(int x, int y)
    {
        float value = (float) ((Math.atan2(x, y) / Math.PI) * 180f);
        if ((x < 98 && x > -98) && (y < 98 && y > -98))
        {
            value = lastVal;
        } else if (value < 0)
        {
            value += 360f;
        }
        lastVal = value;
        return Math.round(value);
    }

    public double getBtnLyGUI()
    {
        return btnLyGUI;
    }

    public int getBtnLy()
    {
        return btnLy;
    }

    public void setBtnLy(int btnLy)
    {
        this.btnLy = btnLy;
        this.btnLyGUI = (double) (this.btnLy / 100.0);
        //System.out.println("L_Y: " + btnLy);
    }

    public int getBtnLx()
    {
        return btnLx;
    }

    public void setBtnLx(int btnLx)
    {
        this.btnLx = btnLx;
    }

    public int getBtnRy()
    {
        return btnRy;
    }

    public void setBtnRy(int btnRy)
    {
        this.btnRy = btnRy;
    }

    public int getBtnRx()
    {
        return btnRx;
    }

    public void setBtnRx(int btnRx)
    {
        //System.out.println("R_X: " + btnRx);
        this.btnRx = btnRx;
    }

    public int getAngle()
    {
        this.angle = this.FindDegree(btnRx, btnRy);
        if (this.angle != this.lastAngle)
        {
            //System.out.println(angle);
            this.lastAngle = this.angle;
        }
        return this.angle;
    }

    public int getAngleForGUI()
    {
        this.angle = this.FindDegree(btnRx, btnRy);
        if (this.angle != this.lastAngle)
        {
            //System.out.println(angle);
            this.lastAngle = this.angle;
        }
        int returnAngle = this.angle - 15;
        if (returnAngle < 0)
        {
            returnAngle = returnAngle + 360;
        }
        return returnAngle;
    }

}
