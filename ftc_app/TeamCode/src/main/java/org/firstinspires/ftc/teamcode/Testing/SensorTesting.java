package org.firstinspires.ftc.teamcode.Testing;

import android.graphics.Color;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DigitalChannel;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;


@TeleOp(name = "SensorTesting", group = "Teleop")
public class SensorTesting extends OpMode {

    DigitalChannel magSwitch;
    DigitalChannel touchSensor;
    ColorSensor color;
    Rev2mDistanceSensor twomds;


    @Override
    public void init() {
        magSwitch = hardwareMap.get(DigitalChannel.class, "magneticSwitch");
        touchSensor = hardwareMap.get(DigitalChannel.class, "touchs");
        color = hardwareMap.get(ColorSensor.class, "colors");
        twomds = hardwareMap.get(Rev2mDistanceSensor.class, "2mds");
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {
        telemetry.addData("Magnetic Limit Switch", magSwitch.getState());
        telemetry.addData("Touch Sensor", touchSensor.getState());
        telemetry.addData("Color A", color.alpha());
        telemetry.addData("Color R", color.red());
        telemetry.addData("Color G", color.green());
        telemetry.addData("Color B", color.blue());

        telemetry.addData("Rev 2M Distance", twomds.getDistance(DistanceUnit.CM));


    }

    @Override
    public void stop() {

    }
}
