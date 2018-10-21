package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.Hardware.Bot;
import org.firstinspires.ftc.teamcode.Matrices.DirRotVector;


@TeleOp(name = "Telebop", group = "Teleop")
public class Telebop extends OpMode {

    Bot robot = new Bot();
    BNO055IMU imu;
    BNO055IMU.Parameters parameters;

    @Override
    public void init() {
        robot.init(hardwareMap);
        /*imu = hardwareMap.get(BNO055IMU.class, "imu");
        parameters = new BNO055IMU.Parameters();
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        imu.initialize(parameters);*/
    }

    @Override
    public void init_loop() {
        //if (imu.isGyroCalibrated()) {
            telemetry.addLine("Ready.");
            telemetry.update();
        //}
    }

    @Override
    public void start() {
        robot.start();
    }

    @Override
    public void loop() {
        //robot.driveTrain.drive(new DirRotVector(gamepad1.left_stick_x, -gamepad1.left_stick_y,
        //        imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS).firstAngle));

        robot.driveTrain.tankControl(gamepad1);

        if (gamepad1.dpad_up) robot.lift.drop();
        else if (gamepad1.dpad_down) robot.lift.lift();
        else robot.lift.stop();

        if (gamepad1.a) robot.intake.intake();
        else if (gamepad1.b) robot.intake.outtake();
        else robot.intake.stop();
    }

    @Override
    public void stop() {
        robot.stop();
    }
}