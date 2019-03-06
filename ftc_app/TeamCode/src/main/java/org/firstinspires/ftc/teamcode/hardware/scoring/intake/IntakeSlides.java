package org.firstinspires.ftc.teamcode.hardware.scoring.intake;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.hardware.State;
import org.firstinspires.ftc.teamcode.hardware.Subsystem;

import wen.control.PIDController;

public class IntakeSlides implements Subsystem {

    public final double pivotUpPos = 1;
    public final double pivotDownPos = -1;

    public final double kp = 1;
    public final double ki = 0;
    public final double kd = 0;

    public final int encoderTicksExtended = 5000; // dummy val
    public final int encoderTicksRetracted = 0;
    public final int encoderTicksPivotDeadzone = 100;

    public final double resolution = 10;

    public DcMotor extendo;
    public CRServo intake;
    public Servo intakePivot;

    public DigitalChannel magSwitchIntake;

    public PIDController pidIntake = new PIDController(kp, ki, kd);

    public IntakeSlideState state;

    @Override
    public void init(HardwareMap hwMap) {
        extendo = hwMap.get(DcMotor.class, "extendo");
        extendo.setDirection(DcMotorSimple.Direction.FORWARD);
        extendo.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extendo.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        intake = hwMap.get(CRServo.class, "intake");

        intakePivot = hwMap.get(Servo.class, "intakePivot");
        intakePivot.setPosition(pivotUpPos);

        magSwitchIntake = hwMap.get(DigitalChannel.class, "magSwitchIntake");

        state = IntakeSlideState.RETRACTED;
    }

    @Override
    public void start() {
        extendo.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        extendo.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakePivot.setPosition(pivotUpPos);
        intake.setPower(0);
    }

    @Override
    public void reset() {
        extendo.setPower(0);
        intakePivot.setPosition(pivotUpPos);
        intake.setPower(0);

    }

    @Override
    public void stop() {
        extendo.setPower(0);
        intake.setPower(0);

    }

    @Override
    public State getState() {
        return state;
    }

    public void intake() {
        intake.setPower(.7);
    }

    public void outtake() {
        intake.setPower(-.7);
    }

    public void notake() {
        intake.setPower(0);
    }

    public void setIntakePosition(double d) {
        if (d < -1) {
            d = -1;
        } else if (d > 1) {
            d = 1;
        }
        d = d + 1;
        d = d / 2;
        double pos = this.pivotDownPos + d*(this.pivotUpPos - this.pivotDownPos);
        intakePivot.setPosition(pos);
        if (Math.abs(pos) > .75) {
            this.intake();
        }
    }

    public void pivotUp() {
        intakePivot.setPosition(pivotUpPos);
    }

    public void pivotDown() {
        intakePivot.setPosition(pivotDownPos);
    }

    public void pivotMiddle() {
        intakePivot.setPosition(0);
    }

    public void extendPID() {
        if (this.state != IntakeSlideState.EXTENDING && this.state != IntakeSlideState.EXTENDED) {
            this.state = IntakeSlideState.EXTENDING;
            pidIntake.setGoal(encoderTicksExtended);
        }
        if (this.state != IntakeSlideState.EXTENDED) {
            pidIntake.updateError((double) extendo.getCurrentPosition());
            if (pidIntake.goalReached(resolution)) {
                this.state = IntakeSlideState.EXTENDED;
                extendo.setPower(0);
            } else {
                extendo.setPower(pidIntake.correction());
            }
        }
    }

    public void retractPID() {
        if (this.state != IntakeSlideState.RETRACTING && this.state != IntakeSlideState.RETRACTED) {
            this.state = IntakeSlideState.RETRACTING;
            pidIntake.setGoal(encoderTicksRetracted);
        }
        if (this.state != IntakeSlideState.RETRACTED) {
            pidIntake.updateError((double) extendo.getCurrentPosition());
            if (pidIntake.goalReached(resolution) || magSwitchIntake.getState() == true) {
                this.state = IntakeSlideState.RETRACTED;
                extendo.setPower(0);
            } else {
                extendo.setPower(pidIntake.correction());
            }
        }
    }

    public void extendBasic() {
        extendo.setPower(1);
    }

    public void moveVariable(double d) {
        double pow = Range.clip(d, -1, 1);
        if (pow < 0) {
            if (magSwitchIntake.getState() == true) {
                extendo.setPower(d);
                pivotMiddle();
            }
        } else {
            extendo.setPower(d);
        }
    }


    public void extendBasicSlow() {
        extendo.setPower(.3);
    }

    public void retractBasic() {
        if (magSwitchIntake.getState() == true) {
            extendo.setPower(-1);
        } else {
            extendo.setPower(0);
        }
    }

    public void retractBasicSlow() {
        if (magSwitchIntake.getState() == true) {
            extendo.setPower(-.3);
        } else {
            extendo.setPower(0);
        }
    }

    public void idle() {
        extendo.setPower(0);
    }


    public void update(Command c) {
        switch (c) {
            case IDLE:
                break;
            case OKTOSTOPANDRESET:
                if (this.state == IntakeSlideState.RETRACTED && extendo.getMode() != DcMotor.RunMode.STOP_AND_RESET_ENCODER) {
                    //]]extendo.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                }
            case EXTEND:
                this.extendPID();
                break;
            case RETRACT:
                this.retractPID();
                break;
        }
        this.pivotEncoder();
        this.intakeEncoder();
    }

    public void intakeEncoder() {
    }

    public void pivotEncoder() {

    }

    public enum Command {
        IDLE,
        OKTOSTOPANDRESET,
        EXTEND,
        RETRACT;
    }

    public enum IntakeSlideState implements State {
        EXTENDED("Extended"),
        EXTENDING("Extending"),
        RETRACTING("Retracting"),
        RETRACTED("Retracted");

        public String str;

        IntakeSlideState(String str) {
            this.str = str;
        }

        @Override
        public String getStateVal() {
            return this.str;
        }
    }
}
