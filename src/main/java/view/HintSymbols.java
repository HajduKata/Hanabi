package view;

import model.CardColor;
import model.CardNumber;

import java.awt.image.BufferedImage;
import java.util.Objects;

import static view.HanabiUtilities.classLoader;
import static view.HanabiUtilities.loadImage;

/**
 * Utility class for the symbol images
 */
class HintSymbols {
    private static BufferedImage redColorSymbolImage;
    private static BufferedImage greenColorSymbolImage;
    private static BufferedImage blueColorSymbolImage;
    private static BufferedImage yellowColorSymbolImage;
    private static BufferedImage whiteColorSymbolImage;
    private static BufferedImage number1SymbolImage;
    private static BufferedImage number2SymbolImage;
    private static BufferedImage number3SymbolImage;
    private static BufferedImage number4SymbolImage;
    private static BufferedImage number5SymbolImage;

    HintSymbols() {
        redColorSymbolImage = loadImage(Objects.requireNonNull(classLoader.getResource("Symbols/red.png")));
        greenColorSymbolImage = loadImage(Objects.requireNonNull(classLoader.getResource("Symbols/green.png")));
        blueColorSymbolImage = loadImage(Objects.requireNonNull(classLoader.getResource("Symbols/blue.png")));
        yellowColorSymbolImage = loadImage(Objects.requireNonNull(classLoader.getResource("Symbols/yellow.png")));
        whiteColorSymbolImage = loadImage(Objects.requireNonNull(classLoader.getResource("Symbols/white.png")));
        number1SymbolImage = loadImage(Objects.requireNonNull(classLoader.getResource("Symbols/1.png")));
        number2SymbolImage = loadImage(Objects.requireNonNull(classLoader.getResource("Symbols/2.png")));
        number3SymbolImage = loadImage(Objects.requireNonNull(classLoader.getResource("Symbols/3.png")));
        number4SymbolImage = loadImage(Objects.requireNonNull(classLoader.getResource("Symbols/4.png")));
        number5SymbolImage = loadImage(Objects.requireNonNull(classLoader.getResource("Symbols/5.png")));
    }

    /**
     * Returns the symbol image of the given color
     *
     * @param color name of the color
     * @return symbol image of the color
     */
    static BufferedImage getImageByColor(CardColor color) {
        switch (color) {
            case RED:
                return redColorSymbolImage;
            case YELLOW:
                return yellowColorSymbolImage;
            case GREEN:
                return greenColorSymbolImage;
            case BLUE:
                return blueColorSymbolImage;
            case WHITE:
                return whiteColorSymbolImage;
        }
        return null;
    }

    /**
     * Returns the symbol image of the given number
     *
     * @param number the number symbol
     * @return symbol image of the number
     */
    static BufferedImage getImageByNumber(CardNumber number) {
        switch (number) {
            case ONE:
                return number1SymbolImage;
            case TWO:
                return number2SymbolImage;
            case THREE:
                return number3SymbolImage;
            case FOUR:
                return number4SymbolImage;
            case FIVE:
                return number5SymbolImage;
        }
        return null;
    }
}
