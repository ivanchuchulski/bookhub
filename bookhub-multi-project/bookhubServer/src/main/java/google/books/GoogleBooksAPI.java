package google.books;

import dto.Items;
import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class GoogleBooksAPI {
    private final static String API_KEY = "AIzaSyCokdzk2dP00RWq31eMgML7nt7KBagatUQ";
    private final static String URL = "https://www.googleapis.com/books/v1/volumes?q=";

    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    // to de deleted, for testing
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        GoogleBooksAPI googleBooksAPI = new GoogleBooksAPI();
        String title = "Thor";

        Items items = googleBooksAPI.getBookFromGoogleAPI(title);

        System.out.println(items);

    }

    public Items getBookFromGoogleAPI(String title) throws ExecutionException, InterruptedException {

        String json = getJsonFromGoogle(title);

        return gson.fromJson(json, Items.class);
    }

    private String getJsonFromGoogle(String title) throws ExecutionException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                                         .uri(URI.create(buildUrlByTitle(title)))
                                         .GET()
                                         .build();

        HttpResponse<String> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                                              .get();

        return response.body();
    }

    private String buildUrlByTitle(String title) {
        String parseTitle = Arrays.stream(title.split("\\s+"))
                                  .collect(Collectors.joining("%20"));

        return String.format("%s%s&%s", URL, parseTitle, API_KEY);
    }
}
