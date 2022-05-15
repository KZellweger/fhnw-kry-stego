package ch.fhnw.kry.krystego.controller;

import javafx.application.Platform;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

public class StegoRevealer implements Runnable{
    private final BufferedImage img;
    private final Consumer<BufferedImage> callBack;

    public StegoRevealer(BufferedImage img, Consumer<BufferedImage> callback) {
        this.img = img;
        this.callBack = callback;
    }

    @Override
    public void run()
    {
        Platform.runLater(() -> callBack.accept(img));
        int width = img.getWidth();
        int height = img.getHeight();
        for (int x = 0; x < width; x++) {
            try {
                for (int y = 0; y < height; y++) {
                    img.setRGB(x, y, extractRedAtPixel(x, y, img).getRGB());
                }
                Platform.runLater(() -> callBack.accept(img));
                Thread.sleep(15);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        for (int x = 0; x < width; x++) {
            try {
                for (int y = 0; y < height; y++) {
                    img.setRGB(x, y, extractGreenAtPixel(x, y, img).getRGB());
                }
                Platform.runLater(() -> callBack.accept(img));
                Thread.sleep(15);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        for (int x = 0; x < width; x++) {
            try {
                for (int y = 0; y < height; y++) {
                    img.setRGB(x, y, extractBlueAtPixel(x, y, img).getRGB());
                }
                Platform.runLater(() -> callBack.accept(img));
                Thread.sleep(15);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Platform.runLater(() -> callBack.accept(img));
    }

    private Color extractRedAtPixel(int x, int y, BufferedImage img)
    {
        Color imgColor = new Color(img.getRGB(x, y));

        return new Color(
                extractColor(imgColor.getRed()),
                imgColor.getGreen(),
                imgColor.getBlue()
        );
    }
    private Color extractBlueAtPixel(int x, int y, BufferedImage img)
    {
        Color imgColor = new Color(img.getRGB(x, y));

        return new Color(
                imgColor.getRed(),
                imgColor.getGreen(),
                extractColor(imgColor.getBlue())
        );
    }
    private Color extractGreenAtPixel(int x, int y, BufferedImage img)
    {
        Color imgColor = new Color(img.getRGB(x, y));

        return new Color(
                imgColor.getRed(),
                extractColor(imgColor.getGreen()),
                imgColor.getBlue()
        );
    }

    private int extractColor(int imageValue)
    {
        String imageBits = padBitString(Integer.toBinaryString(imageValue), 8, false);

        String significantImageBits = getSignificantBits(imageBits, 4);
        String paddedImageBits = padBitString(significantImageBits, 8, true);

        return Integer.parseInt(paddedImageBits, 2);
    }

    private String getSignificantBits(String bitString, int length)
    {
        return bitString.substring(bitString.length() - length);
    }

    private String padBitString(String bitString, int targetLength, boolean rightPad)
    {
        if(rightPad){
            while (bitString.length() < targetLength) {
                bitString = bitString + "0";
            }
        } else {
            while (bitString.length() < targetLength) {
                bitString = "0" + bitString;
            }

        }

        return bitString;
    }

}
