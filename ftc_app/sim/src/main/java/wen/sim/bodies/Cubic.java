package wen.sim.bodies;

import wen.control.PathFunction;

public class Cubic extends PathFunction {
    public Cubic() {
        super.xMin = -1;
        super.xMax = 1;
    }

    @Override
    public float function(float x) {
        return (float) (.9*Math.pow(x,3));
    }
}
