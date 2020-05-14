package view;

import javax.imageio.ImageIO;
import java.awt.Color;
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

    static final int SYMBOL_SIZE_X = 45;
    static final int SYMBOL_SIZE_Y = 45;
    static final int COLOR_OFFSET_X = 18;
    static final int COLOR_OFFSET_Y = 40;
    static final int NUMBER_OFFSET_X = 18;
    static final int NUMBER_OFFSET_Y = 5;

    static final int SYMBOL_WIDTH_HEIGHT = 40;

    static final Color BG_COLOR = Color.decode("#003375");

    public static BufferedImage loadImage(URL imageURL) {
        try {
            assert imageURL != null;
            return ImageIO.read(new File(imageURL.getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
