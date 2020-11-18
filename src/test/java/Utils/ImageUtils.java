package Utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ImageUtils {
    private static final byte[] HEX_ARRAY = "0123456789ABCDEF".getBytes(StandardCharsets.US_ASCII);
    public static String bytesToHex(byte[] bytes) {
        byte[] hexChars = new byte[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars, StandardCharsets.UTF_8);
    }

    public static void saveScreenshot(byte[] screen, String dir, String name){
        FileUtils.createNewDir(dir);

        ByteArrayInputStream bis = new ByteArrayInputStream(screen);
        BufferedImage bImage2 = null;
        try {
            bImage2 = ImageIO.read(bis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ImageIO.write(bImage2, "png", new File(dir + "//" + name));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean compareImages(String img1, String img2) {
        BufferedImage imgA = null;
        BufferedImage imgB = null;

        try {
            imgA = ImageIO.read(new File(img1));
            imgB = ImageIO.read(new File(img2));
        } catch (IOException e) {
            e.printStackTrace();
        }

        int width = imgA.getWidth(), height = imgA.getHeight();
        int width2 = imgB.getWidth(), height2 = imgB.getHeight();

        if (width != width2 || height != height2) {
            throw new IllegalArgumentException("Images must have the same dimensions");
        }

        long diff = 0;
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                //Getting the RGB values of a pixel
                int pixel1 = imgA.getRGB(i, j);
                Color color1 = new Color(pixel1, true);
                int r1 = color1.getRed();
                int g1 = color1.getGreen();
                int b1 = color1.getBlue();

                int pixel2 = imgB.getRGB(i, j);
                Color color2 = new Color(pixel2, true);
                int r2 = color2.getRed();
                int g2 = color2.getGreen();
                int b2 = color2.getBlue();

                //sum of differences of RGB values of the two images
                long data = Math.abs(r1 - r2) + Math.abs(g1 - g2) + Math.abs(b1 - b2);
                diff = diff + data;
            }
        }

        double avg = diff / (width * height * 3);
        double percentage = (avg / 255) * 100;

        return percentage < 1;
    }
}
