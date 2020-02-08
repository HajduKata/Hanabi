package model;

public class Clues {
    private int life;
    private int clues;

    public Clues(int life, int clues) {
        this.life = life;
        this.clues = clues;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getClues() {
        return clues;
    }

    public void setClues(int clues) {
        this.clues = clues;
    }
}
