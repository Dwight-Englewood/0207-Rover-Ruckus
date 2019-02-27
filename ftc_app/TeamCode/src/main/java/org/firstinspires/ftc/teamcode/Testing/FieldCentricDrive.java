package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.ejml.simple.SimpleMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.Hardware.RebuildBot;
import org.firstinspires.ftc.teamcode.Matrices.DirRotVector;


@TeleOp(name = "FieldCentricDrive", group = "Teleop")
//@Disabled
public class FieldCentricDrive extends OpMode {

    RebuildBot boot = new RebuildBot(false, true);
    /*
    fr = 1
    fl = 2
    bl = 3
    br = 4
     */
    double joyL;
    double joyR;
    BNO055IMU imu;

    int timerSwap = 0;

    @Override
    public void init() {
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        boot.init(hardwareMap);
        boot.driveTrain.fl.setDirection(DcMotorSimple.Direction.FORWARD);
        boot.driveTrain.bl.setDirection(DcMotorSimple.Direction.FORWARD);
        boot.driveTrain.fr.setDirection(DcMotorSimple.Direction.REVERSE);
        boot.driveTrain.br.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {

        double botTheta = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS).firstAngle;
        //The readings from the gyro are different from the reading needed for the field centric code, so we apply a function to fix it
        botTheta = (botTheta < 0) ? -botTheta : 2 * Math.PI - botTheta;
        botTheta = -botTheta;
        double lsx = -gamepad1.left_stick_x;
        double lsy = gamepad1.left_stick_y;
        double theta = gamepad1.right_stick_x / 2;


        telemetry.addData("lsx", lsx);
        telemetry.addData("lsy", lsy);
        telemetry.addData("lsr", theta);


        telemetry.addData("botTheta", botTheta);


        SimpleMatrix powVector = boot.driveTrain.drive(lsx, lsy, theta, botTheta);
        telemetry.addData("fr_dri", powVector.get(0, 0));
        telemetry.addData("fl_dri", powVector.get(1, 0));
        telemetry.addData("bl_dri", powVector.get(2, 0));
        telemetry.addData("br_dri", powVector.get(3, 0));

        telemetry.addData("bl encoder", boot.driveTrain.bl.getCurrentPosition());
        telemetry.addData("br encoder", boot.driveTrain.br.getCurrentPosition());
        telemetry.addData("fl encoder", boot.driveTrain.fl.getCurrentPosition());
        telemetry.addData("fr encoder", boot.driveTrain.fr.getCurrentPosition());


    }

    @Override
    public void stop() {

    }
}
