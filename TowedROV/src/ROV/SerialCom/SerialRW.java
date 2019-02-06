/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ROV.SerialCom;

import java.util.concurrent.Semaphore;


/**
 *
 * @author <Robin S. Thorholm>
 */
public class SerialRW implements Runnable
{

    private final Semaphore semaphore;

    public SerialRW (Semaphore semaphore)
    {
        this.semaphore = semaphore;
    }

    @Override
    public void run()
    {
        while (true)
        {

        }
    }

}
