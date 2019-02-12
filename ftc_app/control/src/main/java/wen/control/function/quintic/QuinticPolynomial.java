package wen.control.function.quintic;

import wen.control.function.Coordinate;

public class QuinticPolynomial extends ParamatricFunction {

    public final double a;
    public final double b;
    public final double c;
    public final double d;
    public final double e;
    public final double f;

    public QuinticPolynomial(double a, double b, double c, double d, double e, double f) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
    }

    public Coordinate eval(double t) {
        return (new Coordinate(t, a * Math.pow(t, 5) + b * Math.pow(t, 4) + c * Math.pow(t, 3) + d * Math.pow(t, 2) + e * Math.pow(t, 1) + f));
    }
}
