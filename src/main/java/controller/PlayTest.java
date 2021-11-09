package controller;

import model.DiscardedCards;
import model.Fireworks;
import model.HanabiCards;
import model.History;
import model.Player;
import model.Players;
import model.SelectedSymbol;
import model.Tokens;

public class PlayTest extends PlayAbstractClass {
    private final AIPlayer aiPlayer;
    private final int numberOfPlayers;
    private String result;

    public PlayTest(int numberOfPlayers) {
        aiPlayer = new AIPlayer(numberOfPlayers, true);
        this.numberOfPlayers = numberOfPlayers;
        initGame();
    }

    void initGame() {
        clearPreviousInstances();
        super.initGame(numberOfPlayers);
    }

    // Clear all previous instances before each test run
    private void clearPreviousInstances() {
        DiscardedCards.clearInstance();
        Fireworks.clearInstance();
        History.clearInstance();
        SelectedSymbol.clearInstance();
        Tokens.clearInstance();
        HanabiCards.initDeck();
    }

    public boolean play() {
        boolean endOfDeck;
        boolean gameEnd;
        do {
            Player actualPlayer = Players.nextPlayer();
            actualPlayer.setTheirTurn(true);
            playerTurn(actualPlayer);
            endOfDeck = HanabiCards.DECK.endOfDeck();
            gameEnd = endOfDeck || Tokens.getTokens().getLife() == 0 || Fireworks.getFireworks().allFireworksFinished();
        } while (!gameEnd);

        if (endOfDeck) {
            for (int i = 0; i < Players.numberOfPlayers; i++) {
                Player actualPlayer = Players.nextPlayer();
                actualPlayer.setTheirTurn(true);
                playerTurn(actualPlayer);
            }
        }

        result = "";
        if (Fireworks.getFireworks().allFireworksFinished() || endOfDeck) {
            result = Players.numberOfPlayers + ";" + Fireworks.getFireworks().getNumberOfCardsPlayed();
        } else if (Tokens.getTokens().getLife() == 0) {
            result = Players.numberOfPlayers + ";0";
        }
        return !result.isEmpty();
    }

    void playerTurn(Player actualPlayer) {
        // AI logic comes here
        aiPlayer.chooseAction(actualPlayer);
        actualPlayer.setTheirTurn(false);
    }

    public String getResult() {
        return result;
    }

}