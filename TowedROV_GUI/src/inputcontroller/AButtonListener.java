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
class AButtonListener implements ButtonListener
{

    private InputController ic;

    public AButtonListener(InputController ic)
    {
        this.ic = ic;
    }

    @Override
    public void button(boolean pressed)
    {
        if (pressed)
        {
            //System.out.println("A button was pressed.");
            this.ic.setBtnA(true);
        }
        else
        {
            //System.out.println("A button was released.");
            this.ic.setBtnA(false);
        }
    }
}
