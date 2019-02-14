package wen.control.function;

public abstract class Function {

    public double tMin = 0;
    public double tMax = 1;

    public abstract double eval(double t);

    public double derivitive(double t) {
        return ((this.eval(t + .0005) - this.eval(t - .0005)) / .001);
    }

    public double integral(double tLow, double tMax) {
        double sum = 0;

        for (double i = tLow; i < tMax; i = i + .001) {
            sum = sum + this.eval(i)*.001;
        }

        return sum;
    }

    public double integral (double t) {
        return integral(0, t);
    }

}
