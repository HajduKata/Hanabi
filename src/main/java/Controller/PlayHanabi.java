package controller;

import model.HanabiCards;
import model.Player;
import model.Players;
import model.Tokens;
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
        do {
            Player actualPlayer = Players.nextPlayer();
            if (actualPlayer.isAIPlayer()) {
                // AI logic comes here
                JOptionPane.showMessageDialog(null, actualPlayer.getName(), "Actual player", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // TODO enable/disable mouse listeners
                Tokens.getTokens().decreaseLife();
            }
            gameEnd = Tokens.getTokens().getLife() == 0;
        } while (!gameEnd);
    }

    public void setGameEnd(boolean gameEnd) {
        this.gameEnd = gameEnd;
    }


    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        return false;
    }
}