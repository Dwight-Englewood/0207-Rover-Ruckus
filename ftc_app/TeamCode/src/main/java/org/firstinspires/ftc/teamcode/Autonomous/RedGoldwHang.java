package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.Hardware.Bot;


@Autonomous(name = "normal hanging", group = "Auton")
public class RedGoldwHang extends OpMode {

    Bot robot = new Bot();
    int command = 0;
    ElapsedTime timer = new ElapsedTime();
    BNO055IMU imu;

    @Override
    public void init() {
        robot.init(hardwareMap);
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        telemetry.addLine("ready");
        telemetry.update();
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        robot.start();
    }

    @Override
    public void loop() {
        switch(command) {
            case 0:
                timer.reset();
                robot.lift.drop();
                command = 5;
                break;

            case 1:
                if (timer.milliseconds() > 4000) {
                    robot.lift.stop();
                    timer.reset();
                    command++;
                }
                break;

            case 2:
                robot.driveTrain.drivepow(-.5);
                if (timer.milliseconds() > 500) {
                    robot.driveTrain.stop();
                    command++;
                    timer.reset();
                }
                break;

            case 3:
                robot.driveTrain.strafepow(1);
                if (timer.milliseconds() > 700) {
                    robot.driveTrain.stop();
                    command++;
                    timer.reset();
                }
                break;

            case 4:
                if (timer.milliseconds() > 3000) {
                    robot.driveTrain.stop();
                    timer.reset();
                    command++;
                } else {
                    adjustHeading(0, false);
                }
                break;

            case 5:
                robot.driveTrain.drivepow(1);
                timer.reset();
                command++;
                break;

            case 6:
                if (timer.milliseconds() > 1000) {
                    robot.driveTrain.stop();
                    robot.intake.outtake();
                    command++;
                    timer.reset();
                }
                break;

            case 7:
                if (timer.milliseconds() > 3000) {
                    robot.driveTrain.stop();
                    timer.reset();
                    command++;
                } else {
                    adjustHeading(135, false);
                }
                break;

            case 8:
                robot.driveTrain.drivepow(1);
                timer.reset();
                command++;
                break;

            case 9:
                if (timer.milliseconds() > 15) {
                    robot.stop();
                    command++;
                }
                break;

        }
        telemetry.addData("command ", command);

    }

    @Override
    public void stop() {
        robot.stop();
    }

    public void adjustHeading(int targetHeading, boolean slow) {
        //Initialize the turnleft boolean.
        boolean turnLeft = false;

        //Get the current heading from the imu.
        float curHeading = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;

        //If within a reasonable degree of error of the target heading, set power to zero on all motors.
        if (Math.abs(curHeading - targetHeading) <= .5) {
            robot.driveTrain.stop();
            return;
        }
        //Generate our proportional term
        float powFactor = Math.abs(targetHeading - curHeading) * (float) (slow ? .0055 : .02);

        //Choose the direction of the turn based on given target and current heading
        switch (targetHeading) {
            case 0:
                turnLeft = curHeading <= 0;
                break;

            case 90:
                turnLeft = !(curHeading <= -90 || curHeading >= 90);
                break;

            case 180:
                turnLeft = !(curHeading <= 0);
                break;

            case -90:
                turnLeft = curHeading <= -90 || curHeading >= 90;
                break;

            case 45:
                turnLeft = !(curHeading <= -135 || curHeading >= 45);
                break;

            case -45:
                turnLeft = curHeading <= -45 || curHeading >= 45;
                break;

            case 30:
                turnLeft = !(curHeading <= -30 || curHeading >= 30);
                break;

            case -30:
                turnLeft = curHeading <= -30 || curHeading >= 30;
                break;

            case 60:
                turnLeft = !(curHeading <= -120 || curHeading >= 60);
                break;

            case -60:
                turnLeft = curHeading <= -60 || curHeading >= 60;
                break;

            default:
                turnLeft = targetHeading < 0 ? (curHeading <= targetHeading || curHeading >= -1 * targetHeading) : !(curHeading <= -1 * targetHeading || curHeading >= targetHeading);
        }
        //Clip the powers to within an acceptable range for the motors and apply the proportional factor.
        double leftPower = Range.clip((turnLeft ? -1 : 1) * powFactor, -1, 1);
        double rightPower = Range.clip((turnLeft ? 1 : -1) * powFactor, -1, 1);

        //Set power to all motors
        robot.driveTrain.fl.setPower(leftPower);
        robot.driveTrain.bl.setPower(leftPower);
        robot.driveTrain.fr.setPower(rightPower);
        robot.driveTrain.br.setPower(rightPower);
    }
}

