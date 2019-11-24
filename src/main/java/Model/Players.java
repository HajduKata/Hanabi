package Model;

import View.GameTable;

import java.util.ArrayList;
import java.util.List;

public class Players {
    static List<Player> players;

    private Players(int numberOfPlayers) {
        //This is the human player
        Player human = new Player(true);
        players.add(human);

        //These are the AI players
        for (int i = 1; i < numberOfPlayers; i++) {
            Player ai = new Player(false);
            players.add(ai);
        }
    }

    public static List<Player> getThePlayers(int numberOfPlayers) {
        if (players == null) {
            players = new ArrayList<>(numberOfPlayers);
            new Players(numberOfPlayers);
        }
        return players;
    }

    public static List<Player> getThePlayers() {
        return players;
    }
}
