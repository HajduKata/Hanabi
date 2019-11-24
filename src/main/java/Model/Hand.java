package Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Hand {
    public static final int NUM_OF_CARDS_IN_HAND = 5;
    public List<Card> cards;

    public static void initHand(Hand hand) {
        for (int i = 0; i < Hand.NUM_OF_CARDS_IN_HAND; i++) {
            hand.add(Deck.DECK.pop());
        }
    }

    public Hand() {
        cards = new ArrayList<>(5);
    }

    public void add(Card card) {
        cards.add(card);
    }

    public int size() {
        return cards.size();
    }

    public Iterable<Card> cards() {
        return cards;
    }

    public Iterable<Card> reversed() {
        List<Card> reversed = new ArrayList<>(cards);
        Collections.reverse(reversed);
        return reversed;
    }
}
