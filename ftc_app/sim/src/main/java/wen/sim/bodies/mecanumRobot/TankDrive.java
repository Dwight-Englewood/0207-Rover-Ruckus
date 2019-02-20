package wen.sim.bodies.mecanumRobot;

import wen.control.function.Coordinate;
import wen.sim.bodies.mecanumRobot.driveFunction.MecanumDriveMode;
import wen.sim.bodies.mecanumRobot.normFunction.MecanumNormMode;

public class TankDrive extends MecanumRobot {
    public TankDrive(double botMass, double drag, double linearMotorScale, double wheelRadius, MecanumDriveMode mdm, MecanumNormMode mnm) {
        super(botMass, drag, linearMotorScale, wheelRadius, mdm, mnm);
    }

    public TankDrive(double x, double y, double r, double botMass, double drag, double linearMotorScale, double wheelRadius, MecanumDriveMode mdm, MecanumNormMode mnm) {
        super(x, y, r, botMass, drag, linearMotorScale, wheelRadius, mdm, mnm);
    }

    @Override
    public void update(long window) {

        drive.updateWheelPower(window, this);
        norm.normWheelPower(this);
        updateForce();
        forceToAcceleration();
        this.path.add(new Coordinate(this.botX, this.botY));
        this.rot.add(new Coordinate(this.botX, this.botR));
    }
}
