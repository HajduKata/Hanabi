package view;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Class for hold common data for Hanabi View part
 */
public class HanabiUtilities {
    public final static ClassLoader classLoader = ClassLoader.getSystemClassLoader();
    public static final boolean HUMAN = true;
    public static final boolean AI = false;

    public static final int CARD_START_POS_X = 5;
    public static final int CARD_START_POS_Y = 20;

    public static final int SYMBOL_SIZE_X = 45;
    public static final int SYMBOL_SIZE_Y = 45;

    public static final int COLOR_OFFSET_X = 18;
    public static final int COLOR_OFFSET_Y = 40;
    public static final int NUMBER_OFFSET_X = 18;
    public static final int NUMBER_OFFSET_Y = 5;

    public static final int SYMBOL_WIDTH_HEIGHT = 40;

    public static BufferedImage loadImage(URL imageURL) {
        try {
            assert imageURL != null;
            return ImageIO.read(new File(imageURL.getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean clickContains(int mouseX, int mouseY, int startX, int startY, int endX, int endY) {
        return mouseX > startX && mouseX < endX &&
                mouseY > startY && mouseY < endY;
    }
}
