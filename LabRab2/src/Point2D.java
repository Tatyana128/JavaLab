public class Point2D {
    private double xCoord;
    private double yCoord;

    public Point2D(double x, double y) {
        this.xCoord = x;
        this.yCoord = y;
    }

    public Point2D() {
        this(0.0D, 0.0D);
    }

    public double getX() {
        return this.xCoord;
    }

    public double getY() {
        return this.yCoord;
    }

    public void setX(double val) {
        this.xCoord = val;
    }

    public void setY(double val) {
        this.yCoord = val;
    }
}
