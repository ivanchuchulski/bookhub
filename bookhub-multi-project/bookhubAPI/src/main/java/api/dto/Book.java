package api.dto;

import java.io.Serializable;
import java.util.Arrays;


public class Book implements Serializable {

    private VolumeInfo volumeInfo;

    public Book(VolumeInfo volumeInfo) {
        this.volumeInfo = volumeInfo;
    }

    public String getImageURL() {
        return volumeInfo.imageLinks.smallThumbnail;
    }

    public String prettyPrint() {
        return volumeInfo.toString();
    }

    public VolumeInfo getVolumeInfo() {
        return volumeInfo;
    }

    @Override
    public String toString() {
        return volumeInfo.title + ", " + volumeInfo.publishedDate;
    }

    private class VolumeInfo implements Serializable {
        private String title;
        private String[] authors;
        private String publisher;
        private String description;
        private String publishedDate;
        private GoogleImages imageLinks;

        public VolumeInfo(String title, String[] authors, String publisher, String description, String publishedDate, String[] imageLinks, GoogleImages imageLinks1) {
            this.title = title;
            this.authors = authors;
            this.publisher = publisher;
            this.description = description;
            this.publishedDate = publishedDate;

            this.imageLinks = imageLinks1;
        }

        public GoogleImages getImageLinks() {
            return imageLinks;
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

        public String getDescription() {
            return description;
        }

        public String getPublishedDate() {
            return publishedDate;
        }

        @Override
        public String toString() {
            return
                    "Title: " + title + System.lineSeparator() +
                    "Authors=" + Arrays.toString(authors) + System.lineSeparator() +
                    "Publisher='" + publisher + System.lineSeparator() +
                    "Description='" + description + System.lineSeparator() +
                    "PublishedDate='" + publishedDate;
        }

        private class GoogleImages implements Serializable {
            private String smallThumbnail;
            private String thumbnail;

            public GoogleImages(String smallThumbnail, String thumbnail) {
                this.smallThumbnail = smallThumbnail;
                this.thumbnail = thumbnail;
            }

            public String getSmallThumbnail() {
                return smallThumbnail;
            }

            public String getThumbnail() {
                return thumbnail;
            }
        }
    }


}
