import java.awt.geom.Rectangle2D;
//подкласс FractalGenerator l4
public class Mandelbrot extends FractalGenerator {
    @Override
    public void getInitialRange (Rectangle2D.Double range){
        //позволяет генератору
        //фракталов определить наиболее «интересную» область комплексной плоскости
        //для конкретного фрактала

        //этот метод должен установить начальный диапазон в (-2 - 1.5i) - (1 + 1.5i)
        range.x = -2.0;
        range.y = -1.5;

        range.width =  3;
        range.height = 3;
    }

    public static final int MAX_ITERATIONS = 2000;
    @Override
    public int numIterations (double x, double y){
        //алгоритм Мандельброта
        // Zi+1=Zi2 + C заменяем для упрощения на xi+1=xi2 - yi2 + a, yi+1=2xiyi + b.
        ComplexNumber c = new ComplexNumber(x,y);
        ComplexNumber z = new ComplexNumber();

        int iterations = 0;

        //(x^2 + y^2) без использования встроенной функции для быстроты
        //для проверки сравниваем |z|^2 c 4 и iterations с MAX_ITERATIONS
        while (z.getX() * z.getX() + z.getY() * z.getY() <= 4 && iterations < MAX_ITERATIONS){

            double tempZ = z.getX(); // сохранениея Zx для вычисления z.setY();
            z.setX(z.getX() * z.getX() + c.getX() - z.getY() * z.getY());//(Zx^2 + Cx -Zy^2)
            z.setY(2 * tempZ * z.getY() + c.getY());//(2*Zy*Zx + Cy)

            iterations++;
        }
        if (iterations >= MAX_ITERATIONS) return -1;
        return iterations;

    }

    @Override
    public String toString() {
        return "Mandelbrot";
    }
}
