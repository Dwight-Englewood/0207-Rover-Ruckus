package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Hardware.Bot;


@Autonomous(name = "RedSilver", group = "Auton")
@Disabled
public class RedSilver extends OpMode {
    Bot robot = new Bot();
    int level = 0;
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
        switch (level) {
            case 0:
                timer.reset();
                robot.lift.drop();
                level++;
                break;

            case 1:
                if (timer.milliseconds() > 4000) {
                    robot.lift.stop();
                    timer.reset();
                    level++;
                }
                break;

            case 2:
                robot.driveTrain.drivepow(.5);
                if (timer.milliseconds() > 1000) {
                    robot.driveTrain.stop();
                    level++;
                    timer.reset();
                }
                break;

            case 3:
                robot.driveTrain.strafepow(-1);
                if (timer.milliseconds() > 1000) {
                    robot.driveTrain.stop();
                    level++;
                    timer.reset();
                }
                break;

            case 4:
                robot.stop();
                level++;
                break;
        }

    }

    @Override
    public void stop() {
        robot.stop();
    }
}

