package controller;

import model.HanabiCards;
import model.Player;
import model.Players;
import model.Tokens;
import view.GameTable;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;

public class PlayHanabi implements ImageObserver, ActionListener {

    private boolean gameEnd = false;

    private GameTable table;
    JFrame settingsWindow = new JFrame();
    private String name;
    private JTextField playerName = new JTextField();
    private int number;
    private JComboBox numberOfPlayers = new JComboBox();
    private String diff;
    private JComboBox difficulty;

    public PlayHanabi() {
        settingsWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        settingsWindow.setTitle("Első Lépések");
        settingsWindow.setSize(new Dimension(300, 200));
        settingsWindow.setLayout(new BorderLayout());
        settingsWindow.setLocationRelativeTo(null);

        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new GridLayout(3, 2, 10, 10));

        JLabel nameLabel = new JLabel("Név:");
        settingsPanel.add(nameLabel);
        settingsPanel.add(playerName);
        JLabel numberOfPlayersLabel = new JLabel("Játékosok száma:");
        settingsPanel.add(numberOfPlayersLabel);
        numberOfPlayers.addItem(2);
        numberOfPlayers.addItem(3);
        numberOfPlayers.addItem(4);
        numberOfPlayers.addItem(5);
        settingsPanel.add(numberOfPlayers);
        JLabel difficultyLabel = new JLabel("Játék nehézsége:");
        settingsPanel.add(difficultyLabel);
        String[] diff = {"Könnyű", "Közepes", "Nehéz"};
        difficulty = new JComboBox(diff);
        settingsPanel.add(difficulty);

        settingsWindow.add(new Panel(), BorderLayout.PAGE_START);
        settingsWindow.add(new Panel(), BorderLayout.LINE_START);
        settingsWindow.add(settingsPanel, BorderLayout.CENTER);
        settingsWindow.add(new Panel(), BorderLayout.LINE_END);

        JPanel buttonPanel = new JPanel();
        JButton confirm = new JButton("Játék indítása");
        // TODO initGame ne  az actionlistener-ben legyen!!!
        confirm.addActionListener(this);
        buttonPanel.add(confirm);

        settingsWindow.add(buttonPanel, BorderLayout.PAGE_END);
        settingsWindow.setVisible(true);

        //initGame(settingsWindow, numOfPlayers);
    }

    private void initGame(JFrame jFrame, int numberOfPlayers) {
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
    public void actionPerformed(ActionEvent e) {
        name = playerName.getText();
        number = (int) numberOfPlayers.getSelectedItem();
        diff = (String) difficulty.getSelectedItem();

        initGame(settingsWindow, number);
    }

    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        return false;
    }
}