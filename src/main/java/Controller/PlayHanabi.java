package controller;

import model.HanabiCards;
import model.Player;
import model.Players;
import view.GameTable;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.ImageObserver;
import java.util.List;

public class PlayHanabi implements MouseListener, ImageObserver {
    private static final int MAX_NUMBER_OF_CLUES = 8;
    private static final int MAX_NUMBER_OF_FAILS = 3;

    private int numberOfClues = MAX_NUMBER_OF_CLUES;
    private int numberOfFails = MAX_NUMBER_OF_FAILS;

    private int numOfPlayers = 5;

    private GameTable table;
    private List<Player> players;

    public PlayHanabi() {
        initGame(numOfPlayers);
    }

    private void initGame(int numberOfPlayers) {
        HanabiCards.DECK.shuffle();
        players = Players.setupPlayers(numberOfPlayers);

        table = new GameTable();
    }

    public void play() {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        return false;
    }
}