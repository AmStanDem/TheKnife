package vista;

import java.time.temporal.ValueRange;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import servizi.GeocodingService;

public class MenuIniziale extends Menu {

    @Override
    public void mostra(){
        var sc = new Scanner(System.in);
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Menu Iniziale: ");
        System.out.println("1. Login");
        System.out.println("2. Registrazione");
        System.out.println("3. Guest");
        System.out.println("4. Esci");

        int selezione = sc.nextInt();

        switch (selezione) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                modalitaGuest(sc, r);
                break;
            case 4:
                System.exit(0);
                break;
        }
    }

    public static void modalitaGuest(Scanner sc, BufferedReader r) {
        System.out.println("\nSei ora in modalita' guest.\nInserisci la tua localita', cosi' da consigliarti i ristoranti nelle vicinanze:");
        System.out.print("\nLocalita':");
        String luogo;
        // metodo fittizio della ricerca dei ristoranti...
        try {
            luogo = r.readLine();
            double[] coords = GeocodingService.geocodeAddress(luogo);
            if (coords != null) {
                System.out.println("Latitudine: " + coords[0]);
                System.out.println("Longitudine: " + coords[1]);
            } else
                System.err.println("Geocoding fallito - indirizzo non trovato o errore di rete");
        } catch (IOException e){}
        int selezione = 0;
        while (selezione != 2) {
            System.out.println("\nInserisci uno dei numeri per eseguire il comando:");
            System.out.println(" 1. Inserisci una nuova localita'");
            System.out.println(" 2. Esci dalla modalita' guest");
            System.out.print("\nComando: ");
            selezione = sc.nextInt();
            switch (selezione) {
                case 1:
                    System.out.println("\nInserisci la tua localita', cosi' da consigliarti i ristoranti nelle vicinanze:");
                    System.out.print("\nLocalita':");
                    // metodo fittizio della ricerca dei ristoranti...
                    try {
                        luogo = r.readLine();
                        double[] coords = GeocodingService.geocodeAddress(luogo);
                        if (coords != null) {
                            System.out.println("Latitudine: " + coords[0]);
                            System.out.println("Longitudine: " + coords[1]);
                        } else
                            System.err.println("Geocoding fallito - indirizzo non trovato o errore di rete");
                    } catch (IOException e){}
                    break;
                case 2:
                    System.out.println("\nSei uscito dalla modalita' guest.");
                    break;
                default:
                    System.out.println("\nComando inserito non valido!");
            }
        }
    }

    public static void main(String[] args) {
        MenuIniziale menuIniziale = new MenuIniziale();
        menuIniziale.mostra();
    }
}

