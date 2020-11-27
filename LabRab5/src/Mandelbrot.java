import java.awt.geom.Rectangle2D;
public class Mandelbrot extends FractalGenerator {
    @Override
    public void getInitialRange (Rectangle2D.Double range){

        range.x = -2.0;
        range.y = -1.5;

        range.width =  3;
        range.height = 3;
    }

    public static final int MAX_ITERATIONS = 2000;
    @Override
    public int numIterations (double x, double y){
        // Zi+1=Zi2 + C заменяем для упрощения на xi+1=xi2 - yi^2 + a, yi+1=2xiyi + b.
        ComplexNumber c = new ComplexNumber(x,y);
        ComplexNumber z = new ComplexNumber();

        int iterations = 0;

        //для проверки сравниваем |z|^2 c 4 и iterations с MAX_ITERATIONS
        while (z.getX() * z.getX() + z.getY() * z.getY() <= 4 && iterations < MAX_ITERATIONS){

            double tempZ = z.getX();
            z.setX(z.getX() * z.getX() + c.getX() - z.getY() * z.getY());//(Zx^2 + Cx -Zy^2)
            z.setY(2 * tempZ * z.getY() + c.getY());//(2*Zy*Zx + Cy)
            iterations++;
        }
        if (iterations >= MAX_ITERATIONS) return -1;
        return iterations;

    }
    @Override
    public String toString() {
        return "Maldelbrot";
    }
}
