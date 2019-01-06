package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Hardware.Bot;
import org.firstinspires.ftc.teamcode.Hardware.Sensors.TFWrapperDriveby;


@Autonomous(name = "GoldSample", group = "AutonOppositeCrater")
//@Disabled
public class GoldSample extends OpMode {

    AutonMethods auto = new AutonMethods();
    TFWrapperDriveby.TFStateDriveby state = TFWrapperDriveby.TFStateDriveby.NONE;

    @Override
    public void init() {
        auto.robot.init(hardwareMap);
        telemetry.addLine("ready");
        telemetry.update();
    }

    @Override
    public void init_loop() {
        telemetry.addLine("in init");
    }

    @Override
    public void start() {
        auto.robot.start();
        auto.timer.reset();
    }

    @Override
    public void loop() {
        switch(auto.command) {
            case 0:
                if (auto.robot.lift.newYears()) {
                    auto.command++;
                }
                break;

            case 1:
                auto.setTarget(12);
                break;

            case 2:
                auto.finishDrive();
                break;

            case 3:
                auto.setStrafeTarget(30);
                break;

            case 4:
                auto.finishDrive();
                break;

            case 5:
                auto.gyroCorrect(90, 1,.1, .3);
                break;

            case 6:
                this.state = auto.robot.tensorFlow.getState();
                if (state == TFWrapperDriveby.TFStateDriveby.GOLD) {
                    auto.robot.driveTrain.stop();
                    auto.command++;
                } else {
                    auto.robot.driveTrain.drivepow(-.2);
                }
                break;

            case 7:
                auto.gyroCorrect(-180, 1, .05, .2);
                break;

            case 8:
                auto.robot.intake.intake();
                auto.setTarget(-20);
                break;

            case 9:
                auto.finishDrive();
                break;

            case 10:
                break;
        }

        telemetry.addData("Command: ", auto.command);
        telemetry.addData("lift ticks", auto.robot.lift.getTicks());
        telemetry.addData("bl target", auto.robot.driveTrain.bl.getTargetPosition());
        telemetry.addData("fl target", auto.robot.driveTrain.fl.getTargetPosition());
    }

    @Override
    public void stop() {
        auto.robot.stop();
    }


}

