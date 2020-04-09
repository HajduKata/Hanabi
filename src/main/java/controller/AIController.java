package controller;

import model.Card;
import model.CardColor;
import model.CardNumber;
import model.DiscardedCards;
import model.Fireworks;
import model.HanabiCards;
import model.Hand;
import model.History;
import model.Player;
import model.Players;
import model.Tokens;

import javax.swing.JOptionPane;

import java.util.ArrayList;

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
        if (canPlayACard(thisPlayer.getHand())) {
            playACard(thisPlayer, whatCardToPlay(thisPlayer.getHand()));
            JOptionPane.showMessageDialog(null, playerName + " kijátszott egy kártyát.", playerName + " köre", JOptionPane.INFORMATION_MESSAGE);
        } // 2. If there are less than 5 cards in the discard pile, discard the dead card with lowest index.
        else if (DiscardedCards.getDiscard().getNumberOfDiscardedCards() < 5 && canDiscardADeadCard(thisPlayer.getHand()) && Tokens.getTokens().getClues() < 8) {
            discardADeadCard(thisPlayer, whatDeadCardToDiscard(thisPlayer.getHand()));
            JOptionPane.showMessageDialog(null, playerName + " eldobott egy lapot.", playerName + " köre", JOptionPane.INFORMATION_MESSAGE);
        } // 3. If there are hint tokens available, give a hint.
        else if (Tokens.getTokens().getClues() > 0) {
            Player giveHint = whichPlayerToGiveHintToPlayACard(thisPlayer);
            if (giveHint != null) {
                whatHintToGiveToPlayACard(giveHint.getHand());
                giveHint(giveHint);
            } else if (whichPlayerToGiveHintToDiscardACard(thisPlayer) != null) {
                giveHint = whichPlayerToGiveHintToDiscardACard(thisPlayer);
                whatHintToGiveToDiscardACard(giveHint.getHand());
                giveHint(giveHint);
            }
            JOptionPane.showMessageDialog(null, playerName + " utalást adott.", playerName + " köre", JOptionPane.INFORMATION_MESSAGE);
        } // 4. Discard the dead card with lowest index.
        else if (canDiscardADeadCard(thisPlayer.getHand())) {
            discardADeadCard(thisPlayer, whatDeadCardToDiscard(thisPlayer.getHand()));
            JOptionPane.showMessageDialog(null, playerName + " eldobott egy lapot.", playerName + " köre", JOptionPane.INFORMATION_MESSAGE);
        } // 7. Discard card c1.
        //TODO Supposed to discard the card with the lowest index, if they have no dead cards
        else {
            JOptionPane.showMessageDialog(null, playerName + " semmit se csinááát.", playerName + " köre", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private boolean canPlayACard(Hand hand) {
        for (Card card : hand.cards) {
            // If both Color and Number is known of the card
            if (card.knownNumber && card.knownColor) {
                if (Fireworks.getFireworks().isPlayable(card)) {
                    return true;
                }
            } // If only Number is known of the card
            else if (card.knownNumber) {
                // If there is still some of that number to be played
                // TODO giveHint nem figyel rá hogy ne mutasson meg duplikátumokat
                if (Fireworks.getFireworks().numberCanBePlayed(card.getNumber())) {
                    return true;

                    /*// How many of the assumed colors can be played
                    int counter = 0;
                    for (CardColor color : card.countAssumedColor()) {
                        if (Fireworks.getFireworks().isPlayable(new Card(color, card.getNumber()))) {
                            counter++;
                        }
                    }
                    // If all of the assumed colors can be played
                    if (counter == card.countAssumedColor().size()) {
                        return true;
                    } // Else look at the other people's cards*/
                }
            } // If only Color is known of the card
            else if (card.knownColor) {
                // If this is the only card that has knownColor of the shown color
                int counter = 0;
                for (Card counterCard : hand.cards) {
                    if (counterCard.knownColor && counterCard.getColor() == card.getColor()) {
                        counter++;
                    }
                }
                if (counter == 1) {
                    return true;
                }
            }

            // If none is known of the card
            // ? why would you do that
        }
        return false;
    }

    private Card whatCardToPlay(Hand hand) {
        Card cardToPlay = null;
        for (Card card : hand.cards) {
            // If both Color and Number is known of the card
            if (card.knownNumber && card.knownColor) {
                if (Fireworks.getFireworks().isPlayable(card)) {
                    cardToPlay = card;
                    break;
                }
            } // If only Number is known of the card
            else if (card.knownNumber) {
                // If there is still some of that number to be played
                if (Fireworks.getFireworks().numberCanBePlayed(card.getNumber())) {
                    cardToPlay = card;
                    break;
                }
            } // If only Color is known of the card
            else if (card.knownColor) {
                // If this is the only card that has knownColor of the shown color
                int counter = 0;
                for (Card counterCard : hand.cards) {
                    if (counterCard.knownColor && counterCard.getColor() == card.getColor()) {
                        counter++;
                    }
                }
                if (counter == 1) {
                    cardToPlay = card;
                    break;
                }
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

    private boolean canDiscardADeadCard(Hand hand) {
        for (Card card : hand.cards) {
            // If both Color and Number is known of the card
            if (card.knownNumber && card.knownColor) {
                // If the card has already been played before, it's a dead card
                if (Fireworks.getFireworks().isDeadCard(card)) {
                    return true;
                }
            } // If only Number is known of the card
            else if (card.knownNumber) {
                // If there is none of that number to be played
                if (!Fireworks.getFireworks().numberCanBePlayed(card.getNumber())) {
                    return true;
                }
            } // If only Color is known of the card
            else if (card.knownColor) {
                // If the fireworks of that color is already finished
                // TODO vagy már ki van dobva essetial kártya
                if (Fireworks.getFireworks().howManyOfThatColorPlayed(card.getColor()) == 5) {
                    return true;
                }
            }

            // If none is known of the card
            // ? why would you do that
        }
        return false;
    }

    private Card whatDeadCardToDiscard(Hand hand) {
        Card cardToBeDiscarded = null;
        for (Card card : hand.cards) {
            // If both Color and Number is known of the card
            if (card.knownNumber && card.knownColor) {
                // If the card has already been played before, it's a dead card
                if (Fireworks.getFireworks().isDeadCard(card)) {
                    cardToBeDiscarded = card;
                    break;
                }
            } // If only Number is known of the card
            else if (card.knownNumber) {
                // If there is none of that number to be played
                if (!Fireworks.getFireworks().numberCanBePlayed(card.getNumber())) {
                    cardToBeDiscarded = card;
                    break;
                }
            } // If only Color is known of the card
            else if (card.knownColor) {
                // If the fireworks of that color is already finished
                // TODO vagy már ki van dobva essetial kártya
                if (Fireworks.getFireworks().howManyOfThatColorPlayed(card.getColor()) == 5) {
                    cardToBeDiscarded = card;
                    break;
                }
            }

            // If none is known of the card
            // ? why would you do that
        }
        return cardToBeDiscarded;
    }

    private void discardADeadCard(Player player, Card cardToBeDiscarded) {
        // Check if the card we want to discard is in the hand of the player
        for (Card card : player.getHand().cards) {
            if (card.getColor().equals(cardToBeDiscarded.getColor()) && card.getNumber().equals(cardToBeDiscarded.getNumber())) {
                // Discard the card and gain a clue token
                if (DiscardedCards.getDiscard().addDiscardedCard(card)) {
                    Tokens.getTokens().increaseClues();
                    drawNewCard(player, cardToBeDiscarded);
                    return;
                }
            }
        }
    }

    private boolean oddsOfCardCanBePlayed(Card card) {
        int points = 0;
        if (card.knownColor || card.knownNumber) {
            points += 5;
        }
        points += 5 - Fireworks.getFireworks().howManyOfThatNumberPlayed(card.getNumber());
        points += 5 - Fireworks.getFireworks().howManyOfThatColorPlayed(card.getColor());
        // Above 60% probability of the card being playable
        return points >= 6;
    }

    /**
     * Depends on:
     * If the player has a card to play
     * if the player is the next player
     * if more players have the same playable card
     * if a player has anything shown to them before
     * If the player can discard
     * if the player has a dead card
     * if the player has a not essential card
     */
    private Player whichPlayerToGiveHintToPlayACard(Player thisPlayer) {
        int index = Players.getPlayerIndex();
        increaseAndCheckIndex(index);
        Player player = null;
        Player iteratePlayer = Players.getNextPlayer(index);
        while (!iteratePlayer.equals(thisPlayer)) {
            // If the player has at least one playable card in hand
            //TODO meg van-e már mutatva a játékosnak?
            if (howManyPlayableCardsInHand(iteratePlayer.getHand()) >= 1) {
                player = iteratePlayer;
            }
            increaseAndCheckIndex(index);
            iteratePlayer = Players.getNextPlayer(index);
        }
        /*else if (howManyPlayableCardsInHand(player.getHand()) > 1 && player != Players.getNextPlayer() && Tokens.getTokens().getClues() >= Players.numberOfPlayers) {
            playerToGiveHint = player;
            break;
        }*/
        return player;
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

    private Player whichPlayerToGiveHintToDiscardACard(Player thisPlayer) {
        int index = Players.getPlayerIndex();
        increaseAndCheckIndex(index);
        Player player = null;
        Player iteratePlayer = Players.getNextPlayer(index);
        while (!iteratePlayer.equals(thisPlayer)) {
            // If the player can discard
            if (playerCanDiscardACard(iteratePlayer.getHand())) {
                player = iteratePlayer;
            }
            increaseAndCheckIndex(index);
            iteratePlayer = Players.getNextPlayer(index);
        }
        return player;
    }

    private boolean playerCanDiscardACard(Hand hand) {
        for (Card card : hand.cards) {
            // If it's a dead card
            if (Fireworks.getFireworks().isDeadCard(card)) {
                return true;
            }
            // If it's a non-essential (duplicate) card
            int counter = 0;
            for (Card firework : Fireworks.getFireworks().getCards()) {
                if (card.getColor() == firework.getColor() && card.getNumber().isHigher(firework.getNumber())) {
                    for (Card discardedCard : DiscardedCards.getDiscard().getCards().get(card.getColor())) {
                        if (discardedCard == card) {
                            counter++;
                        }
                    }
                    // If the card hasn't been discarded yet and it's not a 5
                    if (counter == 0 && card.getNumber() != CardNumber.FIVE) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void whatHintToGiveToPlayACard(Hand hand) {
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
            } // 2/b. recommendation
            else if (card.getNumber().equals(CardNumber.FIVE)) {
                checkWhatIsShowable(hand, card);
                break;
            }
        }
    }

    private void whatHintToGiveToDiscardACard(Hand hand) {
        actualColor = null;
        actualNumber = null;
        for (Card card : hand.cards) {
            // 3. recommendation
            if (Fireworks.getFireworks().isDeadCard(card)) {
                if (Fireworks.getFireworks().howManyOfThatNumberPlayed(card.getNumber()) == CardColor.values().length) {
                    //TODO checkWhatIsShowable-hoz hasonlóan megnézni meg volt-e mutatva már
                    actualNumber = card.getNumber();
                } else if (Fireworks.getFireworks().howManyOfThatColorPlayed(card.getColor()) == 5) {
                    actualColor = card.getColor();
                }
            } // 4. recommendation
        }
    }

    private void giveHint(Player player) {
        /**
         * The recommendation for a hand will be determined with following priority:
         * 1. Recommend that the playable card of rank 5 with lowest index be played.
         * 2. Recommend that the playable card with lowest rank be played. If there is a tie for lowest rank, recommend the one with lowest index.
         * 2/b. Recommend 5 to be safe and not discard it accidentally.
         * 3. Recommend that the dead card with lowest index be discarded.
         * 4. Recommend that the card with highest rank that is not indispensable be discarded. If there is a tie, recommend the one with lowest index.
         * 5. Recommend that c1 be discarded.
         */
        Hand hand = player.getHand();

        // Give a hint to the player
        for (Card card : hand.cards) {
            if (actualColor != null) {
                // If the card is the same color as the clue given
                if (card.getColor().equals(actualColor)) {
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
            } else if (actualNumber != null) {
                // If the card is the same number as the clue given
                if (card.getNumber().equals(actualNumber)) {
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
        if (actualNumber != null) {
            History.getHistory().addString(player.getName(), "", History.getHistory().getHistoryNumber(actualNumber));
            Tokens.getTokens().decreaseClues();
        } else if (actualColor != null) {
            History.getHistory().addString(player.getName(), History.getHistory().getHistoryColor(actualColor), "");
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

    void updatePossibilityTable(Player thisPlayer) {
        // Get all the visible cards
        ArrayList<Card> visibleCards = getAllVisibleCards(thisPlayer);
        // Iterate through the hand of the player
        for (Card card : thisPlayer.getHand().cards) {
            for (CardColor color : CardColor.values()) {
                for (CardNumber number : CardNumber.values()) {
                    int howManyVisible = howManyVisible(color, number, visibleCards);
                    int maxNumber = HanabiCards.DECK.howManyOfThatNumberInDeck(number);
                    card.possibilityTable[color.ordinal()][number.ordinal()] = maxNumber - howManyVisible;
                }
            }
        }
    }

    private int howManyVisible(CardColor color, CardNumber number, ArrayList<Card> visibleCards) {
        int counter = 0;
        for (Card card : visibleCards) {
            if (card.getColor() == color && card.getNumber() == number) {
                counter++;
            }
        }
        return counter;
    }

    private ArrayList<Card> getAllVisibleCards(Player thisPlayer) {
        ArrayList<Card> visibleCards = new ArrayList<>();

        // Iterate through the other players and add to the list all of their cards
        for (Player player : Players.getThePlayers()) {
            if (player != thisPlayer) {
                visibleCards.addAll(player.getHand().cards);
            }
        }

        // Iterate through the played cards and add to the list all the cards that have been played
        for (Card playedCard : Fireworks.getFireworks().getCards()) {
            switch (playedCard.getNumber()) {
                case FIVE:
                    visibleCards.add(new Card(playedCard.getColor(), CardNumber.FIVE));
                case FOUR:
                    visibleCards.add(new Card(playedCard.getColor(), CardNumber.FOUR));
                case THREE:
                    visibleCards.add(new Card(playedCard.getColor(), CardNumber.THREE));
                case TWO:
                    visibleCards.add(new Card(playedCard.getColor(), CardNumber.TWO));
                case ONE:
                    visibleCards.add(new Card(playedCard.getColor(), CardNumber.ONE));
                    break;
            }
        }

        // Iterate through the discard pile and add to the list all the cards that have been discarded
        for (CardColor color : DiscardedCards.getDiscard().getCards().keySet()) {
            visibleCards.addAll(DiscardedCards.getDiscard().getCards().get(color));
        }

        return visibleCards;
    }

    private int increaseAndCheckIndex(int index) {
        if (index >= numberOfPlayers) {
            index = 0;
        }
        return index;
    }
}
