package google.books;

import api.interfaces.Categories;
import com.google.gson.Gson;
import dto.Items;

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

    public Items getBookFromGoogleAPIByType(Categories type, String argument) throws ExecutionException, InterruptedException {

        Items items = null;

        switch (type) {
            case TITLE -> {
                String parseTitle = Arrays.stream(argument.split("\\s+"))
                        .collect(Collectors.joining("%20"));

                return getBookFromGoogleAPI(parseTitle);
            }
            case AUTHOR -> {
                String queryParameter = "inauthor:" + argument;
                return getBookFromGoogleAPI(queryParameter);
            }

            case PUBLISHER -> {
                String queryParameter = "inpublisher:" + argument;
                return getBookFromGoogleAPI(queryParameter);
            }
        }

        return items;
    }

    public Items getBookFromGoogleAPI(String argument) throws ExecutionException, InterruptedException {

        String json = getJsonFromGoogle(argument);

        return gson.fromJson(json, Items.class);
    }

    private String getJsonFromGoogle(String argument) throws ExecutionException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(buildUrl(argument)))
                .GET()
                .build();

        HttpResponse<String> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .get();

        return response.body();
    }

    private String buildUrl(String argument) {
        return String.format("%s%s&%s", URL, argument, API_KEY);
    }
}
