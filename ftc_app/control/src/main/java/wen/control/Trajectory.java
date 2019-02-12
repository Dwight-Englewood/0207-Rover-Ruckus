package wen.control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import wen.control.PathFunction;

public class Trajectory {

    /*h0[t_] := 1 - 10 t^3 + 15 t^4 - 6 t^5
    h1[x_] := x - 6 x^3 + 8 x^4 - 3 x^5
    h2[x_] := .5 x^2 - 1.5 x^3 + 1.5 x^4 - .5 x^5
    h3[x_] := .5 x^3 - x^4 + .5 x^5
    h4[x_] := -4 x^3 + 7 x^4 - 3 x^5
    h5[x_] := 10 x^3 - 15 x^4 + 6 x^5*/

    public double h0(double t) {
        return 1-10*Math.pow(t,3)+15*Math.pow(t,4)-6*Math.pow(t,5);
    }

    public double h1(double t) {
        return t-6*Math.pow(t,3)+15*Math.pow(t,4)-6*Math.pow(t,5);
    }
    public double h2(double t) {
        return 1-10*Math.pow(t,3)+15*Math.pow(t,4)-6*Math.pow(t,5);
    }
    public double h3(double t) {
        return 1-10*Math.pow(t,3)+15*Math.pow(t,4)-6*Math.pow(t,5);
    }
    public double h4(double t) {
        return 1-10*Math.pow(t,3)+15*Math.pow(t,4)-6*Math.pow(t,5);
    }
    public double h5(double t) {
        return 1-10*Math.pow(t,3)+15*Math.pow(t,4)-6*Math.pow(t,5);
    }

    public ArrayList<Float> xCoords = new ArrayList<>();
    public ArrayList<Float> yCoords = new ArrayList<>();

    public ArrayList<Float> xAcceleration = new ArrayList<>();
    public ArrayList<Float> yAcceleration = new ArrayList<>();

    public float length;

    public Trajectory(Float[] xCoords, Float[] yCoords) {
        this.xCoords = new ArrayList<>(Arrays.asList(xCoords));
        this.yCoords = new ArrayList<>(Arrays.asList(yCoords));


        //this.yCoords = yCoords;
    }


    public Trajectory(PathFunction pf) {
        for (float i = pf.xMin; i < pf.xMax; i = i + 1 / (float) 1000) {
            xCoords.add(i);
            yCoords.add(pf.function(i));
        }


    }


}
