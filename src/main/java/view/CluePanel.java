package view;

import model.Tokens;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import static view.GameTable.RIGHT_PANEL_WIDTH;

public class CluePanel extends JPanel {
    private Tokens tokens;
    private JLabel lifeLabel;
    private JLabel clueLabel;
    private String lifeString = "Életek: ";
    private String tokenString = "Utalásjelzők: ";

    CluePanel(Tokens tokens) {
        this.tokens = tokens;
        lifeLabel = new JLabel(lifeString + tokens.getLife());
        lifeLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        clueLabel = new JLabel(tokenString + tokens.getClues());
        clueLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        lifeLabel.setMaximumSize(new Dimension(RIGHT_PANEL_WIDTH, 30));
        lifeLabel.setHorizontalAlignment(JLabel.CENTER);
        clueLabel.setMaximumSize(new Dimension(RIGHT_PANEL_WIDTH, 30));
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
        lifeLabel.setText(lifeString + tokens.getLife());
        clueLabel.setText(tokenString + tokens.getClues());
        this.repaint();
    }
}
