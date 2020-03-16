package view;

import model.Card;
import model.CardColor;
import model.DiscardedCards;
import model.Fireworks;
import model.Player;
import model.Players;
import model.Tokens;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
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

    private static final int BORDER_SIZE = 10;
    private static final int GAP = 5;

    private static final int LEFT_PANEL_WIDTH = CARD_SIZE_X + 5 * CARD_OFFSET_X + BORDER_SIZE;
    private static final int MID_PANEL_WIDTH = 5 * CARD_SIZE_X + 4 * GAP + BORDER_SIZE;
    public static final int RIGHT_PANEL_WIDTH = (numberOfColors + 1) * SYMBOL_SIZE_X + numberOfColors * GAP + BORDER_SIZE;
    public static final int CONTROL_PANEL_HEIGHT = 150;

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
        // Initialization with 8 clues and 3 lives
        Tokens tokens = Tokens.getTokens();

        // Fireworks and Discarded cards panels
        JPanel placedCardsContainer = new JPanel();
        placedCardsContainer.setPreferredSize(MID_PANEL_DIMENSION);
        placedCardsContainer.setLayout(new BoxLayout(placedCardsContainer, BoxLayout.Y_AXIS));
        placedCardsContainer.setAlignmentX(LEFT_ALIGNMENT);
        placedCardsContainer.setAlignmentY(TOP_ALIGNMENT);
        // Initializing fireworks panel
        FireworksPanel fireworksPanel = new FireworksPanel(Fireworks.getFireworks());
        // Add empty fireworks to container
        placedCardsContainer.add(fireworksPanel);
        // Create empty discard piles
        DiscardedCards discard = DiscardedCards.getDiscard();
        // Initializing discarded cards panel
        DiscardedCardsPanel discardedCardsPanel = new DiscardedCardsPanel(discard);
        // Add empty dropped cards to container
        placedCardsContainer.add(discardedCardsPanel);

        // Initializing Control Panel
        ControlPanel controlPanel = new ControlPanel();
        // Control panel and information panel
        JPanel controlContainer = new JPanel();
        controlContainer.setPreferredSize(RIGHT_PANEL_DIMENSION);
        controlContainer.setLayout(new BoxLayout(controlContainer, BoxLayout.Y_AXIS));
        controlContainer.setAlignmentX(LEFT_ALIGNMENT);
        controlContainer.setAlignmentY(TOP_ALIGNMENT);

        // Clues and Fails
        CluePanel cluePanel = new CluePanel(tokens);
        cluePanel.setMaximumSize(new Dimension(RIGHT_PANEL_WIDTH, 70));
        cluePanel.setLayout(new BoxLayout(cluePanel, BoxLayout.Y_AXIS));
        cluePanel.setAlignmentX(CENTER_ALIGNMENT);
        cluePanel.setAlignmentY(TOP_ALIGNMENT);

        // Add CluePanel (life + clues) and controlPanel to container
        controlContainer.add(cluePanel);
        controlContainer.add(controlPanel);

        // Player cards panels
        JPanel playersContainer = new JPanel();
        playersContainer.setPreferredSize(LEFT_PANEL_DIMENSION);
        playersContainer.setLayout(new BoxLayout(playersContainer, BoxLayout.Y_AXIS));
        playersContainer.setAlignmentX(LEFT_ALIGNMENT);
        playersContainer.setAlignmentY(TOP_ALIGNMENT);
        for (Player player : Players.getThePlayers()) {
            PlayerPanel playerPanel = new PlayerPanel(player, tokens, controlPanel, fireworksPanel, discardedCardsPanel);
            playerPanel.addMouseListener(playerPanel);
            player.setPlayerPanel(playerPanel);
            playersContainer.add(playerPanel);
        }

        // Add containers to the card table
        setLayout(new FlowLayout(FlowLayout.LEADING));
        getContentPane().add(playersContainer, BorderLayout.LINE_START);
        getContentPane().add(placedCardsContainer, BorderLayout.CENTER);
        getContentPane().add(controlContainer, BorderLayout.LINE_END);
    }
}
