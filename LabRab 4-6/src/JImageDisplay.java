//l4
import javax.swing.JComponent;
import java.awt.*;
import java.awt.image.BufferedImage;

public class JImageDisplay extends JComponent{

    private BufferedImage bufferedImage;//управляет изображением, содержимое которого можно записать.


    public JImageDisplay (int x, int y){
        //конструктор принимает высоту и ширину, инициализирует новое изображение
        //этой высоты и ширины и типом intrgb(8бит)

        bufferedImage = new BufferedImage(x,y,BufferedImage.TYPE_INT_RGB);
        setPreferredSize(new Dimension(x,y));//включение компонента в пользовательский интерфейс
    }

    @Override
    protected void paintComponent(Graphics g) {
        //отрисовка изображения
        g.drawImage (bufferedImage, 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null);
    }


    public void drawPixel (int x, int y, int rgbColor){
        //устанавливает пиксель в определенный цвет
        bufferedImage.setRGB(x,y,rgbColor);
    }

    public BufferedImage getBufferedImage () {
        return bufferedImage;
    }
}
