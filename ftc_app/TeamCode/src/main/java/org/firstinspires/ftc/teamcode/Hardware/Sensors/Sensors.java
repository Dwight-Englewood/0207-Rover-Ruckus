package org.firstinspires.ftc.teamcode.Hardware.Sensors;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Hardware.State;
import org.firstinspires.ftc.teamcode.Hardware.Subsystem;

public class Sensors implements Subsystem {
    public BNO055IMU imu;

    public Sensors(){}

    @Override
    public void init(HardwareMap hwMap) {
        imu = hwMap.get(BNO055IMU.class, "imu");
    }

    @Override
    public void start() {

    }

    @Override
    public void reset() {

    }

    @Override
    public void stop() {

    }

    @Override
    public State getState() {
        return null;
    }
}
