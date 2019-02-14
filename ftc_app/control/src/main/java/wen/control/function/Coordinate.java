package wen.control.function;

public class Coordinate {

    public double x;
    public double y;

    public Coordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Coordinate add(Coordinate c) {
        return new Coordinate(this.x + c.x, this.y + c.y);
    }

    public Coordinate norm() {
        double mag = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        return (new Coordinate(x / mag, y / mag));
    }

    @Override
    public String toString() {
        return "(" + Double.toString(x) + ", " + Double.toString(y) + ")";
    }
}
