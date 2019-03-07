package org.firstinspires.ftc.teamcode.hardware.drivetrain;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.ejml.simple.SimpleMatrix;
import org.firstinspires.ftc.teamcode.hardware.State;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class MecanumDriveTrain extends DriveTrain {

    private final double radiansToEncoderRotation = 1000;
    //make private
    public DcMotor fl, fr, bl, br;
    public double encoderXDriveWheel;
    public double encoderYDriveWheel;
    private int originTick;
    private int lastFLPos;
    private int lastFRPos;
    private int lastBLPos;
    private int lastBRPos;


    //The parameters r, a,  and b correspond to various measurements of the robot
    public MecanumDriveTrain() {

        //This powerMatrix is to perform a rotation depending on the angle of the roller's on the mecanum wheel
        //Our mecanum wheels have rollers mounted at a 45 degree angle

    }

    /*public void updatePos(double rot) {
        double deltaX = ((fl.getCurrentPosition()-lastFLPos + br.getCurrentPosition()-lastBRPos) - (fr.getCurrentPosition()-lastFRPos + bl.getCurrentPosition()-lastBLPos)) / 4d;
        double deltaY = ((fl.getCurrentPosition()-lastFLPos + br.getCurrentPosition()-lastBRPos) + (fr.getCurrentPosition()-lastFRPos + bl.getCurrentPosition()-lastBLPos)) / 4d;
        double
        this.encoderXDriveWheel =

    }*/
    public double encoderXDriveWheel() {
        return ((fl.getCurrentPosition() - lastFLPos + br.getCurrentPosition() - lastBRPos) - (fr.getCurrentPosition() + bl.getCurrentPosition())) / 4d;
    }

    public double encoderYDriveWheel() {
        return ((fl.getCurrentPosition() + br.getCurrentPosition()) + (fr.getCurrentPosition() + bl.getCurrentPosition())) / 4d;
    }

    public double encoderRDriveWheel() {
        return ((fl.getCurrentPosition() + bl.getCurrentPosition()) - (fr.getCurrentPosition() + br.getCurrentPosition())) / 4d;
    }

    @Override
    public void init(HardwareMap hwMap) {
        fl = hwMap.get(DcMotor.class, "fl");
        fr = hwMap.get(DcMotor.class, "fr");
        bl = hwMap.get(DcMotor.class, "bl");
        br = hwMap.get(DcMotor.class, "br");
        fl.setDirection(DcMotorSimple.Direction.FORWARD);
        bl.setDirection(DcMotorSimple.Direction.FORWARD);
        fr.setDirection(DcMotorSimple.Direction.REVERSE);
        br.setDirection(DcMotorSimple.Direction.REVERSE);
        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        fr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        br.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
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
    //Takes a DirRotVector, which encodes the desired rotation and movement in the x and y directionds
    //It also takes the angle of rotation of the robot itself

    @Override
    public void stop() {
        fl.setPower(0);
        fr.setPower(0);
        bl.setPower(0);
        br.setPower(0);
    }

    public SimpleMatrix getJ(double t) {
        SimpleMatrix rotMatrix = new SimpleMatrix(new double[][]{{cos(t), sin(t), 0}, {-sin(t), cos(t), 0}, {0, 0, 1}});
        SimpleMatrix mecanumMatrix = new SimpleMatrix(new double[][]{{1, 1, 1, 1}, {1, -1, -1, 1}, {-.5, .5, -.5, .5}});
        return rotMatrix.mult(mecanumMatrix);
    }

    public SimpleMatrix velocityToWheel(double vx, double vy, double vr, double botTheta) {
        SimpleMatrix velocity = new SimpleMatrix(new double[][]{{vy}, {vx}, {vr}});
        SimpleMatrix inv = (getJ((double) (botTheta))).pseudoInverse();
        return inv.mult(velocity).scale(4);
    }


    public SimpleMatrix drive(double lsx, double lsy, double lsr, double botTheta) {
        SimpleMatrix powVector = this.velocityToWheel(lsx, lsy, lsr, botTheta);
        double max = Math.max(Math.max(powVector.get(0, 0), powVector.get(1, 0)), Math.max(powVector.get(2, 0), powVector.get(3, 0)));
        if (max != 0 && Math.abs(max) > 1) {
            powVector = powVector.scale(1 / Math.abs(max));
        }
        System.out.println(powVector.get(1, 0));
        System.out.println(powVector.get(0, 0));
        System.out.println(powVector.get(2, 0));
        System.out.println(powVector.get(3, 0));

        this.fr.setPower(powVector.get(1, 0));
        this.fl.setPower(powVector.get(0, 0));
        this.bl.setPower(powVector.get(2, 0));
        this.br.setPower(powVector.get(3, 0));
        return powVector;
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

    public void tankControl(Gamepad gamepad, boolean slowMode, boolean reverseMode) {
        //Reverse the controls if reverse mode is on
        double leftTrigger = reverseMode ? gamepad.right_trigger : gamepad.left_trigger;
        double rightTrigger = reverseMode ? gamepad.left_trigger : gamepad.right_trigger;
        double rightStick = reverseMode ? -gamepad.left_stick_y : gamepad.right_stick_y;
        double leftStick = reverseMode ? -gamepad.right_stick_y : gamepad.left_stick_y;

        if (leftTrigger > .15) {
            //scale left strafe for precision mode
            leftTrigger *= (slowMode ? .5 : 1);
            //apply scaled power to motors
            this.strafepow(leftTrigger);
            return;
        }
        if (rightTrigger > .15) {
            //scale right strafe for precision mode
            rightTrigger *= (slowMode ? .5 : 1);
            //apply scaled power to motors
            this.strafepow(-rightTrigger);
            return;
        }
        //scale tank drive for precision mode
        leftStick *= (slowMode ? .5 : 1);
        rightStick *= (slowMode ? .5 : 1);
        //apply scaled power to motors
        fl.setPower(-leftStick);
        bl.setPower(-leftStick);
        fr.setPower(-rightStick);
        br.setPower(-rightStick);
    }

    public void drivepow(double power) {
        fl.setPower(power);
        fr.setPower(power);
        bl.setPower(power);
        br.setPower(power);
    }

    //Note: Pos is strafe right
    public void strafepow(double power) {
        fl.setPower(power);
        fr.setPower(-power);
        bl.setPower(-power);
        br.setPower(power);
    }

    public void setTarget(int targetDistance) {
        int target = this.distanceToRevsNRO20(targetDistance);
        //fl.setTargetPosition(fl.getCurrentPosition() + target);
        //fr.setTargetPosition(fr.getCurrentPosition() + target);
        //bl.setTargetPosition(bl.getCurrentPosition() + target);
        //br.setTargetPosition(br.getCurrentPosition() + target);
        this.setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fl.setTargetPosition(target);
        fr.setTargetPosition(target);
        bl.setTargetPosition(target);
        br.setTargetPosition(target);
        this.setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.originTick = fl.getCurrentPosition();
    }

    public void setStrafeTarget(int targetDistance) {
        int target = this.distanceToRevsNRO20(targetDistance);
        //fl.setTargetPosition(fl.getCurrentPosition() + target);
        //fr.setTargetPosition(fr.getCurrentPosition() - target);
        //bl.setTargetPosition(bl.getCurrentPosition() - target);
        //br.setTargetPosition(br.getCurrentPosition() + target);
        this.setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fl.setTargetPosition(target);
        fr.setTargetPosition(-target);
        bl.setTargetPosition(-target);
        br.setTargetPosition(target);
        this.setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.originTick = fl.getCurrentPosition();
    }

    public void setRunMode(DcMotor.RunMode mode) {
        fl.setMode(mode);
        fr.setMode(mode);
        bl.setMode(mode);
        br.setMode(mode);
        //try {Thread.sleep(1000);} catch (Exception e) {}
    }

    /**
     * @param gyroTarget The target heading in degrees, between 0 and 360
     * @param gyroRange  The acceptable range off target in degrees, usually 1 or 2
     * @param gyroActual The current heading in degrees, between 0 and 360
     * @param minSpeed   The minimum power to apply in order to turn (e.g. 0.05 when moving or 0.15 when stopped)
     * @param addSpeed   The maximum additional speed to apply in order to turn (proportional component), e.g. 0.3
     */
    public void gyroCorrect(double gyroTarget, double gyroRange, double gyroActual, double minSpeed, double addSpeed) {
        double delta = (gyroTarget - gyroActual + 360.0) % 360.0; //the difference between target and actual mod 360
        if (delta > 180.0) delta -= 360.0; //makes delta between -180 and 180
        if (Math.abs(delta) > gyroRange) { //checks if delta is out of range
            double gyroMod = delta / 45.0; //scale from -1 to 1 if delta is less than 45 degrees
            if (Math.abs(gyroMod) > 1.0)
                gyroMod = Math.signum(gyroMod); //set gyromod to 1 or -1 if the error is more than 45 degrees
            this.turn(minSpeed * Math.signum(gyroMod) + addSpeed * gyroMod);
        } else {
            this.turn(0.0);
        }
    }

    public void turn(double sPower) {
        fl.setPower(-sPower);
        fr.setPower(sPower);
        bl.setPower(-sPower);
        br.setPower(sPower);
    }

    public void scalePower() {
        double power;
        int target = fl.getTargetPosition();
        int current = fl.getCurrentPosition();
        int sign = target < current ? -1 : 1;
        int diff = Math.abs(target - current);
        int originDiff = Math.abs(this.originTick - current);

        if (originDiff < 75) {
            power = .1;
        } else if (originDiff < 250) {
            power = .3;
        } else if (originDiff < 400) {
            power = .5;
        } else {
            power = 1;
        }

        if (diff < 100) {
            power = .1;
        } else if (diff < 300) {
            power = .3;
        } else if (diff < 500) {
            power = .5;
        } else if (diff < 750) {
            power = .7;
        }

        this.drivepow(sign * power);
    }

    public void germanScalePower() {
        double power;
        int target = fl.getTargetPosition();
        int current = fl.getCurrentPosition();
        int sign = target < current ? -1 : 1;
        int diff = Math.abs(target - current);
        int originDiff = Math.abs(this.originTick - current);

        if (originDiff < 75) {
            power = .7;
        } else if (originDiff < 250) {
            power = .7;
        } else if (originDiff < 400) {
            power = .7;
        } else {
            power = 1;
        }

        if (diff < 100) {
            power = .7;
        } else if (diff < 300) {
            power = .7;
        } else if (diff < 500) {
            power = .7;
        } else if (diff < 750) {
            power = .7;
        }

        this.drivepow(sign * power);
    }

    public void slightlyLessGermanScalePower() {
        double power;
        int target = fl.getTargetPosition();
        int current = fl.getCurrentPosition();
        int sign = target < current ? -1 : 1;
        int diff = Math.abs(target - current);
        int originDiff = Math.abs(this.originTick - current);

        if (originDiff < 75) {
            power = .7;
        } else if (originDiff < 250) {
            power = .7;
        } else if (originDiff < 400) {
            power = .7;
        } else {
            power = 1;
        }

        if (diff < 100) {
            power = .1;
        } else if (diff < 300) {
            power = .3;
        } else if (diff < 500) {
            power = .7;
        } else if (diff < 750) {
            power = .7;
        }

        this.drivepow(sign * power);
    }

    private int distanceToRevsNRO20(double distance) {
        final double wheelCirc = 31.9185813;
        final double gearMotorTickThing = 537.6; //neverrest orbital 20 = 537.6 counts per revolution
        //1:1 gear ratio so no need for multiplier
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
