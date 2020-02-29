package view;

import model.Card;
import model.CardColor;
import model.CardNumber;
import model.Player;
import model.Players;
import model.SelectedSymbol;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import java.util.Objects;

import static view.GameTable.RIGHT_PANEL_DIMENSION;
import static view.HanabiUtilities.COLOR_OFFSET_X;
import static view.HanabiUtilities.NUMBER_OFFSET_Y;
import static view.HanabiUtilities.SYMBOL_SIZE_X;
import static view.HanabiUtilities.SYMBOL_SIZE_Y;
import static view.HanabiUtilities.classLoader;

public class ControlPanel extends JPanel {
    private static final String SYMBOLS_TITLE = "Select Color or Number";
    private static final Color BG_COLOR = Color.decode("#003366");

    private SelectedSymbol selectedSymbol;
    private PlayerPanel playerPanel;


    public ControlPanel(SelectedSymbol selectedSymbol) {
        this.selectedSymbol = selectedSymbol;
        this.playerPanel = playerPanel;
        HintSymbols hintSymbols = new HintSymbols();

        int x = 5;
        int y = 20;
        for (CardColor color : CardColor.values()) {
            String actualColor = color.name().toLowerCase();
            assert HintSymbols.getImageByColor(color) != null;
            JButton colorButton = new JButton(new ImageIcon(HintSymbols.getImageByColor(color)));
            colorButton.setBounds(x,y, SYMBOL_SIZE_X, SYMBOL_SIZE_Y);
            colorButton.setPreferredSize(new Dimension(SYMBOL_SIZE_X, SYMBOL_SIZE_Y));
            this.add(colorButton);
            colorButton.addActionListener(e -> {
                SelectedSymbol.clearSelection();
                SelectedSymbol.setSelectedColor(color);
                highlightCard();
            });
            x += SYMBOL_SIZE_X + COLOR_OFFSET_X;
        }
        x = 5;
        y = 20 + SYMBOL_SIZE_Y + NUMBER_OFFSET_Y;
        for (CardNumber number : CardNumber.values()) {
            if (number != CardNumber.ZERO) {
                String actualNumber = number.getValue();
                assert HintSymbols.getImageByNumber(number) != null;
                JButton numberButton = new JButton(new ImageIcon(HintSymbols.getImageByNumber(number)));
                numberButton.setBounds(x,y, SYMBOL_SIZE_X, SYMBOL_SIZE_Y);
                numberButton.setPreferredSize(new Dimension(SYMBOL_SIZE_X, SYMBOL_SIZE_Y));
                this.add(numberButton);
                numberButton.addActionListener(e -> {
                    SelectedSymbol.clearSelection();
                    SelectedSymbol.setSelectedNumber(number);
                    highlightCard();
                });
                x += SYMBOL_SIZE_X + COLOR_OFFSET_X;
            }
        }
    }

    private void highlightCard() {
        CardColor color = SelectedSymbol.getSelectedColor();
        CardNumber number = SelectedSymbol.getSelectedNumber();
        for (Player player : Players.getThePlayers()) {
            if (!player.isHumanPlayer()) {
                for (Card card : player.getHand().cards) {
                    card.reset();
                    if (color != null && color.equals(card.getColor())
                            || number != null && number.equals(card.getNumber())) {
                        card.selected();
                    }
                }
            }
            player.getPlayerPanel().repaint();
        }
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(BG_COLOR);
        Border border = new BorderUIResource.TitledBorderUIResource(SYMBOLS_TITLE);
        setBorder(border);

        paintSymbols(g);
    }

    private void paintSymbols(Graphics g) {
    }
}
