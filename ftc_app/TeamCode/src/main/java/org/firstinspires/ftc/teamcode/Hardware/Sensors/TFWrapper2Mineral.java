package org.firstinspires.ftc.teamcode.Hardware.Sensors;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.Hardware.State;
import org.firstinspires.ftc.teamcode.Hardware.Subsystem;

import java.util.List;

public class TFWrapper2Mineral implements Subsystem {

    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";
    private static final String VUFORIA_KEY = "AbZUuPf/////AAAAGUmS0Chan00iu7rnRhzu63+JgDtPo889M6dNtjvv+WKxiMJ8w2DgSJdM2/zEI+a759I7DlPj++D2Ryr5sEHAg4k1bGKdo3BKtkSeh8hCy78w0SIwoOACschF/ImuyP/V259ytjiFtEF6TX4teE8zYpQZiVkCQy0CmHI9Ymoa7NEvFEqfb3S4P6SicguAtQ2NSLJUX+Fdn49SEJKvpSyhwyjbrinJbak7GWqBHcp7fGh7TNFcfPFMacXg28XxlvVpQaVNgkvuqolN7wkTiR9ZMg6Fnm0zN4Xjr5lRtDHeE51Y0bZoBUbyLWSA+ts3SyDjDPPUU7GMI+Ed/ifb0csVpM12aOiNr8d+HsfF2Frnzrj2";

    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    private TFState state;

    public TFWrapper2Mineral() {
    }

    @Override
    public void init(HardwareMap hwMap) {
        this.initVuforia();

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            this.initTfod(hwMap);
        }
    }

    @Override
    public void start() {
        if (tfod != null) {
            tfod.activate();
        }
    }

    @Override
    public void reset() {

    }

    @Override
    public void stop() {
        if (tfod != null) {
            tfod.shutdown();
        }
    }

    @Override
    public TFState getState() {
        this.updateState();
        return state;
    }

    public List<Recognition> badOOP() {
        if (tfod != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            List<Recognition> updatedRecognitions = tfod.getRecognitions();
            return updatedRecognitions;
        } else {
            return null;
        }
    }

    private void updateState() {
        if (tfod != null) {

            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                for (int i = 0; i < updatedRecognitions.size(); i++) {
                    if (updatedRecognitions.get(i).getWidth() * updatedRecognitions.get(i).getHeight() < 10000 || updatedRecognitions.get(i).getWidth() - updatedRecognitions.get(i).getHeight() > 50) {
                        //If the difference between height and width is larger than 50, it is unlikely to be a mineral
                        updatedRecognitions.remove(i); // Thus, we remove the recognition
                        i--;
                    }
                }
                if (updatedRecognitions.size() == 2) {

                    //Initialize the x values to a recognizable value
                    int gold = -1;
                    int silver1 = -1;
                    //For all detected minerals
                    for (Recognition recognition : updatedRecognitions) {
                        //If we have the gold mineral
                        if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                            //Keep it's x position in the variable
                            gold = (int) recognition.getLeft();
                        //Similarly for a silver mineral
                        } else if (silver1 == -1) {
                            silver1 = (int) recognition.getLeft();
                        }
                    }
                    //If at least one value has been read
                    if (gold != -1 || silver1 != -1) {
                        //If we have seen the gold element
                        if (gold != -1) {
                            if (gold < silver1) {
                                //If the gold is located further left than the silver, it is on the left
                                this.state = TFState.LEFT;
                            } else if (silver1 < gold) {
                                //If the gold is located further right than the silver, it is in the center
                                this.state = TFState.CENTER;
                            }
                        } else {
                            //If the gold has not been seen, it must be on the right
                            this.state = TFState.RIGHT;
                        }
                    }
                } else {
                    System.out.println("wtf");
                    this.state = TFState.NOTVISIBLE;
                }
            }
        }
    }

    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }

    /**
     * Initialize the Tensor Flow Object Detection engine.
     */
    private void initTfod(HardwareMap hardwareMap) {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }

    public enum TFState implements State {
        LEFT("Left"),
        CENTER("Center"),
        RIGHT("Right"),
        NOTVISIBLE("None");

        private String str;

        TFState(String str) {
            this.str = str;

        }

        @Override
        public String getStateVal() {
            return str;
        }

    }
}
