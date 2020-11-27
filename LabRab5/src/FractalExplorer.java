//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FractalExplorer {
    private int length;//размер в пикселях
    private JImageDisplay jImageDisplay;//отображает фрактал
    private FractalGenerator fractalGenerator;
    //указывает диапазон комплексной плоскости, которая выводится на экран
    private Rectangle2D.Double aDouble;

    private JComboBox<FractalGenerator> jComboBox;
    private Button btnSave;
    private Button btnReset;

    public FractalExplorer(int l) {
        this.length = l;
        this.aDouble = new Double();
        (new Mandelbrot()).getInitialRange(this.aDouble);
        this.fractalGenerator = new Mandelbrot();
    }

    public void createAndShowGUI() {
        JFrame frame = new JFrame("Fractals");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//операция закрытия окна по умолчанию

        jImageDisplay = new JImageDisplay(length,length);
        jImageDisplay.addMouseListener(new MyMouseListener().mouseListener);

        JPanel jPanelForComboBox = new JPanel();
        jComboBox = new JComboBox<>();
        jComboBox.addItem(new Mandelbrot());
        jComboBox.addItem(new Tricorn());
        jComboBox.addItem(new BurningShip());


        jComboBox.addActionListener(e -> {
            fractalGenerator = (FractalGenerator) jComboBox.getSelectedItem();
            fractalGenerator.getInitialRange(aDouble);
            drawFractal();
            jImageDisplay.repaint();

        });

        jPanelForComboBox.add(new JLabel("Fractal"));
        jPanelForComboBox.add(jComboBox);

        JPanel jPanelForButtons = new JPanel();// панель для кнопок save, reset

        btnReset = new Button("Reset Display");
        ActionListener actionListenerForBtnReset = e -> {
            fractalGenerator.getInitialRange(aDouble);
            drawFractal();
            jImageDisplay.repaint();
        };
        btnReset.addActionListener(actionListenerForBtnReset);


        btnSave = new Button("Save Image");
        ActionListener actionListenerForBtnSave = e -> {

            //создание возможности сохранения через диалоговое окно
            JFileChooser jFileChooser = new JFileChooser();
            FileFilter fileFilter = new FileNameExtensionFilter("PNG Images", "png");
            jFileChooser.setFileFilter(fileFilter);
            jFileChooser.setAcceptAllFileFilterUsed(false);

            if (jFileChooser.showDialog(frame, "Save") == JFileChooser.APPROVE_OPTION){//если пользователь сохраняет

                try {
                    ImageIO.write(jImageDisplay.getBufferedImage(), "png", jFileChooser.getSelectedFile());

                } catch (IOException ioException){
                    JOptionPane.showMessageDialog(frame, ioException.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        btnSave.addActionListener(actionListenerForBtnSave);

        jPanelForButtons.add(btnSave);
        jPanelForButtons.add(btnReset);

        frame.add(jImageDisplay, BorderLayout.CENTER);
        frame.add(jPanelForButtons, BorderLayout.SOUTH);
        frame.add(jPanelForComboBox, BorderLayout.NORTH);

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
        FractalExplorer fractalExplorer = new FractalExplorer(500);
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
