package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;


@TeleOp(name = "DriveTest", group = "Teleop")
@Disabled
public class Drive extends OpMode {

    DcMotor fl, fr, bl, br;
    double joyL;
    double joyR;

    @Override
    public void init() {
        fl = this.hardwareMap.get(DcMotor.class, "fl");
        fr = this.hardwareMap.get(DcMotor.class, "fr");
        bl = this.hardwareMap.get(DcMotor.class, "bl");
        br = this.hardwareMap.get(DcMotor.class, "br");
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {
        fl.setPower(gamepad1.a ? 1 : 0);
        bl.setPower(gamepad1.b ? 1 : 0);
        fr.setPower(gamepad1.y ? 1 : 0);
        br.setPower(gamepad1.x ? 1 : 0);
        telemetry.addData("fl", "a");
        telemetry.addData("bl", "b");
        telemetry.addData("fr", "y");
        telemetry.addData("br", "x");


    }

    @Override
    public void stop() {

    }
}
