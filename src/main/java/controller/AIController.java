package controller;

import model.Card;
import model.CardColor;
import model.CardNumber;
import model.Fireworks;
import model.Hand;
import model.History;
import model.Player;
import model.Players;
import model.Tokens;

public class AIController {
    //    Players players;
    private CardColor actualColor;
    private CardNumber actualNumber;
    private int index = 0;

    public AIController() {
//        this.players = players;
    }

    public void chooseAction() {
        /*
        Action algorithm A player will act using her private information with the following priority:
        1. Play the playable card with lowest index.
        2. If there are less than 5 cards in the discard pile, discard the dead card with lowest index.
        3. If there are hint tokens available, give a hint.
        4. Discard the dead card with lowest index.
        5. If a card in the player’s hand is the same as another card in any player’s hand, i.e. it is a duplicate, discard that card.
        6. Discard the dispensable card with lowest index.
        7. Discard card c1.
        */
        for (Player player : Players.getThePlayers()) {
            if (/*player.isHumanPlayer() &&*/ Tokens.getTokens().getClues() > 0 && Players.getThePlayers().get(index).equals(player)) {
                giveHint(player.getHand(), player.getName());
            }
        }
        index++;
        if(index>=Players.numberOfPlayers) {
            index = 0;
        }

    }

    public void giveHint(Hand hand, String playername) {
        /*
        The recommendation for a hand will be determined with following priority:
        1. Recommend that the playable card of rank 5 with lowest index be played.
        2. Recommend that the playable card with lowest rank be played. If there is a tie for lowest rank, recommend the one with lowest index.
        3. Recommend that the dead card with lowest index be discarded.
        4. Recommend that the card with highest rank that is not indispensable be discarded. If there is a tie, recommend the one with lowest index.
        5. Recommend that c1 be discarded.
        */
        actualColor = null;
        actualNumber = null;
        for (Card card : hand.cards) {
            // 1. recommendation
            if (card.getNumber().equals(CardNumber.FIVE) && Fireworks.getFireworks().isPlayable(card)) {
                checkWhatIsShowable(hand, card);
                break;
            } // 2. recommendation
            else if (card.getNumber().equals(CardNumber.ONE) && Fireworks.getFireworks().isPlayable(card)) {
                checkWhatIsShowable(hand, card);
                break;
            } else if (card.getNumber().equals(CardNumber.TWO) && Fireworks.getFireworks().isPlayable(card)) {
                checkWhatIsShowable(hand, card);
                break;
            } else if (card.getNumber().equals(CardNumber.THREE) && Fireworks.getFireworks().isPlayable(card)) {
                checkWhatIsShowable(hand, card);
                break;
            } else if (card.getNumber().equals(CardNumber.FOUR) && Fireworks.getFireworks().isPlayable(card)) {
                checkWhatIsShowable(hand, card);
                break;
            } // 3. recommendation

        }

        // Give a hint to the player
        for (Card card : hand.cards) {
            if (card.getNumber().equals(actualNumber)) {
                card.knownNumber = true;
            } else if (card.getColor().equals(actualColor)) {
                card.knownColor = true;
            }
        }
        if (actualNumber != null) {
            History.getHistory().addString(playername, "", History.getHistory().getHistoryNumber(actualNumber));
            Tokens.getTokens().decreaseClues();
        } else if (actualColor != null) {
            History.getHistory().addString(playername, History.getHistory().getHistoryColor(actualColor), "");
            Tokens.getTokens().decreaseClues();
        }
    }

    private void checkWhatIsShowable(Hand hand, Card card) {
        // If there is only one of the number to be showed
        if (symbolCounter(hand, null, card.getNumber()) == 1 && !checkIfNumberHasBeenShown(card)) {
            actualNumber = card.getNumber();
        } else {
            // If all cards with that number can be played
            if (areAllSymbolsPlayable(hand, null, card.getNumber())) {
                for (Card actualCard : hand.cards) {
                    // If there is at least one, that hasn't been shown yet
                    if (card.getNumber().equals(actualCard.getNumber()) && !checkIfNumberHasBeenShown(actualCard)) {
                        actualNumber = card.getNumber();
                        break;
                    }
                }
            } // If the card is the only one of that color
            else if (symbolCounter(hand, card.getColor(), null) == 1) {
                actualColor = card.getColor();
            } // If all cards with that color can be played
            else if (areAllSymbolsPlayable(hand, card.getColor(), null)) {
                for (Card actualCard : hand.cards) {
                    // If there is at least one, that hasn't been shown yet
                    if (card.getColor().equals(actualCard.getColor()) && !checkIfColorHasBeenShown(actualCard)) {
                        actualColor = card.getColor();
                        break;
                    }
                }
            }
        }
    }

    public int symbolCounter(Hand hand, CardColor color, CardNumber number) {
        int counter = 0;
        for (Card card : hand.cards) {
            if (card.getColor().equals(color) || card.getNumber().equals(number)) {
                counter++;
            }
        }
        return counter;
    }

    public boolean areAllSymbolsPlayable(Hand hand, CardColor color, CardNumber number) {
        for (Card card : hand.cards) {
            if ((card.getColor().equals(color) || card.getNumber().equals(number))
                    && !Fireworks.getFireworks().isPlayable(card)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkIfColorHasBeenShown(Card card) {
        return card.knownColor;
    }

    private boolean checkIfNumberHasBeenShown(Card card) {
        return card.knownNumber;
    }

}
