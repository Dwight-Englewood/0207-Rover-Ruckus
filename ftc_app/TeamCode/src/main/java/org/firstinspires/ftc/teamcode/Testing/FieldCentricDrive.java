package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Matrices.DirRotVector;


@TeleOp(name = "FieldCentricDrive", group = "Teleop")
public class FieldCentricDrive extends OpMode {

    Bot boot = new Bot();
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
        if (gamepad1.left_trigger > .5) {
            boot.mdts.rotate(true);
        } else if (gamepad1.right_trigger > .5) {
            boot.mdts.rotate(false);

        } else {
            DirRotVector merp = new DirRotVector(gamepad1.right_stick_x, -gamepad1.right_stick_y, imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle);
            boot.mdts.drive(merp);
        }

        if (gamepad1.y) {
            this.boot.mdts.fl.setDirection(DcMotorSimple.Direction.FORWARD);
        } else if (gamepad1.x) {
            this.boot.mdts.bl.setDirection(DcMotorSimple.Direction.FORWARD);
        } else if (gamepad1.a) {
            this.boot.mdts.br.setDirection(DcMotorSimple.Direction.FORWARD);
        } else if (gamepad1.b) {
            this.boot.mdts.fr.setDirection(DcMotorSimple.Direction.FORWARD);
        } else if (gamepad1.dpad_up) {
            this.boot.mdts.fl.setDirection(DcMotorSimple.Direction.REVERSE);
        } else if (gamepad1.dpad_left) {
            this.boot.mdts.bl.setDirection(DcMotorSimple.Direction.REVERSE);
        } else if (gamepad1.dpad_down) {
            this.boot.mdts.br.setDirection(DcMotorSimple.Direction.REVERSE);
        } else if (gamepad1.dpad_right) {
            this.boot.mdts.fr.setDirection(DcMotorSimple.Direction.REVERSE);
        }

        telemetry.addData("fl", this.boot.mdts.fl.getDirection());
        telemetry.addData("bl", this.boot.mdts.bl.getDirection());
        telemetry.addData("br", this.boot.mdts.br.getDirection());
        telemetry.addData("fr", this.boot.mdts.fr.getDirection());

    }

    @Override
    public void stop() {

    }
}
