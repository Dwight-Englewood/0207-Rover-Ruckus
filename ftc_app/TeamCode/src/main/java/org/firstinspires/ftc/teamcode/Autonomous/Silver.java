package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Hardware.Bot;


@Autonomous(name = "Silver", group = "Auton")
//@Disabled
public class Silver extends OpMode {


    Bot robot = new Bot(true);
    int command = 0;
    ElapsedTime timer = new ElapsedTime();
    private final int distanceLanderBracket = 12; // distance for pulling out of the lander
    private final int gyroTurnBracket1 = 0; // turning towards crater
    private final int distance2LanderAvoid = 40; // moving towards crater
    private final int gyroTurnTowardsWall = 90; // turning towards wall
    private final int distanceToWall = 60; // moving towards wall
    private final int gyroParalellWall = 45; // turning towards depot
    private final int distanceDepot = 86+45; // moving into depot
    private final int distanceToCraterBackwards = -420; // backing into crater

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

            case 1: //pulling out of lander bracket
                this.setTarget(distanceLanderBracket);
                break;
            case 2:
                this.finishDrive();
                break;
            case 3:
                this.gyroCorrect(gyroTurnBracket1, 1, .05, .2);
                break;
            case 4:
                //driving out of lander
                this.setTarget(distance2LanderAvoid);
                break;
            case 5:
                this.finishDrive();
                break;

            case 6:
                //turning towards avoiding the minerals and wall
                this.gyroCorrect(gyroTurnTowardsWall, 1, .05, .2);
                break;

            case 7:
                //moving towards wall 1
                this.setTarget(distanceToWall);
                break;
            case 8:
                this.finishDrive();
                break;
            case 9://turning on the wall
                this.gyroCorrect(gyroParalellWall, 1, .05, .2);
                break;
            case 10:
                this.setTarget(distanceDepot);
                break;
            case 11:
                this.finishDrive();
                break;

            case 12:
                if (timer.milliseconds() > 750) {
                    robot.markerDeploy.drop();
                    timer.reset();
                    command++;
                } else if (timer.milliseconds() > 500) {
                    robot.markerDeploy.raise();
                } else {
                    robot.markerDeploy.drop();
                }
                break;
            case 13:
                this.setTarget(distanceToCraterBackwards);
                break;
            case 14:
                this.finishDrive();
                break;
            case 15:
                robot.driveTrain.drivepow(0);
                robot.markerDeploy.raise();
                break;

        }

        telemetry.addData("Command: ", command);
        telemetry.addData("Time: ", timer.milliseconds());
        telemetry.addData("lift ticks", robot.lift.getTicks());
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
        }
        robot.driveTrain.scalePower();
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

