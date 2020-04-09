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

    /**
     * If @this is higher than @number, return true
     */
    public boolean isHigher(CardNumber number) {
        if(this.equals(ONE) && !number.equals(ONE)) {
            return true;
        } else if(this.equals(TWO) && (!number.equals(ONE) || !number.equals(TWO))) {
            return true;
        } else if(this.equals(THREE) && (!number.equals(ONE) || !number.equals(TWO) || !number.equals(THREE))) {
            return true;
        } else if(this.equals(FOUR) && (!number.equals(ONE) || !number.equals(TWO) || !number.equals(THREE) || !number.equals(FOUR))) {
            return true;
        } else return this.equals(FIVE);
    }
}
