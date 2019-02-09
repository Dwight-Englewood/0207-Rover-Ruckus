package wen.control;


import java.math.RoundingMode;
import java.text.DecimalFormat;

public class PIDControllerBadOOP extends PIDController {


    public PIDControllerBadOOP(double pGain, double iGain, double dGain) {
        super(pGain, iGain, dGain);
    }

    public PIDControllerBadOOP(double pGain, double iGain, double dGain, double goal) {
        super(pGain, iGain, dGain, goal);
    }

    public void updateError(double currentPosition) {
        super.updateError(currentPosition);
    }

    public double correction() {
        return super.correction();
    }

    public void setGoalNoReset(double goal) {
        super.setGoalNoReset(goal);
    }


}
