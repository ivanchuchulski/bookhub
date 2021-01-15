package api.interfaces;

import java.io.Serializable;
import java.util.Arrays;

// this object is used for sending to the client
public class BookImpl implements Serializable {
    private final String title;
    // this could become just a single String for one author
    private final String[] authors;
    private final String publisher;
    private final String publishedDate;
    private final String description;
    private final String smallThumbnailLink;

    public BookImpl(String title, String[] authors, String publisher, String publishedDate, String description,
                    String smallThumbnailLink) {
        this.title = title;
        this.authors = authors;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.description = description;
        this.smallThumbnailLink = smallThumbnailLink;
    }

    public BookImpl(String title, String publisher, String publishedDate, String description,
                    String smallThumbnailLink) {
        this.title = title;
        this.publisher = publisher;
        this.authors = null;
        this.publishedDate = publishedDate;
        this.description = description;
        this.smallThumbnailLink = smallThumbnailLink;
    }

    public String getTitle() {
        return title;
    }

    public String[] getAuthors() {
        return authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public String getDescription() {
        return description;
    }

    public String getSmallThumbnailLink() {
        return smallThumbnailLink;
    }

    @Override
    public String toString() {
        return "BookImpl{" +
                "title='" + title + '\'' +
                ", authors=" + Arrays.toString(authors) +
                ", publisher='" + publisher + '\'' +
                ", publishedDate='" + publishedDate + '\'' +
                ", description='" + description + '\'' +
                ", smallThumbnailLink='" + smallThumbnailLink + '\'' +
                '}';
    }
}
