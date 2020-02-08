package model;

public class ClueSymbol {
    private CardColor color;
    private CardNumber number;

    public ClueSymbol(CardColor color, CardNumber number) {
        this.color = color;
        this.number = number;
    }

    public CardColor getColor() {
        return color;
    }

    public CardNumber getNumber() {
        return number;
    }
}