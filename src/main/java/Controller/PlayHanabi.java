package controller;

import java.util.concurrent.TimeUnit;

import model.Fireworks;
import model.HanabiCards;
import model.Player;
import model.Players;
import model.Tokens;
import view.GameEndWindow;
import view.GameTable;
import view.SetupWindow;

public class PlayHanabi extends AbstractPlay {

    private GameTable table;
    private final AIPlayer aiPlayer;
    private final int numberOfPlayers;
    private final String playerName;

    public PlayHanabi() {
        SetupWindow setupWindow = new SetupWindow();
        while (!setupWindow.done) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        aiPlayer = new AIPlayer(setupWindow.getNumberOfPlayers(), false);
        numberOfPlayers = setupWindow.getNumberOfPlayers();
        playerName = setupWindow.getName();
        initGame();
        setupWindow.dispose();
    }

    void initGame() {
        super.initGame(numberOfPlayers);
        Players.initHumanPlayer(playerName);
        table = new GameTable();
    }

    public boolean scoring() {
        if (Fireworks.getFireworks().allFireworksFinished() || HanabiCards.DECK.endOfDeck()) {
            GameEndWindow gameEndWindow = new GameEndWindow(Fireworks.getFireworks().getNumberOfCardsPlayed(), table, true);
            waitForDone(gameEndWindow);
            return gameEndWindow.done;
        } else if (Tokens.getTokens().getLife() == 0) {
            GameEndWindow gameEndWindow = new GameEndWindow(Fireworks.getFireworks().getNumberOfCardsPlayed(), table, false);
            waitForDone(gameEndWindow);
            return gameEndWindow.done;
        }
        return false;
    }

    //TODO: rewrite active wait
    //TODO: PlayerPanel must not contain controlling logic

    private void waitForDone(GameEndWindow gameEndWindow) {
        while (gameEndWindow.done == null) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    void playerTurn(Player actualPlayer) {
        // AI logic comes here
        if (actualPlayer.isAIPlayer()) {
            aiPlayer.chooseAction(actualPlayer);
            actualPlayer.setTheirTurn(false);
            table.repaintAll();
        } else {
            enableButtons();

            while (actualPlayer.isTheirTurn()) {
                // Wait for player's action
                // isTheirTurn changes in PlayerPanel, after action has been done
            }
        }
    }

    private void enableButtons() {
        table.getControlPanel().playCardButton.setEnabled(true);
        table.getControlPanel().discardCardButton.setEnabled(true);
        table.getControlPanel().giveHintButton.setEnabled(true);
    }
}