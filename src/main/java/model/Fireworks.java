package model;

/**
 * Singleton class that holds the last card of each color pile.
 */
public class Fireworks {
    private static final int MAX_NUMBER_OF_COLORS = CardColor.values().length;

    private static Fireworks instance;
    /* Holds the last card of each firework color */
    private static Card[] fireworks = new Card[MAX_NUMBER_OF_COLORS];

    private int numberOfCardsPlayed;

    /**
     * Private empty constructor (singleton) of fireworks.
     */
    private Fireworks() {
        numberOfCardsPlayed = 0;
        for (CardColor color : CardColor.values()) {
            fireworks[color.ordinal()] = new Card(color);
        }
    }

    /**
     * Get the fireworks instance.
     *
     * @return the fireworks instance
     */
    public static Fireworks getFireworks() {
        if (instance == null) {
            instance = new Fireworks();
        }
        return instance;
    }


    /**
     * Get the array of the last card of the firework colors.
     *
     * @return the array of the last card of the firework colors
     */
    public Card[] getCards() {
        getFireworks();
        return fireworks;
    }

    /**
     * Check whether the given card can be added to a firework and add it (the array holds the last card)
     *
     * @param card the card to add
     * @return true if it is a legal play (can be added)
     */
    public boolean addFireworkCard(Card card) {
        assert card != null;
        int index = card.getColor().ordinal();
        boolean success = false;
        if (isPlayable(card)) {
            fireworks[index] = card;
            success = true;
            if (card.getNumber().equals(CardNumber.FIVE)) {
                Tokens.getTokens().increaseClues();
            }
        }
        numberOfCardsPlayed++;
        return success;
    }

    public boolean allFireworksFinished() {
        for (Card card : fireworks) {
            if (!card.getNumber().equals(CardNumber.FIVE)) {
                return false;
            }
        }
        return true;
    }

    public boolean isPlayable(Card card) {
        int index = card.getColor().ordinal();
        return fireworks[index].getNumber().next() == card.getNumber();
    }

    public int howManyOfThatNumberPlayed(CardNumber number) {
        int counter = 0;
        for (Card card : fireworks) {
            if (number.isLowerOrEqual(card.getNumber())) {
                counter++;
            }
        }
        return counter;
    }

    public int howManyOfThatColorPlayed(CardColor color) {
        int index = color.ordinal();
        return fireworks[index].getNumber().ordinal();
    }

    public boolean numberCanBePlayed(CardNumber number) {
        for (Card card : fireworks) {
            if(card.getNumber().equals(CardNumber.FIVE)) {
                continue;
            }
            if(card.getNumber().next().equals(number)) {
                return true;
            }
        }
        return false;
    }

    public boolean isDeadCard(Card card) {
        return card.getNumber().isLowerOrEqual(fireworks[card.getColor().ordinal()].getNumber());
    }

    public int getNumberOfCardsPlayed() {
        return numberOfCardsPlayed;
    }

    public static void clearInstance() {
        instance = null;
    }
}
