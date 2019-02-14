package wen.sim.bodies.basicbody;

public interface BasicBodyDriveMode {

    public void updateKinematics(long window, BasicBody bot);

    public void reset();
}
