package model;

import java.util.ArrayList;
import java.util.List;

import static view.HanabiUtilities.AI;
import static view.HanabiUtilities.HUMAN;

/**
 * Singleton class representing one human and all AI players
 */
public enum Players {

    PLAYERS;

    public static final int MIN_NUMBER_OF_PLAYERS = 2;
    public static final int MAX_NUMBER_OF_PLAYERS = 5;

    private static List<Player> players;
    public static int numberOfPlayers;

    public static Iterable<Player> getThePlayers() {
        return players;
    }

    public static List<Player> setupPlayers(int numberOfPlayers) {
        assert numberOfPlayers >= MIN_NUMBER_OF_PLAYERS;
        assert numberOfPlayers <= MAX_NUMBER_OF_PLAYERS;

        Players.numberOfPlayers = numberOfPlayers;

        players = new ArrayList<>(MAX_NUMBER_OF_PLAYERS);
        //Initialize the human player
        Player human = new Player(HUMAN);
        players.add(0, human); // human player is always the first indexed

        //Initialize all the AI players
        for (int i = 1; i < numberOfPlayers; i++) {
            Player ai = new Player(AI);
            players.add(i, ai);
        }
        return players;
    }

    public void markSelectidCards(CardColor color, CardNumber number) {
// TODO if color != null ciklusban players -> getHand -> összes kártyára  card.setY(card.getY() - 10); amelyre egyezik a szín vagy szám

//        for (Player player : players) {
//            if (player.getHand().cards.contains(color)) {
//                Card card = new Card(color, number);
//                card.setY(card.getY() - 10);
//            }
//        }
    }
}
