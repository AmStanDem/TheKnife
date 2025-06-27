package vista;

import Entita.Cliente;
import Entita.Ristoratore;
import Entita.Utente;
import com.opencsv.exceptions.CsvException;
import servizi.GeocodingService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;

import static io_file.GestoreFile.esisteUtente;

public class RegistrazioneService {
    private Scanner sc;
    public final String STOP = "STOP";

    public RegistrazioneService(Scanner sc) {
        this.sc = sc;
    }

    public Utente registraUtente() throws IOException, CsvException {
        String nome = chiediNome("nome");
        if (STOP.equals(nome)) return null;

        String cognome = chiediNome("cognome");
        if (STOP.equals(cognome)) return null;

        String username = chiediUsername();
        if (STOP.equals(username)) return null;

        String password = chiediPassword();
        if (STOP.equals(password)) return null;

        LocalDate dataNascita = chiediDataNascita();
        if (dataNascita == null) return null;

        String domicilio = chiediDomicilio();
        if (domicilio == null) return null;

        int ruolo = chiediRuolo();
        if (ruolo == 1)
            return new Cliente(nome, cognome, username, password, dataNascita, domicilio);
        else
            return new Ristoratore(nome, cognome, username, password, dataNascita, domicilio);
    }

    private String chiediNome(String campo) {
        while (true) {
            System.out.println("Inserisci il " + campo + ": ");
            String input = sc.nextLine().strip();

            if (input.equals(STOP)) return STOP;

            if (input.isBlank()) {
                System.out.println("Il " + campo + " non può essere vuoto");
                continue;
            }

            boolean soloLettere = true;
            for (char c : input.toCharArray()) {
                if (!Character.isLetter(c)) {
                    soloLettere = false;
                    break;
                }
            }

            if (!soloLettere) {
                System.out.println("Il " + campo + " deve contenere solo lettere");
            } else {
                return input;
            }
        }
    }

    private String chiediUsername() throws IOException, CsvException {
        while (true) {
            System.out.println("Inserisci username: ");
            String input = sc.nextLine().strip();

            if (input.equals(STOP)) return STOP;

            if (input.length() < 3 || input.length() > 16) {
                System.out.println("Deve avere tra 3 e 16 caratteri");
                continue;
            }

            boolean valido = true;
            for (char c : input.toCharArray()) {
                if (!Character.isLetterOrDigit(c) && c != '_') {
                    valido = false;
                    break;
                }
            }

            if (!valido) {
                System.out.println("Sono ammessi solo lettere, numeri e underscore");
                continue;
            }

            if (esisteUtente(input)) {
                System.out.println("Username già in uso");
                continue;
            }

            return input;
        }
    }

    private String chiediPassword() {
        while (true) {
            System.out.println("Inserisci password: ");
            String p = sc.nextLine().strip();

            if (p.equals(STOP)) return STOP;

            if (p.length() < 8 || p.length() > 16) {
                System.out.println("Password deve avere tra 8 e 16 caratteri");
                continue;
            }

            boolean contieneLettera = false, contieneNumero = false, contieneSpazio = false, contieneSimbolo = false;

            for (char c : p.toCharArray()) {
                if (Character.isLetter(c)) contieneLettera = true;
                else if (Character.isDigit(c)) contieneNumero = true;
                else if (Character.isWhitespace(c)) contieneSpazio = true;
                else contieneSimbolo = true;
            }

            if (contieneSpazio) {
                System.out.println("La password non deve contenere spazi.");
                continue;
            }

            if (!contieneLettera || !contieneNumero || !contieneSimbolo) {
                System.out.println("Deve contenere almeno una lettera, un numero e un simbolo.");
                continue;
            }

            return p;
        }
    }

    private LocalDate chiediDataNascita() {
        String anno, mese, giorno;
        int aaaa = 0, mm = 0, gg = 0;
        char c;
        int i;
        boolean bisestile = false;

        do {
            System.out.print("Inserisci l'anno di nascita: ");
            anno = sc.nextLine().strip();

            if (anno.equalsIgnoreCase(STOP)) {
                System.out.println("Registrazione annullata.");
                return null;
            }

            if (anno.isBlank()) {
                System.out.println("L'anno non può essere vuoto.");
                continue;
            }

            boolean valido = true;
            for (i = 0; i < anno.length(); i++) {
                c = anno.charAt(i);
                if (!Character.isDigit(c)) {
                    System.out.println("Carattere non valido: " + c);
                    valido = false;
                    break;
                }
            }

            if (!valido) continue;

            aaaa = Integer.parseInt(anno);
            if (aaaa < 1900 || aaaa > LocalDateTime.now().getYear()) {
                System.out.println("Anno non valido.");
                continue;
            }

            bisestile = (aaaa % 400 == 0 || (aaaa % 4 == 0 && aaaa % 100 != 0));
            break;

        } while (true);

        do {
            System.out.print("Inserisci il mese di nascita: ");
            mese = sc.nextLine().strip();

            if (mese.equalsIgnoreCase(STOP)) return null;
            if (mese.isBlank()) continue;

            try {
                mm = Integer.parseInt(mese);
                if (mm < 1 || mm > 12) {
                    System.out.println("Mese non valido.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Input non numerico.");
            }
        } while (true);

        do {
            System.out.print("Inserisci il giorno di nascita: ");
            giorno = sc.nextLine().strip();

            if (giorno.equalsIgnoreCase(STOP)) return null;
            if (giorno.isBlank()) continue;

            try {
                gg = Integer.parseInt(giorno);
                if (gg < 1 || gg > 31) {
                    System.out.println("Giorno non valido.");
                    continue;
                }
                if ((mm == 2 && !bisestile && gg > 28) || (mm == 2 && gg > 29)) {
                    System.out.println("Febbraio ha solo " + (bisestile ? 29 : 28) + " giorni.");
                    continue;
                }
                if ((mm == 4 || mm == 6 || mm == 9 || mm == 11) && gg > 30) {
                    System.out.println("Il mese ha solo 30 giorni.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Input non numerico.");
            }
        } while (true);

        return LocalDate.of(aaaa, mm, gg);
    }

    private String chiediDomicilio() {
        String via, civico, citta, luogoDomicilio;
        int i;
        char c;

        System.out.println("Inserisci il tuo domicilio");

        do {
            System.out.print("Via (senza numero civico): ");
            via = sc.nextLine().strip();
            if (via.equalsIgnoreCase(STOP)) return null;
        } while (via.isBlank());

        System.out.print("Numero civico (opzionale): ");
        civico = sc.nextLine().strip();
        if (civico.equalsIgnoreCase(STOP)) return null;

        do {
            System.out.print("Città: ");
            citta = sc.nextLine().strip();
            if (citta.equalsIgnoreCase(STOP)) return null;
        } while (citta.isBlank());

        luogoDomicilio = (via + " " + civico + ", " + citta).strip();
        System.out.println("Hai inserito: " + luogoDomicilio);

        try {
            double[] coords = GeocodingService.geocodeAddress(luogoDomicilio);
            System.out.println("Latitudine: " + coords[0] + ", Longitudine: " + coords[1]);
        } catch (Exception e) {
            System.out.println("Errore durante la geocodifica.");
        }

        return luogoDomicilio;
    }

    private int chiediRuolo() {
        while (true) {
            System.out.print("Scegli ruolo - 1: Cliente | 2: Ristoratore: ");
            String input = sc.nextLine().strip();

            if (input.equalsIgnoreCase(STOP)) {
                System.out.println("Registrazione annullata.");
                return -1; // oppure puoi gestire questo valore nel metodo registraUtente()
            }

            if (input.equals("1") || input.equals("2")) {
                return Integer.parseInt(input);
            }

            System.out.println("Scelta non valida. Inserisci 1 o 2.");
        }
    }
}
