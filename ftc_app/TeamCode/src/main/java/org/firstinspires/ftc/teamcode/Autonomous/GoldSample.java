package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import static org.firstinspires.ftc.teamcode.Hardware.Sensors.TFWrapper2.TFState;


@Autonomous(name = "GoldSample", group = "AutonOppositeCrater")
//@Disabled
public class GoldSample extends OpMode {

    AutonMethods auto = new AutonMethods();
    TFState sampleLocation;

    int distExitBracket = 12;
    int distStrafeOut = 40;
    int rotFaceSample = 0;
    int rotParalellSample = 90;
    int distToLeftSample = 12;
    int distToCenterSample = -12;
    int distToRightSample = -24;
    int rotFaceLander = -180;
    int distIntakeSample = -40;
    int distToWall = 90;
    int rotParalellToWall = -45;
    int distToDepot = 160;
    int distToCrater = -190;
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
                    this.sampleLocation = auto.robot.tensorFlow.getState();
                    auto.command++;
                }
                break;

            case 1:
                auto.setTarget(distExitBracket);
                break;

            case 2:
                auto.finishDrive();
                auto.timer.reset();
                break;

            case 3:
                auto.gyroCorrect(rotParalellSample, 1, .05, .2);
                break;
            case 4:
                /*if (auto.timer.milliseconds() > 1000) {
                    this.sampleLocation = auto.robot.tensorFlow.getState();
                    auto.timer.reset();
                    auto.command++;
                }*/
                auto.command++;
                break;
            case 5:
                auto.setStrafeTarget(distStrafeOut);
                break;

            case 6:
                auto.finishDrive();
                break;



            case 7:
                auto.gyroCorrect(rotParalellSample, 1, .05, .2);
                break;

            case 8:
                switch(sampleLocation) {
                    case LEFT:
                        auto.setTarget(distToLeftSample);
                        break;
                    case CENTER:
                        auto.setTarget(distToCenterSample);
                        break;
                    case RIGHT:
                        auto.setTarget(distToRightSample);
                        break;
                    case NOTVISIBLE:
                        auto.setTarget(distToRightSample);
                        break;
                }
                break;

            case 9:
                auto.finishDrive();
                break;

            case 10:
                auto.gyroCorrect(rotFaceLander, 1, .05, .2);
                break;
            case 11:
                auto.setTarget(distIntakeSample);
                break;
            case 12:
                auto.robot.intake.intake();
                auto.finishDrive();
                break;
            case 13:
                auto.setTarget(-distIntakeSample);
                break;
            case 14:
                auto.robot.intake.stop();
                auto.finishDrive();
                break;
            case 15:
                auto.gyroCorrect(rotParalellSample, 1, .05, .2);
                break;
            case 16:
                switch(sampleLocation) {
                    case LEFT:
                        auto.setTarget(distToLeftSample - distToLeftSample);
                        break;
                    case CENTER:
                        auto.setTarget(distToLeftSample - distToCenterSample);
                        break;
                    case RIGHT:
                        auto.setTarget(distToLeftSample - distToRightSample);
                        break;
                    case NOTVISIBLE:
                        auto.setTarget(distToLeftSample - distToRightSample);
                        break;
                }
                break;
            case 17:
                auto.finishDrive();
                break;
            case 18:
                auto.setTarget(distToWall);
                break;
            case 19:
                auto.finishDrive();
                break;
            case 20:
                auto.gyroCorrect(rotParalellToWall, 1, .05, .2);
                break;
            case 21:
                auto.setTarget(distToDepot);
                break;
            case 22:
                auto.finishDrive();
                break;
            case 23: // this might be deprecated
                if (auto.timer.milliseconds() > 750) {
                    auto.robot.markerDeploy.drop();
                    auto.timer.reset();
                    auto.command++;
                } else if (auto.timer.milliseconds() > 500) {
                    auto.robot.markerDeploy.raise();
                } else {
                    auto.robot.markerDeploy.drop();
                }
                break;
            case 24:
                auto.setTarget(distToCrater);
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
        telemetry.addData("sample", this.sampleLocation);
        telemetry.addData("lift ticks", auto.robot.lift.getTicks());
        telemetry.addData("bl target", auto.robot.driveTrain.bl.getTargetPosition());
        telemetry.addData("fl target", auto.robot.driveTrain.fl.getTargetPosition());
    }

    @Override
    public void stop() {
        auto.robot.stop();
    }


}

