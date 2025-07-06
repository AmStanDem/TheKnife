package theknife.vista;

import theknife.entita.Cliente;
import theknife.entita.Ristoratore;
import theknife.entita.Utente;
import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;

import static theknife.io_file.GestoreFile.esisteUtente;
/*
 * Riotto Thomas 760981 VA
 * Pesavento Antonio 759933 VA
 * Tullo Alessandro 760760 VA
 * Zaro Marco 760194 VA
 */
/**
 * Servizio per gestire la registrazione di un nuovo utente nel sistema.
 * Permette di acquisire i dati necessari tramite input da console e validarne la correttezza.
 * Supporta sia utenti Cliente sia Ristoratore.
 *
 * @author Alessandro Tullo
 */
public class RegistrazioneService {
    /**
     * Scanner per leggere l'input da console.
     */
    private final Scanner sc;

    /**
     * Stringa che consente di interrompere la registrazione in qualsiasi momento.
     */
    public final String STOP = "STOP";

    /**
     * Costruisce un servizio di registrazione utilizzando uno Scanner per l'input.
     *
     * @param sc Scanner per l'input da console.
     */
    public RegistrazioneService(Scanner sc) {
        this.sc = sc;
    }

    /**
     * Avvia il processo di registrazione chiedendo tutti i dati necessari.
     *
     * @return Un nuovo oggetto Utente (Cliente o Ristoratore) se la registrazione ha successo,
     *         altrimenti null se la registrazione è stata annullata o fallita.
     * @throws IOException Se ci sono problemi di I/O nella verifica username.
     * @throws CsvException Se ci sono problemi di lettura/scrittura nel file CSV utenti.
     */
    public Utente registraUtente() throws IOException, CsvException {
        String nome = chiediNome("nome");
        if (nome == null) return null;

        String cognome = chiediNome("cognome");
        if (cognome == null) return null;

        String username = chiediUsername();
        if (username == null) return null;

        String password = chiediPassword();
        if (password == null) return null;

        LocalDate dataNascita = chiediDataNascita();
        if (dataNascita == null) return null;

        String domicilio = chiediDomicilio();
        if (domicilio == null) return null;

        int ruolo = chiediRuolo();
        switch (ruolo) {
            case 1 -> {
                return new Cliente(nome, cognome, username, password, dataNascita, domicilio);
            }
            case 2 -> {
                return new Ristoratore(nome, cognome, username, password, dataNascita, domicilio);
            }
            default -> {
                return null;
            }
        }
    }

    /**
     * Chiede e valida un nome o cognome, assicurandosi che contenga solo lettere.
     *
     * @param campo Il nome del campo da chiedere (es. "nome", "cognome").
     * @return La stringa validata o null se l'utente ha inserito STOP.
     */
    private String chiediNome(String campo) {
        while (true) {
            System.out.println("Inserisci il " + campo + ": ");
            String input = sc.next().strip();

            if (input.equalsIgnoreCase(STOP)) return null;

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

    /**
     * Chiede e valida lo username.
     * Lo username deve essere lungo tra 3 e 16 caratteri e contenere solo lettere, numeri o underscore.
     * Verifica anche che lo username non sia già esistente.
     *
     * @return Lo username valido o null se l'utente ha inserito STOP.
     * @throws IOException Se ci sono errori di I/O.
     * @throws CsvException Se ci sono errori nella lettura/scrittura del file CSV.
     */
    private String chiediUsername() throws IOException, CsvException {
        while (true) {
            System.out.println("Inserisci username: ");
            String input = sc.next().strip();

            if (input.equalsIgnoreCase(STOP)) return null;

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

    /**
     * Chiede e valida la password.
     * La password deve essere lunga tra 8 e 16 caratteri, non contenere spazi,
     * e contenere almeno una lettera, un numero e un simbolo.
     *
     * @return La password valida o null se l'utente ha inserito STOP.
     */
    private String chiediPassword() {
        while (true) {
            System.out.println("Inserisci password: ");
            String p = sc.nextLine().strip();

            if (p.equalsIgnoreCase(STOP)) return null;

            if (p.length() < 8 || p.length() > 16) {
                System.out.println("Password deve avere tra 8 e 16 caratteri");
            }

            boolean contieneLettera = false, contieneNumero = false, contieneSpazio = false, contieneSimbolo = false, corretto = true;

            for (char c : p.toCharArray()) {
                if (Character.isLetter(c)) contieneLettera = true;
                else if (Character.isDigit(c)) contieneNumero = true;
                else if (Character.isWhitespace(c)) contieneSpazio = true;
                else contieneSimbolo = true;
            }

            if (contieneSpazio) {
                System.out.println("La password non deve contenere spazi.");
                corretto = false;
            }

            if (!contieneLettera || !contieneNumero || !contieneSimbolo) {
                System.out.println("La password deve contenere almeno una lettera, un numero e un simbolo.");
                corretto = false;
            }

            if(!corretto)
                continue;

            return p;
        }
    }

    /**
     * Chiede e valida la data di nascita (anno, mese, giorno).
     * Gestisce il controllo degli anni bisestili e la correttezza delle date.
     *
     * @return La data di nascita validata come LocalDate oppure null se annullata.
     */
    private LocalDate chiediDataNascita() {
        String anno, mese, giorno;
        int aaaa, mm ,gg;
        char c;
        int i;
        boolean bisestile;

        do {
            System.out.print("Inserisci l'anno di nascita: ");
            anno = sc.nextLine().strip();

            if (anno.equalsIgnoreCase(STOP)) {
                System.out.println("Registrazione annullata.");
                return null;
            }

            if (anno.isBlank()) {
                return null;
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
            if (mese.isBlank()) return null;

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
            if (giorno.isBlank()) return null;

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

    /**
     * Chiede e valida il domicilio dell'utente.
     * Il domicilio è composto da via, numero civico (opzionale) e città.
     *
     * @return La stringa del domicilio completo o null se l'utente ha inserito STOP.
     */
    public String chiediDomicilio() {
        String via, civico, citta, luogoDomicilio;

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

        luogoDomicilio = (via + (civico.isEmpty() ? "" : " " + civico) + ", " + citta).strip();
        System.out.println("Hai inserito: " + luogoDomicilio);

        return luogoDomicilio;
    }

    /**
     * Chiede e valida il ruolo dell'utente.
     * Ruolo 1 = Cliente, Ruolo 2 = Ristoratore.
     *
     * @return Un intero rappresentante il ruolo scelto oppure -1 se l'utente annulla.
     */
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
