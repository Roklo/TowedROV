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
class YButtonListener implements ButtonListener
{

    private InputController ic;

    public YButtonListener(InputController ic)
    {
        this.ic = ic;
    }

    @Override
    public void button(boolean pressed)
    {
        if (pressed)
        {
            //System.out.println("Y button was pressed.");
            this.ic.setBtnY(true);
        }
        else
        {
            //System.out.println("Y button was released.");
            this.ic.setBtnY(false);
        }
    }
}
