package Model;

import View.GameTable;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import static View.GameTable.CARD_WIDTH_HEIGHT;

public class Card {
    private CardColor cardColor;
    private CardNumber cardNumber;
    public Image image;
    private boolean selected = false;
    private int x;
    private int y;
    public boolean knowsColor = false;
    public boolean knowsNumber = false;

    public Card(CardColor cardColor, CardNumber cardNumber) {
        this.cardColor = cardColor;
        this.cardNumber = cardNumber;
        ClassLoader cldr = this.getClass().getClassLoader();
        URL imageURL = cldr.getResource("hanabi_cards/" + cardColor.getValue() + cardNumber.getValue() + ".png");
        this.image = GameTable.loadCardImage(imageURL);
    }

    public CardColor getColor() {
        return cardColor;
    }

    public CardNumber getNumber() {
        return cardNumber;
    }

    public void selected() {
        this.selected = true;
        this.y -= 10;
    }

    public void reset() {
        if (this.selected) {
            this.y += 10;
            this.selected = false;
        }
    }

    public boolean contains(int x, int y) {
        return (x > this.x && x < (this.x + CARD_WIDTH_HEIGHT) &&
                y > this.y && y < (this.y + CARD_WIDTH_HEIGHT));
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
