package org.firstinspires.ftc.teamcode.Hardware.Drivetrain;

import org.firstinspires.ftc.teamcode.Hardware.Subsystem;
import org.firstinspires.ftc.teamcode.Matrices.DirRotVector;

public abstract class Drivetrain implements Subsystem {

    abstract void drive(DirRotVector drv);

}
