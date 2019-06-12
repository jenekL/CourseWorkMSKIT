import bmp.BMPWriter;
import imageFilters.MirrorFilter;
import tga.TGADecoder;
import util.ConvertByteBufferToPixelsArray;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class Main {
    public static void main(String... args) {
        InputStream is = null;
        try {
            is = new BufferedInputStream(
                    new FileInputStream("t34.tga"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        TGADecoder tgaDecoder = new TGADecoder(is);

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(tgaDecoder.getImageSizeInBytes());
        tgaDecoder.decode(byteBuffer);

        int[][] pixels = ConvertByteBufferToPixelsArray.convert(byteBuffer,
                tgaDecoder.getWidth(), tgaDecoder.getHeight(), tgaDecoder.getBytesPerPixel());
        BMPWriter.save(pixels, "newFile.bmp");

        int[][] pixel1 = MirrorFilter.fromArrayToArray(pixels, tgaDecoder.getHeight(), tgaDecoder.getWidth());
        BMPWriter.save(pixel1, "mirror.bmp");
        //BYTES ANOTHER METHOD
//        byte[] arr = new byte[tgaDecoder.getImageSizeInBytes()];
//
//        byteBuffer.rewind();
//        int i = 0;
//        while (byteBuffer.hasRemaining()){
//            arr[i] = byteBuffer.get();
//            i++;
//        }
//            //System.out.println(byteBuffer.get());
//
//
//
//        Bitmap bitMap = new Bitmap(arr);
//        BMPEncoder bmpEncoder = new BMPEncoder(bitMap);
//        byte[] encode = bmpEncoder.encode(arr);
//
//
//
//        System.out.println(byteBuffer.toString());
    }
}
