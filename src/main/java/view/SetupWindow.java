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
    private final JTextField playerName = new JTextField();
    private int numberOfPlayers;
    private final JComboBox<Integer> numberOfPlayersComboBox = new JComboBox<>(new Integer[] {2, 3, 4, 5} );

    public boolean done = false;

    public SetupWindow() {
        // Clear all previous instances
        clearInstances();

        // SetupWindow starts here
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Setup");
        this.setSize(new Dimension(300, 200));
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);

        JPanel settingsPanel = createSettingsPanel();

        this.add(new Panel(), BorderLayout.PAGE_START);
        this.add(new Panel(), BorderLayout.LINE_START);
        this.add(settingsPanel, BorderLayout.CENTER);
        this.add(new Panel(), BorderLayout.LINE_END);

        JPanel buttonPanel = new JPanel();
        JButton confirmButton = new JButton("Start the game");
        confirmButton.addActionListener(this);
        buttonPanel.add(confirmButton);

        this.add(buttonPanel, BorderLayout.PAGE_END);
        this.setVisible(true);
    }

    private void clearInstances() {
        DiscardedCards.clearInstance();
        Fireworks.clearInstance();
        History.clearInstance();
        SelectedSymbol.clearInstance();
        Tokens.clearInstance();
        HanabiCards.initDeck();
    }

    private JPanel createSettingsPanel() {
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new GridLayout(3, 2, 10, 10));
        settingsPanel.add(new JLabel("Name:"));
        settingsPanel.add(playerName);
        settingsPanel.add(new JLabel("Number of players:"));
        settingsPanel.add(numberOfPlayersComboBox);
        return settingsPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        name = playerName.getText();
        numberOfPlayers = getSelectedComboBoxItem();
        done = true;
    }

    private int getSelectedComboBoxItem() {
        switch (numberOfPlayersComboBox.getSelectedIndex()) {
            case 1: return 3;
            case 2: return 4;
            case 3: return 5;
            case 0:
            default: return 2;
        }
    }

    public String getName() {
        return name;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

}
