package Model;

public enum CardNumber {
    ONE("1"), TWO("2"), THREE("3"), FOUR("4"), FIVE("5");

    private String value;

    private CardNumber(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
