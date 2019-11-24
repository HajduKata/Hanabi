package Controller;

import Model.Card;
import Model.CardColor;
import Model.CardNumber;
import Model.ClueSymbol;
import Model.Deck;
import Model.Player;
import Model.Players;
import View.GameTable;

import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;


public class PlayHanabi extends JFrame implements MouseListener, ActionListener {
    private static final int MAX_NUMBER_OF_PLAYERS = 5;
    private static final int MAX_NUMBER_OF_CLUES = 8;
    private static final int MAX_NUMBER_OF_FAILS = 3;

    private int numberOfPlayers = 3; //MAX_NUMBER_OF_PLAYERS
    private int numberOfClues = MAX_NUMBER_OF_CLUES;
    private int numberOfFails = MAX_NUMBER_OF_FAILS;

    private Deck deck = Deck.DECK;
    private boolean endOfGame;
    private boolean hasSelectedSymbol;
    private CardColor selectedColor;
    private CardNumber selectedNumber;
    private int turn = 0;

    private List<ClueSymbol> symbols;

    public PlayHanabi() {
        addMouseListener(this);
    }


    public void play() {
        while (!endOfGame) {
            turn++;

        }
    }


    public static void main(String[] args) {
        PlayHanabi window = new PlayHanabi();
        window.setTitle("Hanabi");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setContentPane(new GameTable());
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    private void initGame(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        Deck.DECK.shuffle();
        symbols = new ArrayList<>(10);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

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

        int startX = GameTable.CANCEL_STARTING_X;
        int startY = GameTable.CANCEL_STARTING_Y;
        int endX = GameTable.CANCEL_STARTING_X + GameTable.COMPONENT_WIDTH_HEIGHT;
        int endY = GameTable.CANCEL_STARTING_Y + GameTable.COMPONENT_WIDTH_HEIGHT;
        //if the mouse clicked on the cancel button
        if (GameTable.clickContains(x, y, startX, startY, endX, endY)) {
            resetAllCards();
        }

        for (Player player : Players.getThePlayers()) {
            if (!player.isHumanPlayer()) {
                startX = player.getX();
                startY = player.getY();
                endX = player.getX() + GameTable.CARD_WIDTH_HEIGHT + 4 * GameTable.CARD_OFFSET;
                endY = player.getY() + GameTable.CARD_WIDTH_HEIGHT;
                //if the mouse clicked on a player's hand
                if (GameTable.clickContains(x, y, startX, startY, endX, endY)) {
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
        for (Player player : Players.getThePlayers()) {
            if (!player.isHumanPlayer()) {
                for (Card card : player.getHand().cards) {
                    card.reset();
                    if (symbol.getColor() != null && symbol.getColor().equals(card.getColor())
                            || symbol.getNumber() != null && symbol.getNumber().equals(card.getNumber())) {
                        card.selected();
                        selectedColor = symbol.getColor();
                        selectedNumber = symbol.getNumber();
                    }
                }
            }
        }
    }

    private void resetAllCards() {
        for (Player player : Players.getThePlayers()) {
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
