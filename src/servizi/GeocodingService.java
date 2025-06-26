package servizi;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Servizio per la geolocalizzazione di luoghi in coordinate geografiche.
 * @author Thomas Riotto
 */
public final class GeocodingService {

    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();

    private GeocodingService() {
    }

    /**
     * Effettua la geocodificazione di un indirizzo e restituisce le coordinate come array di double.
     * @param address L'indirizzo da geocodificare
     * @return Array di double [latitudine, longitudine]
     */
    public static double[] geocodeAddress(String address) {
        try {
            final String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
            final String url = "https://nominatim.openstreetmap.org/search?format=json&q=" + encodedAddress;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("User-Agent", "TheKnife/1.0")
                    .GET()
                    .build();

            final HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                final JsonArray jsonArray = gson.fromJson(response.body(), JsonArray.class);

                if (!jsonArray.isEmpty()) {
                    final JsonObject firstResult = jsonArray.get(0).getAsJsonObject();
                    final double lat = firstResult.get("lat").getAsDouble();
                    final double lng = firstResult.get("lon").getAsDouble();

                    return new double[]{lat, lng};
                }
            }

            return chiediCoordinateManuali();

        } catch (URISyntaxException | IOException | InterruptedException e) {
            return chiediCoordinateManuali();
        }
    }

    private static double[] chiediCoordinateManuali() {
        final Scanner scanner = new Scanner(System.in);
        double latitudine = 0.0, longitudine = 0.0;
        boolean valido = false;

        while (!valido) {
            try {
                System.out.print("Inserisci la latitudine: ");
                latitudine = Double.parseDouble(scanner.nextLine().trim());
                System.out.print("Inserisci la longitudine: ");
                longitudine = Double.parseDouble(scanner.nextLine().trim());
                valido = true;
            } catch (NumberFormatException e) {
                System.err.println("Valore non valido. Inserisci numeri decimali validi.");
            }
        }

        scanner.close();
        return new double[]{latitudine, longitudine};
    }
}