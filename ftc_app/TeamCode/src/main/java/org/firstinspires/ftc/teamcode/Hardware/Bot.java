package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Hardware.Drivetrain.MecanumDriveTrain;
import org.firstinspires.ftc.teamcode.Hardware.Scoring.Lift;
import org.firstinspires.ftc.teamcode.Hardware.Scoring.MarkerDeploy;
import org.firstinspires.ftc.teamcode.Hardware.Sensors.Sensors;

import java.lang.reflect.Field;

public class Bot {

    public Bot() {}

    public MecanumDriveTrain driveTrain = new MecanumDriveTrain(5, 9, 9);
    //public Intake intake = new Intake();
    public Lift lift = new Lift();
    //public Shooter shooter = new Shooter();
    public MarkerDeploy markerDeploy = new MarkerDeploy();
    public Sensors sensors = new Sensors();


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

    public void start() {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field f : fields) {
            try {
                if (f.get(this) instanceof Subsystem) {
                    Subsystem ss = (Subsystem) f.get(this);
                    ss.start();
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void reset() {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field f : fields) {
            try {
                if (f.get(this) instanceof Subsystem) {
                    Subsystem ss = (Subsystem) f.get(this);
                    ss.reset();
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {

    }

    @Override
    public String toString() {
        String state = "";
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field f : fields) {
            try {
                if (f.get(this) instanceof Subsystem) {
                    Subsystem ss = (Subsystem) f.get(this);
                    state += f.getName() + ": " + ss.getState().getStateVal() + "\n";
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return state;
    }
}