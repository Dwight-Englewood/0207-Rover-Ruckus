package wen.control.function.quintic;

import java.util.ArrayList;

import wen.control.function.Coordinate;
import wen.control.function.ParametricFunction;

public class QuinticHermitePath extends ParametricFunction {

    public double tMin;
    public double tMax;
    public ArrayList<QuinticHermiteSpline> q = new ArrayList<>();
    public ArrayList<QuinticHermiteSplineDerivitive> qd = new ArrayList<>();
    public ArrayList<QuinticHermiteSplineDerivitiveDerivitive> qdd = new ArrayList<>();

    public QuinticHermitePath(ArrayList<QuinticHermiteSpline> q, ArrayList<QuinticHermiteSplineDerivitive> qd, ArrayList<QuinticHermiteSplineDerivitiveDerivitive> qdd) {
        this.q = q;
        this.qd = qd;
        this.qdd = qdd;
        this.tMin = 0;
        this.tMax = q.size();
    }

    @Override
    public Coordinate eval(double t) {
        int index = (int) t;
        double along = t % 1;
        return this.q.get(index).eval(along);
    }

    public Coordinate evald(double t) {
        int index = (int) t;
        double along = t % 1;
        return this.qd.get(index).eval(along);
    }

    public Coordinate evaldd(double t) {
        System.out.println("tttt:"+t);
        int index = (int) t;
        double along = t % 1;
        return this.qdd.get(index).eval(along);
    }
}
