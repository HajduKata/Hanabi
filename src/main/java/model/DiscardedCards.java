package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Singleton class that holds the last card of each color pile.
 */
public class DiscardedCards {
    private static final int MAX_NUMBER_OF_NUMBERS = 10;

    private static DiscardedCards instance;
    /* Holds all the cards that have been discarded */
    private SortedMap<CardColor, List<Card>> discardedCards;

    /**
     * Private empty constructor (singleton) of discard.
     */
     private DiscardedCards() {
        discardedCards = new TreeMap<>();
        for (CardColor color : CardColor.values()) {
            Card empyCard = new Card(color);
            List emptyList = new ArrayList<>(MAX_NUMBER_OF_NUMBERS);
            emptyList.add(empyCard);
            discardedCards.put(color, emptyList);
        }
    }

    /**
     * Get the discard instance.
     *
     * @return the discard instance
     */
    public static DiscardedCards getDiscard() {
        if (instance == null) {
            instance = new DiscardedCards();
        }
        return instance;
    }


    /**
     * Get the array of the last card of the firework colors.
     *
     * @return the array of the last card of the firework colors
     */
    public SortedMap<CardColor, List<Card>> getCards() {
        getDiscard();
        return discardedCards;
    }

    /**
     * Check whether the given card can be added to the discard pile and add it
     *
     * @param card the card to add
     * @return true if it is a legal play (can be added)
     */
    public boolean addDiscardedCard(Card card) {
        assert card != null;
        Card emptyCard = new Card(card.getColor());

        List<Card> actualColorList = discardedCards.get(card.getColor());
        if(actualColorList.size() >= MAX_NUMBER_OF_NUMBERS) {
            return false;
        }
        if (discardedCards.get(card.getColor()).get(0).equals(emptyCard)) {
            actualColorList.clear();
        }
        actualColorList.add(card);
        Collections.sort(actualColorList);
        discardedCards.replace(card.getColor(), actualColorList);
        return true;
    }

    public static void clearInstance() {
        instance = null;
    }
}
