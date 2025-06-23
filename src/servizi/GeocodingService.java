package servizi;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class GeocodingService {

    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();

    private GeocodingService() {
    }

    /**
     * Geocodifica un indirizzo e restituisce le coordinate come array di double.
     *
     * @param address L'indirizzo da geocodificare
     * @return Array di double [latitudine, longitudine] o null se non trovato/errore
     */
    public static double[] geocodeAddress(String address) {
        try {
            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
            String url = "https://nominatim.openstreetmap.org/search?format=json&q=" + encodedAddress;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("User-Agent", "TheKnife/1.0")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonArray jsonArray = gson.fromJson(response.body(), JsonArray.class);

                if (!jsonArray.isEmpty()) {
                    JsonObject firstResult = jsonArray.get(0).getAsJsonObject();
                    double lat = firstResult.get("lat").getAsDouble();
                    double lng = firstResult.get("lon").getAsDouble();

                    return new double[]{lat, lng};
                }
            }

            return null; // Errore HTTP o indirizzo non trovato

        } catch (URISyntaxException | IOException | InterruptedException e) {
            return null; // Errore durante la richiesta
        }
    }

    // Esempio di utilizzo
    public static void main(String[] args) {
        double[] coords = geocodeAddress("Via Roma 1, Milano");
        if (coords != null) {
            System.out.println("Latitudine: " + coords[0]);
            System.out.println("Longitudine: " + coords[1]);
        } else {
            System.err.println("Geocoding fallito - indirizzo non trovato o errore di rete");
        }
    }
}