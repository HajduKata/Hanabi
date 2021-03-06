package model;

import java.util.ArrayList;
import java.util.List;

import static view.HanabiUtilities.AI;
import static view.HanabiUtilities.HUMAN;

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

    public static void setupPlayers(int numberOfPlayers, String name) {
        assert numberOfPlayers >= MIN_NUMBER_OF_PLAYERS;
        assert numberOfPlayers <= MAX_NUMBER_OF_PLAYERS;

        Players.numberOfPlayers = numberOfPlayers;
        Players.playerIndex = (int) Math.floor(Math.random()*numberOfPlayers);

        players = new ArrayList<>(numberOfPlayers);
        //Initialize the human player
        Player human = new Player(HUMAN);
        if (!name.equals("")) {
            human.setName(name);
        }
        players.add(0, human); // human player is always the first indexed

        //Initialize all the AI players
        for (int i = 1; i < numberOfPlayers; i++) {
            Player ai = new Player(AI);
            players.add(i, ai);
        }
    }

    public static void setupTestPlayers(int numberOfPlayers) {
        assert numberOfPlayers >= MIN_NUMBER_OF_PLAYERS;
        assert numberOfPlayers <= MAX_NUMBER_OF_PLAYERS;
        Players.numberOfPlayers = numberOfPlayers;
        Players.playerIndex = (int) Math.floor(Math.random()*numberOfPlayers);
        players = new ArrayList<>(numberOfPlayers);
        //Initialize all the AI players
        for (int i = 0; i < numberOfPlayers; i++) {
            Player ai = new Player(AI);
            players.add(i, ai);
        }
    }

    public static int getPlayerIndex() {
        return playerIndex;
    }

    public static Player getIndexPlayer(int index) {
        return players.get(index);
    }

}
