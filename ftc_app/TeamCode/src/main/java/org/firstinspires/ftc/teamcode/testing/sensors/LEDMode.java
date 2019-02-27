package org.firstinspires.ftc.teamcode.testing.sensors;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp(name = "LEDMode", group = "Teleop")
@Disabled
public class LEDMode extends OpMode {

    RevBlinkinLedDriver blinkin;

    @Override
    public void init() {
        blinkin = hardwareMap.get(RevBlinkinLedDriver.class, "rgbReady");
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        blinkin.setPattern(RevBlinkinLedDriver.BlinkinPattern.BLUE);
    }

    @Override
    public void loop() {
        if (gamepad1.a) {
            blinkin.setPattern(RevBlinkinLedDriver.BlinkinPattern.GREEN);
        } else if (gamepad1.b) {
            blinkin.setPattern(RevBlinkinLedDriver.BlinkinPattern.BEATS_PER_MINUTE_FOREST_PALETTE);
        } else if (gamepad1.x) {
            blinkin.setPattern(RevBlinkinLedDriver.BlinkinPattern.BEATS_PER_MINUTE_RAINBOW_PALETTE);
        } else if (gamepad1.y) {
            blinkin.setPattern(RevBlinkinLedDriver.BlinkinPattern.CONFETTI);
        }

    }

    @Override
    public void stop() {

    }
}
