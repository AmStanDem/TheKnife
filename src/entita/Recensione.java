package entita;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Rappresenta una recensione effettuata da un {@code Cliente} verso un {@code Ristorante}.
 * <p>
 * Le recensioni sono associate a un cliente e a un ristorante, e comprendono:
 * <ul>
 *   <li>Punteggio espresso in stelle da 1 a 5</li>
 *   <li>Messaggio testuale opzionale</li>
 *   <li>Risposta del ristoratore (modificabile)</li>
 * </ul>
 * <p>
 * La classe supporta la modifica del punteggio e del messaggio, oltre alla gestione controllata
 * della risposta da parte del ristoratore.
 * Ogni recensione è temporalmente tracciata.
 *
 * @author Antonio Pesavento
 * @author Thomas Riotto
 */
public final class Recensione {

    /** Cliente che ha effettuato la recensione */
    private final Cliente cliente;

    /** Ristorante oggetto della recensione */
    private final Ristorante ristorante;

    /** Numero di stelle attribuite alla recensione (da 1 a 5) */
    private final int stelle;

    /** Eventuale messaggio scritto dal cliente */
    private String messaggio;

    /** Data e ora in cui è stata registrata la recensione */
    private final LocalDateTime dataRecensione;

    /** Risposta del ristoratore al cliente */
    private String rispostaRistoratore;

    /** Data e ora in cui è stata registrata la risposta del ristoratore */
    private LocalDateTime dataRisposta;
    /** Numero minimo di stelle = 1*/
    private static final int MIN_STELLE = 1;
    /** Numero massimo di stelle = 5*/
    private static final int MAX_STELLE = 5;

    /**
     * Formattatore utilizzato per convertire data e ora in formato leggibile.
     * <p>
     * Il formato seguito e {@code "dd/MM/yyyy HH:mm"} e viene applicato a:
     * <ul>
     *     <li>Data della recensione</li>
     *     <li>Data della risposta del ristoratore</li>
     * </ul>
     * Il campo è dichiarato {@code static final} perché condiviso e immutabile tra tutte le istanze della classe {@code Recensione}.
     * */
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
     * Modifica l'eventuale messaggio della recensione.
     *
     * @param messaggio Il contenuto del nuovo messaggio
     */
    public void setMessaggio(String messaggio) {
        this.messaggio = (messaggio != null) ? messaggio.trim() : "";
    }

    /**
     * Aggiunge una risposta da parte del ristoratore.
     * <p>
     * È consentita una sola risposta.
     *
     * @param risposta Testo della risposta
     * @return {@code true} se aggiunta con successo
     */
    public boolean aggiungiRisposta(String risposta) {
        if (rispostaRistoratore == null && risposta != null && !risposta.trim().isEmpty()) {
            this.rispostaRistoratore = risposta.trim();
            this.dataRisposta = LocalDateTime.now();
            return true;
        }
        return false;
    }

    /**
     * Imposta manualmente la data della risposta del ristoratore.
     * <p>
     * @param dataRisposta Data e ora da associare alla risposta
     */
    public void setDataRisposta(LocalDateTime dataRisposta) {
        this.dataRisposta = dataRisposta;
    }

    /**
     * Consente di verificare se la recensione ha una risposta.
     *
     * @return true se la recensione ha una risposta, false altrimenti
     */
    public boolean haRisposta() {
        return rispostaRistoratore != null && !rispostaRistoratore.trim().isEmpty();
    }

    /**
     * Consente al ristoratore di modificare la propria risposta alla recensione.
     * Può essere modificata solo se esiste già una risposta.
     * @return {@code true} se la risposta è stata modificata con successo, {@code false} altrimenti.
     * @param nuovaRisposta la nuova risposta del ristoratore
     */
    public boolean modificaRisposta(String nuovaRisposta) {
        if (rispostaRistoratore != null && nuovaRisposta != null) {
            this.rispostaRistoratore = nuovaRisposta.trim();
            this.dataRisposta = LocalDateTime.now();
            return true;
        }
        return false;
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
    public boolean appartieneA(String username) {
        return cliente != null && cliente.getUsername().equals(username);
    }

    /**
     * Restituisce una rappresentazione testuale dettagliata della recensione.
     * <p>
     * Include username e nome del cliente, punteggio in stelle, nome del ristorante,
     * eventuale messaggio, data della recensione e, se presente, risposta del ristoratore.
     *
     * @return Stringa formattata con tutti i dati principali della recensione
     */
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

    /**
     * Verifica l'uguaglianza tra due recensioni in base al cliente e al ristorante.
     * <p>
     * Due recensioni sono considerate uguali se fanno riferimento allo stesso
     * cliente e allo stesso ristorante, indipendentemente dal contenuto.
     *
     * @param obj Oggetto da confrontare con questa recensione
     * @return {@code true} se la recensione confrontata ha stesso cliente e ristorante
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Recensione recensione)) return false;

        return Objects.equals(cliente, recensione.cliente) &&
                Objects.equals(ristorante, recensione.ristorante);
    }

    /**
     * Calcola il codice hash per la recensione, basandosi su cliente e ristorante.
     * <p>
     * Questo garantisce coerenza con {@code equals}.
     * @return Valore hash della recensione
     */
    @Override
    public int hashCode() {
        return Objects.hash(cliente, ristorante);
    }
}