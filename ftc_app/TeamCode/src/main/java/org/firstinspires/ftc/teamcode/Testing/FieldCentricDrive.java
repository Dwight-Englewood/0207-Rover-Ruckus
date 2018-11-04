package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.Hardware.Bot;
import org.firstinspires.ftc.teamcode.Matrices.DirRotVector;


@TeleOp(name = "FieldCentricDrive", group = "Teleop")
@Disabled
public class FieldCentricDrive extends OpMode {

    Bot boot = new Bot(false);
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
        double rsx = gamepad1.left_stick_x;
        double nrsy = -gamepad1.left_stick_y;
        double theta = gamepad1.right_stick_x;
        DirRotVector merp = new DirRotVector(rsx, nrsy, theta);
        telemetry.addData("right_stick_x", rsx);
        telemetry.addData("-gamepad1.right_stick_y", nrsy);
        telemetry.addData("theta", theta);
        telemetry.addData("botTheta", botTheta);

        telemetry.addData("fr_power", merp.get(0, 0));
        telemetry.addData("fl_power", merp.get(1, 0));
        telemetry.addData("bl_power", merp.get(2, 0));
        telemetry.addData("br_power", merp.get(3, 0));
        boot.driveTrain.drive(merp, botTheta);


        if (gamepad1.y) {
            this.boot.driveTrain.fl.setDirection(DcMotorSimple.Direction.FORWARD);
        } else if (gamepad1.x) {
            this.boot.driveTrain.bl.setDirection(DcMotorSimple.Direction.FORWARD);
        } else if (gamepad1.a) {
            this.boot.driveTrain.br.setDirection(DcMotorSimple.Direction.FORWARD);
        } else if (gamepad1.b) {
            this.boot.driveTrain.fr.setDirection(DcMotorSimple.Direction.FORWARD);
        } else if (gamepad1.dpad_up) {
            this.boot.driveTrain.fl.setDirection(DcMotorSimple.Direction.REVERSE);
        } else if (gamepad1.dpad_left) {
            this.boot.driveTrain.bl.setDirection(DcMotorSimple.Direction.REVERSE);
        } else if (gamepad1.dpad_down) {
            this.boot.driveTrain.br.setDirection(DcMotorSimple.Direction.REVERSE);
        } else if (gamepad1.dpad_right) {
            this.boot.driveTrain.fr.setDirection(DcMotorSimple.Direction.REVERSE);
        }

        telemetry.addData("fl_dir", this.boot.driveTrain.fl.getDirection());
        telemetry.addData("bl_dir", this.boot.driveTrain.bl.getDirection());
        telemetry.addData("br_dir", this.boot.driveTrain.br.getDirection());
        telemetry.addData("fr_dir", this.boot.driveTrain.fr.getDirection());

    }

    @Override
    public void stop() {

    }
}
