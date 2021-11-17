package controller;

import model.Fireworks;
import model.HanabiCards;
import model.Player;
import model.Players;
import model.Tokens;

public abstract class AbstractPlay {

    void initGame(int numberOfPlayers) {
        Players players = new Players();
        HanabiCards.DECK.shuffle();
        players.setupPlayers(numberOfPlayers);
    }

    public void play() {
        // Here are all the game turns, played until:
        // 1) the deck runs out,
        // 2) players loose all 3 lives,
        // 3) players finish by placing all 25 fireworks.
        gameRounds();

        // If the deck has run out, play one last turn.
        if (HanabiCards.DECK.endOfDeck()) {
            lastRound();
        }
    };

    private void gameRounds() {
        boolean gameEnd;
        do {
            Player actualPlayer = Players.nextPlayer();
            actualPlayer.setTheirTurn(true);
            playerTurn(actualPlayer);
            gameEnd = HanabiCards.DECK.endOfDeck() || Tokens.getTokens().getLife() == 0 ||
                    Fireworks.getFireworks().allFireworksFinished();
        } while (!gameEnd);
    }

    private void lastRound() {
        for (int i = 0; i < Players.numberOfPlayers; i++) {
            Player actualPlayer = Players.nextPlayer();
            actualPlayer.setTheirTurn(true);
            playerTurn(actualPlayer);
        }
    }

    abstract void playerTurn(Player actualPlayer);
}
