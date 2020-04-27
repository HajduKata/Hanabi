package model;

import java.util.LinkedList;
import java.util.List;

import static model.Card.CARD_OFFSET_X;
import static view.HanabiUtilities.CARD_START_POS_X;
import static view.HanabiUtilities.CARD_START_POS_Y;

public class Hand {
    public static int numberOfCardsInHand;
    public List<Card> cards;

    Hand() {
        cards = new LinkedList<>();
        if (Players.numberOfPlayers < 4) {
            numberOfCardsInHand = 5;
        } else {
            numberOfCardsInHand = 4;
        }
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

    public static int getNumberOfCardsInHand() {
        return numberOfCardsInHand;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for (Card card : cards) {
            string.append(card.getColor().name()).append(" ").append(card.getNumber().name()).append("\n");
        }
        return string.toString();
    }
}
