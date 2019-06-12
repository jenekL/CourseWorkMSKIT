package panel;

import panel.image.ImageWrapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {

    public ImagePanel() {

        this.addComponentListener(new ComponentAdapter() {
            //перерисовывает картинку в случае изменения размеров фрейма...
            @Override
            public void componentResized(ComponentEvent ce) {
                repaint();
            }
        });

        //добавляем обработчик событий который слушает колёсико мышки, ну и изменяет размер картинки
        this.addMouseWheelListener(e -> {
            if (e.getWheelRotation() == 1) {
                zoom += 0.1;
            } else {
                zoom -= 0.1;
            }
            repaint();
        });

        this.addMouseListener(new MouseAdapter() {
            //записываем первую точку где кликается мышкой и начинается Drag&Dpop
            @Override
            public void mousePressed(MouseEvent e) {
                press = e.getPoint();
                pressedBtn = true;
            }

            //Тут просто быдло кодинг...
            //Метод берет параметры выделеного прямоугольника и перерисовывает тлько выделеную часть картнки
            //Т.к. картинка может менять свои размеры(когда крутишь колесико), то выделеные координаты не соответсвуют
            //координатам картинки. Потому мы делим на zoom
            //Блоки if...else - если бы их не было, тогда когда прямоугольник был больше отрисованной картинки,
            //то отрисовывалась уже удаленная часть картинки. В общем не знаю как обьяснить, удали их сам поймешь.
            @Override
            public void mouseReleased(MouseEvent e) {
                pressedBtn = false;
                if ((int) rect.getWidth() != 0 && (int) rect.getHeight() != 0) {
                    xImg += rect.getX() / zoom;
                    yImg += rect.getY() / zoom;
                    if ((rect.getX() + rect.getWidth()) <= wImg) {
                        wImg = (int) (rect.getWidth() / zoom);
                    } else {
                        wImg = (int) ((wImg - (int) rect.getX()) / zoom);
                    }
                    if ((rect.getY() + rect.getHeight()) <= hImg) {
                        hImg = (int) (rect.getHeight() / zoom);
                    } else {
                        hImg = (int) ((hImg - (int) rect.getY()) / zoom);
                    }
                    zoom = 1;
                    repaint();
                }
            }
        });

        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            //Отрисовываем прямоугольник, который показывает что выделяется.
            public void mouseDragged(MouseEvent e) {
                double x, y, w, h;
                if (e.getX() <= wImg * zoom
                        && e.getY() <= hImg * zoom) {
                    if (e.getX() > (int) press.getX()) {
                        x = press.getX();
                        w = (double) e.getX() - press.getX();
                    } else {
                        x = (double) e.getX();
                        w = press.getX() - (double) e.getX();
                    }

                    if (e.getY() > (int) press.getY()) {
                        y = press.getY();
                        h = (double) e.getY() - press.getY();
                    } else {
                        y = (double) e.getY();
                        h = press.getY() - (double) e.getY();
                    }
                    setRect(new Rectangle2D.Double(x, y, w, h));
                }
            }
        });
    }

    public void setRect(Rectangle2D rect) {
        this.rect = rect;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        if (img != null) {
            g2.drawImage(img, 0, 0, (int) (wImg * zoom), (int) (hImg * zoom),
                    xImg,
                    yImg,
                    xImg + wImg,
                    yImg + hImg,
                    null);
            if (pressedBtn) {
                g2.draw(rect);
            }
        }

        //for pixels without BufferedImage
//        if (pixels != null) {
//            for (int i = 0; i < hImg; i++) {
//                for (int j = 0; j < wImg; j++) {
//                    g2.setColor(new Color(pixels[i][j]));
//                    g2.drawLine((int)(j * zoom), (int)(i * zoom), (int)(j * zoom), (int)(i * zoom));
//                }
//            }
//            if (pressedBtn) {
//                g2.draw(rect);
//            }
//        }
    }

//    public void setImage(String path) {
//        try {
//            changebleImage = ImageIO.read(new File(path));
//            zoom = 1;
//            xImg = 0;
//            yImg = 0;
//            wImg = defaultimg.getWidth(ImagePanel.this);
//            hImg = defaultimg.getHeight(ImagePanel.this);
//            repaint();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public void setPixels(int[][] pixels, int W, int H) {

        // defaultImage = new ImageWrapper(pixels, W, H);
        // changebleImage = new ImageWrapper(pixels, W, H);
        //changebleImage = new ImageWrapper(defaultImage);

        this.pixels = pixels;

        zoom = 1;
        xImg = 0;
        yImg = 0;
        wImg = W;
        hImg = H;

        img = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < hImg; i++) {
            for (int j = 0; j < wImg; j++) {
                ((BufferedImage) img).setRGB(j, hImg - 1 - i, pixels[i][j]);
            }
        }

        repaint();
    }

    private int xImg, yImg, wImg, hImg; // переменные в которых хранятся координаты части картинки которую над показывать.
    // при чем wImg, hImg - это не длина части картинки которую над показать,
    // а координата второго угла прямоугольника.
    private double zoom = 1;
    private static Image img = null;
    private int[][] pixels = null;
    private Rectangle2D rect = new Rectangle2D.Double();
    private static ImageWrapper defaultImage = null;
    private static ImageWrapper changebleImage = null;
    private Point2D press = new Point2D.Double(0, 0);
    private boolean pressedBtn = false;
}
