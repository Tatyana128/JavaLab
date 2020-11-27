import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

public class FractalExplorer {

    private int length; // размер в пикселях
    private JImageDisplay jImageDisplay;  //отображает фрактал,
    private FractalGenerator fractalGenerator;
    //указывает диапазон комплексной плоскости, которая выводится на экран
    private Rectangle2D.Double aDouble;


    private JComboBox<FractalGenerator> jComboBox;
    private Button btnSave;
    private Button btnReset;


    public static void main(String[] args) {
        FractalExplorer fractalExplorer = new FractalExplorer(500);
        fractalExplorer.createAndShowGUI();
        fractalExplorer.drawFractal();
    }


    public FractalExplorer (int l){
        length = l;
        aDouble = new Rectangle2D.Double();
        new Mandelbrot().getInitialRange(aDouble);
        fractalGenerator = new Mandelbrot();
    }



    public void createAndShowGUI(){
        JFrame frame = new JFrame("Fractals");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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


        JPanel jPanelForButtons = new JPanel();


        btnReset = new Button("Reset Display");
        ActionListener actionListenerForBtnReset = e -> {

            fractalGenerator.getInitialRange(aDouble);
            drawFractal();
            jImageDisplay.repaint();
        };
        btnReset.addActionListener(actionListenerForBtnReset);


        btnSave = new Button("Save Image");
        ActionListener actionListenerForBtnSave = e -> {


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


    private int rowsRemaining;
    private void drawFractal (){//для вывода на экран фрактала

        double xCoord;
        double yCoord;

        int numIters;

        enableUI(false);//выключаем кнопки чтобы игнорировать события
        rowsRemaining = length;
        for (int y = 0; y < length; y++){//проходит через каждый пиксель тобы отрисовать фрактал
            FractalWorker fractalWorker = new FractalWorker(y);
            fractalWorker.execute();
        }
    }



    class MyMouseListener extends MouseAdapter {
        public  MouseListener mouseListener = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (rowsRemaining <= 0) { // что это ?
                    double xCord;
                    double yCord;
                    //вычисляем координаты по которым щелкнули
                    xCord = FractalGenerator.getCoord(aDouble.x, aDouble.x + aDouble.width, length, e.getX());
                    yCord = FractalGenerator.getCoord(aDouble.y, aDouble.y + aDouble.height, length, e.getY());

                    FractalGenerator.recenterAndZoomRange(aDouble, xCord, yCord, 0.5);

                    drawFractal();
                }
            }
            @Override
            public void mousePressed(MouseEvent e) { }
            @Override
            public void mouseReleased(MouseEvent e) { }
            @Override
            public void mouseEntered(MouseEvent e) { }
            @Override
            public void mouseExited(MouseEvent e) { }
        };


    }

    private void enableUI (boolean val){
        if (val){
            btnReset.setEnabled(true);
            btnSave.setEnabled(true);
            jComboBox.setEnabled(true);
        } else {
            btnReset.setEnabled(false);
            btnSave.setEnabled(false);
            jComboBox.setEnabled(false);
        }
    }


    private class FractalWorker extends SwingWorker<Object, Object>{

        private int y; //координата вычисляемой строки
        private int[] pixelsRGB;//массив чисел  для хранения
        //вычисленных значений RGB для каждого пикселя в этой строке

        private FractalWorker (int y){
            this.y = y;
        }

        @Override
        protected Object doInBackground() throws Exception {
            //вместо drawfractal
            //метод, который фактически выполняет фоновые операции
            //x-пиксельная координата, xCoord-координата в пространстве фрактала
            //отображение пикселей
            pixelsRGB = new int[length + 1];

            double xCoord;
            double yCoord;
            int numIters;

            yCoord = FractalGenerator.getCoord(aDouble.y, aDouble.y + aDouble.height, length, y);

            for (int x = 0; x < length; x++){

                xCoord = FractalGenerator.getCoord(aDouble.x, aDouble.x + aDouble.width, length, x);

                numIters = fractalGenerator.numIterations(xCoord,yCoord);

                if (numIters != -1){  //если точка  выходит за границы
                    //выберaем значение цвета, основанное на количестве итераций (код из методички)
                    float hue = 0.7f + (float) numIters / 200f;
                    pixelsRGB[x] = Color.HSBtoRGB(hue, 0.74f, 0.74f);
                }
                else pixelsRGB[x] = 0;
            }
            return null;
        }

        @Override
        protected void done() { // этот метод вызывается, когда фоновая задача завершена
            //римует пиксели, которые были получены в предыдущем методе
            super.done();
            for (int x = 0; x < length; x++){
                jImageDisplay.drawPixel(x, y, pixelsRGB[x]);
            }
            jImageDisplay.repaint(1,y, length, 1);

            rowsRemaining--;//следим за тем , когда закончится прорисовка чтобы включить кнопки
            if (rowsRemaining <= 0) enableUI(true);//включаем кнопки
        }
    }
}
