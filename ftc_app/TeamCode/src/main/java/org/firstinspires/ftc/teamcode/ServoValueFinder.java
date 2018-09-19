package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@TeleOp(name = "ServoValueFinder", group = "Teleop")
public class ServoValueFinder extends OpMode {

    final int cooldownTime = 100;
    ArrayList<CRServo> crservos = new ArrayList<CRServo>();
    ArrayList<String> crservosNames = new ArrayList<>();
    ArrayList<Servo> servos = new ArrayList<Servo>();
    ArrayList<String> servosNames = new ArrayList<>();
    double[] servoValues;
    boolean isCR = false;
    int currentServo = 0;
    int currentCRServo = 0;
    int timerServo = 0;
    int timerSwap = 0;
    int timerShuffle = 0;

    @Override
    public void init() {
        try {
            //might be better way to do this
            Field f = hardwareMap.getClass().getDeclaredField("allDevicesMap");
            f.setAccessible(true);
            HashMap<String, List<HardwareDevice>> config = (HashMap<String, List<HardwareDevice>>) f.get(hardwareMap);
            Object[] configNames = config.keySet().toArray();
            for (int i = 0; i < configNames.length; i++) {
                String currentName = (String) configNames[i];
                HardwareDevice chd = hardwareMap.get(currentName);
                if (chd instanceof Servo) {
                    servos.add((Servo) chd);
                    servosNames.add(currentName);
                } else if (chd instanceof CRServo) {
                    crservos.add((CRServo) chd);
                    crservosNames.add(currentName);

                }
                servoValues = new double[servos.size()];
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
    }

    @Override
    public void loop() {
        if (crservos.size() == 0 && servos.size() == 0) {
            telemetry.addData("No Servos/CR Servos Connected - ", "Press dpad up to end program");
            if (gamepad1.dpad_up) {
                this.stop();
            }
        } else {
            if (isCR) {
                if (crservos.size() == 0) {
                    telemetry.addData("No CR Servos Connected", "");
                } else {
                    CRServo current = crservos.get(currentCRServo);
                    current.setPower(gamepad1.right_stick_y);
                    telemetry.addData("CRServo", "");
                    telemetry.addData("Current CRServo Name: ", crservosNames.get(currentCRServo));
                    telemetry.addData("Current CRServo Port: ", current.getPortNumber());
                    telemetry.addData("Current CRServo Power: ", current.getPower());
                }

            } else {
                if (servos.size() == 0) {
                    telemetry.addData("No Servos Connected", "");
                } else {
                    Servo current = servos.get(currentServo);
                    telemetry.addData("Servo", "");
                    telemetry.addData("Current Servo Name: ", servosNames.get(currentServo));
                    telemetry.addData("Current Servo Port: ", current.getPortNumber());
                    telemetry.addData("Current Servo Position:", current.getPosition());
                    if (gamepad1.a && timerServo <= 0) {
                        if (gamepad1.right_bumper) {
                            servoValues[currentServo] = servoValues[currentServo] + .001;
                        } else if (gamepad1.left_bumper) {
                            servoValues[currentServo] = servoValues[currentServo] + .1;
                        } else {
                            servoValues[currentServo] = servoValues[currentServo] + .01;
                        }
                        timerServo = cooldownTime;
                    } else if (gamepad1.y && timerServo <= 0) {
                        if (gamepad1.right_bumper) {
                            servoValues[currentServo] = servoValues[currentServo] - .001;
                        } else if (gamepad1.left_bumper) {
                            servoValues[currentServo] = servoValues[currentServo] - .1;
                        } else {
                            servoValues[currentServo] = servoValues[currentServo] - .01;
                        }
                        timerServo = cooldownTime;
                    }
                }
            }

        }

        //Clipping the servo positions so nothing abd happens
        for (int i = 0; i < servoValues.length; i++) {
            servoValues[i] = Range.clip(servoValues[i], 0, 1);
        }

        //setting all the servo positions
        for (int i = 0; i < servos.size(); i++) {
            servos.get(i).setPosition(servoValues[i]);
        }

        //switching logic
        if (gamepad1.left_trigger > .65 && timerShuffle < 0) {
            if (isCR) {
                currentCRServo--;
                if (currentCRServo < 0) {
                    currentCRServo = currentCRServo + crservos.size();
                }
            } else {
                currentServo--;
                if (currentServo < 0) {
                    currentServo = currentServo + servos.size();
                }
            }
            timerShuffle = cooldownTime;
        } else if (gamepad1.right_trigger > .65 && timerShuffle < 0) {
            if (isCR) {
                currentCRServo++;
                if (currentCRServo > crservos.size() - 1) {
                    currentCRServo = currentCRServo - crservos.size();
                }
            } else {
                currentServo++;
                if (currentServo > servos.size() - 1) {
                    currentServo = currentServo - servos.size();
                }
            }
            timerShuffle = cooldownTime;
        }
        if (gamepad1.start && timerSwap < 0) {
            isCR = !isCR;
            timerSwap = cooldownTime;
        }
        timerSwap--;
        timerShuffle--;
        timerServo--;
    }

    @Override
    public void stop() {

    }
}
