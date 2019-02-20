package wen.sim.bodies;

public abstract class Body implements Drawable {

    public double botX = 0;
    public double botXD = 0;
    public double botXDD = 0;
    public double botY = 0;
    public double botYD = 0;
    public double botYDD = 0;
    public double botR = 0;
    public double botRD = 0;
    public double botRDD = 0;
    public double botXF = 0;
    public double botYF = 0;
    public double botRF = 0;

    public double defaultX = 0;
    public double defaultY = 0;
    public double defaultR = 0;

    public long lasttime;

    public abstract void update(long window);

    public abstract void drawState(long window);

    public abstract void drawData(long window);

    public void simulateStep(long window) {
        double deltaTime = (System.currentTimeMillis() - lasttime) / (double) 1000;
        this.botXD = this.botXD + this.botXDD * deltaTime;
        this.botYD = this.botYD + this.botYDD * deltaTime;
        this.botRD = this.botRD + this.botRDD * deltaTime;
        this.botX = this.botX + this.botXD * deltaTime;
        this.botY = this.botY + this.botYD * deltaTime;
        this.botR = this.botR + this.botRD * deltaTime;
        lasttime = System.currentTimeMillis();
    }

    public void reset() {
        botX = defaultX;
        botXD = 0;
        botXDD = 0;
        botY = defaultY;
        botYD = 0;
        botYDD = 0;
        botR = defaultR;
        botRD = 0;
        botRDD = 0;
        botXF = 0;
        botYF = 0;
        botRF = 0;
    }
}

