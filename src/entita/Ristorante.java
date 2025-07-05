package entita;

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
 */
public class Ristorante {
    /**
     * Nome del ristorante, utilizzato come identificativo.
     */
    private final String nome;
    /**
     * Localizzazione geografica del ristorante, comprendente indirizzo, città e coordinate.
     * <p>
     * L'attributo incapsula informazioni territoriali utili per la geolocalizzazione e la presentazione su mappa.
     */
    private final Localita localita;
    /**
     * Tipologia culinaria offerta dal ristorante, selezionata tra quelle disponibili in {@code TipoCucina}.
     * <p>
     * Aiuta i clienti a filtrare e selezionare i locali in base alle preferenze gastronomiche.
     */
    private final TipoCucina tipoDiCucina;
    /**
     * Valore booleano che indica se il ristorante offre servizio di consegna a domicilio.
     * <p>
     * Determina la visibilità in filtri o funzionalità basate sul delivery.
     */
    private final boolean delivery;

    /**
     * Valore booleano che indica se il ristorante accetta prenotazioni.
     */
    private final boolean prenotazione;

    /**
     * Prezzo medio di una consumazione nel ristorante, espresso come valore float.
     */
    private final float prezzoMedio;

    /**
     * Descrizione testuale del locale, comprensiva di dettagli, storia, atmosfera e altri elementi narrativi.
     */
    private final String descrizione;

    /**
     * Proprietario del ristorante, rappresentato da un oggetto {@code Ristoratore}.
     */
    private Ristoratore proprietario;

    /**
     * Lista delle recensioni lasciate dai clienti per questo ristorante.
     * <p>
     * Rappresenta il feedback degli utenti e viene utilizzata per calcolare la reputazione.
     */
    private List<Recensione> recensioni;

    /**
     * Crea un nuovo oggetto {@code Ristorante} e ne imposta i valori principali.
     * <p>
     * Applica la validazione su nome, località e prezzo medio. La descrizione
     * viene pulita da eventuali spazi superflui e le recensioni inizializzate come lista vuota.
     *
     * @param nome Nome del ristorante
     * @param localita Località geografica del ristorante
     * @param tipoDiCucina Tipologia culinaria offerta
     * @param delivery Flag per il servizio a domicilio
     * @param prenotazione Flag per la prenotazione anticipata
     * @param prezzoMedio Valore medio di spesa
     * @param descrizione Testo descrittivo del locale
     * @param proprietario Proprietario associato al ristorante
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
        this.recensioni = new ArrayList<>();
    }

    // Getters

    /**
     * Restituisce il nome del ristorante.
     *
     * @return Nome del ristorante
     */
    public String getNome() {
        return nome;
    }

    /**
     * Restituisce l’oggetto {@code Localita} che rappresenta l’indirizzo e la posizione del ristorante.
     *
     * @return Informazioni di localizzazione del ristorante
     */
    public Localita getLocalita() {
        return localita;
    }

    /**
     * Restituisce la tipologia di cucina offerta dal ristorante.
     *
     * @return Tipo di cucina selezionato
     */
    public TipoCucina getTipoDiCucina() {
        return tipoDiCucina;
    }

    /**
     * Verifica se il ristorante offre il servizio di consegna a domicilio.
     *
     * @return {@code true} se disponibile, {@code false} altrimenti
     */
    public boolean getDelivery() {
        return delivery;
    }

    /**
     * Verifica se il ristorante consente la prenotazione anticipata.
     *
     * @return {@code true} se accetta prenotazioni, {@code false} altrimenti
     */
    public boolean getPrenotazione() {
        return prenotazione;
    }
    /**
     * Restituisce il prezzo medio di una consumazione nel ristorante.
     *
     * @return Prezzo medio indicativo
     */
    public float getPrezzoMedio() {
        return prezzoMedio;
    }

    /**
     * Restituisce la descrizione testuale del ristorante.
     *
     * @return Descrizione informativa e promozionale
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * Restituisce il proprietario attualmente associato al ristorante.
     *
     * @return Oggetto {@code Ristoratore} proprietario
     */
    public Ristoratore getProprietario() {
        return proprietario;
    }

    /**
     * Imposta il ristoratore proprietario del ristorante.
     * <p>
     * Utilizzato per cambiare la proprietà dell'entità {@code Ristorante}.
     *
     * @param proprietario Nuovo proprietario da associare
     */
    public void setProprietario(Ristoratore proprietario) {
        this.proprietario = proprietario;
    }

    /**
     * Restituisce lo username del proprietario, se presente.
     * <p>
     * Utile per confronti, identificazione o tracciamento nel sistema.
     *
     * @return Username del ristoratore o {@code null}
     */
    public String getUsernameProprietario() {
        return proprietario != null ? proprietario.getUsername().trim() : null;
    }

    /**
     * Verifica se il ristorante appartiene al ristoratore specificato.
     * <p>
     * Utile per confermare l'identità del proprietario, ad esempio prima di
     * eseguire operazioni amministrative come modifiche, cancellazioni o inserimenti.
     *
     * @param proprietario Ristoratore da confrontare con quello registrato
     * @return {@code true} se il ristorante è di proprietà del ristoratore indicato,
     *         {@code false} altrimenti
     */
    public boolean appartieneA(Ristoratore proprietario) {
        return this.proprietario != null && this.proprietario.equals(proprietario);
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

        if (trovaRecensioneCliente(recensione.getCliente()) != null) {
            return false;
        }

        return recensioni.add(recensione);
    }

    /**
     * Modifica una recensione esistente.
     * @param recensioneVecchia Recensione da sostituire
     * @param recensioneNuova   Nuova recensione
     * @return true se modificata con successo, false altrimenti
     */
    public boolean modificaRecensione(Recensione recensioneVecchia, Recensione recensioneNuova) {
        if (recensioneVecchia == null || recensioneNuova == null) {
            return false;
        }

        // Trova l'indice della recensione vecchia
        int indice = recensioni.indexOf(recensioneVecchia);
        if (indice == -1) {
            return false; // Recensione non trovata
        }

        // Sostituisce la recensione
        recensioni.set(indice, recensioneNuova);
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

        boolean rimossa = recensioni.remove(recensione);
        if (rimossa) {
            System.out.println("Recensione rimossa con successo per: " + nome);
        }
        return rimossa;
    }

    /**
     * Ritorna una copia della lista delle recensioni per evitare modifiche esterne.
     */
    public LinkedList<Recensione> getRecensioni() {
        return new LinkedList<>(recensioni);
    }


    /**
     * Imposta la lista delle recensioni associate al ristorante.
     * <p>
     * La lista passata viene copiata per evitare modifiche esterne non controllate.
     *
     * @param recensioni Nuova lista di recensioni da assegnare
     */
    public void setRecensioni(List<Recensione> recensioni) {
        this.recensioni = new ArrayList<>(recensioni);
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

        return trovaRecensioneCliente(cliente.getUsername());
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

        for (Recensione recensione : recensioni) {
            if (recensione.getCliente().getUsername().equals(username.trim())) {
                return recensione;
            }
        }
        return null;
    }

    /**
     * Ritorna le recensioni senza risposta del ristoratore.
     * Utile per i ristoratori che vogliono rispondere alle recensioni.
     * @return Le recensioni senza risposta
     */
    public ArrayList<Recensione> getRecensioniSenzaRisposta() {
        ArrayList<Recensione> recensioniSenzaRisposta = new ArrayList<>();
        for (Recensione recensione : recensioni) {
            if (!recensione.haRisposta()) {
                recensioniSenzaRisposta.add(recensione);
            }
        }
        return recensioniSenzaRisposta;
    }

    /**
     * Ritorna le recensioni con la risposta del ristoratore.
     * Utile per i ristoratori che vogliono rispondere alle recensioni.
     * @return Le recensioni con risposta
     */
    public ArrayList<Recensione> getRecensioniConRisposta() {
        ArrayList<Recensione> recensioniConRisposta = new ArrayList<>();
        for (Recensione recensione : recensioni) {
            if (recensione.haRisposta()) {
                recensioniConRisposta.add(recensione);
            }
        }
        return recensioniConRisposta;
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
        for (Recensione recensione : recensioni) {
            if (recensione.getStelle() == stelle) {
                recensioniPerStelle.add(recensione);
            }
        }
        return recensioniPerStelle;
    }

    /**
     * Ritorna il numero totale di recensioni.
     * @return Il numero totale di recensioni.
     */
    public int getNumeroRecensioni() {
        return recensioni.size();
    }

    /**
     * Restituisce {@code true} se il ristorante ha recensioni, {@code false} altrimenti.
     * @return {@code true} se il ristorante ha recensioni, {@code false} altrimenti.
     */
    public boolean haRecensioni() {
        return !recensioni.isEmpty();
    }

    /**
     * Calcola la media delle stelle delle recensioni.
     * CORRETTA: non stampa messaggi, gestisce correttamente il caso senza recensioni.
     *
     * @return Media delle stelle (0.0 se non ci sono recensioni)
     */
    public float getMediaStelle() {
        if (recensioni.isEmpty()) {
            return 0.0f;
        }

        float somma = 0;
        for (Recensione recensione : recensioni) {
            somma += recensione.getStelle();
        }

        return somma / recensioni.size();
    }

    /**
     * Restituisce una chiave identificativa del ristorante.
     * La chiave è formata dal nome del ristorante e dal suo luogo.
     * @return La chiave del ristorante (nome + luogo).
     */
    public String getChiave() {
        return nome + localita.getNazione() + localita.getCitta() +
                localita.getIndirizzo() + localita.getLatitudine() + localita.getLongitudine();
    }

    /**
     * Verifica la validità dei parametri fondamentali per la creazione di un {@code Ristorante}.
     * <p>
     * Controlla che:
     * <ul>
     *   <li>Il nome sia non nullo e non vuoto</li>
     *   <li>La località sia valorizzata</li>
     *   <li>Il prezzo medio sia maggiore di zero</li>
     * </ul>
     * In presenza di uno o più errori, costruisce un messaggio descrittivo
     * e solleva un'eccezione di tipo {@code IllegalArgumentException}.
     *
     * @param nome Nome del ristorante
     * @param localita Località geografica del ristorante
     * @param prezzoMedio Prezzo medio stimato
     * @throws IllegalArgumentException Se uno o più parametri non rispettano i vincoli minimi
     */
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
    /**
     * Verifica se due oggetti {@code Ristorante} sono uguali.
     * <p>
     * La comparazione si basa su:
     * <ul>
     *   <li>{@code nome}: confrontato in modo case-sensitive</li>
     *   <li>{@code localita}: verificata tramite il metodo {@code equals()} della classe {@code Localita}</li>
     * </ul>
     * Questo metodo è fondamentale per evitare duplicati e gestire correttamente
     * collezioni come {@code Set} o chiavi di mappe.
     *
     * @param obj Oggetto da confrontare con il ristorante corrente
     * @return {@code true} se i due oggetti sono uguali, {@code false} altrimenti
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Ristorante ristorante = (Ristorante) obj;
        return Objects.equals(nome, ristorante.nome) &&
                Objects.equals(localita, ristorante.localita);
    }

    /**
     * Calcola il codice hash per il ristorante.
     * <p>
     * Il valore restituito viene derivato dagli attributi {@code nome} e {@code localita},
     * coerentemente con il metodo {@code equals()}.
     * <p>
     * Questo metodo è essenziale per mantenere la coerenza contrattuale tra {@code equals()} e {@code hashCode()},
     * assicurando che oggetti uguali producano lo stesso hash.
     *
     * @return Il codice hash calcolato per l'istanza corrente di {@code Ristorante}
     */
    @Override
    public int hashCode() {
        return Objects.hash(nome, localita);
    }
    /**
     * Restituisce una rappresentazione testuale dettagliata del ristorante.
     * <p>
     * Include informazioni quali:
     * <ul>
     *   <li>Nome, tipo di cucina, località e prezzo medio</li>
     *   <li>Disponibilità dei servizi di delivery e prenotazione</li>
     *   <li>Descrizione personalizzata</li>
     *   <li>Statistiche sulle recensioni, inclusa la media delle stelle</li>
     * </ul>
     * La formattazione con icone migliora la leggibilità nelle interfacce utente.
     *
     * @return Stringa formattata contenente tutte le informazioni principali del ristorante
     */
    @Override
    public String toString() {
        return "🍽️  " + nome + "\n" +
                "📍  Località: " + localita.getCitta() + "\n" +
                "🍴  Cucina: " + tipoDiCucina + "\n" +
                String.format("💶  Prezzo medio: %.2f €%n", prezzoMedio) +
                "🚚  Delivery: " + (delivery ? "Sì" : "No") + "\n" +
                "📅  Prenotazione online: " + (prenotazione ? "Sì" : "No") + "\n" +
                "📝  Descrizione: " + (descrizione.isEmpty() ? "[Nessuna descrizione]" : descrizione) + "\n" +
                "⭐️  Recensioni: " + getNumeroRecensioni() +
                " (media " + String.format("%.1f", getMediaStelle()) + "/5)" + "\n";
    }

}