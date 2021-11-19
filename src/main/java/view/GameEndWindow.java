package view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameEndWindow extends JFrame implements ActionListener {
    private GameTable gameTable;
    private JButton newGameButton;
    private JButton exitGameButton;
    public Boolean done = null;

    public GameEndWindow(int points, GameTable gameTable, boolean won) {
        this.gameTable = gameTable;

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String endGameText;
        if (won) {
            this.setTitle("Victory!");
            endGameText = "Congratulations! You've reached " + points + " points!";
        } else {
            this.setTitle("Game over!");
            endGameText = "You've lost all your lives!";
        }
        this.setSize(new Dimension(300, 200));
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3, 2, 10, 10));

        JLabel nameLabel = new JLabel(endGameText);
        mainPanel.add(nameLabel);

        this.add(new Panel(), BorderLayout.PAGE_START);
        this.add(new Panel(), BorderLayout.LINE_START);
        this.add(mainPanel, BorderLayout.CENTER);
        this.add(new Panel(), BorderLayout.LINE_END);

        JPanel buttonPanel = new JPanel();
        newGameButton = new JButton("Play again");
        newGameButton.addActionListener(this);
        buttonPanel.add(newGameButton);
        exitGameButton = new JButton("Quit");
        exitGameButton.addActionListener(this);
        buttonPanel.add(exitGameButton);

        this.add(buttonPanel, BorderLayout.PAGE_END);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(newGameButton)) {
            done = false;
        } else if(e.getSource().equals(exitGameButton)){
            done = true;
        }
        this.dispose();
        gameTable.dispose();
    }
}
