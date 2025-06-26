package Entita;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Rappresenta una recensione lasciata da un utente per un Ristorante.
 * Una recensione è immutabile per quanto riguarda cliente e ristorante,
 * ma consente la modifica di stelle, messaggio e risposta del ristoratore.
 *
 * @author Antonio Pesavento, Thomas Riotto - [Matricola] - [VA/CO]
 * @version 1.1
 */
public final class Recensione {

    private final Cliente cliente;
    private final Ristorante ristorante;
    private int stelle;
    private String messaggio;
    private LocalDateTime dataRecensione;
    private String rispostaRistoratore;
    private LocalDateTime dataRisposta;

    // Costanti per la validazione
    private static final int MIN_STELLE = 1;
    private static final int MAX_STELLE = 5;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * Crea una nuova recensione impostando i parametri.
     *
     * @param cliente Cliente che effettua la recensione
     * @param ristorante Ristorante recensito
     * @param stelle Punteggio attribuito al ristorante da 1 a 5
     * @param messaggio Eventuale messaggio opzionale
     * @throws RecensioneException Se i parametri non sono validi
     */
    public Recensione(Cliente cliente, Ristorante ristorante, int stelle, String messaggio) {
        validaAttributi(cliente, ristorante, stelle);

        this.cliente = cliente;
        this.ristorante = ristorante;
        this.stelle = stelle;
        this.messaggio = (messaggio != null) ? messaggio.trim() : "";
        this.dataRecensione = LocalDateTime.now();
    }

    /**
     * Crea una nuova recensione impostando i parametri.
     *
     * @param cliente Cliente che effettua la recensione
     * @param ristorante Ristorante recensito
     * @param stelle Punteggio attribuito al ristorante da 1 a 5
     * @param messaggio Eventuale messaggio opzionale
     * @param dataRecensione Data e ora in cui è stata effettuata la recensione
     * @throws RecensioneException Se i parametri non sono validi
     */
    public Recensione(Cliente cliente, Ristorante ristorante, int stelle, String messaggio, LocalDateTime dataRecensione) {
        validaAttributi(cliente, ristorante, stelle);

        this.cliente = cliente;
        this.ristorante = ristorante;
        this.stelle = stelle;
        this.messaggio = (messaggio != null) ? messaggio.trim() : "";
        this.dataRecensione = dataRecensione;
    }

    /**
     * Costruttore per creare una recensione con solo stelle (senza messaggio).
     *
     * @param cliente Cliente che effettua la recensione
     * @param ristorante Ristorante recensito
     * @param stelle Punteggio attribuito al ristorante da 1 a 5
     * @throws RecensioneException Se i parametri non sono validi
     */
    public Recensione(Cliente cliente, Ristorante ristorante, int stelle) {
        this(cliente, ristorante, stelle, "");
    }

    // Getter methods
    /** @return Il cliente della recensione. */
    public Cliente getCliente() { return cliente; }

    /** @return La data della recensione. */
    public LocalDateTime getDataRecensione() { return dataRecensione; }

    /** @return La data della risposta del ristoratore. */
    public LocalDateTime getDataRisposta() { return dataRisposta; }

    /** @return Il messaggio della recensione. */
    public String getMessaggio() { return messaggio; }

    /** @return La risposta del ristoratore. */
    public String getRispostaRistoratore() { return rispostaRistoratore; }

    /** @return Il ristorante recensito */
    public Ristorante getRistorante() { return ristorante; }

    /** @return Il numero di stelle della recensione, da 1 a 5. */
    public int getStelle() { return stelle; }

    /**
     * Modifica il numero di stelle se compreso tra 1 e 5.
     *
     * @param stelle Il nuovo numero di stelle
     */
    public void setStelle(int stelle) {
        if (stelle >= MIN_STELLE && stelle <= MAX_STELLE) {
            this.stelle = stelle;
        }
    }

    /**
     * Modifica l'eventuale messaggio della recensione.
     *
     * @param messaggio Il contenuto del nuovo messaggio
     */
    public void setMessaggio(String messaggio) {
        this.messaggio = (messaggio != null) ? messaggio.trim() : "";
    }

    /**
     * Consente al ristoratore di aggiungere una risposta alla recensione.
     * È consentita una sola risposta per recensione.
     *
     * @param risposta La risposta del ristoratore
     */
    public void aggiungiRisposta(String risposta) {
        if (rispostaRistoratore == null && risposta != null && !risposta.trim().isEmpty()) {
            this.rispostaRistoratore = risposta.trim();
            this.dataRisposta = LocalDateTime.now();
        }
    }

    public void setDataRisposta(LocalDateTime dataRisposta) {
        this.dataRisposta = dataRisposta;
    }

    /**
     * Consente di verificare se la recensione ha una risposta.
     *
     * @return true se la recensione ha una risposta, false altrimenti
     */
    public boolean haRisposta() {
        return rispostaRistoratore != null;
    }

    /**
     * Consente al ristoratore di modificare la propria risposta alla recensione.
     * Può essere modificata solo se esiste già una risposta.
     *
     * @param nuovaRisposta la nuova risposta del ristoratore
     */
    public void modificaRisposta(String nuovaRisposta) {
        if (rispostaRistoratore != null && nuovaRisposta != null && !nuovaRisposta.trim().isEmpty()) {
            this.rispostaRistoratore = nuovaRisposta.trim();
            this.dataRisposta = LocalDateTime.now();
        }
    }

    /**
     * Verifica che i dati siano valorizzati nel modo corretto.
     *
     * @param cliente Il cliente che effettua la recensione
     * @param ristorante Il ristorante recensito
     * @param stelle Il numero di stelle
     * @throws RecensioneException Se i dati inseriti non sono valorizzati correttamente
     */
    private void validaAttributi(Cliente cliente, Ristorante ristorante, int stelle) {
        StringBuilder errori = new StringBuilder();
        boolean errore = false;

        if (cliente == null) {
            errori.append("Il cliente deve essere valorizzato correttamente.\n");
            errore = true;
        }
        if (ristorante == null) {
            errori.append("Il ristorante deve essere valorizzato correttamente.\n");
            errore = true;
        }
        if (stelle < MIN_STELLE || stelle > MAX_STELLE) {
            errori.append("Le stelle devono avere un valore compreso tra ").append(MIN_STELLE)
                    .append(" e ").append(MAX_STELLE).append(".\n");
            errore = true;
        }

        if (errore) {
            throw new RecensioneException(errori.toString());
        }
    }

    /**
     * Verifica se questa recensione appartiene a un determinato cliente.
     *
     * @param username Username del cliente da verificare
     * @return true se la recensione appartiene al cliente specificato
     */
    public boolean appartienteA(String username) {
        return cliente != null && cliente.getUsername().equals(username);
    }

    @Override
    public String toString() {
        String formattedDateTime = dataRecensione.format(FORMATTER);
        StringBuilder sb = new StringBuilder();

        sb.append(cliente.getUsername()).append(" (")
                .append(cliente.getCognome()).append(" ").append(cliente.getNome())
                .append(")    Stelle: ").append("★".repeat(stelle)).append(" (").append(stelle).append("/5)")
                .append("\nRistorante: ").append(ristorante.getNome());

        if (!messaggio.isEmpty()) {
            sb.append("\nRecensione: ").append(messaggio);
        } else {
            sb.append("\nRecensione: [Nessun commento]");
        }

        sb.append("\nData di recensione: ").append(formattedDateTime);

        if (haRisposta()) {
            String formattedRisposta = dataRisposta.format(FORMATTER);
            sb.append("\n--- Risposta del ristoratore ---")
                    .append("\n").append(rispostaRistoratore)
                    .append("\nData risposta: ").append(formattedRisposta);
        }

        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
       if (!(obj instanceof Recensione recensione)) return false;

       return Objects.equals(cliente, recensione.cliente) &&
                Objects.equals(ristorante, recensione.ristorante);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cliente, ristorante);
    }


    // TESTING
    public static void main(String[] args) {
        System.out.println("=== TEST CLASSE RECENSIONE ===\n");

        // Creazione oggetti di test
        Cliente cliente1 = new Cliente("Antonio", "Pesavento", "apesavento",
                "password123", LocalDate.of(2006, 1, 14), "Varese");
        Cliente cliente2 = new Cliente("Thomas", "Riotto", "triotto",
                "mypass456", LocalDate.of(2005, 3, 22), "Como");

        Localita localita = new Localita("Italia", "Milano", "Via Roma 123", 45.4642, 9.1900);
        // Assumendo che TipoCucina sia un enum con valore ITALIANA
        Ristorante ristorante1 = new Ristorante("La Tavola", localita,
                TipoCucina.ITALIANA, true, true, 35.0f,
                "Ristorante tradizionale italiano", null);

        // Test 1: Creazione recensione
        System.out.println("--- TEST 1: Creazione Recensione ---");
        Recensione rec1 = new Recensione(cliente1, ristorante1, 5, "Ottimo ristorante, cibo eccellente!");
        System.out.println(rec1);
        System.out.println();

        // Test 2: Modifica stelle e testo recensione
        System.out.println("--- TEST 2: Modifica Recensione ---");
        rec1.setStelle(4);
        rec1.setMessaggio("Buon ristorante, ma il servizio potrebbe migliorare.");
        System.out.println("Dopo modifica:");
        System.out.println(rec1);
        System.out.println();

        // Test 3: Aggiunta risposta del ristoratore
        System.out.println("--- TEST 3: Aggiunta Risposta Ristoratore ---");
        rec1.aggiungiRisposta("Grazie per il feedback! Stiamo lavorando per migliorare il servizio.");
        System.out.println("Dopo risposta del ristoratore:");
        System.out.println(rec1);
        System.out.println();

        // Test 4: Tentativo di aggiungere seconda risposta (dovrebbe fallire)
        System.out.println("--- TEST 4: Tentativo Seconda Risposta (deve fallire) ---");
        System.out.println("Prima della seconda risposta - Ha risposta: " + rec1.haRisposta());
        rec1.aggiungiRisposta("Questa non dovrebbe essere aggiunta!");
        System.out.println("Dopo tentativo seconda risposta:");
        System.out.println(rec1);
        System.out.println();

        // Test 5: Modifica risposta esistente
        System.out.println("--- TEST 5: Modifica Risposta Esistente ---");
        rec1.modificaRisposta("La ringraziamo per il prezioso feedback. Abbiamo già implementato dei miglioramenti!");
        System.out.println("Dopo modifica risposta:");
        System.out.println(rec1);
        System.out.println();

        // Test 6: Test validazione stelle (valori non validi)
        System.out.println("--- TEST 6: Test Validazione Stelle ---");
        Recensione rec2 = new Recensione(cliente2, ristorante1, 3, "Nella media");
        System.out.println("Recensione originale (3 stelle):");
        System.out.println("Stelle: " + rec2.getStelle());

        rec2.setStelle(0); // Non valido
        System.out.println("Dopo tentativo di impostare 0 stelle: " + rec2.getStelle());

        rec2.setStelle(6); // Non valido
        System.out.println("Dopo tentativo di impostare 6 stelle: " + rec2.getStelle());

        rec2.setStelle(1); // Valido
        System.out.println("Dopo impostazione 1 stella: " + rec2.getStelle());
        System.out.println();

        // Test 7: Test con stringhe vuote o null
        System.out.println("--- TEST 7: Test Input Vuoti/Null ---");
        Recensione rec3 = new Recensione(cliente1, ristorante1, 2, "   Test con spazi   ");
        System.out.println("Recensione con spazi (dovrebbe essere trimmed):");
        System.out.println("Testo: '" + rec3.getMessaggio() + "'");

        rec3.aggiungiRisposta("   "); // Stringa vuota dopo trim
        System.out.println("Ha risposta dopo tentativo con spazi vuoti: " + rec3.haRisposta());

        rec3.aggiungiRisposta(null); // Null
        System.out.println("Ha risposta dopo tentativo con null: " + rec3.haRisposta());

        rec3.aggiungiRisposta("Risposta valida");
        System.out.println("Ha risposta dopo risposta valida: " + rec3.haRisposta());
        System.out.println();

        // Test 8: Test modifica risposta senza risposta esistente
        System.out.println("--- TEST 8: Modifica Risposta Inesistente ---");
        Recensione rec4 = new Recensione(cliente2, ristorante1, 4, "Buona esperienza");
        System.out.println("Prima della modifica - Ha risposta: " + rec4.haRisposta());
        rec4.modificaRisposta("Tentativo di modifica senza risposta esistente");
        System.out.println("Dopo tentativo modifica - Ha risposta: " + rec4.haRisposta());


        System.out.println("\n=== FINE TEST ===");
    }

}
