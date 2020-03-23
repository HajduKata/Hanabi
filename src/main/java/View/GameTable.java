package view;

import model.DiscardedCards;
import model.Fireworks;
import model.History;
import model.Player;
import model.Players;
import model.Tokens;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

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
    static final int RIGHT_PANEL_WIDTH = (numberOfColors + 1) * SYMBOL_SIZE_X + numberOfColors * GAP + BORDER_SIZE;
    static final int CONTROL_PANEL_HEIGHT = 150;

    private static final Dimension MIN_TABLE_DIMENSION = new Dimension(TABLE_SIZE_WIDTH, TABLE_SIZE_HEIGHT);
    static final Dimension LEFT_PANEL_DIMENSION = new Dimension(LEFT_PANEL_WIDTH, TABLE_SIZE_HEIGHT);
    private static final Dimension MID_PANEL_DIMENSION = new Dimension(MID_PANEL_WIDTH, TABLE_SIZE_HEIGHT);
    private static final Dimension RIGHT_PANEL_DIMENSION = new Dimension(RIGHT_PANEL_WIDTH, TABLE_SIZE_HEIGHT);
    static final Dimension CARD_COLORS_DIMENSION =
            new Dimension(numberOfColors * CARD_SIZE_X + (numberOfColors - 1) * GAP + BORDER_SIZE, CARD_SIZE_Y + 4 * BORDER_SIZE);
    static final int BUTTON_HEIGHT = 25;

    private ControlPanel controlPanel;
    private FireworksPanel fireworksPanel;
    private DiscardedCardsPanel discardedCardsPanel;
    private CluePanel cluePanel;

    private JPanel playersContainer;
    private JPanel placedCardsContainer;
    private JPanel controlContainer;

    public GameTable() {
        setTitle("Hanabi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // fullscreen
        setUndecorated(false); // hide|show window header

        initTable();

        pack();
        setVisible(true);
        requestFocus();
    }

    private void initTable() {
        // Fireworks and Discarded cards panels
        placedCardsContainer = new JPanel();
        placedCardsContainer.setPreferredSize(MID_PANEL_DIMENSION);
        placedCardsContainer.setLayout(new BoxLayout(placedCardsContainer, BoxLayout.Y_AXIS));
        placedCardsContainer.setAlignmentX(LEFT_ALIGNMENT);
        placedCardsContainer.setAlignmentY(TOP_ALIGNMENT);
        // Initializing fireworks panel
        fireworksPanel = new FireworksPanel(Fireworks.getFireworks());
        // Add empty fireworks to container
        placedCardsContainer.add(fireworksPanel);
        // Initializing discarded cards panel
        discardedCardsPanel = new DiscardedCardsPanel(DiscardedCards.getDiscard());
        // Add empty discarded cards to container
        placedCardsContainer.add(discardedCardsPanel);

        // Initializing Control Panel
        controlPanel = new ControlPanel();
        // Control panel and information panel
        controlContainer = new JPanel();
        controlContainer.setPreferredSize(RIGHT_PANEL_DIMENSION);
        controlContainer.setLayout(new BoxLayout(controlContainer, BoxLayout.Y_AXIS));
        controlContainer.setAlignmentX(LEFT_ALIGNMENT);
        controlContainer.setAlignmentY(TOP_ALIGNMENT);

        // Clues and Fails
        cluePanel = new CluePanel(Tokens.getTokens());
        cluePanel.setMaximumSize(new Dimension(RIGHT_PANEL_WIDTH, 70));
        cluePanel.setLayout(new BoxLayout(cluePanel, BoxLayout.Y_AXIS));
        cluePanel.setAlignmentX(CENTER_ALIGNMENT);
        cluePanel.setAlignmentY(TOP_ALIGNMENT);

        // Add CluePanel (life + clues) and controlPanel to container
        controlContainer.add(cluePanel);
        controlContainer.add(controlPanel);


        // Player cards panels
        playersContainer = new JPanel();
        playersContainer.setPreferredSize(LEFT_PANEL_DIMENSION);
        playersContainer.setLayout(new BoxLayout(playersContainer, BoxLayout.Y_AXIS));
        playersContainer.setAlignmentX(LEFT_ALIGNMENT);
        playersContainer.setAlignmentY(TOP_ALIGNMENT);
        for (Player player : Players.getThePlayers()) {
            PlayerPanel playerPanel = new PlayerPanel(player, controlPanel, fireworksPanel, discardedCardsPanel);
            playerPanel.addMouseListener(playerPanel);
            playerPanel.addMouseMotionListener(playerPanel);
            player.setPlayerPanel(playerPanel);
            playersContainer.add(playerPanel);
        }

        // Add containers to the card table
        setLayout(new FlowLayout(FlowLayout.LEADING));
        getContentPane().add(playersContainer, BorderLayout.LINE_START);
        getContentPane().add(placedCardsContainer, BorderLayout.CENTER);
        getContentPane().add(controlContainer, BorderLayout.LINE_END);
    }

    public ControlPanel getControlPanel() {
        return controlPanel;
    }

    public void repaintAll() {
        this.repaint();
        controlPanel.repaint();
    }


}
