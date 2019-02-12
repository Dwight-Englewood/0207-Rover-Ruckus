package wen.control.function.Quintic;

public abstract class ParamatricFunction {

    public double tMin = 0;
    public double tMax = 1;

    public abstract Coordinate eval(double t);
}
