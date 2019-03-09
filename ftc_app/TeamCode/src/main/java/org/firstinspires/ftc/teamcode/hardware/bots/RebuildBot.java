package org.firstinspires.ftc.teamcode.hardware.bots;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.hardware.Subsystem;
import org.firstinspires.ftc.teamcode.hardware.drivetrain.MecanumDriveTrain;
import org.firstinspires.ftc.teamcode.hardware.scoring.Lift;
import org.firstinspires.ftc.teamcode.hardware.scoring.dumper.DumperPivot;
import org.firstinspires.ftc.teamcode.hardware.scoring.intake.IntakeSlides;
import org.firstinspires.ftc.teamcode.hardware.sensors.IMU;
import org.firstinspires.ftc.teamcode.hardware.sensors.vision.opencv.GoldDetectorWrapper;
import org.firstinspires.ftc.teamcode.hardware.sensors.vision.tensorflow.TFWrapper2Mineral;
import org.firstinspires.ftc.teamcode.hardware.sensors.vision.vumark.VumarkWrapper;

import java.lang.reflect.Field;

public class RebuildBot {

    public MecanumDriveTrain driveTrain = new MecanumDriveTrain();
    public IntakeSlides intakeSlides = new IntakeSlides();
    public DumperPivot dumperPivot = new DumperPivot();
    public IMU imu = new IMU();
    public GoldDetectorWrapper goldDetector = new GoldDetectorWrapper();
    public Lift lift = new Lift();

    private boolean isAuton;
    private boolean vumarkOff = true;

    public RebuildBot(boolean isAuton) {
        this.isAuton = isAuton;
    }

    public RebuildBot(boolean isAuton, boolean vumarkOff) {
        this.isAuton = isAuton;
        this.vumarkOff = vumarkOff;
    }

    public void init(HardwareMap hwm) {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field f : fields) {
            try {
                if (f.get(this) instanceof Subsystem) {
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
                    if (!isAuton && f.get(this) instanceof GoldDetectorWrapper) {
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
                    if (!isAuton && f.get(this) instanceof GoldDetectorWrapper) {
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
                    if (!isAuton && f.get(this) instanceof GoldDetectorWrapper) {
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