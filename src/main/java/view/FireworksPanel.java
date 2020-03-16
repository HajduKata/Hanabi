package view;

import model.Card;
import model.CardColor;
import model.CardNumber;
import model.Fireworks;

import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 * UI Panel to show the firework piles.
 */
public class FireworksPanel extends JPanel {
    //private static final String FIREWORKS = "Fireworks";
    private static final Color BG_COLOR = Color.decode("#003366");

    private Fireworks fireworks;

    /**
     * Constructor for the fireworks panel.
     *
     * @param fireworks the last card of the fireworks color
     */
    public FireworksPanel(final Fireworks fireworks) {
        this.fireworks = fireworks;

        setBackground(BG_COLOR);
        setPreferredSize(GameTable.CARD_COLORS_DIMENSION);
        setMaximumSize(GameTable.CARD_COLORS_DIMENSION);
        Border border = new BorderUIResource.TitledBorderUIResource("Fireworks");
        ((BorderUIResource.TitledBorderUIResource) border).setTitleFont(new Font("SansSerif", Font.PLAIN, 14));
        ((BorderUIResource.TitledBorderUIResource) border).setTitleColor(Color.WHITE);
        setBorder(border);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintFireworkColumns(g);
    }

    private void paintFireworkColumns(Graphics g) {
        int x = 5;
        int y = 20;
        for (Card card : fireworks.getCards()) {
            if (card != null) {
                if (card.getNumber() == CardNumber.ZERO) {
                    CardColor color = card.getColor();
                    g.setColor(color.getPaintColor(color));
                    g.drawRect(x, y, Card.CARD_SIZE_X, Card.CARD_SIZE_Y);
                } else {
                    g.drawImage(card.image, x, y, Card.CARD_SIZE_X, Card.CARD_SIZE_Y, this);
                    //y += Card.CARD_OFFSET_Y;
                }
                x += Card.CARD_SIZE_X + 5;
            }
        }
    }
}
