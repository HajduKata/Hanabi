package model;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    static final int NUM_OF_CARDS_IN_HAND;
    public List<Card> cards;

    static {
        if (Players.numberOfPlayers < 4) {
            NUM_OF_CARDS_IN_HAND = 5;
        } else {
            NUM_OF_CARDS_IN_HAND = 4;
        }
    }

    Hand() {
        cards = new ArrayList<>(Players.MAX_NUMBER_OF_PLAYERS);
    }

    void add(Card card) {
        cards.add(card);
    }
}
