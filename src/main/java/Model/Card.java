package model;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Objects;

import static view.HanabiUtilities.classLoader;
import static view.HanabiUtilities.loadImage;

/**
 * Model of a card
 */
public class Card implements Comparable<Card> {
    // UI contants
    public static final int CARD_SIZE_X = 130;
    public static final int CARD_SIZE_Y = 130;
    public static final int CARD_OFFSET_X = 60;
    public static final int CARD_OFFSET_Y = 10;

    public static int numberOfColors = 5; // TODO get from settings
    // UI fields
    public BufferedImage image;
    private int x;
    private int y;
    // Card model fields
    private CardColor cardColor;
    private CardNumber cardNumber;
    private boolean selected = false;
    public boolean knownColor = false;
    public boolean knownNumber = false;


    /**
     * Constructor for card with only color for empty card place.
     *
     * @param cardColor the color of the card
     */
    public Card(CardColor cardColor) {
        this.cardColor = cardColor;
        this.cardNumber = CardNumber.ZERO;
    }

    /**
     * Constructor for a card.
     *
     * @param cardColor  the color of the card
     * @param cardNumber the number of the card
     */
    public Card(CardColor cardColor, CardNumber cardNumber) {
        this.cardColor = cardColor;
        this.cardNumber = cardNumber;

        String cardFilename = cardColor.getValue() + cardNumber.getValue();
        URL imageURL = Objects.requireNonNull(classLoader.getResource("hanabi_cards/" + cardFilename + ".png"));
        this.image = loadImage(imageURL);
    }

    public CardColor getColor() {
        return cardColor;
    }

    public CardNumber getNumber() {
        return cardNumber;
    }

    public void selected() {
        this.selected = true;
        this.y -= 10;
    }

    public void reset() {
        if (this.selected) {
            this.y += 10;
            this.selected = false;
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public int compareTo(Card other) {
        Card self = this;
        int selfHash = self.getColor().ordinal() * 10 + self.getNumber().ordinal();
        int otherHash = other.getColor().ordinal() * 10 + other.getNumber().ordinal();
        return selfHash - otherHash;
    }
}
