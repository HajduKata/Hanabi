package view;

import model.Card;
import model.CardColor;
import model.DiscardedCards;

import javax.swing.JPanel;
import javax.swing.plaf.BorderUIResource.TitledBorderUIResource;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.List;
import java.util.SortedMap;

import static view.GameTable.CARD_COLORS_DIMENSION;

public class DiscardedCardsPanel extends JPanel {
    private static final String DISCARDED_CARDS_TITLE = "Eldobott lapok";
    private static final Color BG_COLOR = Color.decode("#003375");

    private DiscardedCards discardedCards;

    public DiscardedCardsPanel(DiscardedCards discardedCards) {
        this.discardedCards = discardedCards;
        setBackground(BG_COLOR);
        setPreferredSize(CARD_COLORS_DIMENSION);
        setMinimumSize(CARD_COLORS_DIMENSION);
        TitledBorderUIResource border = new TitledBorderUIResource(DISCARDED_CARDS_TITLE);
        border.setTitleFont(new Font("SansSerif", Font.PLAIN, 14));
        border.setTitleColor(Color.WHITE);
        setBorder(border);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintDiscardedCards(g);
    }

    private void paintDiscardedCards(Graphics g) {
        int x = 5;
        SortedMap<CardColor, List<Card>> discard = discardedCards.getCards();
        for (CardColor color : discard.keySet()) {
            int y = 20;
            Card emptyCard = new Card(color);
            if (discard.get(color).get(0).equals(emptyCard)) {
                g.setColor(color.getPaintColor(color));
                g.drawRect(x, y, Card.CARD_SIZE_X, Card.CARD_SIZE_Y);
            } else {
                for (Card card : discard.get(color)) {
                    g.drawImage(card.image, x, y, Card.CARD_SIZE_X, Card.CARD_SIZE_Y, this);
                    y += Card.CARD_OFFSET_Y;
                }
            }
            x += Card.CARD_SIZE_X + 5;
        }
    }
}
