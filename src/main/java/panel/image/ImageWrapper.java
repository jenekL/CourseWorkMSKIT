package panel.image;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageWrapper {
    public Image image;
    private int xImg, yImg, wImg, hImg; // переменные в которых хранятся координаты части картинки которую над показывать.
    // при чем wImg, hImg - это не длина части картинки которую над показать,
    // а координата второго угла прямоугольника.
    private int[][] pixels;
    private double zoom;

    public int getHeight() {
        return wImg - xImg;
    }

    public int getWidth() {
        return hImg - yImg;
    }

    public ImageWrapper(int[][] pixels, int W, int H) {
        this.pixels = pixels;

        zoom = 1;
        xImg = 0;
        yImg = 0;
        wImg = W;
        hImg = H;

        image = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < hImg; i++) {
            for (int j = 0; j < wImg; j++) {
                ((BufferedImage) image).setRGB(j, hImg - 1 - i, pixels[i][j]);
            }
        }
    }

    public ImageWrapper(ImageWrapper other) {
        this.image = other.image;
        this.xImg = other.xImg;
        this.yImg = other.yImg;
        this.wImg = other.wImg;
        this.hImg = other.hImg;
        this.pixels = other.pixels;
        this.zoom = other.zoom;
    }


    public int getxImg() {
        return xImg;
    }

    public void setxImg(int xImg) {
        this.xImg = xImg;
    }

    public int getyImg() {
        return yImg;
    }

    public void setyImg(int yImg) {
        this.yImg = yImg;
    }

    public int getwImg() {
        return wImg;
    }

    public void setwImg(int wImg) {
        this.wImg = wImg;
    }

    public int gethImg() {
        return hImg;
    }

    public void sethImg(int hImg) {
        this.hImg = hImg;
    }

    public int[][] getPixels() {
        return pixels;
    }

    public void setPixels(int[][] pixels) {
        this.pixels = pixels;
    }

    public double getZoom() {
        return zoom;
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
    }
}
