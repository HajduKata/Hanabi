package view;

import model.Card;
import model.CardColor;
import model.Clues;
import model.Fireworks;
import model.Player;
import model.Players;
import model.SelectedSymbol;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import static model.Card.CARD_OFFSET_X;
import static model.Card.CARD_SIZE_X;
import static model.Card.CARD_SIZE_Y;
import static model.Card.numberOfColors;
import static view.HanabiUtilities.SYMBOL_SIZE_X;

/**
 * The view of the table
 * Left panel: players cards
 * Mid panel: fireworks and dropped cards
 * Right panel: control symbols
 */
public class GameTable extends JFrame {
    private static final int TABLE_SIZE_WIDTH = 1200;
    private static final int TABLE_SIZE_HEIGHT = 800;

    public static final int BORDER_SIZE = 10;
    public static final int GAP = 5;

    private static final int LEFT_PANEL_WIDTH = CARD_SIZE_X + 4 * CARD_OFFSET_X + BORDER_SIZE;
    private static final int MID_PANEL_WIDTH = 5 * CARD_SIZE_X + 4 * GAP + BORDER_SIZE;
    public static final int RIGHT_PANEL_WIDTH = (numberOfColors + 1) * SYMBOL_SIZE_X + numberOfColors * GAP + BORDER_SIZE;
    private static final int CONTROL_PANEL_HEIGHT = 150;

    private static final Dimension MIN_TABLE_DIMENSION = new Dimension(TABLE_SIZE_WIDTH, TABLE_SIZE_HEIGHT);
    public static final Dimension LEFT_PANEL_DIMENSION = new Dimension(LEFT_PANEL_WIDTH, TABLE_SIZE_HEIGHT);
    private static final Dimension MID_PANEL_DIMENSION = new Dimension(MID_PANEL_WIDTH, TABLE_SIZE_HEIGHT);
    public static final Dimension RIGHT_PANEL_DIMENSION = new Dimension(RIGHT_PANEL_WIDTH, TABLE_SIZE_HEIGHT);
    public static final Dimension CARD_COLORS_DIMENSION =
            new Dimension(numberOfColors * CARD_SIZE_X + (numberOfColors - 1) * GAP + BORDER_SIZE, CARD_SIZE_Y + 4 * BORDER_SIZE);
    public static final int BUTTON_HEIGHT = 25;

    private final boolean fullscreen = true; // TODO from properties

    public GameTable() {

        setTitle("Hanabi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        if (fullscreen) {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setUndecorated(false); // hide|show window header
        } else {
            setPreferredSize(MIN_TABLE_DIMENSION);
        }

        initTable();

        pack();
        setVisible(true);
        requestFocus();
    }

    private void initTable() {
        // player cards pane
        JPanel playersContainer = new JPanel();
        playersContainer.setPreferredSize(LEFT_PANEL_DIMENSION);
        playersContainer.setLayout(new BoxLayout(playersContainer, BoxLayout.Y_AXIS));
        playersContainer.setAlignmentX(LEFT_ALIGNMENT);
        playersContainer.setAlignmentY(TOP_ALIGNMENT);
        for (Player player : Players.getThePlayers()) {
            PlayerPanel playerPanel = new PlayerPanel(player);
            player.setPlayerPanel(playerPanel);
            playersContainer.add(playerPanel);
        }

        // fireworks and discarded card pane
        JPanel placedCardsContainer = new JPanel();
        placedCardsContainer.setPreferredSize(MID_PANEL_DIMENSION);
        placedCardsContainer.setLayout(new BoxLayout(placedCardsContainer, BoxLayout.Y_AXIS));
        placedCardsContainer.setAlignmentX(LEFT_ALIGNMENT);
        placedCardsContainer.setAlignmentY(TOP_ALIGNMENT);
        // add empty fireworks
        placedCardsContainer.add(new FireworksPanel(Fireworks.getFireworks()));
        // create empty droped cards
        SortedMap<CardColor, Set<Card>> discardedCards = new TreeMap<>();
        Set<Card> emptyCard = new TreeSet<>();
        for (CardColor color : CardColor.values()) {
            emptyCard.add(new Card(color));
            discardedCards.put(color, emptyCard);
        }
        // add empty droped cards
        placedCardsContainer.add(new DiscardedCardsPanel(discardedCards));

        // control pane and information pane
        JPanel controlContainer = new JPanel();
        CardLayout cardLayout = new CardLayout();
        controlContainer.setPreferredSize(RIGHT_PANEL_DIMENSION);
        controlContainer.setLayout(new BoxLayout(controlContainer, BoxLayout.Y_AXIS));
        controlContainer.setAlignmentX(LEFT_ALIGNMENT);
        controlContainer.setAlignmentY(TOP_ALIGNMENT);

        // Clues and Fails
        CluePanel cluePanel = new CluePanel(new Clues(3, 8));
        cluePanel.setMaximumSize(new Dimension(RIGHT_PANEL_WIDTH, 70));
        cluePanel.setLayout(new BoxLayout(cluePanel, BoxLayout.Y_AXIS));
        cluePanel.setAlignmentX(CENTER_ALIGNMENT);
        cluePanel.setAlignmentY(TOP_ALIGNMENT);
        // Control buttons
        JPanel controlButtonsContainer = new JPanel();
        controlButtonsContainer.setLayout(new BoxLayout(controlButtonsContainer, BoxLayout.Y_AXIS));
        controlButtonsContainer.setAlignmentX(CENTER_ALIGNMENT);
        controlButtonsContainer.setAlignmentY(TOP_ALIGNMENT);
        JButton playCardButton = new JButton("Play a card");
        JButton discardCardButton = new JButton("Discard a card");
        JButton giveHintButton = new JButton("Give a hint");
        playCardButton.setMaximumSize(new Dimension(RIGHT_PANEL_WIDTH, BUTTON_HEIGHT));
        discardCardButton.setMaximumSize(new Dimension(RIGHT_PANEL_WIDTH, BUTTON_HEIGHT));
        giveHintButton.setMaximumSize(new Dimension(RIGHT_PANEL_WIDTH, BUTTON_HEIGHT));
        controlButtonsContainer.add(playCardButton);
        controlButtonsContainer.add(Box.createVerticalStrut(10));
        controlButtonsContainer.add(discardCardButton);
        controlButtonsContainer.add(Box.createVerticalStrut(10));
        controlButtonsContainer.add(giveHintButton);
        controlButtonsContainer.add(Box.createVerticalStrut(10));

        JPanel controlPanel = new JPanel(cardLayout);
        Border border = new BorderUIResource.LineBorderUIResource(Color.BLACK);
        controlPanel.setBorder(border);
        controlPanel.setMaximumSize(new Dimension(RIGHT_PANEL_WIDTH, CONTROL_PANEL_HEIGHT));

        JLabel playCardLabel = new JLabel("Select a card to play from your hand");
        JLabel discardCardLabel = new JLabel("Select a card to discard from your hand");
        playCardLabel.setFont (playCardLabel.getFont ().deriveFont (16.0f));
        discardCardLabel.setFont (discardCardLabel.getFont ().deriveFont (16.0f));
        controlPanel.add(playCardLabel, "play");
        controlPanel.add(discardCardLabel, "discard");
        controlPanel.add(new ControlPanel(SelectedSymbol.getSelectedSymbol()), "hint");
        playCardButton.addActionListener(e -> cardLayout.show(controlPanel, "play"));
        discardCardButton.addActionListener(e -> cardLayout.show(controlPanel, "discard"));
        giveHintButton.addActionListener(e -> cardLayout.show(controlPanel, "hint"));

        controlContainer.add(cluePanel);
        controlContainer.add(controlButtonsContainer);
        controlContainer.add(controlPanel);

        // add containers to the card table
        setLayout(new FlowLayout(FlowLayout.LEADING));
        getContentPane().add(playersContainer, BorderLayout.LINE_START);
        getContentPane().add(placedCardsContainer, BorderLayout.CENTER);
        getContentPane().add(controlContainer, BorderLayout.LINE_END);
    }
}
