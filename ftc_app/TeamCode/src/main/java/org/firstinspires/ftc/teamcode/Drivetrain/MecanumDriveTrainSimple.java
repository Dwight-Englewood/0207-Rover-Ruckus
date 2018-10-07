package org.firstinspires.ftc.teamcode.Drivetrain;

import org.ejml.simple.SimpleMatrix;
import org.firstinspires.ftc.teamcode.Matrices.DirRotVector;
import org.firstinspires.ftc.teamcode.Matrices.PowerVector4WD;

class MecanumDriveTrainSimple {

    final double l, alpha, r;
    final SimpleMatrix powerMatrix;

    public MecanumDriveTrainSimple(double r, double a, double b) {
        this.l = Math.sqrt(Math.pow(a, 2.0) + Math.pow(b, 2));
        this.r = a;
        this.alpha = Math.atan(b / a);
        double two = Math.sqrt(2) / 2;
        double thing = l * Math.sin(Math.PI / 4 - alpha);
        this.powerMatrix = new SimpleMatrix(new double[][]{
                {
                        two, two, thing
                },

                {
                        two, -two, thing
                },

                {
                        -two, -two, thing
                },

                {
                        -two, two, thing
                }
        });
    }

    public static void main(String[] args) {
        MecanumDriveTrainSimple mdt = new MecanumDriveTrainSimple(5, 9, 9);

        SimpleMatrix p = mdt.drive(new DirRotVector(1, Math.sqrt(3), Math.PI));
        p.print();
        //mdt.rotationMatrix(Math.PI).mult(new SimpleMatrix(new double[][]{{2, 2}, {1, 1}})).print();
    }

    public PowerVector4WD drive(DirRotVector drv) {
        return new PowerVector4WD(powerMatrix.mult(rotationMatrix(drv.get(2,0)).mult(drv).scale(-Math.sqrt(2)/r)));
    }

    SimpleMatrix rotationMatrix(double theta) {
        return new SimpleMatrix(new double[][]{{Math.cos(theta), Math.sin(theta), 0}, {-Math.sin(theta), Math.cos(theta), 0}, {0, 0, 1}});
    }
}
