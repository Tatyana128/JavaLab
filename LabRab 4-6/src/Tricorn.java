import java.awt.geom.Rectangle2D;

public class Tricorn extends FractalGenerator {
//второй вид фрактала, отличается только начальным диапазоном и уравнением
    @Override
    public void getInitialRange(Rectangle2D.Double range) {
        range.x = -2.0;
        range.y = -2.0;

        range.width =  4;
        range.height = 4;
    }

    public static final int MAX_ITERATIONS = 2000;
    @Override
    public int numIterations(double x, double y) {
        ComplexNumber c = new ComplexNumber(x,-y);
        ComplexNumber z = new ComplexNumber();

        int iterations = 0;

        //(x^2 + y^2)
        while (z.getX() * z.getX() + z.getY() * z.getY() <= 4 && iterations < MAX_ITERATIONS){

            double tempZ = z.getX();

            z.setX(z.getX() * z.getX() + c.getX() - z.getY() * z.getY());
            z.setY(-1 * (2 * tempZ * z.getY() + c.getY()));

            iterations++;
        }
        if (iterations >= MAX_ITERATIONS) return -1;
        return iterations;
    }
    @Override
    public String toString() {
        return "Tricorn";
    }
}