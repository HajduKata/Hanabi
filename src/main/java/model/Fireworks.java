package model;

/**
 * Singleton class that holds the last card of each color pile.
 */
public class Fireworks {
    public static final int MAX_NUMBER_OF_COLORS = CardColor.values().length;

    private static Fireworks instance;
    /* Holds the last card of each firework color */
    private static Card[] fireworks = new Card[MAX_NUMBER_OF_COLORS];

    /**
     * Private empty constructor (singleton) of fireworks.
     */
    private Fireworks() {
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
     * Check wheter the given card can be added to a firework and add it (the array holds the last card)
     *
     * @param card the card to add
     * @return true if it is a legal play (can be added)
     */
    public boolean addCard(Card card) {
        assert card != null;
        int index = card.getColor().ordinal();

        if (fireworks[index] == null && card.getNumber() == CardNumber.ONE) {
            fireworks[index] = card;
            return true;
        } else {
            if (fireworks[index] != null && fireworks[index].getNumber().next() == card.getNumber()) {
                fireworks[index + 1] = card;
                return true;
            }
        }
        return false;
    }
}
