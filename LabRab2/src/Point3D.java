public class Point3D extends Point2D {
    private double zCoord;

    public Point3D(double x, double y, double z) {
        this.setX(x);
        this.setY(y);
        this.zCoord = z;
    }

    public Point3D() {
        this(0.0D, 0.0D, 0.0D);
    }

    public double getZ() {
        return this.zCoord;
    }

    public void setZ(double val) {
        this.zCoord = val;
    }

    public double distanceTo(Point3D otherPoint) {
        return Math.floor(100.0D * Math.sqrt((this.getX() - otherPoint.getX()) * (this.getX() - otherPoint.getX()) + (this.getY() - otherPoint.getY()) * (this.getY() - otherPoint.getY()) + (this.zCoord - otherPoint.getZ()) * (this.zCoord - otherPoint.getZ()))) / 100.0D;
    }

    public boolean equalsTo(Point3D otherPoint) {
        return this.getX() == otherPoint.getX() && this.getY() == otherPoint.getY() && this.zCoord == otherPoint.getZ();
    }
}