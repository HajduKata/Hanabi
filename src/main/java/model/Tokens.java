package model;

public class Tokens {
    private static Tokens instance;
    private static int life;
    private static int clues;

    private Tokens() {
        life = 3;
        clues = 8;
    }

    public static Tokens getTokens() {
        if (instance == null) {
            instance = new Tokens();
        }
        return instance;
    }

    public int getLife() {
        return life;
    }

    public void decreaseLife() {
        if (life > 0) {
            life--;
        }
    }

    public int getClues() {
        return clues;
    }

    public void decreaseClues() {
        if(clues > 0) {
            clues--;
        }
    }

    public void increaseClues() {
        if(clues < 8) {
            clues++;
        }
    }

    public static void clearInstance() {
        instance = null;
    }
}
