package View;

import Model.Card;
import Model.CardColor;
import Model.CardNumber;
import Model.ClueSymbol;
import Model.Deck;
import Model.Player;
import Model.Players;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

// CardTable
public class GameTable extends JComponent implements MouseListener {
    private static final int TABLE_SIZE = 800;    // Pixels.
    private static final int MAX_NUMBER_OF_PLAYERS = 5;
    private static final int MAX_NUMBER_OF_CLUES = 8;
    private static final int MAX_NUMBER_OF_FAILS = 3;

    public static final int CARD_WIDTH_HEIGHT = 130;
    public static final int COMPONENT_WIDTH_HEIGHT = 40;
    public static final int CARD_OFFSET = 60;
    public static final int COLOR_OFFSET_X = 18;
    public static final int COLOR_OFFSET_Y = 40;
    public static final int NUMBER_OFFSET_X = 18;
    public static final int NUMBER_OFFSET_Y = 5;

    public static final int CANCEL_STARTING_X = 1350;
    public static final int CANCEL_STARTING_Y = COMPONENT_WIDTH_HEIGHT + 5;

    public List<Player> players;
    private List<ClueSymbol> symbols;
    private int numberOfPlayers = 3; //MAX_NUMBER_OF_PLAYERS
    private int numberOfClues = MAX_NUMBER_OF_CLUES;
    private int numberOfFails = MAX_NUMBER_OF_FAILS;

    private Image backOfCard;

    private boolean hasSelectedSymbol;
    private CardColor selectedColor;
    private CardNumber selectedNumber;

    public GameTable() {
        //... Add mouse listeners.
        addMouseListener(this);
        loadBackOfCards();

        initGame(numberOfPlayers);
    }

    private void initGame(int numberOfPlayers) {
        ClassLoader classLoader = this.getClass().getClassLoader();

        Deck.DECK.shuffle();
        symbols = new ArrayList<>(10);
        players = Players.getThePlayers(numberOfPlayers);
        //This is the human player
        Player human = players.get(0);
        for (int i = 0; i < human.getHand().cards.size(); i++) {
            human.getHand().cards.get(i).setX(10 + i * CARD_OFFSET);
            human.getHand().cards.get(i).setY(20);
        }
        human.setX(10);
        human.setY(20);
        players.add(human);

        //These are the AI players
        for (int i = 1; i < numberOfPlayers; i++) {
            Player ai = players.get(i);
            for (int j = 0; j < ai.getHand().cards.size(); j++) {
                ai.getHand().cards.get(j).setX(10 + j * CARD_OFFSET);
                ai.getHand().cards.get(j).setY(20 + i * (CARD_WIDTH_HEIGHT + 20));
            }
            ai.setX(10);
            ai.setY(20 + i * (CARD_WIDTH_HEIGHT + 20));
            players.add(ai);
        }

        //Symbols
        //Colors
        int x = 1100; // absolute pixel
        int y = 20;
        for (CardColor color : CardColor.values()) {
            String actualColor = getColorName(color);
            URL imageURL = classLoader.getResource("hanabi_cards/" + actualColor + ".png");
            ClueSymbol symbol = new ClueSymbol(loadCardImage(imageURL), x, y, color, null);
            symbols.add(symbol);
            x += COMPONENT_WIDTH_HEIGHT + 5;
        }
        //Numbers
        x = 1100; // absolute pixel
        y += 50;
        for (CardNumber number : CardNumber.values()) {
            URL imageURL = classLoader.getResource("hanabi_cards/" + number.getValue() + ".png");
            ClueSymbol symbol = new ClueSymbol(loadCardImage(imageURL), x, y, null, number);
            symbols.add(symbol);
            x += COMPONENT_WIDTH_HEIGHT + 5;
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1500, 870);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        //Background
        g.setColor(new Color(220, 220, 255));
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.BLACK);

        for (Player player : players) {
            g2d.setFont(new Font("LithosPro-Black", Font.BOLD, 14));
            g2d.drawString(player.name, player.getX(), player.getY() - 5);
            paintCard(g, player);
        }

        g2d.drawString("Goal Columns", 400, 15);
        paintGoalColumns(g);
        g.setColor(Color.BLACK);
        g2d.drawString("Discard Piles", 400, CARD_WIDTH_HEIGHT + 35);
        paintDiscardPile(g);
        paintClueSymbols(g);
        g.setColor(Color.BLACK);
        g2d.drawString("Clue Tokens", 1100, 195);
        paintClueTokens(g);
        g.setColor(Color.BLACK);
        g2d.drawString("Fail Tokens", 1100, 260);
        paintFailTokens(g);
        paintCancelButton(g);
    }

    private void paintCard(Graphics g, Player player) {
        if (player.isHumanPlayer()) {
            for (Card card : player.getHand().cards) {
                g.drawImage(backOfCard, card.getX(), card.getY(), CARD_WIDTH_HEIGHT, CARD_WIDTH_HEIGHT, this);
                for (ClueSymbol symbol : symbols) {
                    if (card.knowsColor && card.getColor().equals(symbol.getColor())) {
                        g.drawImage(symbol.getImage(), card.getX() + COLOR_OFFSET_X, card.getY() + COLOR_OFFSET_Y, COMPONENT_WIDTH_HEIGHT, COMPONENT_WIDTH_HEIGHT, this);
                    }
                    if (card.knowsNumber && card.getNumber().equals(symbol.getNumber())) {
                        g.drawImage(symbol.getImage(), card.getX() + NUMBER_OFFSET_X, card.getY() + NUMBER_OFFSET_Y, COMPONENT_WIDTH_HEIGHT, COMPONENT_WIDTH_HEIGHT, this);
                    }
                }
            }
        } else {
            for (Card card : player.getHand().cards) {
                g.drawImage(card.image, card.getX(), card.getY(), CARD_WIDTH_HEIGHT, CARD_WIDTH_HEIGHT, this);
                for (ClueSymbol symbol : symbols) {
                    if (card.knowsColor && card.getColor().equals(symbol.getColor())) {
                        g.drawImage(symbol.getImage(), card.getX() + COLOR_OFFSET_X, card.getY() + COLOR_OFFSET_Y, COMPONENT_WIDTH_HEIGHT - 10, COMPONENT_WIDTH_HEIGHT - 10, this);
                    }
                    if (card.knowsNumber && card.getNumber().equals(symbol.getNumber())) {
                        g.drawImage(symbol.getImage(), card.getX() + NUMBER_OFFSET_X, card.getY() + NUMBER_OFFSET_Y, COMPONENT_WIDTH_HEIGHT - 10, COMPONENT_WIDTH_HEIGHT - 10, this);
                    }
                }
            }
        }
    }

    private void paintGoalColumns(Graphics g) {
        int x = 400;
        int y = 20;
        for (CardColor color : CardColor.values()) {
            g.setColor(color.getPaintColor(color));
            g.drawRect(x, y, CARD_WIDTH_HEIGHT, CARD_WIDTH_HEIGHT);
            //g.drawImage(backOfCard, x, y, CARD_WIDTH_HEIGHT, CARD_WIDTH_HEIGHT, this);
            x += CARD_WIDTH_HEIGHT + 5;
        }
    }

    private void paintDiscardPile(Graphics g) {
        int x = 400;
        int y = CARD_WIDTH_HEIGHT + 40;
        for (CardColor color : CardColor.values()) {
            g.setColor(color.getPaintColor(color));
            g.drawRect(x, y, CARD_WIDTH_HEIGHT, CARD_WIDTH_HEIGHT);
            //g.drawImage(card, x, y, CARD_WIDTH_HEIGHT, CARD_WIDTH_HEIGHT, this);
            y += CARD_WIDTH_HEIGHT + 5;
        }
    }

    private void paintClueSymbols(Graphics g) {
        for (ClueSymbol symbol : symbols) {
            g.drawImage(symbol.getImage(), symbol.getX(), symbol.getY(), COMPONENT_WIDTH_HEIGHT, COMPONENT_WIDTH_HEIGHT, this);
        }
    }

    private String getColorName(CardColor color) {
        String actual = "";
        switch (color) {
            case BLUE:
                actual = "blue";
                break;
            case GREEN:
                actual = "green";
                break;
            case RED:
                actual = "red";
                break;
            case WHITE:
                actual = "white";
                break;
            case YELLOW:
                actual = "yellow";
                break;
        }
        return actual;
    }

    private void paintClueTokens(Graphics g) {
        int x = 1100;
        int y = 200;
        Image clue;
        ClassLoader cldr = this.getClass().getClassLoader();
        URL imageURL = cldr.getResource("hanabi_cards/clue.png");
        for (int i = 0; i < numberOfClues; i++) {
            try {
                assert imageURL != null;
                clue = ImageIO.read(new File(imageURL.getPath()));
                g.drawImage(clue, x, y, COMPONENT_WIDTH_HEIGHT, COMPONENT_WIDTH_HEIGHT, this);
                x += COMPONENT_WIDTH_HEIGHT + 5;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void paintFailTokens(Graphics g) {
        int x = 1100;
        int y = 265;
        Image clue;
        ClassLoader cldr = this.getClass().getClassLoader();
        URL imageURL = cldr.getResource("hanabi_cards/fail.png");
        for (int i = 0; i < numberOfFails; i++) {
            try {
                assert imageURL != null;
                clue = ImageIO.read(new File(imageURL.getPath()));
                g.drawImage(clue, x, y, COMPONENT_WIDTH_HEIGHT, COMPONENT_WIDTH_HEIGHT, this);
                x += COMPONENT_WIDTH_HEIGHT + 5;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void paintCancelButton(Graphics g) {
        Image cancelButton;
        ClassLoader cldr = this.getClass().getClassLoader();
        URL imageURL = cldr.getResource("Cancel.png");
        try {
            assert imageURL != null;
            cancelButton = ImageIO.read(new File(imageURL.getPath()));
            g.drawImage(cancelButton, CANCEL_STARTING_X, CANCEL_STARTING_Y, COMPONENT_WIDTH_HEIGHT + 5, COMPONENT_WIDTH_HEIGHT + 5, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Image loadCardImage(URL imageURL) {
        try {
            assert imageURL != null;
            return ImageIO.read(new File(imageURL.getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void loadBackOfCards() {
        ClassLoader cldr = this.getClass().getClassLoader();
        URL imageURL = cldr.getResource("hanabi_cards/zbackground.png");
        try {
            assert imageURL != null;
            backOfCard = ImageIO.read(new File(imageURL.getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        //if the mouse clicked on a symbol
        for (ClueSymbol symbol : symbols) {
            if (symbol.contains(x, y)) {
                showSelectedSymbols(symbol);
                hasSelectedSymbol = true;
            }
        }

        //if the mouse clicked on the cancel button
        if (clickContains(x, y, CANCEL_STARTING_X, CANCEL_STARTING_Y, CANCEL_STARTING_X + COMPONENT_WIDTH_HEIGHT, CANCEL_STARTING_Y + COMPONENT_WIDTH_HEIGHT)) {
            resetAllCards();
        }

        //if the mouse clicked on a player's hand
        for (Player player : players) {
            if (!player.isHumanPlayer()) {
                if (clickContains(x, y, player.getX(), player.getY(), player.getX() + CARD_WIDTH_HEIGHT + 4 * CARD_OFFSET, player.getY() + CARD_WIDTH_HEIGHT)) {
                    if (hasSelectedSymbol && decreaseClues()) {
                        markSelectedClue(player);
                        resetAllCards();
                    }
                    break;
                }
            }
        }

        //after every click, repaint the whole screen
        this.repaint();
    }

    /* This method highlights all of the cards that match the selected symbol */
    private void showSelectedSymbols(ClueSymbol symbol) {
        for (Player player : players) {
            if (!player.isHumanPlayer()) {
                for (Card card : player.getHand().cards) {
                    card.reset();
                    if (symbol.getColor() != null && symbol.getColor().equals(card.getColor())
                            || symbol.getNumber() != null && symbol.getNumber().equals(card.getNumber())) {
                        card.selected();
                        selectedColor = symbol.getColor();
                        selectedNumber = symbol.getNumber();
                        //TODO keret a kijelölt képeknek - graphicsot nem lehet itt elérni?
                        /*g.setColor(Color.MAGENTA);
                        g.drawRect(1500, 1500, 30, 30);
                        g.drawRect(card.getX() - 1, card.getY() - 1, CARD_WIDTH_HEIGHT + 1, CARD_WIDTH_HEIGHT + 1);*/
                    }
                }
            }
        }
    }

    private void resetAllCards() {
        for (Player player : players) {
            if (!player.isHumanPlayer()) {
                for (Card card : player.getHand().cards) {
                    card.reset();
                    hasSelectedSymbol = false;
                    selectedColor = null;
                    selectedNumber = null;
                }
            }
        }
    }

    private boolean decreaseClues() {
        if (numberOfClues > 0) {
            numberOfClues--;
            return true;
        }
        return false;
    }

    private void markSelectedClue(Player player) {
        for (Card card : player.getHand().cards) {
            if (card.isSelected()) {
                if (card.getColor().equals(selectedColor)) {
                    card.knowsColor = true;
                }
                if (card.getNumber().equals(selectedNumber)) {
                    card.knowsNumber = true;
                }
            }
        }
    }

    public static boolean clickContains(int mouseX, int mouseY, int startX, int startY, int endX, int endY) {
        return mouseX > startX && mouseX < endX &&
                mouseY > startY && mouseY < endY;
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
