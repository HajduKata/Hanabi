package view;

import model.Card;
import model.CardColor;

import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Set;
import java.util.SortedMap;

import static view.GameTable.CARD_COLORS_DIMENSION;

public class DiscardedCardsPanel extends JPanel {
    private static final String DISCARDED_CARDS_TITLE = "Discarded cards";
    private static final Color BG_COLOR = Color.decode("#003366");

    private SortedMap<CardColor, Set<Card>> discardedCards;

    public DiscardedCardsPanel(SortedMap<CardColor, Set<Card>> discardedCards) {
        this.discardedCards = discardedCards;

        setBackground(BG_COLOR);
        setPreferredSize(CARD_COLORS_DIMENSION);
        setMinimumSize(CARD_COLORS_DIMENSION);
        Border border = new BorderUIResource.TitledBorderUIResource(DISCARDED_CARDS_TITLE);
        setBorder(border);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        paintDropedCards(g);
    }

    private void paintDropedCards(Graphics g) {
        int x = 5;
        int y = 20;
        for (CardColor color : discardedCards.keySet()) {
            Card testCard = new Card(color);
            if (discardedCards.get(color).contains(testCard)) {
                g.setColor(color.getPaintColor(color));
                g.drawRect(x, y, Card.CARD_SIZE_X, Card.CARD_SIZE_Y);
            } else {
                for (Card card : discardedCards.get(color)) {
                    g.drawImage(card.image, x, y, Card.CARD_SIZE_X, Card.CARD_SIZE_Y, this);
                    y += Card.CARD_OFFSET_Y;
                }
            }
            x += Card.CARD_SIZE_X + 5;
        }
    }
}
