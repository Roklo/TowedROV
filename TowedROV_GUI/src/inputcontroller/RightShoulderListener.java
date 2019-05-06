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
class RightShoulderListener implements ButtonListener
{

    private InputController ic;

    public RightShoulderListener(InputController ic)
    {
        this.ic = ic;
    }

    @Override
    public void button(boolean pressed)
    {
        if (pressed)
        {
            //System.out.println("R1 button was pressed.");
            this.ic.setBtnR1(true);
        }
        else
        {
            //System.out.println("R1 button was released.");
            this.ic.setBtnR1(false);
        }
    }
}
