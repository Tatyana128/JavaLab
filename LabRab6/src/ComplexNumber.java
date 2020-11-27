
// комплексное число вида x + yi;

public class ComplexNumber {
    private double x;
    private double y;

    public ComplexNumber (){
        x = 0;
        y = 0;
    }
    public  ComplexNumber (double x, double y){
        this.x = x;
        this.y = y;
    }

    public double getX (){
        return x;
    }
    public double getY (){
        return y;
    }
    public void setX (double x){
        this.x = x;
    }
    public void setY (double y){
        this.y = y;
    }
}
