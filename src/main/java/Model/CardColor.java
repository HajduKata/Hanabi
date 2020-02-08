package model;

import java.awt.Color;

public enum CardColor {
    RED("r"), YELLOW("y"), GREEN("g"), BLUE("b"), WHITE("w")/*, RAINBOW("rainbow")*/;

    private String value;

    private CardColor(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Color getPaintColor(CardColor cardColor) {
        switch (cardColor) {
            case RED:
                return new Color(237, 28, 36);
            case YELLOW:
                return new Color(255, 242, 0);
            case GREEN:
                return new Color(96, 226, 33);
            case BLUE:
                return new Color(0, 162, 232);
            case WHITE:
                return Color.WHITE;
        }
        return null;
    }
}