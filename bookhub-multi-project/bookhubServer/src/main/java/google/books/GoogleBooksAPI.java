package google.books;

import api.enums.SearchCategory;
import com.google.gson.Gson;
import dto.Items;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;

public class GoogleBooksAPI {
    private final static String API_KEY = "AIzaSyCokdzk2dP00RWq31eMgML7nt7KBagatUQ";
    private final static String URL = "https://www.googleapis.com/books/v1/volumes?q=";

    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    // to de deleted, for testing
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        GoogleBooksAPI googleBooksAPI = new GoogleBooksAPI();
        String title = "Thor";

        Items items = googleBooksAPI.getItemsFromGoogleAPI(title);

        System.out.println(items);

    }

    public Items getBookFromGoogleAPIByType(SearchCategory type, String argument) throws ExecutionException,
            InterruptedException {
        switch (type) {
            case TITLE -> {
                String titleQueryParameter = replaceSpacesWithURLEncodingSymbol(argument);
                return getItemsFromGoogleAPI(titleQueryParameter);
            }
            case AUTHOR -> {
                String authorQueryParameter = "inauthor:" + replaceSpacesWithURLEncodingSymbol(argument);
                return getItemsFromGoogleAPI(authorQueryParameter);
            }

            case PUBLISHER -> {
                String publisherQueryParameter = "inpublisher:" + replaceSpacesWithURLEncodingSymbol(argument);
                return getItemsFromGoogleAPI(publisherQueryParameter);
            }
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    public Items getItemsFromGoogleAPI(String argument) throws ExecutionException, InterruptedException {
        String json = sendRequestAndGetResponseBody(argument);

        return gson.fromJson(json, Items.class);
    }

    private String sendRequestAndGetResponseBody(String argument) throws ExecutionException, InterruptedException {
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

    private String replaceSpacesWithURLEncodingSymbol(String argument) {
        return String.join("%20", argument.split("\\s+"));
    }
}
