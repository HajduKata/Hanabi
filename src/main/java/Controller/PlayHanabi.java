package controller;

import model.HanabiCards;
import model.Player;
import model.Players;
import view.GameTable;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Panel;
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
        JFrame settingsWindow = new JFrame();
        settingsWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        settingsWindow.setTitle("Első Lépések");
        settingsWindow.setSize(new Dimension(300,200));
        settingsWindow.setLayout(new BorderLayout());
        settingsWindow.setLocationRelativeTo(null);

        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new GridLayout(3, 2, 10,10));

        //TODO méretek beállítás?, adatkezelés
        JLabel nameLabel = new JLabel("Név:");
        settingsPanel.add(nameLabel);
        JTextField playerName = new JTextField();
        settingsPanel.add(playerName);
        JLabel numberOfPlayersLabel = new JLabel("Játékosok száma:");
        settingsPanel.add(numberOfPlayersLabel);
        String[] numbers = {"2", "3", "4", "5"};
        JComboBox numberOfPlayers = new JComboBox(numbers);
        settingsPanel.add(numberOfPlayers);
        JLabel difficultyLabel = new JLabel("Játék nehézsége:");
        settingsPanel.add(difficultyLabel);
        String[] diff = {"Könnyű", "Közepes", "Nehéz"};
        JComboBox difficulty = new JComboBox(diff);
        settingsPanel.add(difficulty);

        settingsWindow.add(new Panel(), BorderLayout.PAGE_START);
        settingsWindow.add(new Panel(), BorderLayout.LINE_START);
        settingsWindow.add(settingsPanel, BorderLayout.CENTER);
        settingsWindow.add(new Panel(), BorderLayout.LINE_END);

        JPanel buttonPanel = new JPanel();
        JButton confirm = new JButton("Játék indítása!");
        confirm.addActionListener(e -> initGame(settingsWindow, numOfPlayers));
        buttonPanel.add(confirm);

        settingsWindow.add(buttonPanel, BorderLayout.PAGE_END);
        settingsWindow.setVisible(true);

        //initGame(numOfPlayers);
    }

    private void initGame(JFrame jFrame, int numberOfPlayers) {
        jFrame.dispose();

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