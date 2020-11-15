
import java.util.Scanner;

public class Lab2 {
    public Lab2() {
    }

    public static void main(String[] args) {
        Point3D point1 = new Point3D();
        Point3D point2 = new Point3D();
        Point3D point3 = new Point3D(1.0D, 1.0D, 1.0D);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите координаты первой точки");
        point1.setX(Double.parseDouble(scanner.nextLine()));
        point1.setY(Double.parseDouble(scanner.nextLine()));
        point1.setZ(Double.parseDouble(scanner.nextLine()));
        System.out.println("Введите координаты второй точки");
        point2.setX(Double.parseDouble(scanner.nextLine()));
        point2.setY(Double.parseDouble(scanner.nextLine()));
        point2.setZ(Double.parseDouble(scanner.nextLine()));
        System.out.println("Введите координаты третьей точки");
        point3.setX(Double.parseDouble(scanner.nextLine()));
        point3.setY(Double.parseDouble(scanner.nextLine()));
        point3.setZ(Double.parseDouble(scanner.nextLine()));
        System.out.println("Первая и вторая точки одинаковы - " + point1.equalsTo(point2));
        System.out.println("Первая и третья точки одинаковы - " + point1.equalsTo(point3));
        System.out.println("Вторая и третья точки одинаковы - " + point2.equalsTo(point3));
        System.out.println("Расстояние между третьей и второй точками = " + point3.distanceTo(point2));
        if (!point1.equalsTo(point2) && !point1.equalsTo(point3) && !point2.equalsTo(point3)) {
            System.out.println("Площадь треугольника = " + computeArea(point1, point2, point3));
        } else {
            System.out.println("Одна из точек равна другой! Невозможно вычислить площадь так как треугольника не существует.");
        }

    }

    public static double computeArea(Point3D firstPoint, Point3D secondPoint, Point3D thirdPoint) {
        double a = firstPoint.distanceTo(secondPoint);
        double b = firstPoint.distanceTo(thirdPoint);
        double c = secondPoint.distanceTo(thirdPoint);
        double halfP = (a + b + c) / 2.0D;
        return Math.sqrt(halfP * (halfP - a) * (halfP - b) * (halfP - c));
    }
}