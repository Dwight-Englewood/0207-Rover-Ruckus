package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Hardware.Bot;


@Autonomous(name = "GoldOppositeCrater", group = "AutonOppositeCrater")
//@Disabled
public class GoldOppositeCrater extends OpMode {

    private final int distance1 = 12; // Distance for pulling out of the lander bracket
    private final int distance2 = 86+45; // Distance for driving to depot
    private final int distance3 = 42; // Distance for driving towards wall
    private final int distance4 = 150; // Distance for driving into crater
    private final int gyroTurn1 = 0; // Turning towards depot
    private final int gyroTurn2 = 0; // Algining after depot movement for dropping the
    private final int gyroTurn3 = 90; // Turning to move towards wall
    private final int gyroTurn4 = 135; // Turning towards the crater

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
                this.setTarget(distance1);
                break;
            case 2:
                this.finishDrive();
                break;
            case 3:
                //this.setStrafeTarget(20);
                command++;
                break;
            case 4:
                //this.finishDrive();
                command++;
                break;

            case 5:
                this.gyroCorrect(gyroTurn1, 1, .05, .2);
                break;

            case 6:
                this.setTarget(distance2);
                break;
            case 7:
                this.finishDrive();
                break;
            case 8:
                this.gyroCorrect(gyroTurn2, 1, .05, .2);
                break;

            case 9:
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
            case 10:
                this.gyroCorrect(gyroTurn3, 1, .05, .2);
                break;
            case 11:
                this.setTarget(distance3);
                break;
            case 12:
                this.finishDrive();
                break;
            case 13:
                this.gyroCorrect(gyroTurn4, 1, .05, .2);
                break;
            case 14:
                this.setTarget(distance4);
                break;
            case 15:
                this.finishDrive();
                break;
            case 16:
                robot.driveTrain.drivepow(0);
                robot.markerDeploy.raise();
                command++;
                break;

            case 17:
                robot.stop();
                command++;
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
        } else {
            robot.driveTrain.scalePower();
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

