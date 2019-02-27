package org.firstinspires.ftc.teamcode.matrices;

import org.ejml.simple.SimpleMatrix;

public class DirRotVector extends SimpleMatrix {

    public DirRotVector(double x, double y, double theta) {
        super(3,1);
        this.set(0, 0, x);
        this.set(1, 0, y);
        this.set(2, 0, theta);
    }

    double magnitude() {
        return Math.sqrt(this.dot(this));
    }

    double angle() {
        return (Math.atan(this.get(0,0) / this.get(1,0)));
    }
}
