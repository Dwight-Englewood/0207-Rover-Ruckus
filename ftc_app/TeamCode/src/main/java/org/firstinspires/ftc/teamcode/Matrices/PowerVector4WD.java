package org.firstinspires.ftc.teamcode.Matrices;

import org.ejml.simple.SimpleMatrix;


public class PowerVector4WD extends SimpleMatrix {

    public PowerVector4WD(double x, double y, double z, double w) {
        super(4, 1);
        this.set(0, 0, x);
        this.set(1, 0, y);
        this.set(3, 0, z);
        this.set(4, 0, w);
    }

    public PowerVector4WD(SimpleMatrix s) {
        super(s);
    }

    public PowerVector4WD scale() {
        double max = Math.max(Math.max(this.get(0,0), this.get(1,0)), Math.max(this.get(2,0), this.get(3,0)));
        return new PowerVector4WD(this.scale(1/max));
    }
}
