package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Hardware.Bot;


@Autonomous(name = "Silver", group = "Auton")
//@Disabled
public class Silver extends OpMode {
    Bot robot = new Bot(true);
    int command = 0;
    ElapsedTime timer = new ElapsedTime();

    @Override
    public void init() {
        robot.init(hardwareMap);
        telemetry.addLine("ready");
        telemetry.update();
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        robot.start();
    }

    @Override
    public void loop() {
        switch (command) {
            case 0:
                if (robot.lift.newYears()) {
                    timer.reset();
                    command++;
                }
                break;

            case 1:
                if (timer.milliseconds() > 500) {
                    robot.driveTrain.stop();
                    timer.reset();
                    command++;
                }
                robot.driveTrain.drivepow(.3);
                break;
        }

    }

    @Override
    public void stop() {
        robot.stop();
    }
}

