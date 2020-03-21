package model;

import java.util.LinkedList;
import java.util.List;

import static model.Card.CARD_OFFSET_X;
import static view.HanabiUtilities.CARD_START_POS_X;
import static view.HanabiUtilities.CARD_START_POS_Y;

public class Hand {
    public static final int NUM_OF_CARDS_IN_HAND;
    public List<Card> cards;

    static {
        if (Players.numberOfPlayers < 4) {
            NUM_OF_CARDS_IN_HAND = 5;
        } else {
            NUM_OF_CARDS_IN_HAND = 4;
        }
    }

    Hand() {
        cards = new LinkedList<>();
    }

    public void add(Card card) {
        cards.add(card);
        reset();
    }

    public void remove(Card card) {
        cards.remove(card);
        reset();
    }

    void reset() {
        int index = 0;
        for (Card card : cards) {
            card.setX(CARD_START_POS_X + index * CARD_OFFSET_X);
            card.setY(CARD_START_POS_Y);
            index++;
        }
    }
}
