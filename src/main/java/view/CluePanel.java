package view;

import model.Tokens;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import static view.GameTable.RIGHT_PANEL_WIDTH;

public class CluePanel extends JPanel {
    Tokens tokens;
    JLabel lifeLabel;
    JLabel clueLabel;

    public CluePanel(Tokens tokens) {
        this.tokens = tokens;
        lifeLabel = new JLabel("Life : " + tokens.getLife());
        lifeLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        clueLabel = new JLabel("Tokens: " + tokens.getClues());
        clueLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        lifeLabel.setMaximumSize(new Dimension(RIGHT_PANEL_WIDTH, 25));
        lifeLabel.setHorizontalAlignment(JLabel.CENTER);
        clueLabel.setMaximumSize(new Dimension(RIGHT_PANEL_WIDTH, 25));
        clueLabel.setHorizontalAlignment(JLabel.CENTER);
        this.add(lifeLabel);
        this.add(clueLabel);
    }

    @Override
    protected void paintComponent(Graphics g) {
        paintAllClues(tokens);
        super.paintComponent(g);
    }

    private void paintAllClues(Tokens tokens) {
        lifeLabel.setText("Life : " + tokens.getLife());
        clueLabel.setText("Tokens: " + tokens.getClues());
        this.repaint();
    }
}
