package vista;

import java.time.LocalDate;
import java.time.temporal.ValueRange;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import Entita.Cliente;
import Entita.Ristoratore;
import Entita.Utente;
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
                registrazione(sc);
                break;
            case 3:
                modalitaGuest(sc, r);
                break;
            case 4:
                System.exit(0);
                break;
        }
    }


    public static void registrazione(Scanner sc) {
        /*
        *nome – Nome dell’utente (in chiaro, con qualsiasi formato)
        cognome – Cognome dell’utente (in chiaro)
        username – Username scelto per l’accesso
        password – Password in chiaro da cifrare
        dataNascita – Data di nascita dell’utente
        luogoDomicilio – Luogo di domicilio (in chiaro)
        * */


        System.out.println("Inserisci il nome del cliente");

        String nome;
        boolean stato = true;
        do {
            nome = sc.nextLine();
            for(int i= 0; i< nome.length();i++ ){
                char c = nome.charAt(i);
                if(!Character.isLetter(c)){
                    System.out.println("hai inserito un carattere che non e' una lettera:  "+ c + " in posizione " + (i+1)  );
                    stato = false;
                };
            }

        } while (nome.isBlank());


        System.out.println("Inserisci il cognome del cliente");
        String cognome = sc.nextLine();

        System.out.println("Inserisci il username del cliente");
        String username = sc.nextLine();

        System.out.println("Inserisci il password del cliente");
        String password = sc.nextLine();

        System.out.println("Inserisci il data di nascita");
        LocalDate dataDiNascita = LocalDate.parse(sc.nextLine());

        System.out.println("Inserisci il luogo do di nascita");
        String luogoDomicilio = sc.nextLine();

        System.out.println("ti registri da cliente(1) o da ristoratore(2)");

        int selezione = sc.nextInt();
        switch (selezione) {

            case 1:
                new Cliente(nome, cognome, username, password, dataDiNascita, luogoDomicilio);
                break;
            case 2:
                new Ristoratore(nome, cognome, username, password, dataDiNascita, luogoDomicilio);
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

