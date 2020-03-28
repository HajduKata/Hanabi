package controller;

import model.Card;
import model.CardColor;
import model.CardNumber;
import model.DiscardedCards;
import model.Fireworks;
import model.Hand;
import model.History;
import model.Player;
import model.Players;
import model.Tokens;

import javax.swing.JOptionPane;

import static view.PlayerPanel.drawNewCard;

public class AIController {
    private CardColor actualColor;
    private CardNumber actualNumber;
    private int numberOfPlayers;


    AIController(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    void chooseAction(Player thisPlayer) {
        String playerName = thisPlayer.getName();
        /*
        Action algorithm: A player will act using her private information with the following priority:
        1. Play the playable card with lowest index.
        2. If there are less than 5 cards in the discard pile, discard the dead card with lowest index.
        3. If there are hint tokens available, give a hint.
        4. Discard the dead card with lowest index.
        5. If a card in the player’s hand is the same as another card in any player’s hand, i.e. it is a duplicate, discard that card.
        *** ^ Ezzel nem értek egyet
        6. Discard the dispensable card with lowest index.
        7. Discard card c1.
        */

        actualColor = null;
        actualNumber = null;

        // 1. Play the playable card with lowest index.
        if (canPlayACard(thisPlayer)) {
            // canPlayACard pre-sets the actualColor and actualNumber variables
//            Card cardToBePlayed = new Card(actualColor, actualNumber);
            playACard(thisPlayer, whatCardToPlay(thisPlayer));
            JOptionPane.showMessageDialog(null, playerName + " kijátszott egy kártyát.", playerName + " köre", JOptionPane.INFORMATION_MESSAGE);
        } // 2. If there are less than 5 cards in the discard pile, discard the dead card with lowest index.
        else if (DiscardedCards.getDiscard().getNumberOfDiscardedCards() < 5 && canDiscardACard(thisPlayer)) {
            discardACard(thisPlayer, whatToDiscard(thisPlayer));
            JOptionPane.showMessageDialog(null, playerName + " eldobott egy lapot.", playerName + " köre", JOptionPane.INFORMATION_MESSAGE);
        } // 3. If there are hint tokens available, give a hint. - right now only what can be played
        else if (Tokens.getTokens().getClues() > 0) {
            int nextPlayerIndex = Players.getThePlayers().indexOf(thisPlayer) + 1;
            if (nextPlayerIndex >= numberOfPlayers) {
                nextPlayerIndex = 0;
            }
            Player nextPlayer = Players.getThePlayers().get(nextPlayerIndex);
            if (!nextPlayer.equals(thisPlayer)) {
                giveHint(nextPlayer.getHand(), nextPlayer.getName());
                JOptionPane.showMessageDialog(null, playerName + " utalást adott.", playerName + " köre", JOptionPane.INFORMATION_MESSAGE);
            }
        } // 4. Discard the dead card with lowest index.
        else if (canDiscardACard(thisPlayer)) {
            discardACard(thisPlayer, whatToDiscard(thisPlayer));
            JOptionPane.showMessageDialog(null, playerName + " eldobott egy lapot.", playerName + " köre", JOptionPane.INFORMATION_MESSAGE);
        }
        //TODO Supposed to discard the card with the lowest index, if they have no dead cards
        else {
            JOptionPane.showMessageDialog(null, playerName + " semmit se csinááát.", playerName + " köre", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private boolean canPlayACard(Hand hand) {
        for (Card card : hand.cards) {
            int colorCounter = 0;
            actualColor = null;
            int numberCounter = 0;
            actualNumber = null;

            //TODO tudja a kártya valódi számát
            if (card.knownColor) {
                actualColor = card.getColor();
                actualNumber = card.getNumber();
                if (Fireworks.getFireworks().isPlayable(new Card(actualColor, actualNumber))) {
                    return true;
                }
            } else {
                for (CardColor color : card.getAssumedColor().keySet()) {
                    if (card.getAssumedColor().get(color).equals(true)) {
                        colorCounter++;
                    }
                }
            }
            if (colorCounter == 1) {
                actualColor = card.getColor();
            }

            //TODO tudja a kártya valódi színét
            if (card.knownNumber) {
                actualColor = card.getColor();
                actualNumber = card.getNumber();
                if (Fireworks.getFireworks().isPlayable(new Card(actualColor, actualNumber))) {
                    return true;
                }
            } else {
                for (CardNumber number : card.getAssumedNumber().keySet()) {
                    if (card.getAssumedNumber().get(number).equals(true)) {
                        numberCounter++;
                    }
                }
            }
            if (numberCounter == 1) {
                actualNumber = card.getNumber();
            }

            // If only the number is known, but there wasn't any card played of that number
            if (numberCounter == 1 && Fireworks.getFireworks().noneOfThatNumberYetPlayed(actualNumber)) {
                return true;
            }

            // If both information is assumed, check if the card is playable
            if (colorCounter == 1 && numberCounter == 1) {
                return Fireworks.getFireworks().isPlayable(new Card(actualColor, actualNumber));
            }
        }
        return false;
    }

    private boolean canPlayACard(Player player) {
        for (Card card : player.getHand().cards) {
            if (Fireworks.getFireworks().isPlayable(card)) {
                return true;
            }
        }
        return false;
    }

    private Card whatCardToPlay(Player player) {
        Card cardToPlay = null;

        for (Card card : player.getHand().cards) {
            if (Fireworks.getFireworks().isPlayable(card)) {
                cardToPlay = card;
            }
        }

        return cardToPlay;
    }

    private void playACard(Player player, Card cardToPlayed) {
        // Check if the card we want to play is in the hand of the player
        for (Card card : player.getHand().cards) {
            if (card.getColor().equals(cardToPlayed.getColor()) && card.getNumber().equals(cardToPlayed.getNumber())) {
                // Play the card if its playable
                if (Fireworks.getFireworks().addFireworkCard(card)) {
                } // Else lose life and discard the card
                else {
                    Tokens.getTokens().decreaseLife();
                    DiscardedCards.getDiscard().addDiscardedCard(card);
                }
                drawNewCard(player, card);
                return;
            }
        }


    }

    private boolean canDiscardACard(Player player) {
        for (Card card : player.getHand().cards) {
            if (Fireworks.getFireworks().isDeadCard(card)) {
                return true;
            }
        }
        return false;
    }

    private Card whatToDiscard(Player player) {
        Card cardToBeDiscarded = null;
        for (Card card : player.getHand().cards) {
            if (Fireworks.getFireworks().isDeadCard(card)) {
                cardToBeDiscarded = card;
            }
        }

        return cardToBeDiscarded;
    }

    private void discardACard(Player player, Card cardToBeDiscarded) {
        // Check if the card we want to discard is in the hand of the player
        for (Card card : player.getHand().cards) {
            if (card.getColor().equals(cardToBeDiscarded.getColor()) && card.getNumber().equals(cardToBeDiscarded.getNumber())) {
                // Discard the card and gain a cluetoken
                if (DiscardedCards.getDiscard().addDiscardedCard(card)) {
                    Tokens.getTokens().increaseClues();
                    drawNewCard(player, cardToBeDiscarded);
                    return;
                }
            }
        }

    }

    private void giveHint(Hand hand, String playername) {
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
            if (actualColor != null) {
                if (card.getColor().equals(actualColor)) {
                    card.knownColor = true;
                    card.setAssumedColor(card.getColor(), true);
                } else {
                    card.setAssumedColor(card.getColor(), false);
                }
            } else if (actualNumber != null) {
                if (card.getNumber().equals(actualNumber)) {
                    card.knownNumber = true;
                    card.setAssumedNumber(card.getNumber(), true);
                } else {
                    card.setAssumedNumber(card.getNumber(), false);
                }
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

    private int symbolCounter(Hand hand, CardColor color, CardNumber number) {
        int counter = 0;
        for (Card card : hand.cards) {
            if (card.getColor().equals(color) || card.getNumber().equals(number)) {
                counter++;
            }
        }
        return counter;
    }

    private boolean areAllSymbolsPlayable(Hand hand, CardColor color, CardNumber number) {
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
