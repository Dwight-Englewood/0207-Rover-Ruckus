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

    public double magnitude() {
        return (Math.sqrt(Math.pow(this.x, 2)+Math.pow(this.y,2)));
    }

    public double heading() {
        return (Math.atan(this.y/this.x));
    }

    public Coordinate scale(double s){
        return new Coordinate(this.x*s, this.y*s);
    }

    @Override
    public String toString() {
        return "(" + Double.toString(x) + ", " + Double.toString(y) + ")";
    }
}
