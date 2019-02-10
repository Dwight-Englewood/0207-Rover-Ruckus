package wen.control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import wen.control.PathFunction;

public class Trajectory {

    public ArrayList<Float> xCoords = new ArrayList<>();
    public ArrayList<Float> yCoords = new ArrayList<>();

    public Trajectory(Float[] xCoords, Float[] yCoords) {
        this.xCoords = new ArrayList<>(Arrays.asList(xCoords));
        this.yCoords = new ArrayList<>(Arrays.asList(yCoords));


        //this.yCoords = yCoords;
    }


    public Trajectory(PathFunction pf) {
        for (float i = pf.xMin; i < pf.xMax; i = i + 1 / (float) 100) {
            xCoords.add(i);
            yCoords.add(pf.function(i));
        }
    }

}
