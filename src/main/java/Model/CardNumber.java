package model;

public enum CardNumber {
    ZERO("0"), ONE("1"), TWO("2"), THREE("3"), FOUR("4"), FIVE("5");

    private String value;

    CardNumber(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public CardNumber next() {
        switch (this) {
            case ZERO:
                return ONE;
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

    public CardNumber previous() {
        switch (this) {
            case FIVE:
                return FOUR;
            case FOUR:
                return THREE;
            case THREE:
                return TWO;
            case TWO:
                return ONE;
            case ONE:
                return ZERO;
            default:
                return null;
        }
    }

    /**
     * If @number is lower or equal than @this, return true
     */
    public boolean isLowerOrEqual(CardNumber number) {
        switch (this) {
            case ONE:
                return number.equals(ONE) || number.equals(TWO) || number.equals(THREE) || number.equals(FOUR) || number.equals(FIVE);
            case TWO:
                return number.equals(TWO) || number.equals(THREE) || number.equals(FOUR) || number.equals(FIVE);
            case THREE:
                return number.equals(THREE) || number.equals(FOUR) || number.equals(FIVE);
            case FOUR:
                return number.equals(FOUR) || number.equals(FIVE);
            case FIVE:
                return number.equals(FIVE);
            default:
                return true;
        }
    }
}
