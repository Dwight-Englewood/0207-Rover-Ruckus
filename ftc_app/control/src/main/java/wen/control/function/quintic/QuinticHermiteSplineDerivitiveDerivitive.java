package wen.control.function.quintic;

import wen.control.function.Coordinate;

import static wen.control.function.quintic.QuinticHermiteBasis.h0dd;
import static wen.control.function.quintic.QuinticHermiteBasis.h1dd;
import static wen.control.function.quintic.QuinticHermiteBasis.h2dd;
import static wen.control.function.quintic.QuinticHermiteBasis.h3dd;
import static wen.control.function.quintic.QuinticHermiteBasis.h4dd;
import static wen.control.function.quintic.QuinticHermiteBasis.h5dd;


public class QuinticHermiteSplineDerivitiveDerivitive extends ParamatricFunction {

    public Coordinate p0;
    public Coordinate v0;
    public Coordinate a0;
    public Coordinate p1;
    public Coordinate v1;
    public Coordinate a1;

    public QuinticHermiteSplineDerivitiveDerivitive(Coordinate p0, Coordinate v0, Coordinate a0, Coordinate p1, Coordinate v1, Coordinate a1) {
        this.p0 = p0;
        this.v0 = v0;
        this.a0 = a0;
        this.p1 = p1;
        this.v1 = v1;
        this.a1 = a1;
    }

    @Override
    public Coordinate eval(double t) {
        return (new Coordinate(h0dd.eval(t).y * p0.x + h1dd.eval(t).y * v0.x + h2dd.eval(t).y * a0.x + h3dd.eval(t).y * a1.x + h4dd.eval(t).y * v1.x + h5dd.eval(t).y * p1.x, h0dd.eval(t).y * p0.y + h1dd.eval(t).y * v0.y + h2dd.eval(t).y * a0.y + h3dd.eval(t).y * a1.y + h4dd.eval(t).y * v1.y + h5dd.eval(t).y * p1.y));
    }
}
