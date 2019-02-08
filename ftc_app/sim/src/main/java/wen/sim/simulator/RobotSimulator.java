package wen.sim.simulator;


import wen.sim.bodies.mecanumRobot.driveFunction.TankDriveJoy;
import wen.sim.bodies.Body;
import wen.sim.bodies.mecanumRobot.driveFunction.MecanumDriveKey;
import wen.sim.bodies.mecanumRobot.MecanumRobot;

import static java.util.Collections.max;

public class RobotSimulator implements Simulator {

    float target = 0;
    Body robot = new MecanumRobot(5, -15f, 50,  5, new MecanumDriveKey());
    Body robot2 = new MecanumRobot(5, -15f, 50, 5, new TankDriveJoy());

    Body[] bodies = {robot, robot2};

    private long lasttime = System.currentTimeMillis();
    private boolean bounded = false;
    private boolean shouldNorm = true;

    public void updateSim(long window) {
        for (Body a : bodies) {
            a.update(window);
        }
        simulateStep();
    }


    public void simulateStep() {
        float deltaTime = (System.currentTimeMillis() - lasttime) / (float) 1000;
        for (Body a : bodies) {
            a.botXD = a.botXD + a.botXDD * deltaTime;
            a.botYD = a.botYD + a.botYDD * deltaTime;
            a.botRD = a.botRD + a.botRDD * deltaTime;
            a.botX = a.botX + a.botXD * deltaTime;
            a.botY = a.botY + a.botYD * deltaTime;
            a.botR = a.botR + a.botRD * deltaTime;
            if (bounded) {
                if (a.botX > 1000) {
                    a.botX = 1000;
                } else if (a.botX < -1000) {
                    a.botX = -1000;
                }
                if (a.botY > 1000) {
                    a.botY = 1000;
                } else if (a.botY < -1000) {
                    a.botY = -1000;
                }
            }
        }
        lasttime = System.currentTimeMillis();

    }

    public void draw(long window) {
        updateSim(window);
        for (Body a  : bodies) {
            a.draw(window);
        }

    }


}
