package model;

import view.PlayerPanel;

import static model.Card.CARD_OFFSET_X;
import static view.HanabiUtilities.CARD_START_POS_X;
import static view.HanabiUtilities.CARD_START_POS_Y;

public class Player {
    private static int index = 1;

    private PlayerPanel playerPanel;
    private String name;
    private boolean humanPlayer;
    private boolean firstPlayer;
    private Hand hand;
    private volatile boolean theirTurn;

    Player(boolean humanPlayer) {
        this.hand = new Hand();
        this.humanPlayer = humanPlayer;
        this.theirTurn = false;
        // Generate name
        if (humanPlayer) {
            this.name = "Te";
            index = 1;
        } else {
            this.name = "Gépi Játékos " + index;
            index++;
        }

        for (int i = 0; i < Hand.getNumberOfCardsInHand(); i++) {
            // Deal cards
            Card card = HanabiCards.DECK.pop();
            card.setX(CARD_START_POS_X + i * CARD_OFFSET_X);
            card.setY(CARD_START_POS_Y);
            hand.add(card);
        }
    }

    public Hand getHand() {
        return hand;
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
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

    public boolean isTheirTurn() {
        return theirTurn;
    }

    public void setTheirTurn(boolean theirTurn) {
        this.theirTurn = theirTurn;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Player)) {
            return false;
        }
        Player player = (Player) obj;
        return this.name.equals(player.name);
    }
}
