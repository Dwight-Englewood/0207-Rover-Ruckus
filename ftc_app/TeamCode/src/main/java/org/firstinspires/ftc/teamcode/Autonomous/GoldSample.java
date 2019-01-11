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

    int distExitBracket = 12;
    int distStrafeOut = 30;
    int rotFaceSamplesAfterLander = 0;
    int rotTurnParalellToSamples = 90;
    int distToLeft = 12;
    int distToCenter = -12;
    int distToRight = -24;
    int rotIntakeSample = -180;
    int distSampleIntake = -24;
    int distToWall = 50;
    int rotParalellToWall = -45;
    int distToDepot = -80;
    int distToCrater = 200;
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
                auto.setTarget(distExitBracket);
                break;

            case 2:
                auto.finishDrive();
                break;

            case 3:
                auto.setStrafeTarget(distStrafeOut);
                break;

            case 4:
                auto.finishDrive();
                break;

            case 5:
                auto.gyroCorrect(rotFaceSamplesAfterLander, 1,.1, .3);
                break;

            case 6: // Get the Sample Location
                /*this.state = auto.robot.tensorFlow.getState();
                if (state == TFWrapperDriveby.TFStateDriveby.GOLD) {
                    auto.robot.driveTrain.stop();
                    auto.command++;
                } else {
                    auto.robot.driveTrain.drivepow(-.2);
                }
                break;*/

            case 7:
                auto.gyroCorrect(rotTurnParalellToSamples, 1, .05, .2);
                break;

            case 8:
                switch(sampleLocation) {
                    case LEFT:
                        auto.setTarget(distToLeft);
                        break;
                    case CENTER:
                        auto.setTarget(distToCenter);
                        break;
                    case RIGHT:
                        auto.setTarget(distToRight);
                        break;
                    case NOTVISIBLE:
                        auto.setTarget(distToLeft);
                        break;
                }
                break;

            case 9:
                auto.finishDrive();
                break;

            case 10:
                auto.gyroCorrect(rotIntakeSample, 1, .05, .2);
                break;
            case 11:
                auto.setTarget(distSampleIntake);
                break;
            case 12:
                auto.robot.intake.intake();
                auto.finishDrive();
                break;
            case 13:
                auto.setTarget(-distSampleIntake);
                auto.robot.intake.stop();
                break;
            case 14:
                auto.finishDrive();
                break;
            case 15:
                auto.gyroCorrect(rotTurnParalellToSamples, 1, .05, .2);
                break;
            case 16:
                switch(sampleLocation) {
                    case LEFT:
                        auto.setTarget(distToLeft - distToLeft);
                        break;
                    case CENTER:
                        auto.setTarget(distToLeft - distToCenter);
                        break;
                    case RIGHT:
                        auto.setTarget(distToLeft - distToRight);
                        break;
                    case NOTVISIBLE:
                        auto.setTarget(distToLeft - distToLeft);
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
        telemetry.addData("lift ticks", auto.robot.lift.getTicks());
        telemetry.addData("bl target", auto.robot.driveTrain.bl.getTargetPosition());
        telemetry.addData("fl target", auto.robot.driveTrain.fl.getTargetPosition());
    }

    @Override
    public void stop() {
        auto.robot.stop();
    }


}

