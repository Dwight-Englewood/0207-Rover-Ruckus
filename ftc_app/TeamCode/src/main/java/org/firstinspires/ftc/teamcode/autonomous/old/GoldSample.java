package org.firstinspires.ftc.teamcode.autonomous.old;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.autonomous.AutonMethods;
import org.firstinspires.ftc.teamcode.autonomous.OldAutonMethods;

import static org.firstinspires.ftc.teamcode.hardware.sensors.vision.tensorflow.TFWrapper2Mineral.TFState;


@Autonomous(name = "GoldSampleRebuild", group = "AutonOppositeCrater")
@Disabled
public class GoldSample extends OpMode {

    OldAutonMethods auto = new OldAutonMethods();
    TFState sampleLocation;

    int distExitBracket = 12;
    int distStrafeOut = 50;
    int rotFaceSample = 0;
    int rotParalellSample = 90;
    int distLeftSample = 12;
    int distCenterSample = -30;
    int distRightSample = -68;
    int rotFaceLander = -180;
    int distIntakeSample = -40;
    int distIntakeSampleAdd = -5;
    int distToWall = 45;
    int rotParalellToWall = 315;
    int distStrafeWall = -50;
    int distToDepot = 120;
    int distToCrater = -195;
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
                auto.timer.reset();
                break;

            case 3:
                auto.gyroCorrect(rotParalellSample, 1, .1, .2);
                auto.timer.reset();
                break;

            case 4:
                this.sampleLocation = auto.robot.tensorFlow.getState();
                if (this.sampleLocation != TFState.NOTVISIBLE) {
                    auto.timer.reset();
                    auto.command++;
                }
                if (auto.timer.milliseconds() > 2000) {
                    auto.timer.reset();
                    auto.command++;
                }
                break;
            case 5:
                auto.setStrafeTarget(distStrafeOut);
                break;

            case 6:
                auto.finishDrive();
                break;
            case 7:
                auto.gyroCorrect(rotParalellSample, 1, .1, .2);
                break;

            case 8:
                switch(sampleLocation) {
                    case LEFT:
                        auto.setTarget(distLeftSample);
                        break;
                    case CENTER:
                        auto.setTarget(distCenterSample);
                        break;
                    case RIGHT:
                        auto.setTarget(distRightSample);
                        break;
                    case NOTVISIBLE:
                        auto.setTarget(distLeftSample);
                        break;
                }
                break;

            case 9:
                auto.finishDrive();
                break;
            case 10:
                auto.command++;
                //auto.gyroCorrect(rotFaceLander, 1, .1, .5);
                break;

            case 11:
                auto.setStrafeTarget(-distIntakeSample);
                //auto.setTarget(distIntakeSampleSilver);
                break;

            case 12:
                //auto.robot.intake.outtake();
                auto.finishDrive();
                break;

            case 13:
                auto.setStrafeTarget((distIntakeSample + distIntakeSampleAdd));
                //auto.setTarget(-(distIntakeSampleSilver + distIntakeSampleAdd));
                break;
            case 14:
                auto.robot.intake.stop();
                auto.finishDrive();
                break;
            case 15:
                auto.gyroCorrect(rotParalellSample, 1, .1, .2);
                break;
            case 16:
                switch(sampleLocation) {
                    case LEFT:
                        auto.setTarget(distLeftSample - distLeftSample + distToWall);
                        break;
                    case CENTER:
                        auto.setTarget(distLeftSample - distCenterSample + distToWall);
                        break;
                    case RIGHT:
                        auto.setTarget(distLeftSample - distRightSample + distToWall);
                        break;
                    case NOTVISIBLE:
                        auto.setTarget(distLeftSample - distLeftSample + distToWall);
                        break;
                }
                break;
            case 17:
                auto.finishDrive();
                break;
            case 18:
                auto.gyroCorrect(rotParalellToWall, 1, .1, .2);
                break;
            case 19:
                auto.setStrafeTarget(distStrafeWall);
                break;
            case 20:
                auto.finishDrive();
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
                auto.gyroCorrect(rotParalellToWall, 1, .1, .6);
                break;
            case 25:
                auto.setStrafeTarget(-20);
                break;
            case 26:
                auto.finishDrive();
                break;
            case 27:
                auto.setStrafeTarget(9);
                break;
            case 28:
                auto.finishDrive();
                break;
            case 29:
                auto.setTarget(distToCrater);
                break;
            case 30:
                auto.finishDrive();
                break;
            case 31:
                auto.robot.driveTrain.drivepow(0);
                //auto.robot.driveTrain.br.setPower(-.25);
                //auto.robot.driveTrain.bl.setPower(-.25);
                //auto.robot.driveTrain.fr.setPower(-.1);
                //auto.robot.driveTrain.fl.setPower(-.);
                auto.robot.markerDeploy.raise();
                auto.command++;
                break;
            case 32:
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
        auto.robot.driveTrain.br.setPower(0);
        auto.robot.driveTrain.bl.setPower(0);
        auto.robot.driveTrain.fr.setPower(0);
        auto.robot.driveTrain.fl.setPower(0);
        auto.robot.stop();
    }


}