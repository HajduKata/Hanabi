package model;

import static view.HanabiUtilities.AI;
import static view.HanabiUtilities.HUMAN;

import java.util.ArrayList;
import java.util.List;

/**
 * Representing one human and the other AI players
 */
public class Players {

    private static final int MIN_NUMBER_OF_PLAYERS = 2;
    private static final int MAX_NUMBER_OF_PLAYERS = 5;

    private static List<Player> players;
    private static int playerIndex;

    public static int numberOfPlayers;

    public static List<Player> getThePlayers() {
        return players;
    }

    public static Player nextPlayer() {
        playerIndex++;
        if (playerIndex >= numberOfPlayers) {
            playerIndex = 0;
        }
        return players.get(playerIndex);
    }

    public void setupPlayers(int numberOfPlayers) {
        initNumberOfPlayers(numberOfPlayers);
        initAiPlayers();
    }

    private void initNumberOfPlayers(int numberOfPlayers) {
        assert numberOfPlayers >= MIN_NUMBER_OF_PLAYERS;
        assert numberOfPlayers <= MAX_NUMBER_OF_PLAYERS;
        Players.numberOfPlayers = numberOfPlayers;
        Players.playerIndex = (int) Math.floor(Math.random() * numberOfPlayers);
        players = new ArrayList<>(numberOfPlayers);
    }

    private void initAiPlayers() {
        for (int i = 0; i < numberOfPlayers; i++) {
            Player ai = new Player(AI);
            players.add(i, ai);
        }
    }

    public static void initHumanPlayer(String name) {
        Player human = new Player(HUMAN);
        if (!name.equals("")) {
            human.setName(name);
        }
        // the human player is always on the first index
        players.add(0, human);
        players.remove(numberOfPlayers);
    }

    public static int getPlayerIndex() {
        return playerIndex;
    }

    public static Player getIndexPlayer(int index) {
        return players.get(index);
    }

}
