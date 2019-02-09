package wen.control;


import java.math.RoundingMode;
import java.text.DecimalFormat;

public class PIDController {

    //Much thanks to Wesley, at blog.wesleyac.com

    private final double pGain;
    private final double iGain;
    private final double dGain;

    private double goal;
    private double error;

    private double lX;
    private double dError;
    private double iError;

    public PIDController(double pGain, double iGain, double dGain) {
        this.pGain = pGain;
        this.iGain = iGain;
        this.dGain = dGain;
    }

    public PIDController(double pGain, double iGain, double dGain, double goal) {
        this.pGain = pGain;
        this.iGain = iGain;
        this.dGain = dGain;
        this.goal = goal;
    }

    protected double correction() {
        return ((error * pGain) + (iError * iGain) - (dError * dGain));
    }

    protected void updateError(double currentPosition) {
        this.error = goal - currentPosition;
        this.iError = this.iError + this.error;
        this.dError = currentPosition - this.lX;
        this.lX = currentPosition;
    }

    public void setGoal(double goal) {
        this.goal = goal;
        this.reset();
    }

    protected void setGoalNoReset(double goal) {
        this.goal = goal;
    }

    public boolean goalReached(double resolution) {
        return Math.abs(this.error) < resolution;
    }

    protected void reset() {
        this.goal = 0;
        this.error = 0;
        this.lX = 0;
        this.dError = 0;
        this.iError = 0;
    }

    public static void main(String[] args) {
        int merp = 0;
        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.CEILING);
        PIDController dab = new PIDController(.3, 0, 0);
        //PIDController dab = new PIDController(.6, 0, 2.5);
        //PIDController dab = new PIDController(.4, .001, 6.5);

        double currentPosition = 10;
        double currentVelocity = 0;
        double currentAcceleration = 0;
        final double mass = 100;
        final double gravity = 9.8;
        dab.setGoal(0);

        /*for (int i=0; i < 2000; i++) {
            dab.updateError(currentPosition);
            double force = Range.clip(dab.correction(), -1, 1)*5000;
            currentAcceleration = force/mass - ((10/mass) * currentVelocity)-gravity;
            currentVelocity = (currentVelocity + currentAcceleration / 100);
            currentPosition = (currentPosition + currentVelocity / 100);
            if (i == 150) {
                merp = 1;
            }
            System.out.println(df.format(currentPosition));
        }*/
    }

}
