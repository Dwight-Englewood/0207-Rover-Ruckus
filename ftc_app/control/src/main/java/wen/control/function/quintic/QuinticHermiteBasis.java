package wen.control.function.quintic;

public class QuinticHermiteBasis {

    public static final QuinticPolynomial h0 = new QuinticPolynomial(-6, 15, -10, 0, 0, 1);
    public static final QuinticPolynomial h1 = new QuinticPolynomial(-3, 8, -6, 0, 1, 0);
    public static final QuinticPolynomial h2 = new QuinticPolynomial(-.5, 1.5, -1.5, .5, 0, 0);
    public static final QuinticPolynomial h3 = new QuinticPolynomial(.5, -1, .5, 0, 0, 0);
    public static final QuinticPolynomial h4 = new QuinticPolynomial(-3, 7, -4, 0, 0, 0);
    public static final QuinticPolynomial h5 = new QuinticPolynomial(6, -15, 10, 0, 0, 0);
    public static final QuinticPolynomial h0d = new QuinticPolynomial(0, -30, 60, -30, 0, 1);
    public static final QuinticPolynomial h1d = new QuinticPolynomial(0, -15, 32, -18, 0, 1);
    public static final QuinticPolynomial h2d = new QuinticPolynomial(0, -2.5, 6, -4.5, 1, 0);
    public static final QuinticPolynomial h3d = new QuinticPolynomial(0, 2.5, -4, 1.5, 0, 0);
    public static final QuinticPolynomial h4d = new QuinticPolynomial(0, -15, 28, -12, 0, 0);
    public static final QuinticPolynomial h5d = new QuinticPolynomial(0, 30, -60, 30, 0, 0);
    public static final QuinticPolynomial h0dd = new QuinticPolynomial(0, 0, -120, 180, -60, 0);
    public static final QuinticPolynomial h1dd = new QuinticPolynomial(0, 0, -60, 96, -36, 0);
    public static final QuinticPolynomial h2dd = new QuinticPolynomial(0, 0, -10, 18, -9, 1);
    public static final QuinticPolynomial h3dd = new QuinticPolynomial(0, 0, 10, -12, 3, 0);
    public static final QuinticPolynomial h4dd = new QuinticPolynomial(0, 0, -60, 84, -24, 0);
    public static final QuinticPolynomial h5dd = new QuinticPolynomial(0, 0, 120, -180, 60, 0);

}
