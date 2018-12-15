package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Hardware.Bot;


@Autonomous(name = "GyroRead2", group = "Testing")
//@Disabled
public class GyroRead2 extends OpMode {

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
        this.setTarget(-420);
    }


    int gyro = -135;
    @Override
    public void loop() {
        this.finishDrive();


        //this.gyroCorrect(gyro, 1, .05, .2);

        telemetry.addData("gyro: ", robot.sensorSystem.getGyroRotation(AngleUnit.DEGREES));
        telemetry.addData("Time: ", timer.milliseconds());
        telemetry.addData("lift ticks", robot.lift.getTicks());
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

