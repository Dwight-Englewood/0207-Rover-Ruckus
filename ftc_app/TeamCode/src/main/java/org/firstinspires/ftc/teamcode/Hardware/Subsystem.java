package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;

public interface Subsystem {
    void init(HardwareMap hwMap);
    void start();
    void reset();
}
