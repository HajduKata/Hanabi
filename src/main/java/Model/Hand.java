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
        cards = new ArrayList<>(NUM_OF_CARDS_IN_HAND);
    }

    void add(Card card) {
        cards.add(card);
    }
}
