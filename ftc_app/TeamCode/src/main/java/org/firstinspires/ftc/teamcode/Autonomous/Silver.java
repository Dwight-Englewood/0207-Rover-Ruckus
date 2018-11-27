package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Hardware.Bot;
import org.firstinspires.ftc.teamcode.Hardware.Sensors.TFWrapper;


@Autonomous(name = "Silver", group = "Auton")
//@Disabled
public class Silver extends OpMode {
    Bot robot = new Bot(true, false);
    int command = 0;
    ElapsedTime timer = new ElapsedTime();
    // LEFT, CENTER, RIGHT
    int[] posCounter = {0,0,0};
    TFWrapper.TFState position;

    @Override
    public void init() {
        robot.init(hardwareMap);
        telemetry.addLine("ready");
        telemetry.update();
    }

    @Override
    public void init_loop() {}

    @Override
    public void start() {
        robot.start();
    }

    @Override
    public void loop() {
        switch (command) {
            case 0:
                if (robot.lift.newYears()) {
                    timer.reset();
                    command++;
                }

                position = robot.tensorFlow.getState();
                if (position != TFWrapper.TFState.NOTVISIBLE) {
                    if (position == TFWrapper.TFState.LEFT) {
                        posCounter[0]++;
                    } else if (position == TFWrapper.TFState.CENTER) {
                        posCounter[1]++;
                    } else {
                        posCounter[2]++;
                    }
                }
                break;

            case 1:
                robot.driveTrain.setTarget(12);
                if (posCounter[0] > posCounter[1] && posCounter[0] > posCounter[2]) {
                    position = TFWrapper.TFState.LEFT;
                } else if (posCounter[1] > posCounter[0] && posCounter[1] > posCounter[2]) {
                    position = TFWrapper.TFState.CENTER;
                } else {
                    position = TFWrapper.TFState.RIGHT;
                }
                timer.reset();
                command++;
                break;

            case 2:
                this.finishDrive();
                break;

            case 3:
                this.gyroCorrect(90, 1, .05, .2);
                break;

            case 4:
                robot.driveTrain.setStrafeTarget(-40);
                timer.reset();
                command++;
                break;

            case 5:
                this.finishDrive();
                break;

            case 6:
                this.gyroCorrect(position.getCraterAng(), 1, .05, .35);
                break;

            case 7:
                robot.driveTrain.setTarget(position.getCraterDist());
                timer.reset();
                command++;
                break;

            case 8:
                this.finishDrive();
                break;

            case 9:
                robot.driveTrain.setTarget(-position.getCraterDist());
                timer.reset();
                command++;
                break;

            case 10:
                this.finishDrive();
                break;

            case 11:
                this.gyroCorrect(90, 1, .05, .35);
                break;

            case 12:
                robot.driveTrain.setTarget(100);
                timer.reset();
                command++;
                break;

            case 13:
                this.finishDrive();
                break;

            case 14:
                this.gyroCorrect(135, 1, .05, .35);
                break;

            case 15:
                robot.driveTrain.setTarget(100);
                timer.reset();
                command++;
                break;

            case 16:
                this.finishDrive();
                break;

            case 17:
                this.gyroCorrect(215, 1, .05, .45);
                break;

            case 18:
                robot.markerDeploy.drop();
                if (timer.milliseconds() > 500) {
                    robot.markerDeploy.raise();
                }
                if (timer.milliseconds() > 750) {
                    robot.markerDeploy.drop();
                    timer.reset();
                    command++;
                }
                break;

            case 19:
                this.gyroCorrect(135, 1, .05, .45);
                break;

            case 20:
                robot.driveTrain.setTarget(-420);
                robot.markerDeploy.raise();
                timer.reset();
                command++;
                break;

            case 21:
                this.finishDrive();
                break;

            case 22:
                this.stop();
                command++;
                break;
        }

    }

    @Override
    public void stop() {
        robot.stop();
    }

    private void finishDrive() {
        if (Math.abs(robot.driveTrain.fl.getTargetPosition() - robot.driveTrain.fl.getCurrentPosition()) < 20 &&
                Math.abs(robot.driveTrain.br.getTargetPosition() - robot.driveTrain.br.getCurrentPosition()) < 20 &&
                Math.abs(robot.driveTrain.bl.getTargetPosition() - robot.driveTrain.bl.getCurrentPosition()) < 20 &&
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
}

