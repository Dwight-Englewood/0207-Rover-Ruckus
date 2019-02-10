package wen.sim.bodies;

public interface Drawable {
    public void drawState(long window);

    public void drawData(long window);

    public void reset();

    public void update(long window);

}