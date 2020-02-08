package view;

import model.Clues;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Dimension;
import java.awt.Graphics;

import static view.GameTable.RIGHT_PANEL_WIDTH;

public class CluePanel extends JPanel {
    Clues clues;


    public CluePanel(Clues clues) {
        this.clues = clues;
        JLabel lifeLabel = new JLabel("life : " + clues.getLife());
        JLabel clueLabel = new JLabel("clues: " + clues.getClues());
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
        paintAllClues(clues);
        super.paintComponent(g);
    }

    private void paintAllClues(Clues clues) {
        JLabel lifeLabel = new JLabel("life : " + clues.getLife());
        JLabel clueLabel = new JLabel("clues: " + clues.getClues());
        this.repaint();
    }
}
