/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InputController;

import com.exlumina.j360.ButtonListener;

/**
 *
 * @author Haakon
 */
class LeftShoulderListener implements ButtonListener
{

    private InputController ic;

    public LeftShoulderListener(InputController ic)
    {
        this.ic = ic;
    }

    @Override
    public void button(boolean pressed)
    {
        if (pressed)
        {
            //System.out.println("L1 button was pressed.");
            this.ic.setBtnL1(true);
        }
        else
        {
            //System.out.println("L1 button was released.");
            this.ic.setBtnL1(false);
        }
    }
}
