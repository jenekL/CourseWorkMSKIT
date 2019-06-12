package imageFilters;

import java.nio.ByteBuffer;

public class MirrorFilter {
    public static int[][] fromByteBufferToPixels(ByteBuffer byteBuffer, int H, int W, int bpp) {
        int[][] pixels = new int[H][W];

        int k = 0;
        for (int i = H; i >= 0; i--) {
            for (int j = W; j >= 0; j--) {

                pixels[i][j] = (byteBuffer.get(k) * 65536) //R
                        + (byteBuffer.get(k + 1) * 256) // G
                        + byteBuffer.get(k + 2); // B
                k += bpp;
            }
        }
        return pixels;
    }

    public static ByteBuffer byteBufferToByteBuffer(ByteBuffer byteBuffer, int H, int W, int bpp) {

        return byteBuffer;
    }

    public static int[][] fromArrayToArray(int[][] pixels, int H, int W) {
        int[][] pxls = pixels;
        for (int i = 0; i < H; i++) {
            for (int j = 0; j < W; j++) {
                int tmp = pxls[i][W - 1 - j];
                pxls[i][W - 1 - j] = pxls[i][j];
                pxls[i][j] = tmp;
            }
        }
        return pxls;
    }

}
