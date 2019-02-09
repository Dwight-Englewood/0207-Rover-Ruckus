package wen.sim.bodies;

public abstract class Body {

    public float botX = 0;
    public float botXD = 0;
    public float botXDD = 0;
    public float botY = 0;
    public float botYD = 0;
    public float botYDD = 0;
    public float botR = 0;
    public float botRD = 0;
    public float botRDD = 0;
    public float botXF = 0;
    public float botYF = 0;
    public float botRF = 0;

    public abstract void update(long window);

    public abstract void draw(long window);

    public void reset() {
        botX = 0;
        botXD = 0;
        botXDD = 0;
        botY = 0;
        botYD = 0;
        botYDD = 0;
        botR = 0;
        botRD = 0;
        botRDD = 0;
        botXF = 0;
        botYF = 0;
        botRF = 0;
    }
}

