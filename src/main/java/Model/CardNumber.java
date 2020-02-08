package model;

public enum CardNumber {
    ZERO("0"), ONE("1"), TWO("2"), THREE("3"), FOUR("4"), FIVE("5");

    private String value;

    private CardNumber(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public CardNumber next() {
        switch (this) {
            case ONE:
                return TWO;
            case TWO:
                return THREE;
            case THREE:
                return FOUR;
            case FOUR:
                return FIVE;
            default:
                return null;
        }
    }
}
