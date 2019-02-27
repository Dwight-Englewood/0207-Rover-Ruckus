package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Hardware.Drivetrain.MecanumDriveTrain;
import org.firstinspires.ftc.teamcode.Hardware.Scoring.Dumper;
import org.firstinspires.ftc.teamcode.Hardware.Scoring.Intake;
import org.firstinspires.ftc.teamcode.Hardware.Scoring.Lift;
import org.firstinspires.ftc.teamcode.Hardware.Scoring.MarkerDeploy;
import org.firstinspires.ftc.teamcode.Hardware.Scoring.Rake;
import org.firstinspires.ftc.teamcode.Hardware.Sensors.SensorSystem;
import org.firstinspires.ftc.teamcode.Hardware.Sensors.TFWrapper2Mineral;
import org.firstinspires.ftc.teamcode.Hardware.Sensors.VumarkWrapper;

import java.lang.reflect.Field;

public class Bot {

    public MecanumDriveTrain driveTrain = new MecanumDriveTrain();
    public Lift lift = new Lift();
    public MarkerDeploy markerDeploy = new MarkerDeploy();
    public SensorSystem sensorSystem = new SensorSystem();
    public TFWrapper2Mineral tensorFlow = new TFWrapper2Mineral();
    public VumarkWrapper vumarkWrapper = new VumarkWrapper();
    public Dumper dumper = new Dumper();
    public Intake intake = new Intake();
    public Rake rake = new Rake();

    private boolean isAuton;
    private boolean vumarkOff = true;

    public Bot(boolean isAuton) {
        this.isAuton = isAuton;
    }

    public Bot(boolean isAuton, boolean vumarkOff) {
        this.isAuton = isAuton;
        this.vumarkOff = vumarkOff;
    }

    public void init(HardwareMap hwm) {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field f : fields) {
            try {
                if (f.get(this) instanceof Subsystem) {
                    if (!isAuton && f.get(this) instanceof TFWrapper2Mineral) {
                        continue;
                    }
                    if (f.get(this) instanceof VumarkWrapper && vumarkOff) {
                        continue;
                    }
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
                    if (!isAuton && f.get(this) instanceof TFWrapper2Mineral) {
                        continue;
                    }

                    if (f.get(this) instanceof VumarkWrapper && vumarkOff) {
                        continue;
                    }
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
                    if (!isAuton && f.get(this) instanceof TFWrapper2Mineral) {
                        continue;
                    }

                    if (f.get(this) instanceof VumarkWrapper && vumarkOff) {
                        continue;
                    }
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
                    if (!isAuton && f.get(this) instanceof TFWrapper2Mineral) {
                        continue;
                    }

                    if (f.get(this) instanceof VumarkWrapper && vumarkOff) {
                        continue;
                    }
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