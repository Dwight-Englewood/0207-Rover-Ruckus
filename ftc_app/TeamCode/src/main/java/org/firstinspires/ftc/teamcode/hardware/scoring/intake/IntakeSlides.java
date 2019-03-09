package org.firstinspires.ftc.teamcode.hardware.scoring.intake;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.hardware.State;
import org.firstinspires.ftc.teamcode.hardware.Subsystem;

public class IntakeSlides implements Subsystem {

    private final double pivotUpPos = .2 ;
    private final double pivotMidPos = .65;
    private final double pivotDownPos = 1;

    private final double intakePower = .7;
    private final double outtakePower = -.7;
    private final double notakePower = 0;

    private final double kp = 1;
    private final double ki = 0;
    private final double kd = 0;

    private final int encoderTicksExtended = 5000; // dummy val
    private final int encoderTicksRetracted = 0;
    private final int encoderTicksPivotDeadzone = 100;

    private final double resolution = 10;
    public DigitalChannel magSwitchIntake;
    private DcMotorEx extendo;
    private CRServo intake;
    private Servo intakePivot;

    /*public PIDController pidIntake = new PIDController(kp, ki, kd);

    public IntakeSlideState state;*/

    @Override
    public void init(HardwareMap hwMap) {
        extendo = (DcMotorEx) hwMap.get(DcMotor.class, "extendo");
        extendo.setDirection(DcMotorSimple.Direction.REVERSE);
        extendo.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extendo.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        intake = hwMap.get(CRServo.class, "intake");

        intakePivot = hwMap.get(Servo.class, "intakePivot");
        intakePivot.scaleRange(0.1, 0.8);

        this.pivotUp();
        magSwitchIntake = hwMap.get(DigitalChannel.class, "magSwitchIntake");

        //state = IntakeSlideState.RETRACTED;
    }

    @Override
    public void start() {
        extendo.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        extendo.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intake.setPower(0);
    }

    @Override
    public void reset() {
        extendo.setPower(0);
        this.pivotMiddle();
        intake.setPower(0);

    }

    @Override
    public void stop() {
        extendo.setPower(0);
        intake.setPower(0);

    }

    @Override
    public State getState() {
        return IntakeSlideState.EXTENDED;
    }

    public void intake() {
        intake.setPower(this.intakePower);
    }

    public void outtake() {
        intake.setPower(this.outtakePower);
    }

    public void notake() {
        intake.setPower(this.notakePower);
    }

    public void pivotUp() {
        intakePivot.setPosition(pivotUpPos);
    }

    public void pivotDown() {
        if (magSwitchIntake.getState() == true) {
            intakePivot.setPosition(pivotDownPos);
        }
    }

    public void pivotMiddle() {
        intakePivot.setPosition(pivotMidPos);
    }

    public void idle() {
        extendo.setPower(0);
    }

    public void variableMove(double d) {
        double pow = Range.clip(d, -1, 1);
        if (pow < -.05) {
            if (magSwitchIntake.getState() == true) {
                extendo.setPower(.5*d);
                pivotMiddle();
            }
        } else if (pow > 0.5) {
            extendo.setPower(.5*d);
        } else {
            extendo.setVelocity(0);
        }
    }

    /*public void setIntakePosition(double d) {
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
    }*/



    /*public void extendPID() {
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
    }*/


    /*
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
    }*/



    /*public void update(Command c) {
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
    }*/
    /*
    public void intakeEncoder() {
    }

    public void pivotEncoder() {

    }*/

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
