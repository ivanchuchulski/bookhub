package api.interfaces;

public enum BookStatus {
    FAVOURITE("Favourite"),
    WANT_TO_READ("Want to read"),
    HAVE_READ("Have read"),
    CURRENTLY_READING("Currently reading");

    private final String text;

    BookStatus(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return getText();
    }
}
