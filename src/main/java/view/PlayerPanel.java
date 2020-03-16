package view;

import model.Card;
import model.DiscardedCards;
import model.Fireworks;
import model.Player;
import model.Players;
import model.SelectedSymbol;
import model.Tokens;

import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;

import static model.Card.CARD_OFFSET_X;
import static model.Card.CARD_SIZE_X;
import static model.Card.CARD_SIZE_Y;
import static model.Hand.NUM_OF_CARDS_IN_HAND;
import static view.GameTable.LEFT_PANEL_DIMENSION;
import static view.HanabiUtilities.CARD_START_POS_X;
import static view.HanabiUtilities.COLOR_OFFSET_X;
import static view.HanabiUtilities.COLOR_OFFSET_Y;
import static view.HanabiUtilities.NUMBER_OFFSET_X;
import static view.HanabiUtilities.NUMBER_OFFSET_Y;
import static view.HanabiUtilities.SYMBOL_WIDTH_HEIGHT;

public class PlayerPanel extends JPanel implements MouseListener {
    private static final Color BG_COLOR = Color.decode("#003366");

    private final Player player;
    private final Tokens tokens;
    private final ControlPanel controlPanel;
    private final FireworksPanel fireworksPanel;
    private final DiscardedCardsPanel discardedCardsPanel;

    PlayerPanel(Player player, Tokens tokens, ControlPanel controlPanel, FireworksPanel fireworksPanel, DiscardedCardsPanel discardedCardsPanel) {
        this.player = player;
        this.tokens = tokens;
        this.controlPanel = controlPanel;
        this.fireworksPanel = fireworksPanel;
        this.discardedCardsPanel = discardedCardsPanel;

        setPreferredSize(LEFT_PANEL_DIMENSION);
        setBackground(BG_COLOR);
        setForeground(Color.BLACK);
        Border border = new BorderUIResource.TitledBorderUIResource(player.getName());
        ((BorderUIResource.TitledBorderUIResource) border).setTitleFont(new Font("SansSerif", Font.PLAIN, 14));
        ((BorderUIResource.TitledBorderUIResource) border).setTitleColor(Color.WHITE);
        setBorder(border);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintCards(g, player);
    }

    private void paintCards(Graphics g, Player player) {
        if (player.isHumanPlayer()) {
            Image backOfCard = loadBackOfCards();
            for (Card card : player.getHand().cards) {
                drawCard(g, backOfCard, card.getX(), card.getY(), CARD_SIZE_X, Card.CARD_SIZE_Y);
                drawSymbols(g, card);
            }
        } else { // AI players
            for (Card card : player.getHand().cards) {
                drawCard(g, card.image, card.getX(), card.getY(), CARD_SIZE_X, Card.CARD_SIZE_Y);
                drawSymbols(g, card);
            }
        }
    }

    static BufferedImage loadBackOfCards() {
        URL imageURL = HanabiUtilities.classLoader.getResource("hanabi_cards/zbackground.png");
        assert imageURL != null;
        return HanabiUtilities.loadImage(imageURL);
    }

    private void drawCard(Graphics g, Image cardImage, int x, int y, int width, int height) {
        g.drawImage(cardImage, x, y, width, height, this);
    }

    private void drawSymbols(Graphics g, Card card) {
        int sx = card.getX() + COLOR_OFFSET_X;
        int sy = card.getY() + COLOR_OFFSET_Y;
        if (card.knownColor) {
            drawCard(g, HintSymbols.getImageByColor(card.getColor()), sx, sy, SYMBOL_WIDTH_HEIGHT - 10, SYMBOL_WIDTH_HEIGHT - 10);
        }
        if (card.knownNumber) {
            drawCard(g, HintSymbols.getImageByNumber(card.getNumber()), card.getX() + NUMBER_OFFSET_X, card.getY() + NUMBER_OFFSET_Y, SYMBOL_WIDTH_HEIGHT - 10, SYMBOL_WIDTH_HEIGHT - 10);
        }
    }

    public Player getPlayer() {
        return player;
    }

    /**
     * When clicked on this player panel
     *
     * @param mouseE mouse event object
     */
    @Override
    public void mouseClicked(MouseEvent mouseE) {
        int xOfLastCard = CARD_START_POS_X + (NUM_OF_CARDS_IN_HAND - 1) * CARD_OFFSET_X;
        // When player plays a card
        // TODO call card selector method and handle add to fireworks (life handling)
        if (/*this.getPlayer().isHumanPlayer() &&*/ controlPanel.isPlayACard) {
            for (Card card : this.getPlayer().getHand().cards) {
                if (card.getX() == xOfLastCard) {
                    playACard(mouseE, card, card.getX() + CARD_SIZE_X);
                } else {
                    playACard(mouseE, card, card.getX() + CARD_SIZE_X / 2);
                }

            }
        }

        // When player discards a card
        // TODO call card selector method and handle add to discarded cards (clue handling)
        if (/*this.getPlayer().isHumanPlayer() &&*/ controlPanel.isDiscardACard) {
            for (Card card : this.getPlayer().getHand().cards) {
                if (card.getX() == xOfLastCard) {
                    discardACard(mouseE, card, card.getX() + CARD_SIZE_X);
                } else {
                    discardACard(mouseE, card, card.getX() + CARD_SIZE_X / 2);
                }

            }
        }

        // When there is a symbol selected
        // TODO console log szerűen kiírni alá a historyt
        if (this.getPlayer().isAIPlayer() && (SelectedSymbol.getSelectedColor() != null || SelectedSymbol.getSelectedNumber() != null)) {
            for (Card card : this.getPlayer().getHand().cards) {
                if (this.getPlayer().isAIPlayer() && SelectedSymbol.getSelectedColor() != null && SelectedSymbol.getSelectedColor() == card.getColor()) {
                    card.knownColor = true;
                } else if (this.getPlayer().isAIPlayer() && SelectedSymbol.getSelectedNumber() != null && SelectedSymbol.getSelectedNumber() == card.getNumber()) {
                    card.knownNumber = true;
                }
            }
            this.repaint();
            SelectedSymbol.clearSelection();
            tokens.decreaseClues();
            controlPanel.showDefaultWindow();

            List<Player> players = Players.getThePlayers();
            for (Player player : players) {
                for (Card card : player.getHand().cards) {
                    card.reset();
                }
                player.getPlayerPanel().repaint();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseE) {
        int xOfLastCard = CARD_START_POS_X + (NUM_OF_CARDS_IN_HAND - 1) * CARD_OFFSET_X;
        if (this.getPlayer().isHumanPlayer()) {
            for (Card card : this.getPlayer().getHand().cards) {
                if (card.getX() == xOfLastCard) {
                    if (clickContains(mouseE.getX(), mouseE.getY(), card.getX(), card.getY(), card.getX() + CARD_SIZE_X, card.getY() + CARD_SIZE_Y)) {
                        System.out.println("sikerült");
                        card.selected();
                        this.repaint();
                    }
                } else {
                    if (clickContains(mouseE.getX(), mouseE.getY(), card.getX(), card.getY(), card.getX() + CARD_SIZE_X / 2, card.getY() + CARD_SIZE_Y)) {
                        card.selected();
                        this.repaint();
                    }
                }
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent mouseE) {
        int xOfLastCard = CARD_START_POS_X + (NUM_OF_CARDS_IN_HAND - 1) * CARD_OFFSET_X;
        if (this.getPlayer().isHumanPlayer()) {
            for (Card card : this.getPlayer().getHand().cards) {
                if (card.getX() == xOfLastCard) {
                    if (!clickContains(mouseE.getX(), mouseE.getY(), card.getX(), card.getY(), card.getX() + CARD_SIZE_X, card.getY() + CARD_SIZE_Y)) {
                        card.reset();
                        this.repaint();
                    }
                } else {
                    if (!clickContains(mouseE.getX(), mouseE.getY(), card.getX(), card.getY(), card.getX() + CARD_SIZE_X / 2, card.getY() + CARD_SIZE_Y)) {
                        card.reset();
                        this.repaint();
                    }
                }
            }
        }
    }

    private void cardSelector() {
        //TODO mouse over -> card repaint or y position handling
        //lásd feljebb mouseEntered and mouseExited, miért nem jó?
    }

    private void playACard(MouseEvent mouseE, Card card, int endXOfCards) {
        if (clickContains(mouseE.getX(), mouseE.getY(), card.getX(), card.getY(), endXOfCards, card.getY() + CARD_SIZE_Y)) {
            // TODO új kártyát húzni, régit eldobni

            // Check to see if card is playable
            if (Fireworks.getFireworks().addFireworkCard(card)) {
                fireworksPanel.repaint();
            } // Else lose life and discard the card
            else {
                tokens.decreaseLife();
                DiscardedCards.getDiscard().addDiscardedCard(card);
                discardedCardsPanel.repaint();
            }
        }
    }

    private void discardACard(MouseEvent mouseE, Card card, int endXOfCards) {
        if (clickContains(mouseE.getX(), mouseE.getY(), card.getX(), card.getY(), endXOfCards, card.getY() + CARD_SIZE_Y)) {
            // TODO új kártyát húzni, régit eldobni
            if(DiscardedCards.getDiscard().addDiscardedCard(card)){
                tokens.increaseClues();
            }
            discardedCardsPanel.repaint();
        }
    }

    public static boolean clickContains(int mouseX, int mouseY, int startX, int startY, int endX, int endY) {
        return mouseX > startX && mouseX < endX &&
                mouseY > startY && mouseY < endY;
    }

}
