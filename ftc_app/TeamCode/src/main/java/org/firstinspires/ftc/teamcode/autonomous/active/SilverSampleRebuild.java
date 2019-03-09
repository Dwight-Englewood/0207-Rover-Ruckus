package org.firstinspires.ftc.teamcode.autonomous.active;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.autonomous.AutonMethods;
import org.firstinspires.ftc.teamcode.hardware.sensors.vision.opencv.MineralPosition;


@Autonomous(name = "SilverSampleRebuild", group = "AutonOppositeCrater")
//@Disabled
public class SilverSampleRebuild extends OpMode {

    AutonMethods auto = new AutonMethods();
    MineralPosition sampleLocation;

    int distExitBracket = 12;
    int distStrafeOut = 50;
    int rotFaceSample = 0;
    int rotParalellSample = 90;
    int distLeftSample = 12;
    int distCenterSample = -30;
    int distRightSample = -64;
    int rotFaceLander = -180;
    int distIntakeSample = -40;
    int distIntakeSampleAdd = -5;
    int distToWall = 45;
    int rotParalellToWall = 135;
    int distStrafeWall = 60;
    int distToDepot = 120;
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
        switch (auto.command) {
            case 0:
                if (auto.robot.lift.newYears()) {
                    auto.command++;
                }
                this.sampleLocation = (MineralPosition) auto.robot.goldDetector.getState();
                break;

            case 1:
                auto.setTarget(distExitBracket);
                break;

            case 2:
                auto.finishDrive();
                auto.timer.reset();
                break;

            case 3:
                auto.PIDTurn(rotParalellSample, 1);
                auto.timer.reset();
                break;

            case 4:
                if (this.sampleLocation != MineralPosition.NOTVISIBLE) {
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
                auto.PIDTurn(rotParalellSample, 1);
                break;

            case 8:
                switch (sampleLocation) {
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
                //auto.robot.intake.stop();
                auto.finishDrive();
                break;
            case 15:
                auto.PIDTurn(rotParalellSample, 1);
                break;
            case 16:
                switch (sampleLocation) {
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
                auto.PIDTurn(rotParalellToWall, 1);
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
                if (auto.timer.milliseconds() > 250) {
                    //auto.robot.intakeSlides.outtake();
                    auto.timer.reset();
                    auto.command++;
                } else {
                    auto.robot.intakeSlides.pivotMiddle();
                    auto.robot.intakeSlides.outtake();
                }
                break;

            case 24:
                auto.PIDTurn(rotParalellToWall, 1);
                break;
            case 25:
                auto.setStrafeTarget(20);
                break;
            case 26:
                auto.finishDrive();
                break;
            case 27:
                auto.setStrafeTarget(-3);
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