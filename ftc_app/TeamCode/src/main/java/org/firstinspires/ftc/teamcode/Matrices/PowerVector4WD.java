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

}
