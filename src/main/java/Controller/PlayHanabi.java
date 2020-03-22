package controller;

import model.Card;
import model.CardColor;
import model.Fireworks;
import model.HanabiCards;
import model.Player;
import model.Players;
import model.Tokens;
import view.ControlPanel;
import view.GameTable;
import view.SetupWindow;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.util.concurrent.TimeUnit;

public class PlayHanabi implements ImageObserver {

    private boolean gameEnd = false;
    private GameTable table;

    public PlayHanabi() {
        SetupWindow setupWindow = new SetupWindow();

        while(!setupWindow.done) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        initGame(setupWindow, setupWindow.getNumberOfPlayers(), setupWindow.getName());
    }

    private void initGame(JFrame jFrame, int numberOfPlayers, String name) {
        jFrame.dispose();

        HanabiCards.DECK.shuffle();
        Players.setupPlayers(numberOfPlayers, name);

        table = new GameTable();
        //play();
    }

    public void play() {
        boolean endOfDeck = false;
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
        //TODO játék végi kiértékelés
    }

    public void playerTurn(Player actualPlayer) {
        // AI logic comes here
        if (actualPlayer.isAIPlayer()) {
            JOptionPane.showMessageDialog(null, actualPlayer.getName(), "Actual player", JOptionPane.INFORMATION_MESSAGE);
            actualPlayer.setTheirTurn(false);
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