package org.firstinspires.ftc.teamcode.Hardware.Enums;

public enum Wheel {
    STEALTH,
    OMNI,
    MECANUM;

    //In CM
    private double radius;
    private double circumference;

    public double getRadius(){
        return radius;
    }

    public double getCircumference() {
        return circumference;
    }
}
