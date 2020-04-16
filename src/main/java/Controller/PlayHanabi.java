package controller;

import model.Fireworks;
import model.HanabiCards;
import model.Player;
import model.Players;
import model.Tokens;
import view.GameLostWindow;
import view.GameTable;
import view.GameEndWindow;
import view.SetupWindow;

import javax.swing.JFrame;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.util.concurrent.TimeUnit;

public class PlayHanabi implements ImageObserver {

    private boolean gameEnd = false;
    private GameTable table;
    AIController aiController;

    public PlayHanabi() {
        SetupWindow setupWindow = new SetupWindow();
        while(!setupWindow.done) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        aiController = new AIController(setupWindow.getNumberOfPlayers());
        initGame(setupWindow, setupWindow.getNumberOfPlayers(), setupWindow.getName());
    }

    private void initGame(JFrame jFrame, int numberOfPlayers, String name) {
        jFrame.dispose();

        HanabiCards.DECK.shuffle();
        Players.setupPlayers(numberOfPlayers, name);

        table = new GameTable();

    }

    public boolean play() {
        boolean endOfDeck;
        do {
            Player actualPlayer = Players.nextPlayer();
            actualPlayer.setTheirTurn(true);
            playerTurn(actualPlayer);
            endOfDeck = HanabiCards.DECK.endOfDeck();
            gameEnd = endOfDeck || Tokens.getTokens().getLife() == 0 || Fireworks.getFireworks().allFireworksFinished();
        } while (!gameEnd);
        if(endOfDeck) {
            for (int i = 0; i < Players.numberOfPlayers; i++) {
                Player actualPlayer = Players.nextPlayer();
                actualPlayer.setTheirTurn(true);
                playerTurn(actualPlayer);
            }
        }

        if(Fireworks.getFireworks().allFireworksFinished() || endOfDeck) {
            GameEndWindow gameEndWindow = new GameEndWindow(Fireworks.getFireworks().getNumberOfCardsPlayed(), table, true);
            waitForDone(gameEndWindow);
            return gameEndWindow.done;
        }
        else if(Tokens.getTokens().getLife() == 0) {
            GameEndWindow gameEndWindow = new GameEndWindow(Fireworks.getFireworks().getNumberOfCardsPlayed(), table, false);
            waitForDone(gameEndWindow);
            return gameEndWindow.done;
        }
        return false;
    }

    private void waitForDone(GameEndWindow gameEndWindow) {
        while(gameEndWindow.done == null) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void playerTurn(Player actualPlayer) {
        // AI logic comes here
        if (actualPlayer.isAIPlayer()) {
            aiController.updatePossibilityTable(actualPlayer);
            aiController.chooseAction(actualPlayer);

            actualPlayer.setTheirTurn(false);
            table.repaintAll();
        } else {
            table.getControlPanel().playCardButton.setEnabled(true);
            table.getControlPanel().discardCardButton.setEnabled(true);
            table.getControlPanel().giveHintButton.setEnabled(true);

            while (actualPlayer.isTheirTurn()) {
                // Wait for player's action
                // isTheirTurn changes in PlayerPanel, after action has been done
            }
        }
    }

    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        return false;
    }
}