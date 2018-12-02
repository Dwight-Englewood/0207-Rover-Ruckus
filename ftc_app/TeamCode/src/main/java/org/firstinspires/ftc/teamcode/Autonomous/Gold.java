package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Hardware.Bot;


@Autonomous(name = "Gold", group = "Auton")
//@Disabled
public class Gold extends OpMode {

    Bot robot = new Bot(true);
    int command = 0;
    ElapsedTime timer = new ElapsedTime();

    @Override
    public void init() {
        robot.init(hardwareMap);
        telemetry.addLine("ready");
        telemetry.update();
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        robot.start();
        timer.reset();
    }

    @Override
    public void loop() {
        switch(command) {
            case 0:
                if (robot.lift.newYears()) {
                    timer.reset();
                    command++;
                }
                break;

            case 1:
                if (timer.milliseconds() > 500) {
                    robot.driveTrain.stop();
                    timer.reset();
                    command++;
                }
                robot.driveTrain.drivepow(.3);
                break;

            case 2:
                int gyroVal = (int)robot.sensorSystem.getGyroRotation(AngleUnit.DEGREES);
                robot.driveTrain.gyroCorrect(0, 1, gyroVal, .05, .2);
                if (robot.driveTrain.fl.getPower() == 0) {
                    timer.reset();
                    command++;
                }
                break;

            case 3:
                robot.driveTrain.setTarget(86 + 45);
                robot.driveTrain.drivepow(.5);
                timer.reset();
                command++;
                break;

            case 4:
                if (timer.milliseconds() > 4000 ||
                        Math.abs(robot.driveTrain.fl.getTargetPosition() - robot.driveTrain.fl.getCurrentPosition()) < 25) {
                    robot.driveTrain.stop();
                    robot.driveTrain.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    command++;
                }
                break;

            case 5:
                gyroVal = (int)robot.sensorSystem.getGyroRotation(AngleUnit.DEGREES);
                robot.driveTrain.gyroCorrect(90, 2, gyroVal, .05, .3);
                if (robot.driveTrain.fl.getPower() == 0) {
                    timer.reset();
                    command++;
                }
                break;

            case 6:
                robot.markerDeploy.drop();
                timer.reset();
                command++;
                break;

            case 7:
                if (timer.milliseconds() > 500) {
                    robot.driveTrain.stop();
                    timer.reset();
                    command++;
                }
                robot.driveTrain.strafepow(.5);
                break;

            case 8:
                robot.markerDeploy.drop();
                timer.reset();
                command++;
                break;

            case 9:
                if (timer.milliseconds() > 500) {
                    robot.driveTrain.stop();
                    timer.reset();
                    command++;
                }
                robot.driveTrain.strafepow(-.5);
                break;

            case 10:
                robot.markerDeploy.drop();
                timer.reset();
                command++;
                break;

            case 11:
                if (timer.milliseconds() > 500) {
                    robot.driveTrain.stop();
                    timer.reset();
                    command++;
                }
                robot.driveTrain.strafepow(.1);
                break;

            case 12:
                gyroVal = (int)robot.sensorSystem.getGyroRotation(AngleUnit.DEGREES);
                robot.driveTrain.gyroCorrect(-135, 1, gyroVal, .05, .2);
                if (robot.driveTrain.fl.getPower() == 0) {
                    timer.reset();
                    robot.markerDeploy.raise();
                    command++;
                }
                break;

            case 13:
                robot.driveTrain.setTarget(8 * 60);
                robot.driveTrain.drivepow(.7);
                timer.reset();
                command++;
                break;

            case 14:
                if (timer.milliseconds() > 5000 ||
                        Math.abs(robot.driveTrain.fl.getTargetPosition() - robot.driveTrain.fl.getCurrentPosition()) < 25) {
                    robot.driveTrain.stop();
                    robot.driveTrain.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    command++;
                }
                break;

            case 15:
                robot.stop();
                timer.reset();
                command++;
                break;

        }

        telemetry.addData("Command: ", command);
        telemetry.addData("Time: ", timer.milliseconds());
        telemetry.addData("lift ticks", robot.lift.getTicks());
    }

    @Override
    public void stop() {
        robot.stop();
    }

}

