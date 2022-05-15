package ch.fhnw.kry.krystego.controller;

import javafx.application.Platform;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

public class StegoCalculator implements Runnable {

    private final BufferedImage img;
    private final BufferedImage containment;
    private final Consumer<BufferedImage> callBack;

    public StegoCalculator(BufferedImage img, BufferedImage containment, Consumer<BufferedImage> callback) {
        this.img = img;
        this.containment = containment;
        this.callBack = callback;
    }

    public void run()
    {
        Platform.runLater(() -> callBack.accept(img));
        int width = img.getWidth();
        int height = img.getHeight();
        for (int x = 0; x < width; x++) {
            try {
            for (int y = 0; y < height; y++) {
                img.setRGB(x, y, mergeRedAtPixel(x, y, img, containment).getRGB());
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
                img.setRGB(x, y, mergeGreenAtPixel(x, y, img, containment).getRGB());
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
                img.setRGB(x, y, mergeBlueAtPixel(x, y, img, containment).getRGB());
            }
                Platform.runLater(() -> callBack.accept(img));
                Thread.sleep(15);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Platform.runLater(() -> callBack.accept(img));
    }

    private Color mergeRedAtPixel(int x, int y, BufferedImage img, BufferedImage containment)
    {
        Color imgColor = new Color(img.getRGB(x, y));
        Color containmentColor = new Color(containment.getRGB(x, y));

        return new Color(
                mergeColor(imgColor.getRed(), containmentColor.getRed()),
                imgColor.getGreen(),
                imgColor.getBlue()
        );
    }
    private Color mergeBlueAtPixel(int x, int y, BufferedImage img, BufferedImage containment)
    {
        Color imgColor = new Color(img.getRGB(x, y));
        Color containmentColor = new Color(containment.getRGB(x, y));

        return new Color(
                imgColor.getRed(),
                imgColor.getGreen(),
                mergeColor(imgColor.getBlue(), containmentColor.getBlue())
                );
    }
    private Color mergeGreenAtPixel(int x, int y, BufferedImage img, BufferedImage containment)
    {
        Color imgColor = new Color(img.getRGB(x, y));
        Color containmentColor = new Color(containment.getRGB(x, y));

        return new Color(
                imgColor.getRed(),
                mergeColor(imgColor.getGreen(), containmentColor.getGreen()),
                imgColor.getBlue()
        );
    }

    private int mergeColor(int imageValue, int containmentValue)
    {
        String imageBits = padBitString(Integer.toBinaryString(imageValue), 8);
        String containmentBits = padBitString(Integer.toBinaryString(containmentValue), 8);

        String significantImageBits = getMostSignificantBits(imageBits, 4);
        String significantContainmentBits = getMostSignificantBits(containmentBits, 4);

        return Integer.parseInt(significantContainmentBits + significantImageBits, 2);
    }

    private String getMostSignificantBits(String bitString, int length)
    {
        return bitString.substring(0, length);
    }

    private String padBitString(String bitString, int targetLength)
    {
        while (bitString.length() < targetLength) {
            bitString = "0" + bitString;
        }
        return bitString;
    }
}
