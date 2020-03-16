package view;

import model.Card;
import model.CardColor;
import model.CardNumber;
import model.Player;
import model.Players;
import model.SelectedSymbol;
import model.Tokens;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import static view.GameTable.BUTTON_HEIGHT;
import static view.GameTable.CONTROL_PANEL_HEIGHT;
import static view.GameTable.RIGHT_PANEL_WIDTH;
import static view.HanabiUtilities.COLOR_OFFSET_X;
import static view.HanabiUtilities.NUMBER_OFFSET_Y;
import static view.HanabiUtilities.SYMBOL_SIZE_X;
import static view.HanabiUtilities.SYMBOL_SIZE_Y;

public class ControlPanel extends JPanel {
    private static final Color BG_COLOR = Color.decode("#003366");
    public boolean isPlayACard = false;
    public boolean isDiscardACard = false;

    private SelectedSymbol selectedSymbol;
    CardLayout cardLayout;
    JPanel extensionPanel;

    public ControlPanel() {
        // Control buttons
        JPanel controlButtonsContainer = new JPanel();
        controlButtonsContainer.setLayout(new BoxLayout(controlButtonsContainer, BoxLayout.Y_AXIS));
        controlButtonsContainer.setAlignmentX(CENTER_ALIGNMENT);
        controlButtonsContainer.setAlignmentY(TOP_ALIGNMENT);
        JButton playCardButton = new JButton("Play a card");
        JButton discardCardButton = new JButton("Discard a card");
        JButton giveHintButton = new JButton("Give a hint");
        playCardButton.setMaximumSize(new Dimension(RIGHT_PANEL_WIDTH, BUTTON_HEIGHT));
        discardCardButton.setMaximumSize(new Dimension(RIGHT_PANEL_WIDTH, BUTTON_HEIGHT));
        giveHintButton.setMaximumSize(new Dimension(RIGHT_PANEL_WIDTH, BUTTON_HEIGHT));
        controlButtonsContainer.add(playCardButton);
        controlButtonsContainer.add(Box.createVerticalStrut(10));
        controlButtonsContainer.add(discardCardButton);
        controlButtonsContainer.add(Box.createVerticalStrut(10));
        controlButtonsContainer.add(giveHintButton);
        controlButtonsContainer.add(Box.createVerticalStrut(10));

        cardLayout = new CardLayout(5, 5);
        extensionPanel = new JPanel(cardLayout);
        Border border = new BorderUIResource.LineBorderUIResource(Color.BLACK);
        extensionPanel.setBorder(border);
        extensionPanel.setMaximumSize(new Dimension(RIGHT_PANEL_WIDTH, CONTROL_PANEL_HEIGHT));

        JLabel defaultLabel = new JLabel("Select an action");
        defaultLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        JLabel playCardLabel = new JLabel("Select a card to play from your hand");
        playCardLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        JLabel discardCardLabel = new JLabel("Select a card to discard from your hand");
        discardCardLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        extensionPanel.add(defaultLabel, "default");
        extensionPanel.add(playCardLabel, "play");
        extensionPanel.add(discardCardLabel, "discard");
        extensionPanel.add(setSelectedHint(SelectedSymbol.getSelectedSymbol()), "hint");

        this.add(controlButtonsContainer);
        this.add(extensionPanel);

        playCardButton.addActionListener(e -> setPlayACard(cardLayout, extensionPanel));
        discardCardButton.addActionListener(e -> setDiscardACard(cardLayout, extensionPanel));
        giveHintButton.addActionListener(e -> showHintButtons(cardLayout, extensionPanel));
    }

    void showDefaultWindow() {
        cardLayout.show(extensionPanel, "default");
    }

    private void setPlayACard(CardLayout cardLayout, JPanel extensionPanel) {
        isPlayACard = true;
        isDiscardACard = false;
        resetAllCards();
        cardLayout.show(extensionPanel, "play");
    }

    private void setDiscardACard(CardLayout cardLayout, JPanel extensionPanel) {
        isDiscardACard = true;
        isPlayACard = false;
        resetAllCards();
        cardLayout.show(extensionPanel, "discard");
    }

    private void showHintButtons(CardLayout cardLayout, JPanel extensionPanel) {
        isDiscardACard = false;
        isPlayACard = false;
        if(Tokens.getTokens().getClues() > 0) {
            cardLayout.show(extensionPanel, "hint");
        } else {
            showDefaultWindow();
        }
    }

    private JPanel setSelectedHint(SelectedSymbol selectedSymbol) {
        this.selectedSymbol = selectedSymbol;
        JPanel hintPanel = new JPanel();
        hintPanel.setLayout(new BoxLayout(hintPanel, BoxLayout.Y_AXIS));

        JPanel colorPanel = new JPanel();
        colorPanel.setBackground(BG_COLOR);

        int x = 5;
        int y = 20;
        for (CardColor color : CardColor.values()) {
            assert HintSymbols.getImageByColor(color) != null;
            JButton colorButton = new JButton(new ImageIcon(new HintSymbols().getImageByColor(color)));
            colorButton.setBounds(x, y, SYMBOL_SIZE_X, SYMBOL_SIZE_Y);
            colorButton.setPreferredSize(new Dimension(SYMBOL_SIZE_X, SYMBOL_SIZE_Y));
            this.add(colorButton);
            colorButton.addActionListener(e -> {
                SelectedSymbol.clearSelection();
                SelectedSymbol.setSelectedColor(color);
                highlightCard();
            });
            colorPanel.add(colorButton);
            x += SYMBOL_SIZE_X + COLOR_OFFSET_X;
        }

        JPanel numberPanel = new JPanel();
        numberPanel.setBackground(BG_COLOR);

        x = 5;
        y = 20 + SYMBOL_SIZE_Y + NUMBER_OFFSET_Y;
        for (CardNumber number : CardNumber.values()) {
            if (number != CardNumber.ZERO) {
                assert HintSymbols.getImageByNumber(number) != null;
                JButton numberButton = new JButton(new ImageIcon(new HintSymbols().getImageByNumber(number)));
                numberButton.setBounds(x, y, SYMBOL_SIZE_X, SYMBOL_SIZE_Y);
                numberButton.setPreferredSize(new Dimension(SYMBOL_SIZE_X, SYMBOL_SIZE_Y));
                this.add(numberButton);
                numberButton.addActionListener(e -> {
                    SelectedSymbol.clearSelection();
                    SelectedSymbol.setSelectedNumber(number);
                    highlightCard();
                });
                numberPanel.add(numberButton);
                x += SYMBOL_SIZE_X + COLOR_OFFSET_X;
            }
        }
        hintPanel.add(colorPanel);
        hintPanel.add(numberPanel);
        return hintPanel;
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

    private void resetAllCards() {
        for (Player player : Players.getThePlayers()) {
            if (!player.isHumanPlayer()) {
                for (Card card : player.getHand().cards) {
                    card.reset();
                }
            }
            player.getPlayerPanel().repaint();
        }
        SelectedSymbol.clearSelection();
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
//        setBackground(BG_COLOR);
        paintSymbols(g);
    }

    private void paintSymbols(Graphics g) {
    }
}
