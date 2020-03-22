package controller;

public class AIController {
    public AIController(){}

    public void chooseAction() {
        /*
        Action algorithm The action a player will take will be decided in the following order of priority.
        1. If the most recent recommendation was to play a card and no card has been played since the last hint, play the recommended card.
        2. If the most recent recommendation was to play a card, one card has been played since the hint was given, and the players have made fewer than two errors, play the recommended card.
        3. If the players have a hint token, give a hint.
        4. If the most recent recommendation was to discard a card, discard the requested card.
        5. Discard card c1.
        */

    }

    public void giveHint() {
        /*
        The recommendation for a hand will be determined with following priority:
        1. Recommend that the playable card of rank 5 with lowest index be played.
        2. Recommend that the playable card with lowest rank be played. If there is a tie for lowest rank, recommend the one with lowest index.
        3. Recommend that the dead card with lowest index be discarded.
        4. Recommend that the card with highest rank that is not indispensable be discarded. If there is a tie, recommend the one with lowest index.
        5. Recommend that c1 be discarded.
        */

    }

}
