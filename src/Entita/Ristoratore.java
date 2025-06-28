package Entita;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta un ristoratore nel sistema TheKnife.
 * Un ristoratore è un tipo specializzato di utente che può possedere e gestire
 * uno o più ristoranti, visualizzare le recensioni ricevute e rispondere a esse.
 *
 * @author Thomas Riotto
 * @version 1.0
 */
public class Ristoratore extends Utente {

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
    public List<Ristorante> getRistoranti() {
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
     * Restituisce il numero totale di ristoranti posseduti dal ristoratore.
     *
     * @return Il numero di ristoranti nella lista
     */
    public int getNumeroRistoranti() {
        return ristoranti.size();
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
     * Risponde a una recensione specifica lasciata da un cliente per uno dei ristoranti del ristoratore.
     * Il ristoratore può rispondere solo una volta per ogni recensione.
     * Utilizza nome e località per identificare univocamente il ristorante.
     *
     * @param nomeRistorante  Il nome del ristorante per cui si vuole rispondere alla recensione
     * @param localita        La località del ristorante per disambiguare ristoranti con stesso nome
     * @param usernameCliente Lo username del cliente che ha lasciato la recensione
     * @param risposta        Il testo della risposta del ristoratore
     * @return true se la risposta è stata aggiunta con successo, false altrimenti
     */
    public boolean rispondiARecensione(String nomeRistorante, Localita localita, String usernameCliente, String risposta) {
        Ristorante ristorante = trovaRistorante(nomeRistorante, localita);
        if (ristorante == null) {
            return false;
        }

        List<Recensione> recensioni = ristorante.getRecensioni();
        for (Recensione rec : recensioni) {
            if (rec.appartienteA(usernameCliente)) {
                if (rec.haRisposta()) {
                    return false;
                }
                rec.aggiungiRisposta(risposta);
                return true;
            }
        }
        return false;
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
            if (rec.appartienteA(usernameCliente) && rec.haRisposta()) {
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

    private Ristorante trovaRistorante(String nome, Localita localita) {
        for (Ristorante ristorante : ristoranti) {
            if (ristorante.getNome().equals(nome) && ristorante.getLocalita().equals(localita)) {
                return ristorante;
            }
        }
        return null;
    }

    /**
     * Metodo main per testare la funzionalità della classe Ristoratore.
     * Crea un ristoratore di esempio, aggiunge un ristorante con recensioni
     * e dimostra l'uso delle principali funzionalità.
     *
     * @param args Argomenti della linea di comando (non utilizzati)
     */
    public static void main(String[] args) {
        // Creo un ristoratore
        Ristoratore ristoratore = new Ristoratore(
                "Mario",
                "Rossi",
                "mrossi",
                "password123",
                LocalDate.of(1980, 3, 15),
                "Milano"
        );

        // Creo un ristorante
        Localita loc1 = new Localita("Italia", "Milano", "Via Roma 10", 45.46, 9.19);
        Ristorante r1 = new Ristorante("La Trattoria", loc1, TipoCucina.ITALIANA, true, true, 35.0f, "Cucina tipica italiana", ristoratore);

        // Creo un cliente e due recensioni
        Cliente c1 = new Cliente("Luca", "Verdi", "lverdi", "pass", LocalDate.of(1995, 5, 12), "Como");
        Cliente c2 = new Cliente("Anna", "Bianchi", "abianchi", "pass", LocalDate.of(1992, 7, 25), "Varese");

        Recensione rec1 = new Recensione(c1, r1, 4, "Buono!");
        Recensione rec2 = new Recensione(c2, r1, 5, "Eccellente!");

        // Aggiungo recensioni al ristorante
        r1.aggiungiRecensione(rec1);
        r1.aggiungiRecensione(rec2);

        // Aggiungo il ristorante al ristoratore
        ristoratore.aggiungiRistorante(r1);

        // Stampo tipo utente
        System.out.println("Tipo utente: " + ristoratore.getTipoUtente());

        // Visualizzo riepilogo recensioni
        ristoratore.visualizzaRiepilogo();
    }
}