package wen.control.function;

import java.util.ArrayList;
import java.util.Arrays;

public class PiecewiseLinear extends Function {

    //Assumed to be strictly increasing x values - NOT PARAMETRIC
    public ArrayList<Coordinate> coords;

    public PiecewiseLinear(Coordinate[] coords) {
        this.coords = new ArrayList<>(Arrays.asList(coords));
    }

    @Override
    public double eval(double t) {
        for (int i = 0; i < this.coords.size()-1; i++) {
            Coordinate one = this.coords.get(i);
            Coordinate two = this.coords.get(i+1);
            if (one.x < t && t < two.x) {
                return (two.y - one.y)/(two.x - one.x) * (t-one.x) + one.y;
            }
        }
        return 0; // If the supplied value isnt within range just assume 0 i guess
    }

}
