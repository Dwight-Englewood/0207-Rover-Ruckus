package wen.sim;

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
}

