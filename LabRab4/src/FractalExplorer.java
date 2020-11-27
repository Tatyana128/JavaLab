//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.awt.Button;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class FractalExplorer {
    private int length;//размер в пикселях
    private JImageDisplay jImageDisplay;//отображает фрактал
    private FractalGenerator fractalGenerator;
    //указывает диапазон комплексной плоскости, которая выводится на экран
    private Rectangle2D.Double aDouble;

    public FractalExplorer(int l) {
        this.length = l;
        this.aDouble = new Double();
        (new Mandelbrot()).getInitialRange(this.aDouble);
        this.fractalGenerator = new Mandelbrot();
    }

    public void createAndShowGUI() {
        JFrame frame = new JFrame("Фрактал Мандельброта");
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        this.jImageDisplay = new JImageDisplay(this.length, this.length);
        this.jImageDisplay.addMouseListener((new FractalExplorer.MyMouseListener()).mouseListener);


        Button button = new Button("Reset");

        ActionListener actionListener = (e) -> {
            this.fractalGenerator.getInitialRange(this.aDouble);
            this.drawFractal();
            this.jImageDisplay.repaint();
        };

        button.addActionListener(actionListener);

        frame.add(this.jImageDisplay, "Center");
        frame.add(button, "South");


        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    private void drawFractal() {
        for(int y = 1; y < this.length; ++y) {
            for(int x = 1; x < this.length; ++x) {
                double xCoord = FractalGenerator.getCoord(this.aDouble.x, this.aDouble.x + this.aDouble.width, this.length, x);
                double yCoord = FractalGenerator.getCoord(this.aDouble.y, this.aDouble.y + this.aDouble.height, this.length, y);
                int numIters = this.fractalGenerator.numIterations(xCoord, yCoord);
                if (numIters != -1) {
                    float hue = 0.7F + (float)numIters / 200.0F;
                    int rgbColor = Color.HSBtoRGB(hue, 0.74F, 0.74F);
                    this.jImageDisplay.drawPixel(x, y, rgbColor);
                } else {
                    this.jImageDisplay.drawPixel(x, y, 0);
                }
            }
        }

        this.jImageDisplay.repaint();
    }

    public static void main(String[] args) {
        FractalExplorer fractalExplorer = new FractalExplorer(600);
        fractalExplorer.createAndShowGUI();
        fractalExplorer.drawFractal();
    }

    class MyMouseListener extends MouseAdapter {
        public MouseListener mouseListener = new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                double xCord = FractalGenerator.getCoord(FractalExplorer.this.aDouble.x, FractalExplorer.this.aDouble.x + FractalExplorer.this.aDouble.width, FractalExplorer.this.length, e.getX());
                double yCord = FractalGenerator.getCoord(FractalExplorer.this.aDouble.y, FractalExplorer.this.aDouble.y + FractalExplorer.this.aDouble.height, FractalExplorer.this.length, e.getY());
                FractalGenerator.recenterAndZoomRange(FractalExplorer.this.aDouble, xCord, yCord, 0.5D);
                FractalExplorer.this.drawFractal();
            }
            public void mousePressed(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }
        };

        MyMouseListener() {
        }
    }
}
