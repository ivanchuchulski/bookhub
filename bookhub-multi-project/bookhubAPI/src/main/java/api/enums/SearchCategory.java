package api.enums;

public enum SearchCategory {
    TITLE("Title"),
    AUTHOR("Author"),
    PUBLISHER("Publisher");

    private final String text;

    SearchCategory(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
