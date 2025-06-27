package Entita;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;

/**
 * Classe che rappresenta un ristorante nel sistema TheKnife.
 * Contiene informazioni essenziali per la gestione delle recensioni
 * e delle caratteristiche del ristorante.
 *
 * @author Marco Zaro
 * @version 2.0
 */
public class Ristorante {
    private String nome;
    private Localita localita;
    private TipoCucina tipoDiCucina;
    private boolean delivery;
    private boolean prenotazione;
    private float prezzoMedio;
    private String descrizione;
    private Ristoratore proprietario;
    private List<Recensione> listaRecensioni;

    /**
     * Costruttore completo con proprietario.
     */
    public Ristorante(String nome, Localita localita, TipoCucina tipoDiCucina,
                      boolean delivery, boolean prenotazione, float prezzoMedio,
                      String descrizione, Ristoratore proprietario) {

        validaAttributi(nome, localita, prezzoMedio);

        this.nome = nome.trim();
        this.localita = localita;
        this.tipoDiCucina = tipoDiCucina;
        this.delivery = delivery;
        this.prenotazione = prenotazione;
        this.prezzoMedio = prezzoMedio;
        this.descrizione = descrizione != null ? descrizione.trim() : "";
        this.proprietario = proprietario;
        this.listaRecensioni = new LinkedList<>();
    }

    // Getters
    public String getNome() {
        return nome;
    }

    public Localita getLocalita() {
        return localita;
    }

    public TipoCucina getTipoDiCucina() {
        return tipoDiCucina;
    }

    public boolean getDelivery() {
        return delivery;
    }

    public boolean getPrenotazione() {
        return prenotazione;
    }

    public float getPrezzoMedio() {
        return prezzoMedio;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public Ristoratore getProprietario() {
        return proprietario;
    }

    public void setProprietario(Ristoratore proprietario) {
        this.proprietario = proprietario;
    }

    public String getUsernameProprietario() {
        return proprietario != null ? proprietario.getUsername().trim() : null;
    }

    public boolean appartieneA(Ristoratore proprietario) {
        return this.proprietario != null && this.proprietario.equals(proprietario);
    }

    public boolean appartieneA(String username) {
        return this.proprietario != null && this.proprietario.getUsername().equals(username);
    }

    /**
     * Aggiunge una recensione al ristorante.
     *
     * @param recensione Recensione da aggiungere
     * @return {@code true} se aggiunta con successo, {@code false} altrimenti
     */
    public boolean aggiungiRecensione(Recensione recensione) {
        if (recensione == null) {
            return false;
        }

        // Verifica se esiste già una recensione dello stesso cliente
        if (trovaRecensioneCliente(recensione.getCliente()) != null) {
            return false; // Cliente ha già recensito questo ristorante
        }

        boolean aggiunta = listaRecensioni.add(recensione);
        System.out.println("Recensione aggiunta con successo per: " + nome);
        return aggiunta;
    }

    /**
     * Modifica una recensione esistente.
     *
     * @param recensioneVecchia Recensione da sostituire
     * @param recensioneNuova   Nuova recensione
     * @return true se modificata con successo, false altrimenti
     */
    public boolean modificaRecensione(Recensione recensioneVecchia, Recensione recensioneNuova) {
        if (recensioneVecchia == null || recensioneNuova == null) {
            return false;
        }

        // Trova l'indice della recensione vecchia
        int indice = listaRecensioni.indexOf(recensioneVecchia);
        if (indice == -1) {
            return false; // Recensione non trovata
        }

        // Sostituisce la recensione
        listaRecensioni.set(indice, recensioneNuova);
        System.out.println("Recensione modificata con successo per: " + nome);
        return true;
    }

    /**
     * Rimuove una recensione dal ristorante.
     *
     * @param recensione Recensione da rimuovere
     * @return true se rimossa con successo, false altrimenti
     */
    public boolean rimuoviRecensione(Recensione recensione) {
        if (recensione == null) {
            return false;
        }

        boolean rimossa = listaRecensioni.remove(recensione);
        if (rimossa) {
            System.out.println("Recensione rimossa con successo per: " + nome);
        }
        return rimossa;
    }

    /**
     * Ritorna una copia della lista delle recensioni per evitare modifiche esterne.
     */
    public LinkedList<Recensione> getListaRecensioni() {
        return new LinkedList<>(listaRecensioni);
    }

    /**
     * Trova la recensione di un cliente specifico.
     *
     * @param cliente Cliente di cui cercare la recensione
     * @return La recensione del cliente o null se non trovata
     */
    public Recensione trovaRecensioneCliente(Cliente cliente) {
        if (cliente == null) {
            return null;
        }

        for (Recensione recensione : listaRecensioni) {
            if (recensione.getCliente().equals(cliente)) {
                return recensione;
            }
        }
        return null;
    }

    /**
     * Trova la recensione di un cliente tramite username.
     *
     * @param username Username del cliente
     * @return La recensione del cliente o null se non trovata
     */
    public Recensione trovaRecensioneCliente(String username) {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }

        for (Recensione recensione : listaRecensioni) {
            if (recensione.getCliente().getUsername().equals(username.trim())) {
                return recensione;
            }
        }
        return null;
    }

    /**
     * Ritorna le recensioni senza risposta del ristoratore.
     * Utile per i ristoratori che vogliono rispondere alle recensioni.
     */
    public ArrayList<Recensione> getRecensioniSenzaRisposta() {
        ArrayList<Recensione> recensioniSenzaRisposta = new ArrayList<>();
        for (Recensione recensione : listaRecensioni) {
            if (!recensione.haRisposta()) {
                recensioniSenzaRisposta.add(recensione);
            }
        }
        return recensioniSenzaRisposta;
    }

    /**
     * Ritorna le recensioni con un numero specifico di stelle.
     *
     * @param stelle Numero di stelle (1-5)
     * @return Lista delle recensioni con quel numero di stelle
     */
    public ArrayList<Recensione> getRecensioniPerStelle(int stelle) {
        if (stelle < 1 || stelle > 5) {
            return new ArrayList<>(); // Stelle non valide
        }

        ArrayList<Recensione> recensioniPerStelle = new ArrayList<>();
        for (Recensione recensione : listaRecensioni) {
            if (recensione.getStelle() == stelle) {
                recensioniPerStelle.add(recensione);
            }
        }
        return recensioniPerStelle;
    }

    /**
     * Ritorna il numero totale di recensioni.
     */
    public int getNumeroRecensioni() {
        return listaRecensioni.size();
    }

    /**
     * Calcola la media delle stelle delle recensioni.
     *
     * @return Media delle stelle (0.0 se non ci sono recensioni)
     */
    public float getMediaStelle() {
        if (listaRecensioni.isEmpty()) {
            return 0.0f;
        }

        float somma = 0;
        for (Recensione recensione : listaRecensioni) {
            somma += recensione.getStelle();
        }

        return somma / listaRecensioni.size();
    }

    private void validaAttributi(String nome, Localita localita, float prezzoMedio) {
        StringBuilder errori = new StringBuilder();
        boolean errore = false;
        if (nome == null || nome.isEmpty()) {
            String messaggio = "Il nome di un ristorante deve essere valorizzato.\n";
            errori.append(messaggio);
            errore = true;
        }
        if (localita == null) {
            String messaggio = "Il luogo di un ristorante deve essere valorizzato.\n";
            errori.append(messaggio);
            errore = true;
        }
        if (prezzoMedio <= 0) {
            String messaggio = "Il prezzo medio di un ristorante non può essere negativo.\n";
            errori.append(messaggio);
            errore = true;
        }

        if (errore) {
            throw new IllegalArgumentException(errori.toString());
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Ristorante ristorante = (Ristorante) obj;
        return Objects.equals(nome, ristorante.nome) &&
                Objects.equals(localita, ristorante.localita);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, localita);
    }

    @Override
    public String toString() {
        return String.format("Ristorante{nome='%s', località='%s', cucina=%s, prezzo=%.2f€, " +
                        "delivery=%s, prenotazione=%s, recensioni=%d, media=%.1f/5}",
                nome, localita.getCitta(), tipoDiCucina, prezzoMedio,
                delivery ? "Sì" : "No", prenotazione ? "Sì" : "No",
                getNumeroRecensioni(), getMediaStelle());
    }
}