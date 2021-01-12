package api.interfaces;

public enum Categories {
    TITLE("TITLE"), AUTHOR("AUTHOR"), PUBLISHER("PUBLISHER");

    private String text;

    Categories(String text) {
        this.text = text;
    }
}
