package implementations;

import api.interfaces.Book;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.List;

// this object is used for sending to the client
public class BookImpl extends UnicastRemoteObject implements Book {

    private final String id;
    private final String title;
    // this could become just a single String for one author
    private final String[] authors;
    private final String publisher;
    private final String publishedDate;
    private final String description;
    private final String smallThumbnailLink;

    public BookImpl(String title, String id, String[] authors, String publisher, String publishedDate, String description,
                    String smallThumbnailLink) throws RemoteException {
        super();
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.description = description;
        this.smallThumbnailLink = smallThumbnailLink;
    }

    public BookImpl(String title, String id, String publisher, String publishedDate, String description,
                    String smallThumbnailLink) throws RemoteException {
        super();
        this.id = id;
        this.title = title;
        this.publisher = publisher;
        this.authors = null;
        this.publishedDate = publishedDate;
        this.description = description;
        this.smallThumbnailLink = smallThumbnailLink;
    }

    @Override
    public String getId() throws RemoteException {
        return id;
    }

    @Override
    public String getTitle() throws RemoteException {
        return title;
    }

    @Override
    public String[] getAuthors() throws RemoteException {
        return authors;
    }

    @Override
    public String getPublisher() throws RemoteException {
        return publisher;
    }

    @Override
    public String getPublishedDate() throws RemoteException {
        return publishedDate;
    }

    @Override
    public String getDescription() throws RemoteException {
        return description;
    }

    @Override
    public String getSmallThumbnailLink() throws RemoteException {
        return smallThumbnailLink;
    }

    @Override
    public String printInfo() throws RemoteException {
        String separator = " : ";

        return String.join(separator, getTitle(), Arrays.toString(getAuthors()), getPublishedDate());
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
