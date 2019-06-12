package panel;

import bmp.BMPWriter;
import imageFilters.MirrorFilter;
import tga.TGADecoder;
import util.ConvertByteBufferToPixelsArray;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.ByteBuffer;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;


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

        JMenu menu = new JMenu("Файл");
        menuBar.add(menu);

        JMenuItem openItem = new JMenuItem("Открыть");
        menu.add(openItem);

        openItem.addActionListener(e -> {
            chooser.setCurrentDirectory(new File("."));
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "TGA images", "tga");
            chooser.setFileFilter(filter);

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

        JMenuItem saveItem = new JMenuItem("Сохранить");
        menu.add(saveItem);

        saveItem.addActionListener(e->{
            if(pixels == null){
                JOptionPane.showMessageDialog(ImageViewerFrame.this,
                        "Нечего сохранять");
            }
            else {
                chooser.setCurrentDirectory(new File("."));
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "BMP images", "bmp");
                chooser.setFileFilter(filter);

                int result = chooser.showSaveDialog(ImageViewerFrame.this);

                if (result == JFileChooser.APPROVE_OPTION) {
                    BMPWriter.save(pixels, chooser.getSelectedFile().getPath() + ".bmp");
                }
            }

        });

        panel = new ImagePanel();
        this.add(panel);

        JMenuItem exitItem = new JMenuItem("Закрыть");
        menu.add(exitItem);
        exitItem.addActionListener(event -> System.exit(0));

        JMenu filterMenu = new JMenu("Фильтры");
        menuBar.add(filterMenu);

        JMenuItem mirrorButton = new JMenuItem("Отзеркалить");
        mirrorButton.addActionListener(e->{
            if(pixels == null){
                JOptionPane.showMessageDialog(ImageViewerFrame.this,
                        "Нечего отзеркаливать");
            }
            else {
                panel.setPixels(MirrorFilter.fromArrayToArray(pixels, tgaDecoder.getHeight(), tgaDecoder.getWidth()),
                        tgaDecoder.getWidth(), tgaDecoder.getHeight());
                panel.repaint();
            }
        });
        filterMenu.add(mirrorButton);


    }

    private JLabel label;
    private ImagePanel panel;
    private JFileChooser chooser;
    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 400;
}
