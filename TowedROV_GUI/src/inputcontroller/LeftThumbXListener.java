/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InputController;

import com.exlumina.j360.ValueListener;

/**
 *
 * @author Haakon
 */
class LeftThumbXListener implements ValueListener
{

    private InputController ic;

    public LeftThumbXListener(InputController ic)
    {
        this.ic = ic;
    }

    @Override
    public void value(int newValue)
    {
        newValue = map(newValue, -32768, 32768, -100, 100);
        this.ic.setBtnLx(newValue);
        //System.out.printf("Lx: " + "%6d\n", newValue);
    }

    private int map(int x, int in_min, int in_max, int out_min, int out_max)
    {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

}
