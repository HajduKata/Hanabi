package model;

import view.PlayerPanel;

import static java.util.Optional.ofNullable;
import static model.Card.CARD_OFFSET_X;
import static view.HanabiUtilities.CARD_START_POS_X;
import static view.HanabiUtilities.CARD_START_POS_Y;

public class Player {
    private static int index = 1;

    private PlayerPanel playerPanel;
    private String name;
    private int x;
    private int y;
    private boolean humanPlayer;
    private boolean firstPlayer;
    private Hand hand;

    Player(boolean humanPlayer) {
        this.hand = new Hand();
        this.humanPlayer = humanPlayer;
        // Generate name
        if (humanPlayer) {
            this.name = "Te";
        } else {
            this.name = "AI Játékos " + index;
            index++;
        }

        for (int i = 0; i < Hand.NUM_OF_CARDS_IN_HAND; i++) {
            // Deal cards
            Card card = ofNullable(HanabiCards.DECK.pop()).orElseThrow(IllegalArgumentException::new);
            card.setX(CARD_START_POS_X + i * CARD_OFFSET_X);
            card.setY(CARD_START_POS_Y);
            card.knownColor = false;
            card.knownNumber = false;
            hand.add(card);
        }
    }

    public Hand getHand() {
        return hand;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHumanPlayer() {
        return humanPlayer;
    }

    public boolean isAIPlayer() {
        return !humanPlayer;
    }

    public boolean isFirstPlayer() {
        return firstPlayer;
    }

    public void setFirstPlayer(boolean firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public PlayerPanel getPlayerPanel() {
        return playerPanel;
    }

    public void setPlayerPanel(PlayerPanel playerPanel) {
        this.playerPanel = playerPanel;
    }
}
