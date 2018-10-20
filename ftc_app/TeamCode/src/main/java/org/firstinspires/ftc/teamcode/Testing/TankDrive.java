package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


@TeleOp(name = "TankDrive", group = "Teleop")
public class TankDrive extends OpMode {

    DcMotor fl, fr, bl, br;
    double joyL;
    double joyR;

    @Override
    public void init() {
        fl = this.hardwareMap.get(DcMotor.class, "fl");
        fr = this.hardwareMap.get(DcMotor.class, "fr");
        bl = this.hardwareMap.get(DcMotor.class, "bl");
        br = this.hardwareMap.get(DcMotor.class, "br");
        this.fl.setDirection(DcMotorSimple.Direction.FORWARD);
        this.bl.setDirection(DcMotorSimple.Direction.REVERSE);
        this.fr.setDirection(DcMotorSimple.Direction.FORWARD);
        this.br.setDirection(DcMotorSimple.Direction.REVERSE);
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
        joyL = -gamepad1.left_stick_y;
        joyR = -gamepad1.right_stick_y;
        fl.setPower(joyL);
        bl.setPower(joyL);
        fr.setPower(joyR);
        br.setPower(joyR);

    }

    @Override
    public void stop() {

    }
}
