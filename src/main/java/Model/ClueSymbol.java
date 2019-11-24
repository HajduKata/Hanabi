package Model;

import View.GameTable;

import java.awt.Image;

import static View.GameTable.COMPONENT_WIDTH_HEIGHT;

public class ClueSymbol {
    private CardColor color;
    private CardNumber number;
    private Image image;
    private int x;
    private int y;


    public ClueSymbol(CardColor color, CardNumber number) {
        this.color = color;
        this.number = number;
    }

    public ClueSymbol(Image image, int x, int y, CardColor color, CardNumber number) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.color = color;
        this.number = number;
    }

    public boolean contains(int x, int y) {
        return GameTable.clickContains(x, y, this.x, this.y, this.x + COMPONENT_WIDTH_HEIGHT,this.y + COMPONENT_WIDTH_HEIGHT);
    }

    public CardColor getColor() {
        return color;
    }

    public CardNumber getNumber() {
        return number;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
