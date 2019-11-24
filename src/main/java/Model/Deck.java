package Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum Deck {

    DECK;

    private List<Card> cards;

    Deck() {
        cards = new ArrayList<>(50);
        for (CardColor cardColor : CardColor.values()) {
            for (CardNumber cardNumber : CardNumber.values()) {
                switch (cardNumber) {
                    case ONE:
                        cards.add(new Card(cardColor, cardNumber));
                        cards.add(new Card(cardColor, cardNumber));
                        cards.add(new Card(cardColor, cardNumber));
                        break;
                    case TWO:
                    case THREE:
                    case FOUR:
                        cards.add(new Card(cardColor, cardNumber));
                        cards.add(new Card(cardColor, cardNumber));
                        break;
                    case FIVE:
                        cards.add(new Card(cardColor, cardNumber));
                        break;
                }
            }
        }
        shuffle();
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card pop() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.remove(0);
    }

    public void push(Card card) {
        cards.add(card);
    }

}