package model;

import javax.swing.JLabel;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;

public class History {
    private static History instance;
    private static ArrayList<JLabel> historyList;

    private History() {
        historyList = new ArrayList<>();
    }

    public static History getHistory() {
        if (instance == null) {
            instance = new History();
        }
        return instance;
    }

    public void addString(String playerName, String color, String number) {
        String historyString = "";
        if (!color.equals("")) {
            historyString = "<html>" + playerName + "-nak/-nek megmutatták<br/>a" + color + " színű lapjait.</html>";
        } else if (!number.equals("")) {
            historyString = "<html>" + playerName + "-nak/-nek megmutatták<br/>a" + number + " számú lapjait.</html>";
        }
        JLabel historyLabel = new JLabel(historyString);
        //historyLabel.setSize(new Dimension(RIGHT_PANEL_WIDTH-10, 450));
        historyLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        Collections.reverse(historyList);
        historyList.add(historyLabel);
        Collections.reverse(historyList);
    }

    public ArrayList getHistoryList() {
        return historyList;
    }

    public String getHistoryColor(CardColor color) {
        String string = "";
        switch (color) {
            case RED:
                string = " piros";
                break;
            case GREEN:
                string = " zöld";
                break;
            case BLUE:
                string = " kék";
                break;
            case YELLOW:
                string = " sárga";
                break;
            case WHITE:
                string = " fehér";
                break;
        }
        return string;
    }

    public String getHistoryNumber(CardNumber number) {
        String string = "";
        switch (number) {
            case ONE:
                string = "z egyes";
                break;
            case TWO:
                string = " kettes";
                break;
            case THREE:
                string = " hármas";
                break;
            case FOUR:
                string = " négyes";
                break;
            case FIVE:
                string = "z ötös";
                break;
        }
        return string;
    }
}
