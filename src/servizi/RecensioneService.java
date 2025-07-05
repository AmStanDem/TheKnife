package servizi;

import entita.Cliente;
import entita.Recensione;
import entita.Ristorante;
import entita.Ristoratore;
import com.opencsv.exceptions.CsvException;
import io_file.GestoreFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static servizi.RistoranteService.getRecensioniRistorante;

/**
 * Servizio per la gestione delle recensioni e delle operazioni correlate.
 * Funge da intermediario tra la logica di business e la persistenza dei dati.
 *
 * @author Antonio Pesavento
 * @author Alessandro Tullo
 */
public final class RecensioneService {

    /**
     * Aggiunge una recensione tramite un cliente.
     *
     * @param cliente    Cliente che aggiunge la recensione.
     * @param ristorante Ristorante da recensire.
     * @param recensione Recensione da aggiungere.
     * @return {@code true} se la recensione è stata aggiunta correttamente, {@code false} altrimenti.
     * @throws IOException  Se si verifica un errore durante l'accesso al file.
     * @throws CsvException Se si verifica un errore durante la gestione del CSV.
     */
    public static boolean aggiungiRecensione(Cliente cliente, Ristorante ristorante, Recensione recensione)
            throws IOException, CsvException {

        if (cliente == null || ristorante == null || recensione == null) {
            return false;
        }

        if (!recensione.getCliente().equals(cliente)) {
            return false;
        }
        if (!recensione.getRistorante().equals(ristorante)) {
            return false;
        }
        if (!GestoreFile.aggiungiRecensione(recensione)) {
            return false;
        }

        return cliente.aggiungiRecensione(ristorante, recensione);
    }

    /**
     * Recupera le recensioni di un cliente
     * @param cliente Cliente
     * @return Le recensioni di un cliente
     * @throws IOException Errore nella lettura del file
     * @throws CsvException Errore nel parsing del file CSV
     */
    public static ArrayList<Recensione> getRecensioniCliente(Cliente cliente) throws IOException, CsvException {
        return GestoreFile.caricaRecensioniCliente(cliente);
    }

    /**
     * Effettua il caricamento delle recensioni per i ristoranti
     * @param ristoranti I ristoranti
     * @throws IOException Errore nella lettura del file
     * @throws CsvException Errore nel parsing del file CSV
     */
    public static void caricaRecensioniPerTuttiRistoranti(ArrayList<Ristorante> ristoranti)
            throws IOException, CsvException {

        if (ristoranti == null || ristoranti.isEmpty()) {
            return;
        }

        // Mappa per associare ristoranti tramite una chiave
        // Usando capacità iniziale ottimizzata e load factor 0.75
        Map<String, Ristorante> mappaRistoranti = new HashMap<>(ristoranti.size() * 4 / 3 + 1);

        // Inizializzazione ristoranti con pre-allocazione delle liste
        for (Ristorante r : ristoranti) {
            mappaRistoranti.put(r.getChiave(), r);
            // Pre-alloca ArrayList con capacità ragionevole (es. 10)
            r.setRecensioni(new ArrayList<>(10));
        }

        // Caricamento recensioni
        ArrayList<Recensione> tutteLeRecensioni = GestoreFile.caricaRecensioni();

        if (tutteLeRecensioni.isEmpty()) {
            return;
        }

        // Assegnazione recensioni ai ristoranti
        for (Recensione r : tutteLeRecensioni) {
            String chiaveRistorante = r.getRistorante().getChiave();
            Ristorante ristorante = mappaRistoranti.get(chiaveRistorante);
            if (ristorante != null) {
                ristorante.aggiungiRecensione(r);
            }
        }
    }

    /**
     * Carica le recensioni di un ristorante
     * @param ristorante Ristorante
     * @throws IOException Errore nella lettura del file
     * @throws CsvException Errore nel parsing del file CSV
     */
    public static void caricaRecensioniRistorante(Ristorante ristorante)
            throws IOException, CsvException {

        var recensioni = getRecensioniRistorante(ristorante);
        ristorante.setRecensioni(recensioni);
    }

    /**
     * Elimina una recensione tramite un cliente.
     *
     * @param cliente    Cliente che elimina la recensione.
     * @param ristorante Ristorante da cui eliminare la recensione.
     * @return {@code true} se la recensione è stata eliminata correttamente, {@code false} altrimenti.
     * @throws IOException  Se si verifica un errore durante l'accesso al file.
     * @throws CsvException Se si verifica un errore durante la gestione del CSV.
     */
    public static boolean eliminaRecensione(Cliente cliente, Ristorante ristorante)
            throws IOException, CsvException {

        if (cliente == null || ristorante == null) {
            return false;
        }

        caricaRecensioniRistorante(ristorante);

        Recensione recensione = ristorante.trovaRecensioneCliente(cliente);
        if (recensione == null) {
            return false;
        }
        if (!GestoreFile.eliminaRecensione(recensione)) {
            return false;
        }
        return cliente.rimuoviRecensione(ristorante);
    }

    /**
     * Modifica una recensione tramite un cliente.
     *
     * @param cliente         Cliente che modifica la recensione.
     * @param ristorante      Ristorante su cui modificare la recensione.
     * @param nuovaRecensione Nuova recensione.
     * @return {@code true} se la recensione è stata modificata correttamente, {@code false} altrimenti.
     * @throws IOException  Se si verifica un errore durante l'accesso al file.
     * @throws CsvException Se si verifica un errore durante la gestione del CSV.
     */
    public static boolean modificaRecensione(Cliente cliente, Ristorante ristorante, Recensione nuovaRecensione)
            throws IOException, CsvException {

        if (cliente == null || ristorante == null || nuovaRecensione == null) {
            return false;
        }

        if (!nuovaRecensione.getCliente().equals(cliente)) {
            return false;
        }
        if (!nuovaRecensione.getRistorante().equals(ristorante)) {
            return false;
        }

        caricaRecensioniRistorante(ristorante);

        Recensione vecchiaRecensione = ristorante.trovaRecensioneCliente(cliente);
        if (vecchiaRecensione == null) {
            return false;
        }
        if(!GestoreFile.aggiornaRecensione(vecchiaRecensione,  nuovaRecensione)) {
            return false;
        }

        return cliente.modificaRecensione(ristorante, nuovaRecensione);
    }

    /**
     * Aggiunge una risposta del ristoratore a una recensione.
     *
     * @param ristoratore Ristoratore che risponde alla recensione.
     * @param ristorante  Ristorante per cui è stata fatta la recensione.
     * @param recensione  Recensione a cui rispondere.
     * @param testoRisposta Testo della risposta del ristoratore.
     * @return {@code true} se la risposta è stata aggiunta correttamente, {@code false} altrimenti.
     * @throws IOException  Se si verifica un errore durante l'accesso al file.
     * @throws CsvException Se si verifica un errore durante la gestione del CSV.
     */
    public static boolean rispondiARecensione(Ristoratore ristoratore, Ristorante ristorante,
                                              Recensione recensione, String testoRisposta)
            throws IOException, CsvException {

        if (ristoratore == null || ristorante == null || recensione == null ||
                testoRisposta == null || testoRisposta.trim().isEmpty()) {
            return false;
        }

        if (!ristorante.appartieneA(ristoratore)) {
            return false;
        }

        if (!recensione.getRistorante().equals(ristorante)) {
            return false;
        }

        boolean rispostaAggiunta = recensione.aggiungiRisposta(testoRisposta.trim());

        if (!rispostaAggiunta) {
            return false;
        }

        if (!GestoreFile.eliminaRecensione(recensione)) {
            return false;
        }
        return GestoreFile.aggiungiRecensione(recensione);
    }

    /**
     * Modifica la risposta del ristoratore a una recensione esistente.
     *
     * @param ristoratore Ristoratore che modifica la risposta alla recensione.
     * @param ristorante  Ristorante per cui è stata fatta la recensione.
     * @param recensione  Recensione di cui modificare la risposta.
     * @param nuovoTestoRisposta Nuovo testo della risposta del ristoratore.
     * @return {@code true} se la risposta è stata modificata correttamente, {@code false} altrimenti.
     * @throws IOException  Se si verifica un errore durante l'accesso al file.
     * @throws CsvException Se si verifica un errore durante la gestione del CSV.
     */
    public static boolean modificaRispostaRecensione(Ristoratore ristoratore, Ristorante ristorante,
                                                     Recensione recensione, String nuovoTestoRisposta)
            throws IOException, CsvException {

        // Validazione parametri di input
        if (ristoratore == null || ristorante == null || recensione == null ||
                nuovoTestoRisposta == null) {
            return false;
        }

        // Verifica che il ristorante appartenga al ristoratore
        if (!ristorante.appartieneA(ristoratore)) {
            return false;
        }

        // Verifica che la recensione sia relativa al ristorante specificato
        if (!recensione.getRistorante().equals(ristorante)) {
            return false;
        }

        // Verifica che la recensione abbia già una risposta da modificare
        if (!recensione.haRisposta()) {
            return false;
        }

        // Salva la risposta precedente per eventuale rollback
        String rispostaPrecedente = recensione.getRispostaRistoratore();

        // Modifica la risposta nella recensione
        boolean rispostaModificata = recensione.modificaRisposta(nuovoTestoRisposta.trim());

        if (!rispostaModificata) {
            return false;
        }

        if (!GestoreFile.eliminaRecensione(recensione)) {
            recensione.modificaRisposta(rispostaPrecedente);
            return false;
        }

        if (!GestoreFile.aggiungiRecensione(recensione)) {
            recensione.modificaRisposta(rispostaPrecedente);
            GestoreFile.aggiungiRecensione(recensione);
            return false;
        }

        return true;
    }

    /**
     * Visualizza le recensioni di un cliente.
     *
     * @param cliente    Cliente di cui visualizzare le recensioni.
     */
    public static void visualizzaRecensioniCliente(Cliente cliente) throws IOException, CsvException {
        System.out.println("=== Recensioni di " + cliente.getUsername() + " ===");
        var recensioni = GestoreFile.caricaRecensioniCliente(cliente);

        for (Recensione recensione : recensioni) {
            System.out.println(recensione);
        }
    }

    /**
     * Mostra le recensioni di un ristorante in forma anonima:
     * solo stelle e testo, senza informazioni sull'autore e senza ripetere il nome del ristorante.
     */
    public static void visualizzaRecensioniAnonime(Ristorante ristorante) throws IOException, CsvException {
        // Carica recensioni dal file associato al ristorante
        caricaRecensioniRistorante(ristorante);
        var recensioni = ristorante.getRecensioni();
        if (recensioni == null || recensioni.isEmpty()) {
            System.out.println("Nessuna recensione disponibile.");
            return;
        }
        // Stampa in forma anonima: solo stelle e messaggio
        System.out.println("--- Recensioni anonime ---");
        for (Recensione rev : recensioni) {
            System.out.printf("%d stelle: %s%n", rev.getStelle(), rev.getMessaggio());
        }
    }

    /**
     * Verifica se un cliente può aggiungere una recensione per un ristorante.
     *
     * @param cliente    Cliente da verificare.
     * @param ristorante Ristorante da verificare.
     * @return {@code true} se il cliente può aggiungere la recensione, {@code false} altrimenti.
     */
    public static boolean puoAggiungereRecensione(Cliente cliente, Ristorante ristorante) {
        return !cliente.haRecensito(ristorante);
    }
}