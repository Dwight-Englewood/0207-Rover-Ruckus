package org.firstinspires.ftc.teamcode.sandbox.testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autonomous.AutonMethods;


@Autonomous(name = "AutonTest", group = "Auton")
@Disabled
public class AutonTest extends OpMode {
    public AutonMethods auto = new AutonMethods();

    @Override
    public void init() {
        auto.robot.init(hardwareMap);
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        auto.robot.start();
    }

    @Override
    public void loop() {
        switch(auto.command) {
            case 0:
                auto.setTarget(20);
                break;
            case 1:
                auto.finishDrive();
                break;
            case 2:
                auto.setTarget(-20);
                break;
            case 3:
                auto.finishDrive();
                break;
            case 4:
                auto.setStrafeTarget(20);
                break;
            case 5:
                auto.finishDrive();
                break;
            case 6:
                auto.setStrafeTarget(-20);
                break;
            case 7:
                auto.finishDrive();
                break;
            case 8:
                auto.gyroCorrect(180, 1, .1, .3);
                break;
            case 9:
                auto.gyroCorrect(-90, 1, .1, .3);
                break;
            case 10:
                auto.gyroCorrect(0, 1, .1, .3);
                break;
            case 11:
                telemetry.addLine("Done");
                break;

        }
    }

    @Override
    public void stop() {
        auto.robot.stop();
    }
}

