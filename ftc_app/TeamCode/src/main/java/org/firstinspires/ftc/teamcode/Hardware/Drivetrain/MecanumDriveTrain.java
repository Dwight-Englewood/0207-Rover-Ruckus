package org.firstinspires.ftc.teamcode.Hardware.Drivetrain;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.ejml.simple.SimpleMatrix;
import org.firstinspires.ftc.teamcode.Hardware.State;
import org.firstinspires.ftc.teamcode.Matrices.DirRotVector;
import org.firstinspires.ftc.teamcode.Matrices.PowerVector4WD;

public class MecanumDriveTrain extends GenericDriveTrain {

    private final double l, alpha, r;
    private final SimpleMatrix powerMatrix;
    
    //make private
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

    @Override
    public void init(HardwareMap hwMap) {
        fl = hwMap.get(DcMotor.class, "fl");
        fr = hwMap.get(DcMotor.class, "fr");
        bl = hwMap.get(DcMotor.class, "bl");
        br = hwMap.get(DcMotor.class, "br");
        fl.setDirection(DcMotorSimple.Direction.REVERSE);
        bl.setDirection(DcMotorSimple.Direction.REVERSE);
        fr.setDirection(DcMotorSimple.Direction.FORWARD);
        br.setDirection(DcMotorSimple.Direction.FORWARD);
        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        br.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //Set to brake zeroPower for wacky drifting?
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
    public void stop() {
        fl.setPower(0);
        fr.setPower(0);
        bl.setPower(0);
        br.setPower(0);
    }

    public PowerVector4WD driveVector(DirRotVector drv, double botTheta) {
        return new PowerVector4WD(powerMatrix.mult(rotationMatrix(botTheta).mult(drv).scale(-Math.sqrt(2)/r)));
    }

    public void drive(DirRotVector drv, double botTheta) {
        PowerVector4WD merp = this.driveVector(drv, botTheta);
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

    public void tankControl(Gamepad gamepad) {
        if (gamepad.left_trigger >.15) {
            fl.setPower(-gamepad.left_trigger);
            fr.setPower(gamepad.left_trigger);
            bl.setPower(gamepad.left_trigger);
            br.setPower(-gamepad.left_trigger);
            return;
        }
        if (gamepad.right_trigger >.15) {
            fl.setPower(gamepad.right_trigger);
            fr.setPower(-gamepad.right_trigger);
            bl.setPower(-gamepad.right_trigger);
            br.setPower(gamepad.right_trigger);
            return;
        }
        fl.setPower(-gamepad.left_stick_y);
        bl.setPower(-gamepad.left_stick_y);
        fr.setPower(-gamepad.right_stick_y);
        br.setPower(-gamepad.right_stick_y);

    }

    public void drivepow(double power) {
        fl.setPower(power);
        fr.setPower(power);
        bl.setPower(power);
        br.setPower(power);
    }

    public void strafepow(double power) {
        fl.setPower(-power);
        fr.setPower(power);
        bl.setPower(power);
        br.setPower(-power);
    }

    public void setTarget(int targetDistance) {
        int target = this.distanceToRevsNRO20(targetDistance);
        fl.setTargetPosition(fl.getCurrentPosition() + target);
        fr.setTargetPosition(fr.getCurrentPosition() + target);
        bl.setTargetPosition(bl.getCurrentPosition() + target);
        br.setTargetPosition(br.getCurrentPosition() + target);
    }

    public void setRunMode(DcMotor.RunMode mode) {
        fl.setMode(mode);
        fr.setMode(mode);
        bl.setMode(mode);
        br.setMode(mode);
    }

    /**
     * @param gyroTarget The target heading in degrees, between 0 and 360
     * @param gyroRange The acceptable range off target in degrees, usually 1 or 2
     * @param gyroActual The current heading in degrees, between 0 and 360
     * @param minSpeed The minimum power to apply in order to turn (e.g. 0.05 when moving or 0.15 when stopped)
     * @param addSpeed The maximum additional speed to apply in order to turn (proportional component), e.g. 0.3
     */
    public void gyroCorrect(double gyroTarget, double gyroRange, double gyroActual, double minSpeed, double addSpeed) {
        double delta = (gyroTarget - gyroActual + 360.0) % 360.0; //the difference between target and actual mod 360
        if (delta > 180.0) delta -= 360.0; //makes delta between -180 and 180
        if (Math.abs(delta) > gyroRange) { //checks if delta is out of range
            double gyroMod = delta / 45.0; //scale from -1 to 1 if delta is less than 45 degrees
            if (Math.abs(gyroMod) > 1.0) gyroMod = Math.signum(gyroMod); //set gyromod to 1 or -1 if the error is more than 45 degrees
            this.turn(minSpeed * Math.signum(gyroMod) + addSpeed * gyroMod);
        }
        else {
            this.turn(0.0);
        }
    }
    private void turn(double sPower) {
        fl.setPower(-sPower);
        fr.setPower(sPower);
        bl.setPower(-sPower);
        br.setPower(sPower);
    }

    private int distanceToRevsNRO20(double distance) {
        final double wheelCirc = 31.9185813;
        final double gearMotorTickThing = 537.6; //neverrest orbital 20 = 537.6 counts per revolution
        return (int) (gearMotorTickThing * (distance / wheelCirc));
    }

    @Override
    public State getState() {
        return null;
    }

    @Override
    public String toString() {
        return "";
    }
}
