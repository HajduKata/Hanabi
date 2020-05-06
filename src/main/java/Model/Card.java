package model;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

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
    public static final int CARD_OFFSET_Y = 20;

    public static int numberOfColors = 5; // TODO get from settings
    // UI fields
    public BufferedImage image;
    private int x;
    private int y;
    // Card model fields
    private CardColor cardColor;
    private CardNumber cardNumber;
    private boolean selected;
    public boolean knownColor;
    public boolean knownNumber;
    public boolean discardable;
    public boolean nonPlayable;
    private SortedMap<CardColor, Boolean> assumedColor;
    private SortedMap<CardNumber, Boolean> assumedNumber;
    public int[][] possibilityTable;

    /**
     * Constructor for card with only color for empty card place.
     *
     * @param cardColor the color of the card
     */
    public Card(CardColor cardColor) {
        this.cardColor = cardColor;
        this.cardNumber = CardNumber.ZERO;
        initCard();
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
        image = loadImage(imageURL);

        initCard();
    }

    private void initCard() {
        knownColor = false;
        knownNumber = false;
        selected = false;
        discardable = false;
        nonPlayable = false;

        assumedColor = new TreeMap<>();
        for (CardColor color : CardColor.values()) {
            assumedColor.put(color, true);
        }
        assumedNumber = new TreeMap<>();
        for (CardNumber number : CardNumber.values()) {
            assumedNumber.put(number, true);
        }

        possibilityTable = new int[CardColor.values().length][CardNumber.values().length];
        for(CardColor color : CardColor.values()) {
            for (CardNumber number : CardNumber.values()) {
                switch (cardNumber) {
                    case ZERO:
                        possibilityTable[color.ordinal()][number.ordinal()] = 0;
                    case ONE:
                        possibilityTable[color.ordinal()][number.ordinal()] = 3;
                        break;
                    case TWO:
                    case THREE:
                    case FOUR:
                        possibilityTable[color.ordinal()][number.ordinal()] = 2;
                        break;
                    case FIVE:
                        possibilityTable[color.ordinal()][number.ordinal()] = 1;
                        break;
                }
            }
        }
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

    public ArrayList<CardColor> countAssumedColor() {
        ArrayList<CardColor> listOfColors = new ArrayList<>();
        for (CardColor color : this.getAssumedColor().keySet()) {
            if (this.getAssumedColor().get(color).equals(true)) {
                listOfColors.add(color);
            }
        }
        return listOfColors;
    }

    public ArrayList<CardNumber> countAssumedNumber() {
        ArrayList<CardNumber> listOfNumbers = new ArrayList<>();
        for (CardNumber number : this.getAssumedNumber().keySet()) {
            if (this.getAssumedColor().get(number).equals(true)) {
                listOfNumbers.add(number);
            }
        }
        return listOfNumbers;
    }

    public int getX() {
        return x;
    }

    void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    void setY(int y) {
        this.y = y;
    }

    public SortedMap<CardColor, Boolean> getAssumedColor() {
        return assumedColor;
    }

    public void setAssumedColor(CardColor color, boolean assumption) {
        assumedColor.put(color, assumption);
    }

    public SortedMap<CardNumber, Boolean> getAssumedNumber() {
        return assumedNumber;
    }

    public void setAssumedNumber(CardNumber number, boolean assumption) {
        assumedNumber.put(number, assumption);
    }

    @Override
    public int compareTo(Card other) {
        Card self = this;
        int selfHash = self.getColor().ordinal() * 10 + self.getNumber().ordinal();
        int otherHash = other.getColor().ordinal() * 10 + other.getNumber().ordinal();
        return selfHash - otherHash;
    }

    @Override
    public boolean equals(Object card) {
        if (!(card instanceof Card)) {
            return false;
        }
        Card thisCard = this;
        Card otherCard = (Card) card;
        return (thisCard.getColor().equals(otherCard.getColor()) && thisCard.getNumber().equals(otherCard.getNumber()));
    }
}

