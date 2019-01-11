package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Hardware.Bot;
import org.firstinspires.ftc.teamcode.Hardware.Sensors.TFWrapperDriveby;

import static org.firstinspires.ftc.teamcode.Hardware.Sensors.TFWrapper.TFState;


@Autonomous(name = "GoldSample", group = "AutonOppositeCrater")
//@Disabled
public class GoldSample extends OpMode {

    AutonMethods auto = new AutonMethods();
    TFWrapperDriveby.TFStateDriveby state = TFWrapperDriveby.TFStateDriveby.NONE;
    TFState sampleLocation;
    ElapsedTime timer = new ElapsedTime();

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
                auto.gyroCorrect(0, 1,.1, .3);
                break;

            case 6: // Get the Sample Location
                this.state = auto.robot.tensorFlow.getState();
                if (state == TFWrapperDriveby.TFStateDriveby.GOLD) {
                    auto.robot.driveTrain.stop();
                    auto.command++;
                } else {
                    auto.robot.driveTrain.drivepow(-.2);
                }
                break;

            case 7:
                auto.gyroCorrect(90, 1, .05, .2);
                break;

            case 8:
                switch(sampleLocation) {
                    case LEFT:
                        auto.setTarget(12);
                        break;
                    case RIGHT:
                        auto.setTarget(-12);
                        break;
                    case CENTER:
                        auto.setTarget(-24);
                        break;
                    case NOTVISIBLE:
                        auto.setTarget(12);
                        break;
                }
                break;

            case 9:
                auto.finishDrive();
                break;

            case 10:
                auto.gyroCorrect(-180, 1, .05, .2);
                break;
            case 11:
                auto.setTarget(-24);
                break;
            case 12:
                auto.robot.intake.intake();
                auto.finishDrive();
                break;
            case 13:
                auto.setTarget(24);
                auto.robot.intake.stop();
                break;
            case 14:
                auto.finishDrive();
                break;
            case 15:
                auto.gyroCorrect(90, 1, .05, .2);
                break;
            case 16:
                switch(sampleLocation) {
                    case LEFT:
                        auto.setTarget(0);
                        break;
                    case RIGHT:
                        auto.setTarget(12);
                        break;
                    case CENTER:
                        auto.setTarget(24);
                        break;
                    case NOTVISIBLE:
                        auto.setTarget(0);
                        break;
                }
                break;
            case 17:
                auto.finishDrive();
                break;
            case 18:
                auto.setTarget(50);
                break;
            case 19:
                auto.finishDrive();
                break;
            case 20:
                auto.gyroCorrect(-45, 1, .05, .2);
                break;
            case 21:
                auto.setTarget(-160);
                break;
            case 22:
                auto.finishDrive();
                break;
            case 23: // this might be deprecated
                if (timer.milliseconds() > 750) {
                    auto.robot.markerDeploy.drop();
                    timer.reset();
                    auto.command++;
                } else if (timer.milliseconds() > 500) {
                    auto.robot.markerDeploy.raise();
                } else {
                    auto.robot.markerDeploy.drop();
                }
                break;
            case 24:
                auto.setTarget(200);
                break;
            case 25:
                auto.finishDrive();
                break;
            case 26:
                auto.robot.driveTrain.drivepow(0);
                auto.robot.markerDeploy.raise();
                auto.command++;
                break;
            case 27:
                auto.robot.stop();
                auto.command++;
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

