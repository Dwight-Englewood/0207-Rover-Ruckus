package org.firstinspires.ftc.teamcode.hardware.scoring;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.hardware.State;
import org.firstinspires.ftc.teamcode.hardware.Subsystem;

import wen.control.PIDController;

public class IntakeSlides implements Subsystem {

    private final double pivotUpPos = 1;
    private final double pivotDownPos = -1;
    private final double kp = 1;
    private final double ki = 0;
    private final double kd = 0;
    private final int encoderTicksExtended = 5000; // dummy val
    private final int encoderTicksRetracted = 0;
    private final int encoderTicksPivotDeadzone = 100;
    private final double resolution = 10;

    private DcMotor extendo;
    private CRServo intake;
    private Servo pivot;
    private DigitalChannel magSwitch;

    private PIDController pidIntake = new PIDController(kp, ki, kd);

    private IntakeSlideState state;

    @Override
    public void init(HardwareMap hwMap) {
        extendo = hwMap.get(DcMotor.class, "extendo");
        extendo.setDirection(DcMotorSimple.Direction.FORWARD);
        extendo.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extendo.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        intake = hwMap.get(CRServo.class, "intake");

        pivot = hwMap.get(Servo.class, "pivot");
        pivot.setPosition(pivotUpPos);

        magSwitch = hwMap.get(DigitalChannel.class, "magSwitch");

        state = IntakeSlideState.RETRACTED;
    }

    @Override
    public void start() {
        extendo.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        pivot.setPosition(pivotUpPos);
        intake.setPower(0);
    }

    @Override
    public void reset() {
        extendo.setPower(0);
        pivot.setPosition(pivotUpPos);
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
        intake.setPower(1);
    }

    public void outtake() {
        intake.setPower(-1);
    }

    public void pivotUp() {
        pivot.setPosition(pivotUpPos);
    }

    public void pivotDown() {
        pivot.setPosition(pivotDownPos);
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
            if (pidIntake.goalReached(resolution) || magSwitch.getState() == true) {
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

    public void retractBasic() {
        extendo.setPower(-1);
    }

    public void update(Command c) {
        switch (c) {
            case IDLE:
                break;
            case OKTOSTOPANDRESET:
                if (this.state == IntakeSlideState.RETRACTED && extendo.getMode() != DcMotor.RunMode.STOP_AND_RESET_ENCODER) {
                    extendo.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
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

    private enum IntakeSlideState implements State {
        EXTENDED("Extended"),
        EXTENDING("Extending"),
        RETRACTING("Retracting"),
        RETRACTED("Retracted");

        private String str;

        IntakeSlideState(String str) {
            this.str = str;
        }

        @Override
        public String getStateVal() {
            return this.str;
        }
    }
}
