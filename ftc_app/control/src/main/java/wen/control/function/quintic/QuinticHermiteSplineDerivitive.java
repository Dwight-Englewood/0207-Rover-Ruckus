package wen.control.function.quintic;

import wen.control.function.Coordinate;
import wen.control.function.ParamatricFunction;

import static wen.control.function.quintic.QuinticHermiteBasis.h0d;
import static wen.control.function.quintic.QuinticHermiteBasis.h1d;
import static wen.control.function.quintic.QuinticHermiteBasis.h2d;
import static wen.control.function.quintic.QuinticHermiteBasis.h3d;
import static wen.control.function.quintic.QuinticHermiteBasis.h4d;
import static wen.control.function.quintic.QuinticHermiteBasis.h5d;


public class QuinticHermiteSplineDerivitive extends ParamatricFunction {

    public Coordinate p0;
    public Coordinate v0;
    public Coordinate a0;
    public Coordinate p1;
    public Coordinate v1;
    public Coordinate a1;

    public QuinticHermiteSplineDerivitive(Coordinate p0, Coordinate v0, Coordinate a0, Coordinate p1, Coordinate v1, Coordinate a1) {
        this.p0 = p0;
        this.v0 = v0;
        this.a0 = a0;
        this.p1 = p1;
        this.v1 = v1;
        this.a1 = a1;
    }

    @Override
    public Coordinate eval(double t) {
        return (new Coordinate(h0d.eval(t) * p0.x + h1d.eval(t) * v0.x + h2d.eval(t) * a0.x + h3d.eval(t) * a1.x + h4d.eval(t) * v1.x + h5d.eval(t) * p1.x, h0d.eval(t) * p0.y + h1d.eval(t)* v0.y + h2d.eval(t) * a0.y + h3d.eval(t) * a1.y + h4d.eval(t) * v1.y + h5d.eval(t)* p1.y));
    }
}
