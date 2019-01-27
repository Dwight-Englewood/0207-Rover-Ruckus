package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Matrices.DirRotVector;

import static org.firstinspires.ftc.teamcode.Hardware.Sensors.TFWrapper2.TFState;


@Autonomous(name = "DoubleSilverSample", group = "AutonOppositeCrater")
//@Disabled
public class DoubleSilverSample extends OpMode {

    AutonMethods auto = new AutonMethods();
    TFState sampleLocation;

    int distExitBracket = 12;
    int distStrafeOut = 50;
    int rotFaceSample = 0;
    int rotParalellSampleSilver = 90;
    int distLeftSampleSilver = 12;
    int distCenterSampleSilver = -30;
    int distRightSampleSilver = -64;
    int rotFaceLander = -180;
    int distIntakeSampleSilver = -40;
    int distIntakeSampleAdd = -5;
    int distToWall = 45;
    int rotParalellToWall = 135;
    int distStrafeWall = 60;
    int distToDepot = 100;
    int distStrafeAwayWall = -40;
    int rotParalellSampleGold = 180;
    int distLeftSampleGold = 40;
    int distCenterSampleGold = 10;
    int distRightSampleGold = -40;
    int distIntakeSampleGold = -50;
    int distToWallGold = 0;
    int distToCrater = -150;
    int distToCraterRightOnly = -170;


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
                auto.gyroCorrect(rotParalellSampleSilver, 1, .1, .2);
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
                auto.gyroCorrect(rotParalellSampleSilver, 1, .1, .2);
                break;

            case 8:
                switch(sampleLocation) {
                    case LEFT:
                        auto.setTarget(distLeftSampleSilver);
                        break;
                    case CENTER:
                        auto.setTarget(distCenterSampleSilver);
                        break;
                    case RIGHT:
                        auto.setTarget(distRightSampleSilver);
                        break;
                    case NOTVISIBLE:
                        auto.setTarget(distLeftSampleSilver);
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
                auto.setStrafeTarget(-distIntakeSampleSilver);
                //auto.setTarget(distIntakeSampleSilver);
                break;

            case 12:
                //auto.robot.intake.outtake();
                auto.finishDrive();
                break;

            case 13:
                auto.setStrafeTarget((distIntakeSampleSilver + distIntakeSampleAdd));
                //auto.setTarget(-(distIntakeSampleSilver + distIntakeSampleAdd));
                break;
            case 14:
                auto.robot.intake.stop();
                auto.finishDrive();
                break;
            case 15:
                auto.gyroCorrect(rotParalellSampleSilver, 1, .1, .2);
                break;
            case 16:
                switch(sampleLocation) {
                    case LEFT:
                        auto.setTarget(distLeftSampleSilver - distLeftSampleSilver + distToWall);
                        break;
                    case CENTER:
                        auto.setTarget(distLeftSampleSilver - distCenterSampleSilver + distToWall);
                        break;
                    case RIGHT:
                        auto.setTarget(distLeftSampleSilver - distRightSampleSilver + distToWall);
                        break;
                    case NOTVISIBLE:
                        auto.setTarget(distLeftSampleSilver - distLeftSampleSilver + distToWall);
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
                auto.germanFinishDrive();
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
                auto.setStrafeTarget(distStrafeAwayWall);
                break;
            case 25:
                auto.finishDrive();
                break;
            case 26:
                auto.gyroCorrect(rotParalellSampleGold, 1, .1, .2);
                break;
            case 27:
                switch(sampleLocation) {
                    case LEFT:
                        auto.setTarget(distLeftSampleGold);
                        break;
                    case CENTER:
                        auto.setTarget(distCenterSampleGold);
                        break;
                    case RIGHT:
                        auto.setTarget(distRightSampleGold);
                        break;
                    case NOTVISIBLE:
                        auto.setTarget(distRightSampleGold);
                        break;
                }
                break;
            case 28:
                auto.finishDrive();
                break;
            case 29:
                auto.setStrafeTarget(distIntakeSampleGold);
                //auto.setTarget(distIntakeSampleSilver);
                break;

            case 30:
                //auto.robot.intake.outtake();
                auto.finishDrive();
                break;

            case 31:
                auto.setStrafeTarget(-distIntakeSampleGold - distIntakeSampleAdd);
                //auto.setTarget(-(distIntakeSampleSilver + distIntakeSampleAdd));
                break;
            case 32:
                auto.robot.intake.stop();
                auto.finishDrive();
                break;
            case 33:
                switch(sampleLocation) {
                    case LEFT:
                        auto.setTarget(distRightSampleGold - distLeftSampleGold );
                        break;
                    case CENTER:
                        auto.setTarget(distRightSampleGold - distCenterSampleGold);
                        break;
                    case RIGHT:
                        auto.setTarget(distRightSampleGold - distRightSampleGold);
                        break;
                    case NOTVISIBLE:
                        auto.setTarget(distRightSampleGold - distRightSampleGold );
                        break;
                }
                break;
            case 34:
                auto.finishDrive();
            case 35:
                auto.gyroCorrect(rotParalellToWall, 1, .1, .4);
                break;
            case 36:
                auto.setStrafeTarget(distStrafeWall);
                break;
            case 37:
                auto.germanFinishDrive();
                break;
            case 38:
                switch (sampleLocation) {
                    case RIGHT:
                        auto.setTarget(distToCraterRightOnly);
                        break;
                    default:
                        auto.setTarget(distToCrater);
                        break;

                }

                break;

            case 39:
                auto.slightlyLesGgermanFinishDrive();
                break;

            case 40:
                auto.robot.driveTrain.drivepow(0);

                //auto.robot.driveTrain.br.setPower(-.25);
                //auto.robot.driveTrain.bl.setPower(-.25);
                //auto.robot.driveTrain.fr.setPower(-.25);
                //auto.robot.driveTrain.fl.setPower(-.1);
                auto.robot.markerDeploy.raise();
                auto.command++;
                break;

            case 41:
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