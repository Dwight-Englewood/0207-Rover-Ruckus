package org.firstinspires.ftc.teamcode.sandbox.motion;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.ejml.simple.SimpleMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.firstinspires.ftc.teamcode.hardware.bots.RebuildBot;
import org.firstinspires.ftc.teamcode.hardware.sensors.imu.NaiveAccelerationIntegrator;


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
    long lastTime;

    int timerSwap = 0;

    @Override
    public void init() {
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        boot.init(hardwareMap);
        boot.driveTrain.fl.setDirection(DcMotorSimple.Direction.FORWARD);
        boot.driveTrain.bl.setDirection(DcMotorSimple.Direction.FORWARD);
        boot.driveTrain.fr.setDirection(DcMotorSimple.Direction.REVERSE);
        boot.driveTrain.br.setDirection(DcMotorSimple.Direction.REVERSE);
        boot.driveTrain.setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new NaiveAccelerationIntegrator();
        imu.initialize(parameters);
        imu.startAccelerationIntegration(new Position(), new Velocity(), 200);



    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        boot.driveTrain.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lastTime = System.currentTimeMillis();

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

        //double dab = imu.getAcceleration().zAccel; //use this to get noise???


        /*telemetry.addData("lsx", lsx);
        telemetry.addData("lsy", lsy);
        telemetry.addData("lsr", theta);
        */

        /*
        telemetry.addData("botTheta", botTheta);
         */

        SimpleMatrix powVector = boot.driveTrain.drive(lsx, lsy, theta, botTheta);

        if (gamepad1.left_bumper) {
            Acceleration a = imu.getAcceleration();
            telemetry.addData("Acl X", a.xAccel);
            telemetry.addData("Acl Y", a.yAccel);
            telemetry.addData("Acl Z", a.zAccel);
        }

        if (gamepad1.left_trigger > .5) {
            Velocity v = imu.getVelocity();
            telemetry.addData("Vcy X", v.xVeloc);
            telemetry.addData("Vcy Y", v.yVeloc);
            telemetry.addData("Vcy Z", v.zVeloc);
        }

        if (gamepad1.right_trigger > .5) {

            telemetry.addData("PosE X", boot.driveTrain.encoderXDriveWheel());
            telemetry.addData("PosE Y", boot.driveTrain.encoderYDriveWheel());
            telemetry.addData("PosE R", boot.driveTrain.encoderRDriveWheel());

        }

        if (gamepad1.right_bumper) {
            Position p = imu.getPosition();
            telemetry.addData("PosI X", p.x);
            telemetry.addData("PosI Y", p.y);
            telemetry.addData("PosI Z", p.z);

        }


        /*
        telemetry.addData("fr_dri", powVector.get(0, 0));
        telemetry.addData("fl_dri", powVector.get(1, 0));
        telemetry.addData("bl_dri", powVector.get(2, 0));
        telemetry.addData("br_dri", powVector.get(3, 0));
        */
        /*
        telemetry.addData("bl encoder", boot.driveTrain.bl.getCurrentPosition());
        telemetry.addData("br encoder", boot.driveTrain.br.getCurrentPosition());
        telemetry.addData("fl encoder", boot.driveTrain.fl.getCurrentPosition());
        telemetry.addData("fr encoder", boot.driveTrain.fr.getCurrentPosition());
        */


    }

    @Override
    public void stop() {

    }
}
