package view;

import model.Tokens;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;

import static view.GameTable.RIGHT_PANEL_WIDTH;

public class CluePanel extends JPanel {
    Tokens tokens;
    JLabel lifeLabel;
    JLabel clueLabel;

    public CluePanel(Tokens tokens) {
        this.tokens = tokens;
        lifeLabel = new JLabel("life : " + tokens.getLife());
        clueLabel = new JLabel("tokens: " + tokens.getClues());
        lifeLabel.setMaximumSize(new Dimension(RIGHT_PANEL_WIDTH, 25));
        lifeLabel.setHorizontalAlignment(JLabel.CENTER);
        lifeLabel.setFont (lifeLabel.getFont ().deriveFont (24.0f));
        clueLabel.setMaximumSize(new Dimension(RIGHT_PANEL_WIDTH, 25));
        clueLabel.setHorizontalAlignment(JLabel.CENTER);
        clueLabel.setFont (clueLabel.getFont ().deriveFont (24.0f));
        this.add(lifeLabel);
        this.add(clueLabel);
    }

    @Override
    protected void paintComponent(Graphics g) {
        paintAllClues(tokens);
        super.paintComponent(g);
    }

    private void paintAllClues(Tokens tokens) {
        lifeLabel.setText("life : " + tokens.getLife());
        clueLabel.setText("tokens: " + tokens.getClues());
        this.repaint();
    }
}
