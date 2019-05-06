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
class XButtonListener implements ButtonListener
{

    private InputController ic;

    public XButtonListener(InputController ic)
    {
        this.ic = ic;
    }

    @Override
    public void button(boolean pressed)
    {
        if (pressed)
        {
            //System.out.println("X button was pressed.");
            this.ic.setBtnX(true);
        }
        else
        {
            //System.out.println("X button was released.");
            this.ic.setBtnX(false);
        }
    }
}
