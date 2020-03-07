package view;

import model.Card;
import model.Player;
import model.Players;
import model.SelectedSymbol;
import model.Tokens;

import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;

import static view.GameTable.LEFT_PANEL_DIMENSION;
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

    PlayerPanel(Player player, Tokens tokens, ControlPanel controlPanel) {
        this.player = player;
        this.tokens = tokens;
        this.controlPanel = controlPanel;

        setPreferredSize(LEFT_PANEL_DIMENSION);
        setBackground(BG_COLOR);
        setForeground(Color.BLACK);
        Border border = new BorderUIResource.TitledBorderUIResource(player.getName());
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
                drawCard(g, backOfCard, card.getX(), card.getY(), Card.CARD_SIZE_X, Card.CARD_SIZE_Y);
                drawSymbols(g, card);
            }
        } else { // AI players
            for (Card card : player.getHand().cards) {
                drawCard(g, card.image, card.getX(), card.getY(), Card.CARD_SIZE_X, Card.CARD_SIZE_Y);
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
        if (controlPanel.isPlayACard) {
            //TODO call card selector method and handle add to fireworks (life handling)
        }

        if (controlPanel.isDiscardACard) {
            //TODO call card selector method and handle add to discarded cards (clue handling)
        }

        // If there is no symbol selected
        if (SelectedSymbol.getSelectedColor() == null && SelectedSymbol.getSelectedNumber() == null) {
            return;
        }

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

        List<Player> players = Players.getThePlayers();
        for (Player player : players) {
            for (Card card : player.getHand().cards) {
                card.reset();
            }
            player.getPlayerPanel().repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
