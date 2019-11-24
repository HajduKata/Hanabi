package Model;

public class Player {
    private Hand hand;
    private boolean humanPlayer;
    public String name;
    private static int index = 1;
    private int x;
    private int y;

    public Player(boolean humanPlayer) {
        this.hand = new Hand();
        this.humanPlayer = humanPlayer;
        for (int i = 0; i < Hand.NUM_OF_CARDS_IN_HAND; i++) {
            hand.add(Deck.DECK.pop());
        }
        if(humanPlayer) {
            this.name = "You";
        } else {
            this.name = "AI Player" + index;
            index++;
        }
    }

    public Hand getHand() {
        return hand;
    }

    public boolean isHumanPlayer() {
        return humanPlayer;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
