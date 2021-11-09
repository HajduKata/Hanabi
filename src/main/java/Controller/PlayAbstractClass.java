package controller;

import model.HanabiCards;
import model.Player;
import model.Players;

public abstract class PlayAbstractClass {
    protected Players players;

    void initGame(int numberOfPlayers) {
        players = new Players();
        HanabiCards.DECK.shuffle();
        players.setupPlayers(numberOfPlayers);
    }

    public abstract boolean play();

    abstract void playerTurn(Player actualPlayer);
}
