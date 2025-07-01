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
 * @author Antonio Pesavento, Thomas Riotto
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

    private static final int MIN_STELLE = 1;
    private static final int MAX_STELLE = 5;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * Crea una nuova recensione impostando i parametri.
     *
     * @param cliente    Cliente che effettua la recensione
     * @param ristorante Ristorante recensito
     * @param stelle     Punteggio attribuito al ristorante da 1 a 5
     * @param messaggio  Eventuale messaggio opzionale
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
     * @param cliente        Cliente che effettua la recensione
     * @param ristorante     Ristorante recensito
     * @param stelle         Punteggio attribuito al ristorante da 1 a 5
     * @param messaggio      Eventuale messaggio opzionale
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
     * @param cliente    Cliente che effettua la recensione
     * @param ristorante Ristorante recensito
     * @param stelle     Punteggio attribuito al ristorante da 1 a 5
     * @throws RecensioneException Se i parametri non sono validi
     */
    public Recensione(Cliente cliente, Ristorante ristorante, int stelle) {
        this(cliente, ristorante, stelle, "");
    }

    // Getter methods

    /**
     * @return Il cliente della recensione.
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * @return La data della recensione.
     */
    public LocalDateTime getDataRecensione() {
        return dataRecensione;
    }

    /**
     * @return La data della risposta del ristoratore.
     */
    public LocalDateTime getDataRisposta() {
        return dataRisposta;
    }

    /**
     * @return Il messaggio della recensione.
     */
    public String getMessaggio() {
        return messaggio;
    }

    /**
     * @return La risposta del ristoratore.
     */
    public String getRispostaRistoratore() {
        return rispostaRistoratore;
    }

    /**
     * @return Il ristorante recensito
     */
    public Ristorante getRistorante() {
        return ristorante;
    }

    /**
     * @return Il numero di stelle della recensione, da 1 a 5.
     */
    public int getStelle() {
        return stelle;
    }

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
    public boolean aggiungiRisposta(String risposta) {
        if (rispostaRistoratore == null && risposta != null && !risposta.trim().isEmpty()) {
            this.rispostaRistoratore = risposta.trim();
            this.dataRisposta = LocalDateTime.now();
            return true;
        }
        return false;
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
        if (rispostaRistoratore != null && nuovaRisposta != null) {
            this.rispostaRistoratore = nuovaRisposta.trim();
            this.dataRisposta = LocalDateTime.now();
        }
    }

    /**
     * Verifica che i dati siano valorizzati nel modo corretto.
     *
     * @param cliente    Il cliente che effettua la recensione
     * @param ristorante Il ristorante recensito
     * @param stelle     Il numero di stelle
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
}