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
import model.Symbol;
import model.Tokens;

import static view.PlayerPanel.drawNewCard;

public class AIPlayer {
    private int numberOfPlayers;

    AIPlayer(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    void chooseAction(Player thisPlayer) {
        /*
         * Action algorithm: A player will act using her private information with the following priority:
         * 1. Play the playable card with lowest index.
         * 2. If there are hint tokens available, give a hint.
         * 3. Discard the dead card with lowest index.
         * 4. Discard the dispensable card with lowest index.
         * 5. Discard card c1.
         */

        String actionMessage = "";
        Symbol hintSymbol = new Symbol(null, null);
        Player playerToGiveHintTo = whichPlayerToGiveHintTo(thisPlayer);

        // 1. Play the playable card with lowest index.
        Card cardToPlay = whatCardToPlay(thisPlayer.getHand());
        if (cardToPlay != null) {
            playACard(thisPlayer, cardToPlay);
            actionMessage = thisPlayer.getName() + " kijátszott egy kártyát.";
        } // 2. If there are hint tokens available && there is a player with playable cards in hand && we can give a hint that only shows the playable cards, give a hint.
        else if (Tokens.getTokens().getClues() > 0 && playerToGiveHintTo != null && whatHintToGive(whichPlayerToGiveHintTo(thisPlayer), hintSymbol)) {
            giveHint(thisPlayer, playerToGiveHintTo, hintSymbol);
            actionMessage = thisPlayer.getName() + " utalást adott.";
        } // 3. Discard the dead card with lowest index.
        /*
        else if (canDiscardADeadCard(thisPlayer.getHand())) {
            discardACard(thisPlayer, whatDeadCardToDiscard(thisPlayer.getHand()));
            actionMessage = thisPlayer.getName() + " eldobott egy lapot.";
        }
        */
        // 5. Discard card c1.
        else {
            discardACard(thisPlayer, thisPlayer.getHand().cards.get(0));
            actionMessage = thisPlayer.getName() + " eldobta a legrégebbi lapját.";
        }


        //JOptionPane.showMessageDialog(null, actionMessage, thisPlayer.getName() + " köre", JOptionPane.INFORMATION_MESSAGE);
    }

    private Card whatCardToPlay(Hand hand) {
        Card cardToPlay = null;
        for (Card card : hand.cards) {
            if (card.knownNumber || card.knownColor) {
                cardToPlay = card;
                break;
            }
        }
        return cardToPlay;
    }

    private void playACard(Player player, Card cardToPlayed) {
        // Check if the card we want to play is in the hand of the player
        for (Card card : player.getHand().cards) {
            if (card.equals(cardToPlayed)) {
                // Play the card if its playable
                if (Fireworks.getFireworks().isPlayable(cardToPlayed)) {
                    Fireworks.getFireworks().addFireworkCard(card);
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

    private void discardACard(Player player, Card cardToBeDiscarded) {
        // Check if the card we want to discard is in the hand of the player
        for (Card card : player.getHand().cards) {
            if (card.equals(cardToBeDiscarded)) {
                // Discard the card and gain a clue token
                if (DiscardedCards.getDiscard().addDiscardedCard(card)) {
                    Tokens.getTokens().increaseClues();
                    drawNewCard(player, cardToBeDiscarded);
                    return;
                }
            }
        }
    }

    private void giveHint(Player thisPlayer, Player playerToGiveHintTo, Symbol hintSymbol) {
        // Give a hint to the player
        for (Card card : playerToGiveHintTo.getHand().cards) {
            if (hintSymbol.hintColor != null) {
                // If the card is the same color as the clue given
                if (card.getColor().equals(hintSymbol.hintColor)) {
                    card.knownColor = true;
                    card.setAssumedColor(card.getColor(), true);
                    // The possibility table of the card sets all other colors "false", 0
                    for (CardColor color : CardColor.values()) {
                        if (color != card.getColor()) {
                            for (CardNumber number : CardNumber.values()) {
                                card.possibilityTable[color.ordinal()][number.ordinal()] = 0;
                            }
                        }
                    }
                } // If the card is not the same color as the clue given
                else {
                    card.setAssumedColor(card.getColor(), false);
                    // The possibility table of the card sets all of that color "false", 0
                    for (CardColor color : CardColor.values()) {
                        if (color == card.getColor()) {
                            for (CardNumber number : CardNumber.values()) {
                                card.possibilityTable[color.ordinal()][number.ordinal()] = 0;
                            }
                        }
                    }
                }
            } else if (hintSymbol.hintNumber != null) {
                // If the card is the same number as the clue given
                if (card.getNumber().equals(hintSymbol.hintNumber)) {
                    card.knownNumber = true;
                    card.setAssumedNumber(card.getNumber(), true);
                    // The possibility table of the card sets all other numbers "false", 0
                    for (CardNumber number : CardNumber.values()) {
                        if (number != card.getNumber()) {
                            for (CardColor color : CardColor.values()) {
                                card.possibilityTable[color.ordinal()][number.ordinal()] = 0;
                            }
                        }
                    }
                } // If the card is not the same number as the clue given
                else {
                    card.setAssumedNumber(card.getNumber(), false);
                    // The possibility table of the card sets all of that number "false", 0
                    for (CardNumber number : CardNumber.values()) {
                        if (number == card.getNumber()) {
                            for (CardColor color : CardColor.values()) {
                                card.possibilityTable[color.ordinal()][number.ordinal()] = 0;
                            }
                        }
                    }
                }
            }
        }
        if (hintSymbol.hintNumber != null) {
            History.getHistory().addString(playerToGiveHintTo.getName(), "", History.getHistory().getHistoryNumber(hintSymbol.hintNumber));
        } else if (hintSymbol.hintColor != null) {
            History.getHistory().addString(playerToGiveHintTo.getName(), History.getHistory().getHistoryColor(hintSymbol.hintColor), "");
        }
        Tokens.getTokens().decreaseClues();
    }

    private Player whichPlayerToGiveHintTo(Player thisPlayer) {
        int index = increaseAndCheckIndex(Players.getPlayerIndex());
        Player playerToGiveHint = null;
        Player iteratePlayer = Players.getIndexPlayer(index);
        while (!iteratePlayer.equals(thisPlayer)) {
            // If the player has playable cards in hand
            if (howManyPlayableCardsInHand(iteratePlayer.getHand()) > 0) {

                /*
                // If the player already has all cards shown to him that can be played, skip to the next player
                if (playerAlreadyHasClues(iteratePlayer.getHand())) {
                    index = increaseAndCheckIndex(index);
                    iteratePlayer = Players.getIndexPlayer(index);
                    continue;
                }
                */

                playerToGiveHint = iteratePlayer;
            }
            index = increaseAndCheckIndex(index);
            iteratePlayer = Players.getIndexPlayer(index);
        }
        return playerToGiveHint;
    }

    private int howManyPlayableCardsInHand(Hand hand) {
        int counter = 0;
        for (Card card : hand.cards) {
            if (Fireworks.getFireworks().isPlayable(card)) {
                counter++;
            }
        }
        return counter;
    }

    private boolean playerAlreadyHasClues(Hand hand) {
        int playableCounter = 0;
        int shownCounter = 0;
        for (Card card : hand.cards) {
            if (Fireworks.getFireworks().isPlayable(card)) {
                playableCounter++;
            }
            if (card.knownColor || card.knownNumber) {
                shownCounter++;
            }
        }
        return playableCounter == shownCounter;
    }

    private int increaseAndCheckIndex(int index) {
        index++;
        if (index >= numberOfPlayers) {
            index = 0;
        }
        return index;
    }

    private boolean whatHintToGive(Player playerToGiveHintTo, Symbol hintSymbol) {
        /*
         * The recommendation for a hand will be determined with following priority:
         * 1. Recommend that the playable card of rank 5 with lowest index be played.
         * 2. Recommend that the playable card with lowest rank be played. If there is a tie for lowest rank, recommend the one with lowest index.
         * 3. Recommend that the dead card with lowest index be discarded.
         * 3/b. Recommend 5 to be safe and not discard it accidentally.
         * 4. Recommend that the card with highest rank that is not indispensable be discarded. If there is a tie, recommend the one with lowest index.
         * 5. Recommend that c1 be discarded.
         */

        for (Card card : playerToGiveHintTo.getHand().cards) {
            // 1. recommendation
            if (card.getNumber().equals(CardNumber.FIVE) && Fireworks.getFireworks().isPlayable(card)) {
                if (checkWhatIsShowable(playerToGiveHintTo.getHand(), card, hintSymbol)) {
                    return true;
                }
            } // 2. recommendation
            else if (card.getNumber().equals(CardNumber.ONE) && Fireworks.getFireworks().isPlayable(card)) {
                if (checkWhatIsShowable(playerToGiveHintTo.getHand(), card, hintSymbol)) {
                    return true;
                }
            } else if (card.getNumber().equals(CardNumber.TWO) && Fireworks.getFireworks().isPlayable(card)) {
                if (checkWhatIsShowable(playerToGiveHintTo.getHand(), card, hintSymbol)) {
                    return true;
                }
            } else if (card.getNumber().equals(CardNumber.THREE) && Fireworks.getFireworks().isPlayable(card)) {
                if (checkWhatIsShowable(playerToGiveHintTo.getHand(), card, hintSymbol)) {
                    return true;
                }
            } else if (card.getNumber().equals(CardNumber.FOUR) && Fireworks.getFireworks().isPlayable(card)) {
                if (checkWhatIsShowable(playerToGiveHintTo.getHand(), card, hintSymbol)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkWhatIsShowable(Hand hand, Card card, Symbol hintSymbol) {
        // If there is only one of the number to be showed
        if (symbolCounter(hand, null, card.getNumber()) == 1 && !card.knownNumber) {
            hintSymbol.hintNumber = card.getNumber();
            return true;
        } else {
            // If all cards with that number can be played
            if (areAllSymbolsPlayable(hand, null, card.getNumber())) {
                for (Card actualCard : hand.cards) {
                    // If there is at least one, that hasn't been shown yet
                    if (card.getNumber().equals(actualCard.getNumber()) && !actualCard.knownNumber) {
                        hintSymbol.hintNumber = card.getNumber();
                        return true;
                    }
                }
            } // If the card is the only one of that color
            else if (symbolCounter(hand, card.getColor(), null) == 1) {
                hintSymbol.hintColor = card.getColor();
                return true;
            }
        }
        return false;
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

}
