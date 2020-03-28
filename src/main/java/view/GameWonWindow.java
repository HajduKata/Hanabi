package view;

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

public class GameWonWindow extends JFrame implements ActionListener {
    private GameTable gameTable;
    private JButton newGameButton;
    private JButton exitGameButton;
    public Boolean done = null;

    public GameWonWindow(int points, GameTable gameTable) {
        this.gameTable = gameTable;

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Győzelem!");
        this.setSize(new Dimension(300, 200));
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3, 2, 10, 10));

        String winningText = "Gratulálunk! " + points + " pontot szereztél!";
        JLabel nameLabel = new JLabel(winningText);
        mainPanel.add(nameLabel);

        this.add(new Panel(), BorderLayout.PAGE_START);
        this.add(new Panel(), BorderLayout.LINE_START);
        this.add(mainPanel, BorderLayout.CENTER);
        this.add(new Panel(), BorderLayout.LINE_END);

        JPanel buttonPanel = new JPanel();
        newGameButton = new JButton("Új játék indítása?");
        newGameButton.addActionListener(this);
        buttonPanel.add(newGameButton);
        exitGameButton = new JButton("Kilépés a játékból?");
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
