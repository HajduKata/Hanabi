package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The Hanabi card deck
 */
public enum HanabiCards {

    DECK;

    private static List<Card> cards;

    HanabiCards() {
        initDeck();
        shuffle();
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card pop() {
        if (endOfDeck()) {
            return new Card(CardColor.RED);
        }
        return cards.remove(0);
    }

    public boolean endOfDeck() {
        return cards.isEmpty();
    }

    public static void initDeck() {
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
    }

    public int howManyOfThatNumberInDeck(CardNumber number) {
        switch (number) {
            case ONE:
                return 3;
            case TWO:
            case THREE:
            case FOUR:
                return 2;
            case FIVE:
                return 1;
        }
        return 0;
    }

    public static List<Card> getCards() {
        return cards;
    }
}