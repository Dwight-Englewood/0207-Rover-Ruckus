package wen.control.function;

import wen.control.function.Coordinate;

public abstract class ParamatricFunction {

    public double tMin = 0;
    public double tMax = 1;

    public abstract Coordinate eval(double t);

    public double length() {
        double collect = 0;
        for (double t = tMin+.001; t < tMax; t = t + .001) {
            Coordinate j = this.eval(t-.001);
            Coordinate i = this.eval(t);
            collect = collect + Math.sqrt(Math.pow(i.x-j.x, 2) + Math.pow(i.y-j.y, 2));
        }
        return collect;
    }


}
