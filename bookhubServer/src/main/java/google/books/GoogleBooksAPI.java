package google.books;

import api.enums.SearchCategory;
import com.google.gson.Gson;
import dto.BookTransfer;
import dto.Items;
import exceptions.BookQueryException;
import server.BookhubServerConfig;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutionException;

public class GoogleBooksAPI {
    private final static String API_KEY = BookhubServerConfig.GOOGLE_BOOKS_API_KEY;
    private final static String URL = "https://www.googleapis.com/books/v1/volumes?q=";

    private final HttpClient client;
    private final Gson gson;

    public GoogleBooksAPI() {
        client = HttpClient.newHttpClient();
        gson = new Gson();
    }

    // to de deleted, for testing
    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {
        try {
            GoogleBooksAPI googleBooksAPI = new GoogleBooksAPI();
            String title = "Thor";

            Items items = googleBooksAPI.getBookFromGoogleAPIByType(SearchCategory.TITLE, title);

            for (BookTransfer item : items.getItems()) {
                System.out.println(item);
            }
        } catch (BookQueryException e) {
            e.printStackTrace();
        }
    }

    public Items getBookFromGoogleAPIByType(SearchCategory type, String argument) throws ExecutionException,
            InterruptedException, IOException, BookQueryException {
        String searchParameter =
                switch (type) {
                    case TITLE -> replaceSpacesWithURLEncodingSymbol(argument);
                    case AUTHOR -> "inauthor:" + replaceSpacesWithURLEncodingSymbol(argument);
                    case PUBLISHER -> "inpublisher:" + replaceSpacesWithURLEncodingSymbol(argument);
                };

        URI requestURI = URI.create(buildUrl(searchParameter));

        String responseAsJSON = sendRequestAndGetResponseBody(requestURI);

        return gson.fromJson(responseAsJSON, Items.class);
    }

    private String sendRequestAndGetResponseBody(URI requestURI) throws BookQueryException {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                                             .uri(requestURI)
                                             .GET()
                                             .timeout(Duration.of(5, ChronoUnit.SECONDS))
                                             .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            int statusCode = response.statusCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                return response.body();
            } else {
                throw new BookQueryException("error fetching books, server responded with code %d".formatted(statusCode));
            }

        } catch (IOException | InterruptedException e) {
            throw new BookQueryException("error fetching books", e);
        }
    }

    private String buildUrl(String argument) {
        return String.format("%s%s&%s", URL, argument, API_KEY);
    }

    private String replaceSpacesWithURLEncodingSymbol(String argument) {
        return String.join("%20", argument.split("\\s+"));
    }
}
