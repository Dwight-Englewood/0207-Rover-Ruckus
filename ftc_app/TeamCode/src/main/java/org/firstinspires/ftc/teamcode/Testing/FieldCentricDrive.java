package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.Hardware.Bot;
import org.firstinspires.ftc.teamcode.Matrices.DirRotVector;
import org.firstinspires.ftc.teamcode.Matrices.PowerVector4WD;


@TeleOp(name = "FieldCentricDrive", group = "Teleop")
//@Disabled
public class FieldCentricDrive extends OpMode {

    Bot boot = new Bot(false, true);
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
        boot.driveTrain.fl.setDirection(DcMotorSimple.Direction.REVERSE);
        boot.driveTrain.bl.setDirection(DcMotorSimple.Direction.REVERSE);
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
        botTheta =  (botTheta < 0) ? -botTheta : 2*Math.PI-botTheta;
        double rsx = -gamepad1.left_stick_x;
        double nrsy = -gamepad1.left_stick_y;
        double theta = gamepad1.right_stick_x;
        if (gamepad1.dpad_up) {
            rsx = 0;
            nrsy = 1;
        } else if (gamepad1.dpad_down) {
            rsx = 0;
            nrsy = -1;
        } else if (gamepad1.dpad_left) {
            rsx = 1;
            nrsy = 0;
        } else if (gamepad1.dpad_right) {
            rsx = -1;
            nrsy = 0;
        }

        if (gamepad1.right_trigger > .4) {
            boot.driveTrain.fr.setPower(1);
            boot.driveTrain.fl.setPower(1);
            boot.driveTrain.bl.setPower(1);
            boot.driveTrain.br.setPower(1);
        } else if (gamepad1.left_trigger > .4) {
            boot.driveTrain.fr.setPower(-1);
            boot.driveTrain.fl.setPower(-1);
            boot.driveTrain.bl.setPower(-1);
            boot.driveTrain.br.setPower(-1);
        } else {
            DirRotVector merp = new DirRotVector(rsx, nrsy, theta);
            telemetry.addData("right_stick_x", rsx);
            telemetry.addData("-gamepad1.right_stick_y", nrsy);
            telemetry.addData("theta", theta);
            telemetry.addData("botThetaRaw1", botTheta);

            telemetry.addData("botTheta", botTheta);

            PowerVector4WD powVector = boot.driveTrain.drive(merp, botTheta);
            telemetry.addData("fr_dri", powVector.get(0, 0));
            telemetry.addData("fl_dri", powVector.get(1, 0));
            telemetry.addData("bl_dri", powVector.get(2, 0));
            telemetry.addData("br_dri", powVector.get(3, 0));
        }



    }

    @Override
    public void stop() {

    }
}
