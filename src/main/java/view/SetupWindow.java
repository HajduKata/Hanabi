package view;

import model.DiscardedCards;
import model.Fireworks;
import model.HanabiCards;
import model.History;
import model.SelectedSymbol;
import model.Tokens;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SetupWindow extends JFrame implements ActionListener {
    private String name;
    private JTextField playerName = new JTextField();
    private int numberOfPlayers;
    private JComboBox numberOfPlayersComboBox = new JComboBox();
//    private String diff;
//    private JComboBox difficulty;

    public boolean done = false;

    public SetupWindow() {
        // Clear all previous instances
        DiscardedCards.clearInstance();
        Fireworks.clearInstance();
        History.clearInstance();
        SelectedSymbol.clearInstance();
        Tokens.clearInstance();
        HanabiCards.initDeck();

        // SetupWindow starts here
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Első Lépések");
        this.setSize(new Dimension(300, 200));
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);

        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new GridLayout(3, 2, 10, 10));

        JLabel nameLabel = new JLabel("Név:");
        settingsPanel.add(nameLabel);
        settingsPanel.add(playerName);
        JLabel numberOfPlayersLabel = new JLabel("Játékosok száma:");
        settingsPanel.add(numberOfPlayersLabel);
        numberOfPlayersComboBox.addItem(2);
        numberOfPlayersComboBox.addItem(3);
        numberOfPlayersComboBox.addItem(4);
        numberOfPlayersComboBox.addItem(5);
        settingsPanel.add(numberOfPlayersComboBox);
        /*
        JLabel difficultyLabel = new JLabel("Játék nehézsége:");
        settingsPanel.add(difficultyLabel);
        String[] diff = {"Könnyű", "Közepes", "Nehéz"};
        difficulty = new JComboBox(diff);
        settingsPanel.add(difficulty);
        */

        this.add(new Panel(), BorderLayout.PAGE_START);
        this.add(new Panel(), BorderLayout.LINE_START);
        this.add(settingsPanel, BorderLayout.CENTER);
        this.add(new Panel(), BorderLayout.LINE_END);

        JPanel buttonPanel = new JPanel();
        JButton confirmButton = new JButton("Játék indítása");
        confirmButton.addActionListener(this);
        buttonPanel.add(confirmButton);

        this.add(buttonPanel, BorderLayout.PAGE_END);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        name = playerName.getText();
        numberOfPlayers = (int) numberOfPlayersComboBox.getSelectedItem();
//        diff = (String) difficulty.getSelectedItem();
        done = true;
    }

    public String getName() {
        return name;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

//    public String getDiff() {
//        return diff;
//    }
}
