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

import java.util.ArrayList;

import static view.PlayerPanel.drawNewCard;

public class AIController {
    private CardColor hintColor;
    private CardNumber hintNumber;
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

        hintColor = null;
        hintNumber = null;

        // 1. Play the playable card with lowest index.
        if (canPlayACard(thisPlayer.getHand())) {
            playACard(thisPlayer, whatCardToPlay(thisPlayer.getHand()));
//            JOptionPane.showMessageDialog(null, playerName + " kijátszott egy kártyát.", playerName + " köre", JOptionPane.INFORMATION_MESSAGE);
        }
        /*// 2. If there are less than 5 cards in the discard pile, discard the dead card with lowest index.
        else if (DiscardedCards.getDiscard().getNumberOfDiscardedCards() < 5 && canDiscardADeadCard(thisPlayer.getHand()) && Tokens.getTokens().getClues() < 8) {
            discardACard(thisPlayer, whatDeadCardToDiscard(thisPlayer.getHand()));
            JOptionPane.showMessageDialog(null, playerName + " eldobott egy lapot.", playerName + " köre", JOptionPane.INFORMATION_MESSAGE);
        }*/
        // 3. If there are hint tokens available, give a hint.
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
//            JOptionPane.showMessageDialog(null, playerName + " utalást adott.", playerName + " köre", JOptionPane.INFORMATION_MESSAGE);
        } // 4. Discard the dead card with lowest index.
        else if (canDiscardADeadCard(thisPlayer.getHand())) {
            discardACard(thisPlayer, whatDeadCardToDiscard(thisPlayer.getHand()));
//            JOptionPane.showMessageDialog(null, playerName + " eldobott egy lapot.", playerName + " köre", JOptionPane.INFORMATION_MESSAGE);
        } // 7. Discard card c1.
        else {
            discardACard(thisPlayer, thisPlayer.getHand().cards.get(0));
//            JOptionPane.showMessageDialog(null, playerName + " eldobta a legrégebbi lapját.", playerName + " köre", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private boolean canPlayACard(Hand hand) {
        // If both Color and Number is known of the card
        for (Card card : hand.cards) {
            if (card.knownNumber && card.knownColor) {
                if (Fireworks.getFireworks().isPlayable(card)) {
                    return true;
                }
            }
        }
        // If only Number is known of the card
        for (Card card : hand.cards) {
            if (card.knownNumber) {
                // If there is still some of that number to be played
                // TODO giveHint nem figyel rá hogy ne mutasson meg duplikátumokat
                /*
                if (Fireworks.getFireworks().numberCanBePlayed(card.getNumber())) {
                    int playableCounter = 0;
                    for (CardColor color : CardColor.values()) {
                        if(Fireworks.getFireworks().isPlayable(new Card(color, card.getNumber())) && card.possibilityTable[color.ordinal()][card.getNumber().ordinal()] > 0) {
//                            HanabiCards.DECK.howManyOfThatNumberInDeck(card.getNumber());
                            playableCounter++;
                        }
                    }
                    if(playableCounter>0) {
                        return true;
                    }

                    // How many of the assumed colors can be played
                    int counter = 0;
                    for (CardColor color : card.countAssumedColor()) {
                        if (Fireworks.getFireworks().isPlayable(new Card(color, card.getNumber()))) {
                            counter++;
                        }
                    }
                    // If all of the assumed colors can be played
                    if (counter == card.countAssumedColor().size()) {
                        return true;
                    } // Else look at the other people's cards
                }
                */
            }
        }
        // If only Color is known of the card
        for (Card card : hand.cards) {
            if (card.knownColor) {
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
        // If both Color and Number is known of the card
        for (Card card : hand.cards) {
            if (card.knownNumber && card.knownColor) {
                if (Fireworks.getFireworks().isPlayable(card)) {
                    return card;
                }
            }
        }
        // If only Number is known of the card
        for (Card card : hand.cards) {
            if (card.knownNumber) {
                // If there is still some of that number to be played
                /*if (Fireworks.getFireworks().numberCanBePlayed(card.getNumber())) {
                    cardToPlay = card;
                    break;
                }*/
            }
        }
        // If only Color is known of the card
        for (Card card : hand.cards) {
            if (card.knownColor) {
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
                /*if (!Fireworks.getFireworks().numberCanBePlayed(card.getNumber())) {
                    return true;
                }*/
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
                /*if (!Fireworks.getFireworks().numberCanBePlayed(card.getNumber())) {
                    cardToBeDiscarded = card;
                    break;
                }*/
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

    private void discardACard(Player player, Card cardToBeDiscarded) {
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
        int index = increaseAndCheckIndex(Players.getPlayerIndex());
        Player playerToGiveHint = null;
        Player iteratePlayer = Players.getIndexPlayer(index);
        int maxPlayable = 0;
        while (!iteratePlayer.equals(thisPlayer)) {
            int iteratePlayable = howManyPlayableCardsInHand(iteratePlayer.getHand());
            // If the player has the most playable cards in hand
            if (iteratePlayable > maxPlayable) {
                // If the player already has all cards shown to him that can be played, skip to the next player
                if (playerAlreadyHasClues(iteratePlayer.getHand())) {
                    index = increaseAndCheckIndex(index);
                    iteratePlayer = Players.getIndexPlayer(index);
                    continue;
                }
                maxPlayable = iteratePlayable;
                playerToGiveHint = iteratePlayer;
            }
            index = increaseAndCheckIndex(index);
            iteratePlayer = Players.getIndexPlayer(index);
        }
        // If all players have something shown to them already, give hint to the next player
        if (playerToGiveHint == null) {
            playerToGiveHint = Players.getIndexPlayer(increaseAndCheckIndex(index));
        }
        /*else if (howManyPlayableCardsInHand(player.getHand()) > 1 && player != Players.getIndexPlayer() && Tokens.getTokens().getClues() >= Players.numberOfPlayers) {
            playerToGiveHint = player;
            break;
        }*/
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

    private Player whichPlayerToGiveHintToDiscardACard(Player thisPlayer) {
        int index = increaseAndCheckIndex(Players.getPlayerIndex());
        Player player = null;
        Player iteratePlayer = Players.getIndexPlayer(index);
        while (!iteratePlayer.equals(thisPlayer)) {
            // If the player can discard
            if (playerCanDiscardACard(iteratePlayer.getHand())) {
                player = iteratePlayer;
            }
            index = increaseAndCheckIndex(index);
            iteratePlayer = Players.getIndexPlayer(index);
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
                /*if (card.getColor() == firework.getColor() && card.getNumber().isHigher(firework.getNumber())) {
                    for (Card discardedCard : DiscardedCards.getDiscard().getCards().get(card.getColor())) {
                        if (discardedCard == card) {
                            counter++;
                        }
                    }
                    // If the card hasn't been discarded yet and it's not a 5
                    if (counter == 0 && card.getNumber() != CardNumber.FIVE) {
                        return true;
                    }
                }*/
            }
        }
        return false;
    }

    private void whatHintToGiveToPlayACard(Hand hand) {
        hintColor = null;
        hintNumber = null;
        // Number showing cycle * and sometimes color, depending
        for (Card card : hand.cards) {
            // If the number hasn't been shown yet
            if (!card.knownNumber) {
                // 1. recommendation
                if (card.getNumber().equals(CardNumber.FIVE) && Fireworks.getFireworks().isPlayable(card)) {
                    if (checkWhatIsShowable(hand, card)) {
                        break;
                    }
                } // 2. recommendation
                else if (card.getNumber().equals(CardNumber.ONE) && Fireworks.getFireworks().isPlayable(card)) {
                    if (checkWhatIsShowable(hand, card)) {
                        break;
                    }
                } else if (card.getNumber().equals(CardNumber.TWO) && Fireworks.getFireworks().isPlayable(card)) {
                    if (checkWhatIsShowable(hand, card)) {
                        break;
                    }
                } else if (card.getNumber().equals(CardNumber.THREE) && Fireworks.getFireworks().isPlayable(card)) {
                    if (checkWhatIsShowable(hand, card)) {
                        break;
                    }
                } else if (card.getNumber().equals(CardNumber.FOUR) && Fireworks.getFireworks().isPlayable(card)) {
                    if (checkWhatIsShowable(hand, card)) {
                        break;
                    }
                }
            }
        }
        // Color showing cycle if number hasn't been chosen to be shown
        if (hintNumber == null) {
            for (Card card : hand.cards) {
                if (!card.knownColor) {
                    // 1. recommendation
                    if (card.getNumber().equals(CardNumber.FIVE) && Fireworks.getFireworks().isPlayable(card)) {
                        hintColor = card.getColor();
                        break;
                    } // 2. recommendation
                    else if (card.getNumber().equals(CardNumber.ONE) && Fireworks.getFireworks().isPlayable(card)) {
                        hintColor = card.getColor();
                        break;
                    } else if (card.getNumber().equals(CardNumber.TWO) && Fireworks.getFireworks().isPlayable(card)) {
                        hintColor = card.getColor();
                        break;
                    } else if (card.getNumber().equals(CardNumber.THREE) && Fireworks.getFireworks().isPlayable(card)) {
                        hintColor = card.getColor();
                        break;
                    } else if (card.getNumber().equals(CardNumber.FOUR) && Fireworks.getFireworks().isPlayable(card)) {
                        hintColor = card.getColor();
                        break;
                    }
                }
            }
        }
    }

    private boolean checkWhatIsShowable(Hand hand, Card card) {
        // If there is only one of the number to be showed
        if (symbolCounter(hand, null, card.getNumber()) == 1 && !card.knownNumber) {
            hintNumber = card.getNumber();
            return true;
        } else {
            // If all cards with that number can be played
            if (areAllSymbolsPlayable(hand, null, card.getNumber())) {
                for (Card actualCard : hand.cards) {
                    // If there is at least one, that hasn't been shown yet
                    if (card.getNumber().equals(actualCard.getNumber()) && !actualCard.knownNumber) {
                        hintNumber = card.getNumber();
                        return true;
                    }
                }
            } // If the card is the only one of that color
            else if (symbolCounter(hand, card.getColor(), null) == 1) {
                hintColor = card.getColor();
                return true;
            } // If all cards with that color can be played
            else if (areAllSymbolsPlayable(hand, card.getColor(), null)) {
                for (Card actualCard : hand.cards) {
                    // If there is at least one, that hasn't been shown yet
                    if (card.getColor().equals(actualCard.getColor()) && !actualCard.knownColor) {
                        hintColor = card.getColor();
                        return true;
                    }
                }
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

    private void whatHintToGiveToDiscardACard(Hand hand) {
        hintColor = null;
        hintNumber = null;
        for (Card card : hand.cards) {
            // 3. recommendation
            if (Fireworks.getFireworks().isDeadCard(card)) {
                if (Fireworks.getFireworks().howManyOfThatNumberPlayed(card.getNumber()) == CardColor.values().length) {
                    //TODO checkWhatIsShowable-hoz hasonlóan megnézni meg volt-e mutatva már
                    hintNumber = card.getNumber();
                } else if (Fireworks.getFireworks().howManyOfThatColorPlayed(card.getColor()) == 5) {
                    hintColor = card.getColor();
                }
            }  // 3/b. recommendation
            else if (card.getNumber().equals(CardNumber.FIVE) && !card.knownNumber) {
                hintNumber = card.getNumber();
                break;
            }// 4. recommendation
        }
    }

    private void giveHint(Player player) {
        /**
         * The recommendation for a hand will be determined with following priority:
         * 1. Recommend that the playable card of rank 5 with lowest index be played.
         * 2. Recommend that the playable card with lowest rank be played. If there is a tie for lowest rank, recommend the one with lowest index.
         * 3. Recommend that the dead card with lowest index be discarded.
         * 3/b. Recommend 5 to be safe and not discard it accidentally.
         * 4. Recommend that the card with highest rank that is not indispensable be discarded. If there is a tie, recommend the one with lowest index.
         * 5. Recommend that c1 be discarded.
         */
        Hand hand = player.getHand();

        // Give a hint to the player
        for (Card card : hand.cards) {
            if (hintColor != null) {
                // If the card is the same color as the clue given
                if (card.getColor().equals(hintColor)) {
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
            } else if (hintNumber != null) {
                // If the card is the same number as the clue given
                if (card.getNumber().equals(hintNumber)) {
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
        if (hintNumber != null) {
            History.getHistory().addString(player.getName(), "", History.getHistory().getHistoryNumber(hintNumber));
//            Tokens.getTokens().decreaseClues();
        } else if (hintColor != null) {
            History.getHistory().addString(player.getName(), History.getHistory().getHistoryColor(hintColor), "");
        }
        Tokens.getTokens().decreaseClues();
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
        index++;
        if (index >= numberOfPlayers) {
            index = 0;
        }
        return index;
    }
}
