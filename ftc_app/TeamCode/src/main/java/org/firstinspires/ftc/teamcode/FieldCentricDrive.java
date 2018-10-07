package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.Drivetrain.MecanumDriveTrainSimple;
import org.firstinspires.ftc.teamcode.Matrices.DirRotVector;
import org.firstinspires.ftc.teamcode.Matrices.PowerVector4WD;


@TeleOp(name = "FieldCentricDrive", group = "Teleop")
public class FieldCentricDrive extends OpMode {

    DcMotor fl, fr, bl, br;
    /*
    fr = 1
    fl = 2
    bl = 3
    br = 4
     */
    double joyL;
    double joyR;
    BNO055IMU imu;
    MecanumDriveTrainSimple mdts = new MecanumDriveTrainSimple(5, 9, 9);




    @Override
    public void init() {
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        fl = this.hardwareMap.get(DcMotor.class, "fl");
        fr = this.hardwareMap.get(DcMotor.class, "fr");
        bl = this.hardwareMap.get(DcMotor.class, "bl");
        br = this.hardwareMap.get(DcMotor.class, "br");
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {
        PowerVector4WD merp = mdts.drive(new DirRotVector(gamepad1.right_stick_x, -gamepad1.right_stick_y, imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle));
        merp = merp.scale();
        this.fl.setPower(merp.get(0,0));
        this.fr.setPower(merp.get(1,0));
        this.br.setPower(merp.get(2,0));
        this.bl.setPower(merp.get(3,0));


    }

    @Override
    public void stop() {

    }
}
