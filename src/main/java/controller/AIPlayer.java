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

import javax.swing.JOptionPane;

import java.util.ArrayList;
import java.util.List;

import static view.PlayerPanel.drawNewCard;

class AIPlayer {
    private int numberOfPlayers;
    private boolean isTest;
    private Player playerToGiveHintTo;
    private Symbol hintSymbol;

    AIPlayer(int numberOfPlayers, boolean isTest) {
        this.numberOfPlayers = numberOfPlayers;
        this.isTest = isTest;
    }

    void chooseAction(Player thisPlayer) {
        /*
         * Action algorithm: A player will act using her private information with the following priority:
         * 1. Play the playable card with lowest index.
         * 2. If there are hint tokens available, give a hint.
         * 3. Discard the dead card with lowest index.
         * 4. Discard the expendable card with lowest index.
         * 5. Discard card c1.
         */

        String actionMessage;

        Card cardToPlay = whatCardToPlay(thisPlayer.getHand());

        Card cardToDiscard = whatDeadCardToDiscard(thisPlayer.getHand());

        Card expendableCard = whatExpendableCardToDiscard(thisPlayer.getHand());

        hintSymbol = new Symbol(null, null);
        playerToGiveHintTo = null;

        // 1. Play the playable card with lowest index.
        if (cardToPlay != null) {
            playACard(thisPlayer, cardToPlay);
            actionMessage = thisPlayer.getName() + " played a " + cardToPlay.getColor() + " " + cardToPlay.getNumber() + " card.";
        } // 2. If there are hint tokens available && there is a player with playable cards in hand && we can give a hint that only shows the playable cards, give a hint.
        else if (Tokens.getTokens().getClues() > 0 && hintChooser(thisPlayer)) {
            giveHint();
            actionMessage = thisPlayer.getName() + " gave a hint.";
        } // 3. Discard the dead card with lowest index.
        else if (cardToDiscard != null) {
            discardACard(thisPlayer, cardToDiscard);
            actionMessage = thisPlayer.getName() + " discarded a " + cardToDiscard.getColor() + " " + cardToDiscard.getNumber() + "  card.";
        } // 4. Discard the expendable card with lowest index.
        else if (expendableCard != null) {
            discardACard(thisPlayer, expendableCard);
            actionMessage = thisPlayer.getName() + " discarded a " + expendableCard.getColor() + " " + expendableCard.getNumber() + "  card.";
        } // 5. Discard card c1 except if it's a 5.
        else {
            Card lastCard = thisPlayer.getHand().cards.get(0);
            int index = 1;
            while (index < thisPlayer.getHand().cards.size() && lastCard.knownNumber && lastCard.getNumber().equals(CardNumber.FIVE)) {
                lastCard = thisPlayer.getHand().cards.get(index);
                index++;
            }
            discardACard(thisPlayer, lastCard);
            actionMessage = thisPlayer.getName() + " discarded their oldest card. (" + lastCard.getColor() + " " + lastCard.getNumber() + ")";
        }

        if (!isTest) {
            JOptionPane.showMessageDialog(null, actionMessage, thisPlayer.getName() + "'s turn", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private Card whatCardToPlay(Hand hand) {
        Card cardToPlay = null;
        for (Card card : hand.cards) {
            // If Number and Color is known of the card
            if (card.knownNumber && card.knownColor) {
                if (Fireworks.getFireworks().isPlayable(card)) {
                    cardToPlay = card;
                    break;
                }
            } // If only Number is known of the card and there is still some of that Number to be played next AND is not discardalbe
            else if (card.knownNumber && !card.discardable && Fireworks.getFireworks().numberCanBePlayed(card.getNumber())
                    && checkShownAndPlayableNumbers(hand, card.getNumber())) {
                cardToPlay = card;
                break;
            } // If only Color is known of the card and there is still some of that Color to be played
            else if (card.knownColor && Fireworks.getFireworks().howManyOfThatColorPlayed(card.getColor()) < 5) {
                // If this is the only card that has knownColor of the shown color
                int counter = 0;
                for (Card counterCard : hand.cards) {
                    if (counterCard.knownColor && counterCard.getColor().equals(card.getColor())) {
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

    private boolean checkShownAndPlayableNumbers(Hand hand, CardNumber number) {
        int counter = 0;
        for(Card card : hand.cards) {
            if(card.getNumber().equals(number) && card.knownNumber && !card.knownColor) {
                counter++;
            }
        }
        return counter <= Fireworks.getFireworks().howManyOfThatNumberPlayed(number.previous());
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
                if (Fireworks.getFireworks().howManyOfThatNumberPlayed(card.getNumber()) == CardColor.values().length) {
                    cardToBeDiscarded = card;
                    break;
                }
            } // If only Color is known of the card
            else if (card.knownColor) {
                // If the fireworks of that color is already finished
                if (Fireworks.getFireworks().howManyOfThatColorPlayed(card.getColor()) == 5) {
                    cardToBeDiscarded = card;
                    break;
                }
            }
            // If none is known of the card
            // ?
        }
        return cardToBeDiscarded;
    }

    private Card whatExpendableCardToDiscard(Hand hand) {
        Card expendalbeCardToBeDiscarded = null;
        for (Card card : hand.cards) {
            if (card.knownNumber && card.discardable) {
                expendalbeCardToBeDiscarded = card;
                break;
            }
        }
        return expendalbeCardToBeDiscarded;
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

    private boolean hintChooser(Player thisPlayer) {
        /*
         * The recommendation for a hand will be determined with following priority:
         * 1. Recommend that the playable card of rank 5 with lowest index be played.
         * 2. Recommend that the playable card with lowest rank be played. If there is a tie for lowest rank, recommend the one with lowest index.
         * 3. Recommend that the dead card with lowest index be discarded.
         * 4. Recommend that the card with highest rank that is expendable be discarded. If there is a tie, recommend the one with lowest index.
         * 5. Recommend that c1 be discarded.
         */

        // If there are players with playable cards in hand
        List<Player> playableCardsPlayers = whichPlayersToPlayACard(thisPlayer);
        if (!playableCardsPlayers.isEmpty()) {
            for (Player iteratePlayer : playableCardsPlayers) {
                for (Card card : iteratePlayer.getHand().cards) {
                    if (Fireworks.getFireworks().isPlayable(card)) {
                        // 1. recommendation
                        switch (card.getNumber()) {
                            case FIVE:
                                if (checkWhatIsShowable(iteratePlayer.getHand(), card)) {
                                    playerToGiveHintTo = iteratePlayer;
                                    return true;
                                }
                                break;
                            // 2. recommendation
                            case ONE:
                            case TWO:
                            case THREE:
                            case FOUR:
                                if (!otherPlayersHasBeenShown(thisPlayer, iteratePlayer, card) && checkWhatIsShowable(iteratePlayer.getHand(), card)) {
                                    playerToGiveHintTo = iteratePlayer;
                                    return true;
                                }
                                break;
                        }
                    }
                }
            }
        }

        // If there are players with discardable dead cards in hand
        List<Player> deadCardsPlayers = whichPlayersToDeadCard(thisPlayer);
        if (!deadCardsPlayers.isEmpty()) {
            for (Player iteratePlayer : deadCardsPlayers) {
                for (Card card : iteratePlayer.getHand().cards) {
                    // 3. recommendation
                    if (Fireworks.getFireworks().isDeadCard(card)) {
                        // If the Fireworks of that Number have all been played
                        if (Fireworks.getFireworks().howManyOfThatNumberPlayed(card.getNumber()) == CardColor.values().length && !card.knownNumber) {
                            hintSymbol.hintNumber = card.getNumber();
                            playerToGiveHintTo = iteratePlayer;
                            return true;
                        } // If the Fireworks of that Color have all been played
                        else if (Fireworks.getFireworks().howManyOfThatColorPlayed(card.getColor()) == 5 && !card.knownColor) {
                            hintSymbol.hintColor = card.getColor();
                            playerToGiveHintTo = iteratePlayer;
                            return true;
                        }
                    }
                }
            }
        }

        // If there is a player no information of their hands, show them expendable cards in hand
        Player playerWithNoInfo = whichPlayerHasNoInformation(thisPlayer);
        if (playerWithNoInfo != null) {
            // Showing the number 4
            if (expendableCardCanBeShown(thisPlayer, playerWithNoInfo, CardNumber.FOUR)) {
                hintSymbol.hintNumber = CardNumber.FOUR;
                for (Card card : playerWithNoInfo.getHand().cards) {
                    if (card.getNumber().equals(CardNumber.FOUR))
                        card.discardable = true;
                }
                playerToGiveHintTo = playerWithNoInfo;
                return true;
            } // Showing the number 3
            else if (expendableCardCanBeShown(thisPlayer, playerWithNoInfo, CardNumber.THREE)) {
                hintSymbol.hintNumber = CardNumber.THREE;
                for (Card card : playerWithNoInfo.getHand().cards) {
                    if (card.getNumber().equals(CardNumber.THREE))
                        card.discardable = true;
                }
                playerToGiveHintTo = playerWithNoInfo;
                return true;
            } // Showing the number 2
            else if (expendableCardCanBeShown(thisPlayer, playerWithNoInfo, CardNumber.TWO)) {
                hintSymbol.hintNumber = CardNumber.TWO;
                for (Card card : playerWithNoInfo.getHand().cards) {
                    if (card.getNumber().equals(CardNumber.TWO))
                        card.discardable = true;
                }
                playerToGiveHintTo = playerWithNoInfo;
                return true;
            }
        }

        return false;
    }

    private List<Player> whichPlayersToPlayACard(Player thisPlayer) {
        int index = increaseAndCheckIndex(Players.getPlayerIndex());
        List<Player> playersToGiveHintTo = new ArrayList<>();
        Player iteratePlayer = Players.getIndexPlayer(index);
        while (!iteratePlayer.equals(thisPlayer)) {
            // If the player has playable cards in hand and doesn't have all cards shown to him that can be played
            if (howManyPlayableCardsInHand(iteratePlayer.getHand()) > 0 && !playerAlreadyHasAllClues(iteratePlayer.getHand())) {
                playersToGiveHintTo.add(iteratePlayer);
            }
            index = increaseAndCheckIndex(index);
            iteratePlayer = Players.getIndexPlayer(index);
        }
        return playersToGiveHintTo;
    }

    private int increaseAndCheckIndex(int index) {
        index++;
        if (index >= numberOfPlayers) {
            index = 0;
        }
        return index;
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

    private boolean playerAlreadyHasAllClues(Hand hand) {
        int playableCounter = 0;
        int shownCounter = 0;
        for (Card card : hand.cards) {
            if (Fireworks.getFireworks().isPlayable(card)) {
                playableCounter++;
            }
            if ((card.knownColor || card.knownNumber) && Fireworks.getFireworks().isPlayable(card)) {
                shownCounter++;
            }
        }
        return playableCounter == shownCounter;
    }

    private boolean checkWhatIsShowable(Hand hand, Card card) {
        // If there is only one of the number to be showed
        if (symbolCounter(hand, null, card.getNumber()) == 1 && !card.knownNumber) {
            hintSymbol.hintNumber = card.getNumber();
            return true;
        } else {
            // If all cards with that number can be played and there is no duplicate in hand
            if (areAllSymbolsPlayable(hand, null, card.getNumber())) {
                for (Card actualCard : hand.cards) {
                    // If there is at least one, that hasn't been shown yet
                    if (card.getNumber().equals(actualCard.getNumber()) && !actualCard.knownNumber) {
                        hintSymbol.hintNumber = card.getNumber();
                        return true;
                    }
                }
            } // If the card is the only one of that color
            else if (symbolCounter(hand, card.getColor(), null) == 1 && !card.knownColor) {
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
            if (card.getColor().equals(color) || card.getNumber().equals(number)
                    && (!Fireworks.getFireworks().isPlayable(card) || isDuplicate(hand, card))) {
                return false;
            }
        }

        return true;
    }

    private boolean otherPlayersHasBeenShown(Player thisPlayer, Player playerToGiveHintTo, Card checkCard) {
        for (Player player : Players.getThePlayers()) {
            if (!thisPlayer.equals(player) && !thisPlayer.equals(playerToGiveHintTo)) {
                for (Card card : player.getHand().cards) {
                    if (card.equals(checkCard) && (card.knownColor || card.knownNumber)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private List<Player> whichPlayersToDeadCard(Player thisPlayer) {
        int index = increaseAndCheckIndex(Players.getPlayerIndex());
        List<Player> playersWithDeadCard = new ArrayList<>();
        Player iteratePlayer = Players.getIndexPlayer(index);
        while (!iteratePlayer.equals(thisPlayer)) {
            // If the player has dead cards in hand (and there hasn't been a player chosen yet)
            if (howManyDeadCardsInHand(iteratePlayer.getHand()) > 0) {
                playersWithDeadCard.add(iteratePlayer);
            }
            index = increaseAndCheckIndex(index);
            iteratePlayer = Players.getIndexPlayer(index);
        }
        return playersWithDeadCard;
    }

    private int howManyDeadCardsInHand(Hand hand) {
        int counter = 0;
        for (Card card : hand.cards) {
            if (Fireworks.getFireworks().isDeadCard(card)) {
                counter++;
            }
        }
        return counter;
    }

    private Player whichPlayerHasNoInformation(Player thisPlayer) {
        int index = increaseAndCheckIndex(Players.getPlayerIndex());
        Player iteratePlayer = Players.getIndexPlayer(index);
        while (!iteratePlayer.equals(thisPlayer)) {
            // Return with the first player who has no information of their hand
            if (howMuchInformation(iteratePlayer.getHand()) == 0) {
                return iteratePlayer;
            }
            index = increaseAndCheckIndex(index);
            iteratePlayer = Players.getIndexPlayer(index);
        }
        return null;
    }

    private int howMuchInformation(Hand hand) {
        int counter = 0;
        for (Card card : hand.cards) {
            if (card.knownNumber || card.knownColor) {
                counter++;
            }
        }
        return counter;
    }

    private boolean expendableCardCanBeShown(Player thisPlayer, Player playerToGiveHintTo, CardNumber numberToBeShown) {
        int howManyShown = 0;
        // If the player has numberToBeShown and that number cannot be played yet
        if (symbolCounter(playerToGiveHintTo.getHand(), null, numberToBeShown) > 0 && Fireworks.getFireworks().howManyOfThatNumberPlayed(numberToBeShown.previous()) == 0) {
            for (Card card : playerToGiveHintTo.getHand().cards) {
                if (card.getNumber().equals(numberToBeShown)) {
                    // If the card is a duplicate in the same hand or has been shown to other players
                    if (isDuplicate(playerToGiveHintTo.getHand(), card) || checkForDuplicatesShownEverywhere(playerToGiveHintTo, thisPlayer, card)) {
                        return false;
                    }
                    if (card.knownNumber) {
                        howManyShown++;
                    }
                }
            }
            // If all symbols have been shown, don't show it again
            return howManyShown != symbolCounter(playerToGiveHintTo.getHand(), null, numberToBeShown);

        }
        return false;
    }

    private boolean isDuplicate(Hand hand, Card cardToCheck) {
        int counter = 0;
        for (Card card : hand.cards) {
            if (card.equals(cardToCheck)) {
                counter++;
            }
        }
        return counter > 1;
    }

    private boolean checkForDuplicatesShownEverywhere(Player playerToGiveHintTo, Player thisPlayer, Card cardToCheck) {
        for (Player player : Players.getThePlayers()) {
            // If the player is not the current player and not the player we want to give hint to, AND the player has a duplicate of the card to be shown
            if (!player.equals(playerToGiveHintTo) && !player.equals(thisPlayer) && isDuplicate(player.getHand(), cardToCheck)) {
                for (Card card : player.getHand().cards) {
                    // If the duplicate has already been shown
                    if (card.equals(cardToCheck) && card.knownNumber) {
                        return true;
                    }
                }
            }
            // If the player is the current player
            if (player.equals(thisPlayer)) {
                for (Card card : player.getHand().cards) {
                    if (card.knownNumber && card.getNumber().equals(cardToCheck.getNumber())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void giveHint() {
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
}
