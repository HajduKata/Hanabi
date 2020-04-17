package controller;

import model.DiscardedCards;
import model.Fireworks;
import model.HanabiCards;
import model.History;
import model.Player;
import model.Players;
import model.SelectedSymbol;
import model.Tokens;
import view.GameTable;

public class PlayTest {
    private boolean gameEnd = false;
    private GameTable table;
    private AIPlayer aiPlayer;

    public PlayTest(int numberOfPlayers) {
        aiPlayer = new AIPlayer(numberOfPlayers);

        initGame(numberOfPlayers);
    }

    private void initGame(int numberOfPlayers) {
        // Clear all previous instances
        DiscardedCards.clearInstance();
        Fireworks.clearInstance();
        History.clearInstance();
        SelectedSymbol.clearInstance();
        Tokens.clearInstance();
        HanabiCards.initDeck();

        HanabiCards.DECK.shuffle();
        Players.setupTestPlayers(numberOfPlayers);

//        table = new GameTable();
    }

    public String play() {
        boolean endOfDeck;
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
        String result = "";
        if (Fireworks.getFireworks().allFireworksFinished() || endOfDeck) {
            result = Players.numberOfPlayers + ";" + Fireworks.getFireworks().getNumberOfCardsPlayed();
        } else if (Tokens.getTokens().getLife() == 0) {
            result = Players.numberOfPlayers + ";-1";
        }
        return result;
    }

    private void playerTurn(Player actualPlayer) {
        // AI logic comes here
        //aiController.updatePossibilityTable(actualPlayer);
        aiPlayer.chooseAction(actualPlayer);

        actualPlayer.setTheirTurn(false);
    }
}