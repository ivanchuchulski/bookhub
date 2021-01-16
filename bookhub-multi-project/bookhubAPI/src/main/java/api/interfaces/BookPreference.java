package api.interfaces;

public enum BookPreference {
    FAVOURITE("Favourite"),
    WANT_TO_READ("Want to read"),
    HAVE_READ("Have read"),
    CURRENTLY_READING("Currently reading");

    private final String text;

    BookPreference(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
