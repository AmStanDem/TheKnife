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
 *
 * @author Thomas Riotto
 */
public final class GeocodingService {

    /** Oggetto per effettuare le richieste HTTP. */
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    /** Oggetto per effettuare la lettura dei dati in JSON. */
    private static final Gson gson = new Gson();

    /**
     * Costruttore privato della classe GeocodingService.
     * <p>
     * Utilizzato per evitare l'istanziamento diretto e forzare l'uso di metodi statici
     * o di singleton, se previsti dalla logica della classe.
     */
    private GeocodingService() {}


    /**
     * Effettua la geocodificazione di un indirizzo e restituisce le coordinate come array di double.
     *
     * @param address L'indirizzo da geocodificare
     * @return Array di double [latitudine, longitudine] o null se si è verificato un errore.
     */
    public static double[] geocodeAddress(String address) {
        try {
            final String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
            final String url = "https://nominatim.openstreetmap.org/search?format=json&q=" + encodedAddress;

            final HttpRequest request = HttpRequest.newBuilder()
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

            return null;

        } catch (URISyntaxException | IOException | InterruptedException e) {
            return null;
        }
    }

    /**
     * Consente l'inserimento manuale delle coordinate.
     * @param scanner Scanner per I/O.
     * @return Array di double [latitudine, longitudine]
     */
    public static double[] chiediCoordinateManuali(Scanner scanner) {
        double latitudine = 0.0, longitudine = 0.0;
        boolean valido = false;
        final String stop = "STOP";
        String input;

        while (!valido) {
            try {
                System.out.println("Url con: https://www.where-am-i.net/");
                System.out.print("Inserisci la latitudine: ");
                input = scanner.nextLine().trim();
                if (input.equalsIgnoreCase(stop)) {
                    System.out.println("Inserito Stop; Inserimento coordinate manuali interrotto.");
                    return null;
                }
                latitudine = Double.parseDouble(input);
                System.out.print("Inserisci la longitudine: ");
                input = scanner.nextLine().trim();
                if (input.equalsIgnoreCase(stop)) {
                    System.out.println("Inserito Stop; Inserimento coordinate manuali interrotto.");
                    return null;
                }
                longitudine = Double.parseDouble(input);
                valido = true;
            } catch (NumberFormatException e) {
                System.err.println("Valore non valido. Inserisci numeri decimali validi.");
            }
        }
        return new double[]{latitudine, longitudine};
    }
}