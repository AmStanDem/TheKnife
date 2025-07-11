package theknife.entita;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/*
 * Riotto Thomas 760981 VA
 * Pesavento Antonio 759933 VA
 * Tullo Alessandro 760760 VA
 * Zaro Marco 760194 VA
 */
/**
 * Rappresenta un ristoratore nel sistema theknife.TheKnife.
 * Un ristoratore è un tipo specializzato di utente che può possedere e gestire
 * uno o più ristoranti, visualizzare le recensioni ricevute e rispondere a esse.
 *
 * @author Thomas Riotto
 */
public class Ristoratore extends Utente {

    /**
     * Elenco dei ristoranti associati al ristoratore.
     * <p>
     * Ogni oggetto {@code Ristorante} rappresenta un locale gestito dal ristoratore corrente.
     * La lista può essere inizializzata vuota o con elementi già esistenti,
     * e viene utilizzata per operazioni di consultazione, modifica, risposta alle recensioni
     * e per il calcolo di statistiche aggregate.
     */
    private final List<Ristorante> ristoranti;


    /**
     * Costruttore per creare un nuovo ristoratore senza ristoranti.
     *
     * @param nome           Il nome del ristoratore
     * @param cognome        Il cognome del ristoratore
     * @param username       Lo username univoco per il login
     * @param password       La password per l'autenticazione
     * @param dataDiNascita  La data di nascita del ristoratore
     * @param luogoDomicilio Il luogo di domicilio del ristoratore
     */
    public Ristoratore(String nome, String cognome, String username, String password, LocalDate dataDiNascita, String luogoDomicilio) {
        super(nome, cognome, username, password, dataDiNascita, luogoDomicilio);
        ristoranti = new ArrayList<>();
    }

    /**
     * Costruttore per creare un ristoratore con una lista di ristoranti esistenti.
     *
     * @param nome           Il nome del ristoratore
     * @param cognome        Il cognome del ristoratore
     * @param username       Lo username univoco per il login
     * @param password       La password per l'autenticazione
     * @param dataDiNascita  La data di nascita del ristoratore
     * @param luogoDomicilio Il luogo di domicilio del ristoratore
     * @param ristoranti     Lista di ristoranti già posseduti dal ristoratore
     */
    public Ristoratore(String nome, String cognome, String username, String password, LocalDate dataDiNascita, String luogoDomicilio, ArrayList<Ristorante> ristoranti) {
        super(nome, cognome, username, password, dataDiNascita, luogoDomicilio);
        this.ristoranti = new ArrayList<>(ristoranti);
    }

    /**
     * Restituisce una copia della lista dei ristoranti posseduti dal ristoratore.
     *
     * @return Una nuova ArrayList contenente tutti i ristoranti del ristoratore
     */
    public ArrayList<Ristorante> getRistoranti() {
        return new ArrayList<>(ristoranti);
    }

    /**
     * Aggiunge un nuovo ristorante alla lista dei ristoranti posseduti dal ristoratore.
     * Se il ristorante è già presente nella lista, non viene aggiunto nuovamente.
     * Imposta automaticamente questo ristoratore come proprietario del ristorante.
     *
     * @param ristorante Il ristorante da aggiungere
     * @return true se il ristorante è stato aggiunto con successo, false se era già presente
     */
    public boolean aggiungiRistorante(Ristorante ristorante) {
        if (ristorante == null) {
            return false;
        }
        if (possiede(ristorante)) {
            return false;
        }
        ristorante.setProprietario(this);
        return ristoranti.add(ristorante);
    }

    /**
     * Verifica se il ristoratore possiede un determinato ristorante.
     *
     * @param ristorante Il ristorante da verificare
     * @return true se il ristorante appartiene al ristoratore, false altrimenti
     */
    public boolean possiede(Ristorante ristorante) {
        if (ristorante == null) {
            return false;
        }
        return ristoranti.contains(ristorante);
    }

    /**
     * Modifica una risposta esistente a una recensione.
     * Permette al ristoratore di cambiare il testo di una risposta già fornita.
     * Utilizza nome e località per identificare univocamente il ristorante.
     *
     * @param nomeRistorante  Il nome del ristorante
     * @param localita        La località del ristorante per disambiguare ristoranti con stesso nome
     * @param usernameCliente Lo username del cliente che ha lasciato la recensione
     * @param nuovaRisposta   Il nuovo testo della risposta
     * @return true se la risposta è stata modificata con successo, false altrimenti
     */
    public boolean modificaRisposta(String nomeRistorante, Localita localita, String usernameCliente, String nuovaRisposta) {
        Ristorante ristorante = trovaRistorante(nomeRistorante, localita);
        if (ristorante == null) {
            return false;
        }

        List<Recensione> recensioni = ristorante.getRecensioni();
        for (Recensione rec : recensioni) {
            if (rec.appartieneA(usernameCliente) && rec.haRisposta()) {
                rec.modificaRisposta(nuovaRisposta);
                return true;
            }
        }
        return false;
    }

    /**
     * Visualizza un riepilogo delle recensioni per tutti i ristoranti del ristoratore.
     * Mostra il numero totale di recensioni ricevute e la media complessiva delle stelle.
     * Se non ci sono recensioni, viene mostrato un messaggio appropriato.
     * Il calcolo della media globale tiene conto del peso di ogni ristorante
     * in base al numero di recensioni ricevute.
     */
    public void visualizzaRiepilogo() {
        int totaleRecensioni = 0;
        float sommaStelle = 0;

        for (Ristorante ristorante : ristoranti) {
            int numRecensioni = ristorante.getNumeroRecensioni();
            totaleRecensioni += numRecensioni;
            sommaStelle += ristorante.getMediaStelle() * numRecensioni;
        }

        if (totaleRecensioni == 0) {
            System.out.println("Nessuna recensione disponibile per i tuoi ristoranti.");
        } else {
            float mediaGlobale = sommaStelle / totaleRecensioni;
            System.out.printf("Hai %d recensioni totali. Media stelle complessiva: %.2f%n", totaleRecensioni, mediaGlobale);
        }
    }
    /**
     * Cerca un ristorante tra quelli posseduti dal ristoratore in base al nome e alla località.
     * <p>
     * Utile per identificare univocamente il ristorante in operazioni che coinvolgono recensioni.
     *
     * @param nome Nome del ristorante da cercare
     * @param localita Località geografica associata al ristorante
     * @return Il ristorante corrispondente, oppure {@code null} se non trovato
     */
    private Ristorante trovaRistorante(String nome, Localita localita) {
        for (Ristorante ristorante : ristoranti) {
            if (ristorante.getNome().equals(nome) && ristorante.getLocalita().equals(localita)) {
                return ristorante;
            }
        }
        return null;
    }
}