package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Hardware.Drivetrain.MecanumDriveTrain;
import org.firstinspires.ftc.teamcode.Hardware.Subsystem;

import java.lang.reflect.Field;

public class Bot {

    public Bot() {

    }

    public MecanumDriveTrain mdts = new MecanumDriveTrain(5, 9, 9);

    public void init(HardwareMap hwm) {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field f : fields) {
            try {
                if (f.get(this) instanceof Subsystem) {
                    Subsystem ss = (Subsystem) f.get(this);
                    ss.init(hwm);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
