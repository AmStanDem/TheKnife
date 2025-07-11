package theknife.io_file;

import theknife.entita.*;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.nio.file.Paths;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
/*
 * Riotto Thomas 760981 VA
 * Pesavento Antonio 759933 VA
 * Tullo Alessandro 760760 VA
 * Zaro Marco 760194 VA
 */
/**
 * Gestore per le operazioni di lettura e scrittura su file CSV
 * per utenti, ristoranti, recensioni e preferiti.
 *
 * @author Thomas Riotto
 */
public class GestoreFile {

    //region === ATTRIBUTI ===

    // === METODO PER OTTENERE IL PERCORSO DINAMICO ===
    private static Path getPercorso(String nomeFile) {
        try {
            Path baseDir = Paths.get(GestoreFile.class.getProtectionDomain()
                            .getCodeSource()
                            .getLocation()
                            .toURI())
                    .getParent();

            // Se siamo in esecuzione da IDE (es. out/production/TheKnife), saliamo di 2 livelli
            if (baseDir.endsWith("production") || baseDir.toString().contains("out")) {
                baseDir = baseDir.getParent().getParent(); // Torna a TheKnife/
            } else {
                baseDir = baseDir.getParent(); // Siamo nel JAR in bin/
            }

            return baseDir.resolve("data").resolve(nomeFile);
        } catch (Exception e) {
            throw new RuntimeException("Errore nel calcolo del percorso del file: " + nomeFile, e);
        }
    }

    //region === PERCORSI DEI FILE CSV ===

    /**
     * Path del file utenti
     */
    private static final Path DATASET_UTENTI = getPercorso("Utenti.csv");

    /**
     * Path del file ristoranti
     */
    private static final Path DATASET_RISTORANTI = getPercorso("Ristoranti.csv");

    /**
     * Path del file recensioni
     */
    private static final Path DATASET_RECENSIONI = getPercorso("Recensioni.csv");

    /**
     * Path del file preferiti
     */
    private static final Path DATASET_PREFERITI = getPercorso("Preferiti.csv");

    //endregion

    //region === INTESTAZIONI DEI FILE CSV ===
    /**
     * Intestazione del file recensioni.csv
     */
    private static final String[] INTESTAZIONE_RECENSIONI = {
            "Username", "Ristorante", "Nazione", "Città", "Indirizzo",
            "Latitudine", "Longitudine", "Stelle", "Messaggio", "Data", "Risposta", "DataRisposta"
    };
    //endregion

    //region === CLASSI STATICHE PER INDICI COLONNE CSV ===

    /**
     * Rappresenta le posizioni dei campi del file Utenti.csv
     */
    private static class ColonneUtenteCSV {
        public static final int NOME = 0;
        public static final int COGNOME = 1;
        public static final int USERNAME = 2;
        public static final int PASSWORD = 3;
        public static final int DATA_NASCITA = 4;
        public static final int LUOGO_DOMICILIO = 5;
        public static final int TIPO_UTENTE = 6;
    }

    /**
     * Rappresenta le posizioni dei campi del file Ristoranti.csv
     */
    private static class ColonneRistoranteCSV {
        public static final int NOME = 0;
        public static final int PREZZO_MEDIO = 1;
        public static final int TIPO_CUCINA = 2;
        public static final int NAZIONE = 3;
        public static final int CITTA = 4;
        public static final int INDIRIZZO = 5;
        public static final int LATITUDINE = 6;
        public static final int LONGITUDINE = 7;
        public static final int DESCRIZIONE = 8;
        public static final int DELIVERY = 9;
        public static final int PRENOTAZIONE = 10;
        public static final int USERNAME = 11;
    }

    /**
     * Rappresenta le posizioni dei campi del file Recensioni.csv
     */
    private static class ColonneRecensioneCSV {
        public static final int USERNAME = 0;
        public static final int RISTORANTE = 1;
        public static final int NAZIONE = 2;
        public static final int CITTA = 3;
        public static final int INDIRIZZO = 4;
        public static final int LATITUDINE = 5;
        public static final int LONGITUDINE = 6;
        public static final int STELLE = 7;
        public static final int MESSAGGIO = 8;
        public static final int DATA = 9;
        public static final int RISPOSTA = 10;
        public static final int DATA_RISPOSTA = 11;
    }

    /**
     * Rappresenta le posizioni dei campi del file Preferiti.csv
     */
    private static class ColonnePreferitiCSV {
        public static final int USERNAME = 0;
        public static final int RISTORANTE = 1;
        public static final int NAZIONE = 2;
        public static final int CITTA = 3;
        public static final int INDIRIZZO = 4;
        public static final int LATITUDINE = 5;
        public static final int LONGITUDINE = 6;
    }
    //endregion

    /** Valore di lookup per i boolean del delivery e prenotazione. */
    private static final String VALORE_SI = "Sì";
    /** Valore di lookup per i boolean del delivery e prenotazione. */
    private static final String VALORE_NO = "No";

    //endregion

    //region === COSTRUTTORI ===

    /**
     * Cotruttore vuoto tenuto intenzionalmente privato.
     */
    private GestoreFile() {
    }
    //endregion

    //region === METODI ===

    //region === OPERAZIONI I/O SUI RISTORANTI ===

    /**
     * Carica tutti i ristoranti dal file CSV.
     *
     * @return Lista dei ristoranti caricati
     * @throws IOException  se si verifica un errore di I/O
     * @throws CsvException se si verifica un errore nel parsing del CSV
     */
    public static ArrayList<Ristorante> caricaRistoranti() throws IOException, CsvException {
        ArrayList<Ristorante> ristoranti = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(DATASET_RISTORANTI.toFile()))) {
            List<String[]> righe = reader.readAll();

            // Salta l'intestazione (prima riga)
            for (int i = 1; i < righe.size(); i++) {
                String[] riga = righe.get(i);
                Ristorante ristorante = creaRistoranteDaRiga(riga);
                ristoranti.add(ristorante);
            }
        }

        return ristoranti;
    }

    /**
     * Carica tutti i ristoranti di un ristoratore dal file CSV.
     *
     * @param username Username del ristoratore
     * @return Lista dei ristoranti caricati
     * @throws IOException  se si verifica un errore di I/O
     * @throws CsvException se si verifica un errore nel parsing del CSV
     */
    public static ArrayList<Ristorante> caricaRistoranti(String username) throws IOException, CsvException {

        if (username == null) {
            return null;
        }

        ArrayList<Ristorante> ristoranti = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(DATASET_RISTORANTI.toFile()))) {
            List<String[]> righe = reader.readAll();

            // Salta l'intestazione (prima riga)
            for (int i = 1; i < righe.size(); i++) {
                String[] riga = righe.get(i);
                if (username.equals(riga[ColonneRistoranteCSV.USERNAME])) {
                    Ristorante ristorante = creaRistoranteDaRiga(riga);
                    ristoranti.add(ristorante);
                }
            }
        }

        return ristoranti;
    }

    /**
     * Aggiunge un nuovo ristorante al file CSV, controllando che non esistano duplicati.
     * Un duplicato è definito come un ristorante con lo stesso nome e la stessa località.
     *
     * @param ristorante Il ristorante da aggiungere
     * @return {@code true} se il ristorante è stato aggiunto con successo, {@code false} se esiste già
     * @throws IOException  se si verifica un errore di I/O
     * @throws CsvException se si verifica un errore nel parsing del CSV
     */
    public static boolean aggiungiRistorante(Ristorante ristorante) throws IOException, CsvException {
        if (esisteRistorante(ristorante.getNome(), ristorante.getLocalita())) {
            return false;
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(DATASET_RISTORANTI.toFile(), true))) {
            String[] datiRistorante = creaRigaDaRistorante(ristorante);
            writer.writeNext(datiRistorante);
        }

        return true;
    }

    /**
     * Cerca un ristorante specifico per nome e località.
     *
     * @param nome     Il nome del ristorante da cercare
     * @param localita La località del ristorante da cercare
     * @return Il ristorante trovato o null se non esiste
     * @throws IOException  se si verifica un errore di I/O
     * @throws CsvException se si verifica un errore nel parsing del CSV
     */
    public static Ristorante cercaRistorante(String nome, Localita localita) throws IOException, CsvException {
        try (CSVReader reader = new CSVReader(new FileReader(DATASET_RISTORANTI.toFile()))) {
            List<String[]> righe = reader.readAll();

            // Salta l'intestazione (prima riga)
            for (int i = 1; i < righe.size(); i++) {
                String[] riga = righe.get(i);

                String nomeFile = riga[ColonneRistoranteCSV.NOME];
                String nazioneFile = riga[ColonneRistoranteCSV.NAZIONE];
                String cittaFile = riga[ColonneRistoranteCSV.CITTA];
                String indirizzoFile = riga[ColonneRistoranteCSV.INDIRIZZO];
                double latitudineFile = Double.parseDouble(riga[ColonneRistoranteCSV.LATITUDINE]);
                double longitudineFile = Double.parseDouble(riga[ColonneRistoranteCSV.LONGITUDINE]);

                // Controlla se corrisponde al ristorante cercato
                if (nomeFile.equals(nome) &&
                        nazioneFile.equals(localita.getNazione()) &&
                        cittaFile.equals(localita.getCitta()) &&
                        indirizzoFile.equals(localita.getIndirizzo()) &&
                        latitudineFile == localita.getLatitudine() &&
                        longitudineFile == localita.getLongitudine()) {

                    return creaRistoranteDaRiga(riga);
                }
            }
        }

        return null;
    }

    /**
     * Verifica se esiste già un ristorante con il dato nome e località.
     *
     * @param nome     Il nome del ristorante da verificare
     * @param localita La località del ristorante da verificare
     * @return {@code true} se il ristorante esiste, {@code false} altrimenti
     * @throws IOException  se si verifica un errore di I/O
     * @throws CsvException se si verifica un errore nel parsing del CSV
     */
    public static boolean esisteRistorante(String nome, Localita localita) throws IOException, CsvException {
        return cercaRistorante(nome, localita) != null;
    }

    private static Ristorante creaRistoranteDaRiga(String[] riga) {
        try {
            // Estrai i dati dalla riga
            String nome = riga[ColonneRistoranteCSV.NOME];
            String nazione = riga[ColonneRistoranteCSV.NAZIONE];
            String citta = riga[ColonneRistoranteCSV.CITTA];
            String indirizzo = riga[ColonneRistoranteCSV.INDIRIZZO];
            double latitudine = Double.parseDouble(riga[ColonneRistoranteCSV.LATITUDINE]);
            double longitudine = Double.parseDouble(riga[ColonneRistoranteCSV.LONGITUDINE]);
            float prezzoMedio = Float.parseFloat(riga[ColonneRistoranteCSV.PREZZO_MEDIO]);
            boolean delivery = Boolean.parseBoolean(riga[ColonneRistoranteCSV.DELIVERY]);
            boolean prenotazione = Boolean.parseBoolean(riga[ColonneRistoranteCSV.PRENOTAZIONE]);
            TipoCucina tipoCucina = TipoCucina.valueOf(riga[ColonneRistoranteCSV.TIPO_CUCINA].toUpperCase());
            String descrizione = riga[ColonneRistoranteCSV.DESCRIZIONE];
            String usernameProprietario = riga[ColonneRistoranteCSV.USERNAME];

            // Crea la località
            Localita localita = new Localita(nazione, citta, indirizzo, latitudine, longitudine);

            // Cerca il ristoratore tramite username
            Utente utente = cercaUtente(usernameProprietario);
            if (!(utente instanceof Ristoratore proprietario)) {
                System.err.println("Ristoratore non trovato o non valido per username: " + usernameProprietario);
                return null;
            }
            return new Ristorante(nome, localita, tipoCucina, delivery, prenotazione,
                    prezzoMedio, descrizione, proprietario);

        } catch (Exception e) {
            System.err.println("Errore nella creazione del ristorante dalla riga CSV: " + e.getMessage());
            return null;
        }
    }

    /**
     * Converte un oggetto Ristorante in un array di stringhe per il CSV.
     */
    private static String[] creaRigaDaRistorante(Ristorante ristorante) {
        return new String[]{
                ristorante.getNome(),
                String.valueOf(ristorante.getPrezzoMedio()),
                String.valueOf(ristorante.getTipoDiCucina()),
                ristorante.getLocalita().getNazione(),
                ristorante.getLocalita().getCitta(),
                ristorante.getLocalita().getIndirizzo(),
                String.valueOf(ristorante.getLocalita().getLatitudine()),
                String.valueOf(ristorante.getLocalita().getLongitudine()),
                ristorante.getDescrizione(),
                ristorante.getDelivery() ? VALORE_SI : VALORE_NO,
                ristorante.getPrenotazione() ? VALORE_SI : VALORE_NO,
                ristorante.getUsernameProprietario()
        };
    }

    //endregion

    //region === OPERAZIONI GENERALI SUGLI UTENTI ===

    /**
     * Aggiunge un nuovo utente al file CSV.
     * Funziona sia per clienti che per ristoratori.
     *
     * @param utente L'utente da aggiungere
     * @return {@code true} se l'utente è stato aggiunto con successo, {@code false} se esiste già
     * @throws IOException  se si verifica un errore di I/O
     * @throws CsvException se si verifica un errore nel parsing del CSV
     */
    public static boolean aggiungiUtente(Utente utente) throws IOException, CsvException {
        if (esisteUtente(utente.getUsername())) {
            return false;
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(DATASET_UTENTI.toFile(), true))) {
            String[] datiUtente = creaRigaDaUtente(utente);
            writer.writeNext(datiUtente);
        }
        return true;
    }

    /**
     * Cerca un utente per username.
     * Restituisce il tipo corretto (Cliente o Ristoratore).
     *
     * @param username L'username dell'utente da cercare
     * @return L'utente trovato o null se non esiste
     * @throws IOException  se si verifica un errore di I/O
     * @throws CsvException se si verifica un errore nel parsing del CSV
     */
    public static Utente cercaUtente(String username) throws IOException, CsvException {
        try (BufferedReader br = Files.newBufferedReader(DATASET_UTENTI);

             CSVReader reader = new CSVReaderBuilder(br)
                     .withSkipLines(1)
                     .build()) {

            String[] riga;
            while ((riga = reader.readNext()) != null) {
                if (username.equals(riga[ColonneUtenteCSV.USERNAME])) {
                    return creaUtenteDaRiga(riga);
                }
            }
        }
        return null;
    }

    /**
     * Verifica se esiste già un utente con il dato username.
     *
     * @param username L'username da verificare
     * @return {@code true} se l'utente esiste, {@code false} altrimenti
     * @throws IOException  se si verifica un errore di I/O
     * @throws CsvException se si verifica un errore nel parsing del CSV
     */
    public static boolean esisteUtente(String username) throws IOException, CsvException {
        return cercaUtente(username) != null;
    }

    /**
     * Verifica le credenziali di login di un utente.
     * La password viene verificata tramite BCrypt contro l'hash salvato.
     *
     * @param username L'username dell'utente
     * @param password La password in chiaro da verificare
     * @return I dati dell'utenti se esso esiste, altrimenti null
     * @throws IOException  se si verifica un errore di I/O
     * @throws CsvException se si verifica un errore nel parsing del CSV
     */
    public static Utente verificaLogin(String username, String password) throws IOException, CsvException {
        if (username == null || password == null || username.isBlank() || password.isBlank()) {
            return null;
        }

        try (BufferedReader br = Files.newBufferedReader(DATASET_UTENTI);
             CSVReader reader = new CSVReaderBuilder(br)
                     .withSkipLines(1)
                     .build()) {

            String[] riga;
            while ((riga = reader.readNext()) != null) {
                if (username.equals(riga[ColonneUtenteCSV.USERNAME])) {
                    Utente utente = creaUtenteDaRiga(riga);

                    if (utente != null && utente.verificaPassword(password)) {
                        return utente;
                    }
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * Crea un oggetto Utente (Cliente o Ristoratore) da una riga del CSV.
     */
    private static Utente creaUtenteDaRiga(String[] riga) {
        try {
            String nome = riga[ColonneUtenteCSV.NOME];
            String cognome = riga[ColonneUtenteCSV.COGNOME];
            String username = riga[ColonneUtenteCSV.USERNAME];
            String password = riga[ColonneUtenteCSV.PASSWORD];
            LocalDate dataNascita = riga[ColonneUtenteCSV.DATA_NASCITA].isEmpty() ?
                    null : LocalDate.parse(riga[ColonneUtenteCSV.DATA_NASCITA]);
            String luogoDomicilio = riga[ColonneUtenteCSV.LUOGO_DOMICILIO];
            String tipoUtente = riga[ColonneUtenteCSV.TIPO_UTENTE];

            Utente utente;

            switch (tipoUtente.toLowerCase()) {
                case "cliente":
                    utente = new Cliente(nome, cognome, username, "temp", dataNascita, luogoDomicilio);
                    break;
                case "ristoratore":
                    utente = new Ristoratore(nome, cognome, username, "temp", dataNascita, luogoDomicilio);
                    break;
                default:
                    return null;
            }

            // Imposta la password già cifrata
            utente.setPasswordCifrata(password);
            return utente;

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Converte un oggetto Utente in un array di stringhe per il CSV.
     */
    private static String[] creaRigaDaUtente(Utente utente) {
        return new String[]{
                utente.getNome(),
                utente.getCognome(),
                utente.getUsername(),
                utente.getPassword(),
                utente.getDataNascita() != null ?
                        utente.getDataNascita().format(DateTimeFormatter.ISO_DATE) : "",
                utente.getLuogoDomicilio(),
                utente.getTipoUtente()
        };
    }


    //region === OPERAZIONI GENERALI SULLE RECENSIONI ===

    /**
     * Carica tutte le recensioni dal file CSV.
     *
     * @return Lista delle recensioni caricate
     * @throws IOException  se si verifica un errore di I/O
     * @throws CsvException se si verifica un errore nel parsing del CSV
     */
    public static ArrayList<Recensione> caricaRecensioni() throws IOException, CsvException {
        ArrayList<Recensione> recensioni = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(DATASET_RECENSIONI.toFile()))) {
            List<String[]> righe = reader.readAll();

            // Salta l'intestazione (prima riga)
            for (int i = 1; i < righe.size(); i++) {
                String[] riga = righe.get(i);
                Recensione recensione = creaRecensioneDaRiga(riga);
                if (recensione != null) {
                    recensioni.add(recensione);
                }
            }
        }
        return recensioni;
    }

    /**
     * Aggiunge una nuova recensione al file CSV, controllando che non esistano duplicati.
     * Un duplicato è definito come una recensione dello stesso cliente per lo stesso ristorante.
     *
     * @param recensione La recensione da aggiungere
     * @return {@code true} se la recensione è stata aggiunta con successo, {@code false} altrimenti.
     * @throws IOException  se si verifica un errore di I/O
     * @throws CsvException se si verifica un errore nel parsing del CSV
     */
    public static boolean aggiungiRecensione(Recensione recensione) throws IOException, CsvException {
        Recensione recensioneEsistente = cercaRecensione(recensione.getCliente(), recensione.getRistorante());

        if (recensioneEsistente != null) {
            return false;
        }

        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(DATASET_RECENSIONI.toFile(), true))) {
            String[] datiRecensione = creaRigaDaRecensione(recensione);
            csvWriter.writeNext(datiRecensione);
        }
        return true;
    }

    /**
     * Carica le recensioni di un cliente specifico
     *
     * @param username Username del cliente
     * @return Lista delle recensioni del cliente
     * @throws IOException  se si verifica un errore di I/O
     * @throws CsvException se si verifica un errore nel parsing del CSV
     */
    public static ArrayList<Recensione> caricaRecensioniCliente(String username) throws IOException, CsvException {
        ArrayList<Recensione> recensioniCliente = new ArrayList<>();

        for (Recensione recensione : caricaRecensioni()) {
            if (recensione.appartieneA(username)) {
                recensioniCliente.add(recensione);
            }
        }

        return recensioniCliente;
    }

    /**
     * Carica le recensioni di un cliente specifico
     *
     * @return Lista delle recensioni del cliente
     * @throws IOException  se si verifica un errore di I/O
     * @throws CsvException se si verifica un errore nel parsing del CSV
     */
    public static ArrayList<Recensione> caricaRecensioniCliente(Cliente cliente) throws IOException, CsvException {
        return caricaRecensioniCliente(cliente.getUsername());
    }

    /**
     * Carica le recensioni di un ristorante specifico
     *
     * @param ristorante Il ristorante di cui caricare le recensioni
     * @return Lista delle recensioni del ristorante
     * @throws IOException  se si verifica un errore di I/O
     * @throws CsvException se si verifica un errore nel parsing del CSV
     */
    public static ArrayList<Recensione> caricaRecensioniRistorante(Ristorante ristorante) throws IOException, CsvException {
        ArrayList<Recensione> recensioniRistorante = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(DATASET_RECENSIONI.toFile()))) {
            List<String[]> righe = reader.readAll();

            // Salta l'intestazione (prima riga)
            for (int i = 1; i < righe.size(); i++) {
                String[] riga = righe.get(i);
                Recensione recensione = creaRecensioneDaRiga(riga);

                if (recensione != null && recensione.getRistorante().equals(ristorante)) {
                    recensioniRistorante.add(recensione);
                }
            }
        }

        return recensioniRistorante;
    }


    /**
     * Aggiorna una recensione esistente nel file CSV
     *
     * @param vecchiaRecensione La recensione da sostituire
     * @param nuovaRecensione   La nuova recensione
     * @throws IOException  se si verifica un errore di I/O
     * @throws CsvException se si verifica un errore nel parsing del CSV
     */
    public static boolean aggiornaRecensione(Recensione vecchiaRecensione, Recensione nuovaRecensione)
            throws IOException, CsvException {

        if (vecchiaRecensione == null) {
            return false;
        }

        if (nuovaRecensione == null) {
            return false;
        }

        ArrayList<Recensione> tutteRecensioni = caricaRecensioni();

        for (int i = 0; i < tutteRecensioni.size(); i++) {
            if (tutteRecensioni.get(i).equals(vecchiaRecensione)) {
                tutteRecensioni.set(i, nuovaRecensione);
                break;
            }
        }
        riscriviFileRecensioni(tutteRecensioni);
        return true;
    }

    /**
     * Elimina una recensione dal file CSV
     *
     * @param recensione La recensione da eliminare
     * @return {@code true} se la recensione è stata rimossa correttamente, {@code false} altrimenti
     * @throws IOException  se si verifica un errore di I/O
     * @throws CsvException se si verifica un errore nel parsing del CSV
     */
    public static boolean eliminaRecensione(Recensione recensione) throws IOException, CsvException {
        ArrayList<Recensione> tutteRecensioni = caricaRecensioni();
        tutteRecensioni.removeIf(r -> r.equals(recensione));

        // Riscrivi tutto il file
        riscriviFileRecensioni(tutteRecensioni);
        return true;
    }

    /**
     * Cerca una recensione specifica nel database
     *
     * @param cliente    Il cliente che ha fatto la recensione
     * @param ristorante Il ristorante recensito
     * @return La recensione trovata o null se non esiste
     * @throws IOException  se si verifica un errore di I/O
     * @throws CsvException se si verifica un errore nel parsing del CSV
     */
    public static Recensione cercaRecensione(Cliente cliente, Ristorante ristorante) throws IOException, CsvException {
        for (Recensione recensione : caricaRecensioni()) {
            if (recensione.getCliente().equals(cliente) && recensione.getRistorante().equals(ristorante)) {
                return recensione;
            }
        }
        return null;
    }

    /**
     * Crea un oggetto Recensione da una riga del CSV.
     */
    private static Recensione creaRecensioneDaRiga(String[] riga) {
        try {
            String username = riga[ColonneRecensioneCSV.USERNAME];
            String nomeRistorante = riga[ColonneRecensioneCSV.RISTORANTE];
            String nazione = riga[ColonneRecensioneCSV.NAZIONE];
            String citta = riga[ColonneRecensioneCSV.CITTA];
            String indirizzo = riga[ColonneRecensioneCSV.INDIRIZZO];
            double latitudine = Double.parseDouble(riga[ColonneRecensioneCSV.LATITUDINE]);
            double longitudine = Double.parseDouble(riga[ColonneRecensioneCSV.LONGITUDINE]);
            int stelle = Integer.parseInt(riga[ColonneRecensioneCSV.STELLE]);
            String messaggio = riga[ColonneRecensioneCSV.MESSAGGIO];
            LocalDateTime data = LocalDateTime.parse(riga[ColonneRecensioneCSV.DATA]);

            // Trova il cliente
            Utente utente = cercaUtente(username);
            if (!(utente instanceof Cliente cliente)) {
                return null;
            }

            // Crea il ristorante dalla riga (assumendo che esista nel file ristoranti)
            Localita localita = new Localita(nazione, citta, indirizzo, latitudine, longitudine);

            // Trova il ristorante completo dal database
            Ristorante ristorante = null;
            for (Ristorante r : caricaRistoranti()) {
                if (r.getNome().equals(nomeRistorante) && r.getLocalita().equals(localita)) {
                    ristorante = r;
                    break;
                }
            }

            if (ristorante == null) {
                return null;
            }

            // Crea la recensione
            Recensione recensione = new Recensione(cliente, ristorante, stelle, messaggio, data);

            // Gestisce risposta del ristoratore se presente
            String risposta = riga[ColonneRecensioneCSV.RISPOSTA];
            if (risposta != null && !risposta.trim().isEmpty()) {
                recensione.aggiungiRisposta(risposta);
                recensione.setDataRisposta(LocalDateTime.parse(riga[ColonneRecensioneCSV.DATA_RISPOSTA]));
            }

            return recensione;

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Converte un oggetto Recensione in un array di stringhe per il CSV.
     */
    private static String[] creaRigaDaRecensione(Recensione recensione) {
        return new String[]{
                recensione.getCliente().getUsername(),
                recensione.getRistorante().getNome(),
                recensione.getRistorante().getLocalita().getNazione(),
                recensione.getRistorante().getLocalita().getCitta(),
                recensione.getRistorante().getLocalita().getIndirizzo(),
                String.valueOf(recensione.getRistorante().getLocalita().getLatitudine()),
                String.valueOf(recensione.getRistorante().getLocalita().getLongitudine()),
                String.valueOf(recensione.getStelle()),
                recensione.getMessaggio(),
                recensione.getDataRecensione().toString(),
                recensione.getRispostaRistoratore() != null ? recensione.getRispostaRistoratore() : "",
                recensione.getDataRisposta() != null ? recensione.getDataRisposta().toString() : ""
        };
    }

    /**
     * Riscrive completamente il file delle recensioni
     *
     * @param recensioni Lista delle recensioni da scrivere
     * @throws IOException se si verifica un errore di I/O
     */
    private static void riscriviFileRecensioni(ArrayList<Recensione> recensioni) throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(DATASET_RECENSIONI.toFile()))) {
            writer.writeNext(INTESTAZIONE_RECENSIONI);

            for (Recensione recensione : recensioni) {
                String[] riga = creaRigaDaRecensione(recensione);
                writer.writeNext(riga);
            }
        }
    }

    //endregion

    //region === OPERAZIONI SUI PREFERITI ===

    /**
     * Carica tutti i ristoranti preferiti di un cliente specifico.
     *
     * @param username Username del cliente
     * @return Lista dei ristoranti preferiti del cliente
     * @throws IOException  se si verifica un errore di I/O
     * @throws CsvException se si verifica un errore nel parsing del CSV
     */
    public static ArrayList<Ristorante> caricaPreferiti(String username) throws IOException, CsvException {
        ArrayList<Ristorante> ristoranti = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(DATASET_PREFERITI.toFile()))) {
            List<String[]> righe = reader.readAll();

            // Salta l'intestazione (prima riga)
            for (int i = 1; i < righe.size(); i++) {
                String[] riga = righe.get(i);

                if (riga[ColonnePreferitiCSV.USERNAME].equals(username)) {
                    Ristorante ristorante = creaRistoranteDaRigaPreferiti(riga);
                    if (ristorante != null) {
                        ristoranti.add(ristorante);
                    }
                }
            }
        }
        return ristoranti;
    }

    /**
     * Aggiunge un ristorante ai preferiti di un cliente, controllando che non esistano duplicati.
     * Un duplicato è definito come lo stesso ristorante già presente nei preferiti dello stesso cliente.
     *
     * @param cliente    Il cliente che aggiunge il preferito
     * @param ristorante Il ristorante da aggiungere ai preferiti
     * @return {@code true} se il preferito è stato aggiunto con successo, {@code false} altrimenti
     * @throws IOException  se si verifica un errore di I/O
     * @throws CsvException se si verifica un errore nel parsing del CSV
     */
    public static boolean aggiungiPreferito(Cliente cliente, Ristorante ristorante) throws IOException, CsvException {
        if (esistePreferito(cliente.getUsername(), ristorante)) {
            return false;
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(DATASET_PREFERITI.toFile(), true))) {
            String[] datiPreferito = creaRigaDaPreferito(cliente, ristorante);
            writer.writeNext(datiPreferito);
        }
        return true;
    }

    /**
     * Rimuove un ristorante dai preferiti di un cliente.
     *
     * @param cliente    Il cliente che rimuove il preferito
     * @param ristorante Il ristorante da rimuovere dai preferiti
     * @return {@code true} se il preferito è stato rimosso con successo, {@code false} altrimenti
     * @throws IOException  se si verifica un errore di I/O
     * @throws CsvException se si verifica un errore nel parsing del CSV
     */
    public static boolean rimuoviPreferito(Cliente cliente, Ristorante ristorante) throws IOException, CsvException {
        LinkedList<String[]> tutteLeRighe = new LinkedList<>();
        boolean rimosso = false;

        try (CSVReader reader = new CSVReader(new FileReader(DATASET_PREFERITI.toFile()))) {
            List<String[]> righe = reader.readAll();

            // Aggiungi l'intestazione
            if (!righe.isEmpty()) {
                tutteLeRighe.add(righe.getFirst());
            }

            // Processa le righe saltando quella da rimuovere
            for (int i = 1; i < righe.size(); i++) {
                String[] riga = righe.get(i);

                String usernameFile = riga[ColonnePreferitiCSV.USERNAME];
                String nomeRistorante = riga[ColonnePreferitiCSV.RISTORANTE];
                String nazione = riga[ColonnePreferitiCSV.NAZIONE];
                String citta = riga[ColonnePreferitiCSV.CITTA];
                String indirizzo = riga[ColonnePreferitiCSV.INDIRIZZO];

                // Se questa è la riga da rimuovere, saltala
                if (usernameFile.equals(cliente.getUsername()) &&
                        nomeRistorante.equals(ristorante.getNome()) &&
                        nazione.equals(ristorante.getLocalita().getNazione()) &&
                        citta.equals(ristorante.getLocalita().getCitta()) &&
                        indirizzo.equals(ristorante.getLocalita().getIndirizzo())) {
                    rimosso = true;
                    continue; // Salta questa riga
                }

                tutteLeRighe.add(riga);
            }
        }

        if (rimosso) {
            // Riscrivi tutto il file
            riscriviFilePreferiti(tutteLeRighe);
        }
        return rimosso;
    }

    /**
     * Verifica se un ristorante è già nei preferiti di un cliente.
     *
     * @param username   Username del cliente
     * @param ristorante Il ristorante da verificare
     * @return {@code true} se il ristorante è già nei preferiti, {@code false} altrimenti
     * @throws IOException  se si verifica un errore di I/O
     * @throws CsvException se si verifica un errore nel parsing del CSV
     */
    public static boolean esistePreferito(String username, Ristorante ristorante) throws IOException, CsvException {
        try (CSVReader reader = new CSVReader(new FileReader(DATASET_PREFERITI.toFile()))) {
            List<String[]> righe = reader.readAll();

            // Salta l'intestazione (prima riga)
            for (int i = 1; i < righe.size(); i++) {
                String[] riga = righe.get(i);

                String usernameFile = riga[ColonnePreferitiCSV.USERNAME];
                String nomeRistorante = riga[ColonnePreferitiCSV.RISTORANTE];
                String nazione = riga[ColonnePreferitiCSV.NAZIONE];
                String citta = riga[ColonnePreferitiCSV.CITTA];
                String indirizzo = riga[ColonnePreferitiCSV.INDIRIZZO];
                double latitudine = Double.parseDouble(riga[ColonnePreferitiCSV.LATITUDINE]);
                double longitudine = Double.parseDouble(riga[ColonnePreferitiCSV.LONGITUDINE]);

                if (usernameFile.equals(username) &&
                        nomeRistorante.equals(ristorante.getNome()) &&
                        nazione.equals(ristorante.getLocalita().getNazione()) &&
                        citta.equals(ristorante.getLocalita().getCitta()) &&
                        indirizzo.equals(ristorante.getLocalita().getIndirizzo()) &&
                        latitudine == ristorante.getLocalita().getLatitudine() &&
                        longitudine == ristorante.getLocalita().getLongitudine()) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Crea un oggetto Ristorante da una riga del CSV dei preferiti.
     *
     * @param riga Array contenente i dati della riga CSV
     * @return Ristorante creato dai dati della riga, o null se si verifica un errore
     */
    private static Ristorante creaRistoranteDaRigaPreferiti(String[] riga) {
        try {
            String nomeRistorante = riga[ColonnePreferitiCSV.RISTORANTE];
            String nazione = riga[ColonnePreferitiCSV.NAZIONE];
            String citta = riga[ColonnePreferitiCSV.CITTA];
            String indirizzo = riga[ColonnePreferitiCSV.INDIRIZZO];
            double latitudine = Double.parseDouble(riga[ColonnePreferitiCSV.LATITUDINE]);
            double longitudine = Double.parseDouble(riga[ColonnePreferitiCSV.LONGITUDINE]);

            Localita localita = new Localita(nazione, citta, indirizzo, latitudine, longitudine);

            for (Ristorante r : caricaRistoranti()) {
                if (r.getNome().equals(nomeRistorante) && r.getLocalita().equals(localita)) {
                    return r;
                }
            }
            return null;

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Converte i dati di un preferito in un array di stringhe per il CSV.
     *
     * @param cliente    Il cliente proprietario del preferito
     * @param ristorante Il ristorante preferito
     * @return Array di stringhe rappresentante la riga CSV
     */
    private static String[] creaRigaDaPreferito(Cliente cliente, Ristorante ristorante) {
        return new String[]{
                cliente.getUsername(),
                ristorante.getNome(),
                ristorante.getLocalita().getNazione(),
                ristorante.getLocalita().getCitta(),
                ristorante.getLocalita().getIndirizzo(),
                String.valueOf(ristorante.getLocalita().getLatitudine()),
                String.valueOf(ristorante.getLocalita().getLongitudine())
        };
    }

    /**
     * Riscrive completamente il file dei preferiti.
     *
     * @param righe Lista delle righe (inclusa intestazione) da scrivere
     * @throws IOException se si verifica un errore di I/O
     */
    private static void riscriviFilePreferiti(LinkedList<String[]> righe) throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(DATASET_PREFERITI.toFile()))) {
            // Scrivi tutte le righe (intestazione + dati)
            for (String[] riga : righe) {
                writer.writeNext(riga);
            }
        }
    }

    //endregion
}