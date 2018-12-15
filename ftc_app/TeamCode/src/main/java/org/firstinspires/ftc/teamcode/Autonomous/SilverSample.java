package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Hardware.Bot;
import org.firstinspires.ftc.teamcode.Hardware.Sensors.TFWrapper;
import org.firstinspires.ftc.teamcode.Hardware.Sensors.TFWrapperDriveby;

import java.util.Arrays;


@Autonomous(name = "SilverSample", group = "AutonSample")
//@Disabled
public class SilverSample extends OpMode {
    Bot robot = new Bot(true, true);
    int command = 0;
    ElapsedTime timer = new ElapsedTime();
    int goldCount = 0;
    TFWrapper.TFState position;

    @Override
    public void init() {
        robot.init(hardwareMap);
        telemetry.addLine("ready");
        telemetry.update();
    }

    @Override
    public void init_loop() {
        telemetry.addLine("in init");
    }

    @Override
    public void start() {
        robot.start();
        timer.reset();
    }

    @Override
    public void loop() {
        switch (command) {
            case 0:
                if (robot.lift.newYears()) {
                    this.timer.reset();
                    this.command++;
                }
                break;

            case 1:
                this.setTarget(-12);
                break;

            case 2:
                this.finishDrive();
                break;

            case 3:
                this.gyroCorrect(-90, 1, .05, .2);
                break;

            case 4:
                this.setStrafeTarget(-40);
                break;

            case 5:
                this.finishDrive();
                break;

            case 6:
                this.gyroCorrect(-90, 1, .05, .2);
                break;

            case 7:
                this.setTarget(50);
                break;

            case 8:
                this.finishDrive();
                if (robot.tensorFlow.getState() == TFWrapperDriveby.TFStateDriveby.GOLD) this.goldCount++;
                if (goldCount > 10) {
                    robot.stop();
                    command++;
                }
                break;

            case 9:
                this.setStrafeTarget(-10);
                break;

            case 10:
                this.finishDrive();
                break;

            case 11:
                this.setStrafeTarget(10);
                break;

            case 12:
                this.finishDrive();
                break;

            case 13:
                this.gyroCorrect(-90, 1, .05, .2);
                break;

            case 14:
                this.setTarget(0);
                break;
        }
        telemetry.update();
    }

    @Override
    public void stop() {
        robot.stop();
    }

    private void finishDrive() {
        if (Math.abs(robot.driveTrain.fl.getTargetPosition() - robot.driveTrain.fl.getCurrentPosition()) < 20 ||
                Math.abs(robot.driveTrain.br.getTargetPosition() - robot.driveTrain.br.getCurrentPosition()) < 20 ||
                Math.abs(robot.driveTrain.bl.getTargetPosition() - robot.driveTrain.bl.getCurrentPosition()) < 20 ||
                Math.abs(robot.driveTrain.fr.getTargetPosition() - robot.driveTrain.fr.getCurrentPosition()) < 20) {
            robot.driveTrain.stop();
            robot.driveTrain.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
            this.timer.reset();
            this.command++;
        }
        robot.driveTrain.scalePower();
    }

    private void gyroCorrect(int gyroTarget, int gyroRange, double minSpeed, double addSpeed) {
        int gyroVal = (int)this.robot.sensorSystem.getGyroRotation(AngleUnit.DEGREES);
        this.robot.driveTrain.gyroCorrect(gyroTarget, gyroRange, gyroVal, minSpeed, addSpeed);
        if (this.robot.driveTrain.fl.getPower() == 0) {
            this.timer.reset();
            this.command++;
        }
    }

    private void setTarget(int target) {
        this.robot.driveTrain.setTarget(target);
        this.timer.reset();
        this.command++;
    }

    private void setStrafeTarget(int target) {
        this.robot.driveTrain.setStrafeTarget(target);
        this.timer.reset();
        this.command++;
    }

}

