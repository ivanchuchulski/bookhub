package api.dto;

import java.io.Serializable;
import java.util.Arrays;


public class BookTransfer implements Serializable {
    private final VolumeInfo volumeInfo;

    public BookTransfer(VolumeInfo volumeInfo) {
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
        return volumeInfo.toString();
    }

    public class VolumeInfo implements Serializable {
        private final String title;
        private final String[] authors;
        private final String publisher;
        private final String description;
        private final String publishedDate;
        private final GoogleImages imageLinks;

        public VolumeInfo(String title, String[] authors, String publisher, String description, String publishedDate,
                          GoogleImages imageLink) {
            this.title = title;
            this.authors = authors;
            this.publisher = publisher;
            this.description = description;
            this.publishedDate = publishedDate;
            this.imageLinks = imageLink;
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

        public GoogleImages getImageLinks() {
            return imageLinks;
        }

        @Override
        public String toString() {
            String separator = ":";
            return title + separator +
                    Arrays.toString(authors) + separator +
                    publisher + separator +
                    description + separator +
                    publishedDate + separator +
                    imageLinks.smallThumbnail;
        }

        public class GoogleImages implements Serializable {
            private final String smallThumbnail;
            private final String thumbnail;

            public GoogleImages(String smallThumbnail, String thumbnail) {
                this.smallThumbnail = smallThumbnail;
                this.thumbnail = thumbnail;
            }

            public String getSmallThumbnail() {
                return smallThumbnail;
            }
        }
    }


}
