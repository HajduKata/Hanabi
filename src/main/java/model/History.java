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
            historyString = "<html>" + playerName + " was showed their<br/>" + color + " colored cards.</html>";
        } else if (!number.equals("")) {
            historyString = "<html>" + playerName + " was showed their<br/>number " + number + " cards.</html>";
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
                string = "red";
                break;
            case GREEN:
                string = "green";
                break;
            case BLUE:
                string = "blue";
                break;
            case YELLOW:
                string = "yellow";
                break;
            case WHITE:
                string = "white";
                break;
        }
        return string;
    }

    public String getHistoryNumber(CardNumber number) {
        String string = "";
        switch (number) {
            case ONE:
                string = "one";
                break;
            case TWO:
                string = "two";
                break;
            case THREE:
                string = "three";
                break;
            case FOUR:
                string = "four";
                break;
            case FIVE:
                string = "five";
                break;
        }
        return string;
    }

    public static void clearInstance() {
        instance = null;
    }
}
