package wen.sim.bodies;

public abstract class Body implements Drawable {

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

    public float defaultX = 0;
    public float defaultY = 0;
    public float defaultR = 0;

    private long lasttime;

    public abstract void update(long window);

    public abstract void drawState(long window);

    public abstract void drawData(long window);

    public void simulateStep(long window) {
        float deltaTime = (System.currentTimeMillis() - lasttime) / (float) 1000;
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

