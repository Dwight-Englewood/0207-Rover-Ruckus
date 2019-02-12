package wen.sim.bodies;

import wen.control.PathFunction;

public class Cubic extends PathFunction {
    public Cubic() {
        super.xMin = -1;
        super.xMax = 1;
    }

    @Override
    public double function(double x) {
        return (double) (.9 * Math.pow(x, 3));
    }
}
