package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Hardware.Bot;
import org.firstinspires.ftc.teamcode.Hardware.Sensors.TFWrapperDriveby;


@Autonomous(name = "GoldSample", group = "AutonOppositeCrater")
//@Disabled
public class GoldSample extends OpMode {

    Bot robot = new Bot(true);
    int command = 0;
    ElapsedTime timer = new ElapsedTime();
    TFWrapperDriveby.TFStateDriveby state = TFWrapperDriveby.TFStateDriveby.NONE;

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
                //if (true) {
                    timer.reset();
                    command++;
                }
                break;

            case 1:
                this.setTarget(12);
                break;
            case 2:
                this.finishDrive();
                break;
            case 3:
                this.setStrafeTarget(50);
                break;
            case 4:
                this.finishDrive();
                break;
            case 5:
                this.gyroCorrect(90, 1,.1, .3);
                break;
            case 6:
                this.state = this.robot.tensorFlow.getState();
                if (state == TFWrapperDriveby.TFStateDriveby.GOLD) {
                    robot.driveTrain.stop();
                    command++;
                } else {
                    robot.driveTrain.drivepow(-.2);
                }
                break;

            case 7:
                this.gyroCorrect(-180, 1, .05, .2);
                break;

            case 8:

                break;


        }

        telemetry.addData("Command: ", command);
        telemetry.addData("Time: ", timer.milliseconds());
        telemetry.addData("lift ticks", robot.lift.getTicks());
        telemetry.addData("bl target", robot.driveTrain.bl.getTargetPosition());
    }

    @Override
    public void stop() {
        robot.driveTrain.drivepow(0);
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
        } else {
            robot.driveTrain.scalePower();
            //robot.driveTrain.drivepow(.2);
        }
    }

    private int gyroCorrect(int gyroTarget, int gyroRange, double minSpeed, double addSpeed) {
        int gyroVal = (int) this.robot.sensorSystem.getGyroRotation(AngleUnit.DEGREES);
        this.robot.driveTrain.gyroCorrect(gyroTarget, gyroRange, gyroVal, minSpeed, addSpeed);
        if (this.robot.driveTrain.fl.getPower() == 0) {
            this.timer.reset();
            this.command++;
        }
        return gyroVal;
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

