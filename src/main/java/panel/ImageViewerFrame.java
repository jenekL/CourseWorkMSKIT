package panel;

import imageFilters.MirrorFilter;
import tga.TGADecoder;
import util.ConvertByteBufferToPixelsArray;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.ByteBuffer;
import javax.swing.*;


class ImageViewerFrame extends JFrame {
    private int[][] pixels = null;
    private TGADecoder tgaDecoder = null;

    public ImageViewerFrame() {
        // Выделение области мышкой

        setTitle("ImageViewer");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        label = new JLabel();
        add(label);
        chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu menu = new JMenu("File");
        menuBar.add(menu);

        JMenuItem openItem = new JMenuItem("Open");
        menu.add(openItem);

        openItem.addActionListener(e -> {
            chooser.setCurrentDirectory(new File("."));
            int result = chooser.showOpenDialog(ImageViewerFrame.this);

            if (result == JFileChooser.APPROVE_OPTION) {
                InputStream is = null;
                try {
                    is = new BufferedInputStream(
                            new FileInputStream(chooser.getSelectedFile().getPath()));
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
                tgaDecoder = new TGADecoder(is);

                ByteBuffer byteBuffer = ByteBuffer.allocateDirect(tgaDecoder.getImageSizeInBytes());
                tgaDecoder.decode(byteBuffer);

                pixels = ConvertByteBufferToPixelsArray.convert(byteBuffer,
                        tgaDecoder.getWidth(), tgaDecoder.getHeight(), tgaDecoder.getBytesPerPixel());

                panel.setPixels(pixels, tgaDecoder.getWidth(), tgaDecoder.getHeight());
                //panel.setImage(chooser.getSelectedFile().getPath());
            }

        });

        panel = new ImagePanel();
        this.add(panel);

        JMenuItem exitItem = new JMenuItem("Exit");
        menu.add(exitItem);
        exitItem.addActionListener(event -> System.exit(0));

        JMenuItem mirrorButton = new JMenuItem("Отзеркалить");
        mirrorButton.addActionListener(e->{
            panel.setPixels(MirrorFilter.fromArrayToArray(pixels, tgaDecoder.getHeight(), tgaDecoder.getWidth()),
                    tgaDecoder.getWidth(), tgaDecoder.getHeight());
            panel.repaint();
        });
        menuBar.add(mirrorButton);


    }

    private JLabel label;
    //добавление экземпляра ImagePanel
    private ImagePanel panel;
    private JFileChooser chooser;
    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 400;
}
