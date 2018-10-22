package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Hardware.Bot;


@Autonomous(name = "BlueGold", group = "Auton")
@Disabled
public class BlueGold extends OpMode {

    Bot robot = new Bot();
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
        switch(command) {
            case 0:
                timer.reset();
                robot.lift.drop();
                command++;
                break;

            case 1:
                if (timer.milliseconds() > 4000) {
                    robot.lift.stop();
                    timer.reset();
                    command++;
                }
                break;

            case 2:
                robot.driveTrain.drivepow(.5);
                if (timer.milliseconds() > 1000) {
                    robot.driveTrain.stop();
                    command++;
                    timer.reset();
                }
                break;

            case 3:
                robot.driveTrain.strafepow(-1);
                if (timer.milliseconds() > 1000) {
                    robot.driveTrain.stop();
                    command++;
                    timer.reset();
                }
                break;

            case 4:
                if (timer.milliseconds() > 1500) {
                    robot.driveTrain.stop();
                    timer.reset();
                    command++;
                } else {
                    //adjustHeading(0, false);
                }
                command++;
                break;
        }

    }

    @Override
    public void stop() {
        robot.stop();
    }
}

