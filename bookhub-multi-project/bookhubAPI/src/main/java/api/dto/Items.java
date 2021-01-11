package api.dto;

import java.io.Serializable;
import java.util.Arrays;

public class Items implements Serializable {

    private Book[] items;

    public Items(Book[] items) {
        this.items = items;
    }

    public Book[] getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "Items{" +
               "items=" + Arrays.toString(items) +
               '}';
    }
}
