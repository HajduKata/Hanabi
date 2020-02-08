package model;

public class SelectedSymbol {
    private static CardColor selectedColor;
    private static CardNumber selectedNumber;

    private static SelectedSymbol instance;

    private SelectedSymbol() {
        clearSelection();
    }

    public static SelectedSymbol getSelectedSymbol() {
        if (instance == null) {
            instance = new SelectedSymbol();
        }

        return instance;
    }

    public static CardColor getSelectedColor() {
        return selectedColor;
    }

    public static CardNumber getSelectedNumber() {
        return selectedNumber;
    }

    public static void setSelectedColor(CardColor selectedColor) {
        SelectedSymbol.selectedColor = selectedColor;
    }

    public static void setSelectedNumber(CardNumber selectedNumber) {
        SelectedSymbol.selectedNumber = selectedNumber;
    }

    public static void clearSelection() {
        selectedColor = null;
        selectedNumber = null;
    }
}
