package view;

import model.Card;
import model.CardColor;
import model.CardNumber;
import model.DiscardedCards;
import model.Fireworks;
import model.HanabiCards;
import model.History;
import model.Player;
import model.Players;
import model.SelectedSymbol;
import model.Tokens;

import javax.swing.JPanel;
import javax.swing.plaf.BorderUIResource.TitledBorderUIResource;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
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

public class PlayerPanel extends JPanel implements MouseListener, MouseMotionListener {
    private static final Color BG_COLOR = Color.decode("#003366");

    private final Player player;
    private final Tokens tokens;
    private final ControlPanel controlPanel;
    private final FireworksPanel fireworksPanel;
    private final DiscardedCardsPanel discardedCardsPanel;
    private History history;

    PlayerPanel(Player player, Tokens tokens, ControlPanel controlPanel, FireworksPanel fireworksPanel, DiscardedCardsPanel discardedCardsPanel, History history) {
        this.player = player;
        this.tokens = tokens;
        this.controlPanel = controlPanel;
        this.fireworksPanel = fireworksPanel;
        this.discardedCardsPanel = discardedCardsPanel;
        this.history = history;

        setPreferredSize(LEFT_PANEL_DIMENSION);
        setBackground(BG_COLOR);
        setForeground(Color.BLACK);
        TitledBorderUIResource border = new TitledBorderUIResource(player.getName());
        border.setTitleFont(new Font("SansSerif", Font.PLAIN, 14));
        border.setTitleColor(Color.WHITE);
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

    private static BufferedImage loadBackOfCards() {
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
        if (/*this.getPlayer().isHumanPlayer() &&*/ controlPanel.isPlayACard) {
            Card clickedCard = new Card(CardColor.RED);
            for (Card card : this.getPlayer().getHand().cards) {
                if (card.getX() == xOfLastCard) {
                    if (clickContains(mouseE.getX(), mouseE.getY(), card.getX(), card.getY(), card.getX() + CARD_SIZE_X, card.getY() + CARD_SIZE_Y)) {
                        clickedCard = card;
                    }
                } else {
                    if (clickContains(mouseE.getX(), mouseE.getY(), card.getX(), card.getY(), card.getX() + CARD_SIZE_X / 2, card.getY() + CARD_SIZE_Y)) {
                        clickedCard = card;
                    }
                }
            }
            if (!clickedCard.getNumber().equals(CardNumber.ZERO)) {
                playACard(clickedCard);
            }
        }

        // When player discards a card
        if (/*this.getPlayer().isHumanPlayer() &&*/ controlPanel.isDiscardACard) {
            Card clickedCard = new Card(CardColor.RED);
            for (Card card : this.getPlayer().getHand().cards) {
                if (card.getX() == xOfLastCard) {
                    if (clickContains(mouseE.getX(), mouseE.getY(), card.getX(), card.getY(), card.getX() + CARD_SIZE_X, card.getY() + CARD_SIZE_Y)) {
                        clickedCard = card;
                    }
                } else {
                    if (clickContains(mouseE.getX(), mouseE.getY(), card.getX(), card.getY(), card.getX() + CARD_SIZE_X / 2, card.getY() + CARD_SIZE_Y)) {
                        clickedCard = card;
                    }
                }
            }
            if (!clickedCard.getNumber().equals(CardNumber.ZERO)) {
                discardACard(clickedCard);
            }
        }

        // When there is a symbol selected
        if (this.getPlayer().isAIPlayer() && (SelectedSymbol.getSelectedColor() != null || SelectedSymbol.getSelectedNumber() != null)) {
            String color = "";
            String number = "";
            for (Card card : this.getPlayer().getHand().cards) {
                if (this.getPlayer().isAIPlayer() && SelectedSymbol.getSelectedColor() != null) {
                    color = history.getHistoryColor(SelectedSymbol.getSelectedColor());
                    if(SelectedSymbol.getSelectedColor() == card.getColor()) {
                        card.knownColor = true;
                    }
                } else if (this.getPlayer().isAIPlayer() && SelectedSymbol.getSelectedNumber() != null) {
                    number = history.getHistoryNumber(SelectedSymbol.getSelectedNumber());
                    if(SelectedSymbol.getSelectedNumber() == card.getNumber()) {
                        card.knownNumber = true;
                    }
                }
            }
            this.repaint();
            SelectedSymbol.clearSelection();
            tokens.decreaseClues();
            history.addString(this.getPlayer().getName(), color, number);
            controlPanel.showDefaultWindow();
            controlPanel.repaint();

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
    }

    @Override
    public void mouseExited(MouseEvent mouseE) {
    }

    @Override
    public void mouseMoved(MouseEvent mouseE) {
        //TODO ha a panelből kivisszük az egeret, a kártya selected marad
        int xOfLastCard = CARD_START_POS_X + (NUM_OF_CARDS_IN_HAND - 1) * CARD_OFFSET_X;
        if (this.getPlayer().isHumanPlayer() && (controlPanel.isPlayACard || controlPanel.isDiscardACard)) {
            for (Card card : this.getPlayer().getHand().cards) {
                card.reset();
                if (card.getX() == xOfLastCard) {
                    if (clickContains(mouseE.getX(), mouseE.getY(), card.getX(), card.getY(), card.getX() + CARD_SIZE_X, card.getY() + CARD_SIZE_Y)) {
                        card.selected();
                    }
                } else {
                    if (clickContains(mouseE.getX(), mouseE.getY(), card.getX(), card.getY(), card.getX() + CARD_SIZE_X / 2, card.getY() + CARD_SIZE_Y)) {
                        card.selected();
                    }
                }
                this.repaint();
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    private void drawNewCard(Player player, Card oldCard) {
        player.getHand().remove(oldCard);
        Card newCard = HanabiCards.DECK.pop();
        if (!newCard.getNumber().equals(CardNumber.ZERO)) {
            player.getHand().add(newCard);
        }
    }

    private void playACard(Card card) {
        // Check to see if card is playable
        if (Fireworks.getFireworks().addFireworkCard(card)) {
            fireworksPanel.repaint();
        } // Else lose life and discard the card
        else {
            tokens.decreaseLife();
            DiscardedCards.getDiscard().addDiscardedCard(card);
            discardedCardsPanel.repaint();
        }
        controlPanel.isPlayACard = false;
        controlPanel.showDefaultWindow();
        drawNewCard(this.getPlayer(), card);
        this.repaint();
    }

    private void discardACard(Card card) {
        // Check to see if card is discardable
        if (DiscardedCards.getDiscard().addDiscardedCard(card)) {
            tokens.increaseClues();
        }
        controlPanel.isDiscardACard = false;
        controlPanel.showDefaultWindow();
        discardedCardsPanel.repaint();
        drawNewCard(this.getPlayer(), card);
        this.repaint();
    }

    private static boolean clickContains(int mouseX, int mouseY, int startX, int startY, int endX, int endY) {
        return mouseX > startX && mouseX < endX &&
                mouseY > startY && mouseY < endY;
    }
}
