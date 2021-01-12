package api.dto;

import java.io.Serializable;
import java.util.Arrays;

public class Items implements Serializable {

    private BookTransfer[] items;

    public Items(BookTransfer[] items) {
        this.items = items;
    }

    public BookTransfer[] getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "Items{" +
               "items=" + Arrays.toString(items) +
               '}';
    }
}
