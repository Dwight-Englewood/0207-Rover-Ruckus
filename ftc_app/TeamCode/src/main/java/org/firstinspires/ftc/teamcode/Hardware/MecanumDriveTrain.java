package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.ejml.simple.SimpleMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.Matrices.DirRotVector;
import org.firstinspires.ftc.teamcode.Matrices.PowerVector4WD;

public class MecanumDriveTrain implements Subsystem {

    final double l, alpha, r;
    final SimpleMatrix powerMatrix;

    public DcMotor fl, fr, bl, br;



    public MecanumDriveTrain(double r, double a, double b) {
        this.l = Math.sqrt(Math.pow(a, 2.0) + Math.pow(b, 2));
        this.r = a;
        this.alpha = Math.atan(b / a);
        double rollerAngleFactor = Math.sqrt(2) / 2;
        double scaleFactor = l * Math.sin(Math.PI / 4 - alpha);
        this.powerMatrix = new SimpleMatrix(new double[][]{
                {
                        rollerAngleFactor, rollerAngleFactor, scaleFactor
                },

                {
                        rollerAngleFactor, -rollerAngleFactor, scaleFactor
                },

                {
                        -rollerAngleFactor, -rollerAngleFactor, scaleFactor
                },

                {
                        -rollerAngleFactor, rollerAngleFactor, scaleFactor
                }
        });
    }

    public PowerVector4WD driveVector(DirRotVector drv) {
        return new PowerVector4WD(powerMatrix.mult(rotationMatrix(drv.get(2,0)).mult(drv).scale(-Math.sqrt(2)/r)));
    }

    public void drive(DirRotVector drv) {
        PowerVector4WD merp = this.driveVector(drv);
        merp = merp.scale();
        this.fr.setPower(merp.get(0, 0));
        this.fl.setPower(merp.get(1, 0));
        this.bl.setPower(merp.get(2, 0));
        this.br.setPower(merp.get(3, 0));
    }

    public void rotate(boolean m) {
        if (m) {
            this.fl.setPower(1);
            this.bl.setPower(1);
            this.fr.setPower(-1);
            this.br.setPower(-1);
        } else {
            this.fl.setPower(-1);
            this.bl.setPower(-1);
            this.fr.setPower(1);
            this.br.setPower(1);
        }
    }


    private SimpleMatrix rotationMatrix(double theta) {
        return new SimpleMatrix(new double[][]{{Math.cos(theta), Math.sin(theta), 0}, {-Math.sin(theta), Math.cos(theta), 0}, {0, 0, 1}});
    }

    @Override
    public void init(HardwareMap hwMap) {
        fl = hwMap.get(DcMotor.class, "fl");
        fr = hwMap.get(DcMotor.class, "fr");
        bl = hwMap.get(DcMotor.class, "bl");
        br = hwMap.get(DcMotor.class, "br");
        this.fl.setDirection(DcMotorSimple.Direction.FORWARD);
        this.bl.setDirection(DcMotorSimple.Direction.REVERSE);
        this.fr.setDirection(DcMotorSimple.Direction.FORWARD);
        this.br.setDirection(DcMotorSimple.Direction.REVERSE);


    }

    @Override
    public void start() {
        fl.setPower(0);
        fr.setPower(0);
        bl.setPower(0);
        br.setPower(0);
    }

    @Override
    public void reset() {

    }

    @Override
    public Enum getState() {
        return null;
    }
}
