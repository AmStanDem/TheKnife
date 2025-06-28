package servizi;

import Entita.Cliente;
import Entita.Recensione;
import Entita.Ristorante;
import com.opencsv.exceptions.CsvException;
import io_file.GestoreFile;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Servizio per la gestione delle recensioni e delle operazioni correlate.
 * Funge da intermediario tra la logica di business e la persistenza dei dati.
 *
 * @author Thomas Riotto
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
     * Elimina una recensione tramite un cliente.
     * Prima rimuove dalla persistenza, poi aggiorna lo stato in memoria.
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
     * Prima aggiorna la persistenza, poi aggiorna lo stato in memoria.
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

        Recensione vecchiaRecensione = ristorante.trovaRecensioneCliente(cliente);
        if (vecchiaRecensione == null) {
            return false;
        }
        if (!GestoreFile.eliminaRecensione(vecchiaRecensione)) {
            return false;
        }

        return cliente.modificaRecensione(ristorante, nuovaRecensione);
    }

    /**
     * Visualizza tutte le recensioni di un ristorante.
     * Utilizza il metodo già implementato nella classe Ristorante.
     *
     * @param ristorante Ristorante di cui visualizzare le recensioni.
     */
    public static void visualizzaRecensioni(Ristorante ristorante) {
        if (ristorante.getListaRecensioni().isEmpty()) {
            System.out.println("Nessuna recensione trovata per il ristorante: " + ristorante.getNome());
            return;
        }

        System.out.println("=== Recensioni per " + ristorante.getNome() + " ===");
        for (Recensione recensione : ristorante.getListaRecensioni()) {
            System.out.println(recensione);
            System.out.println("-".repeat(50));
        }
    }

    /**
     * Visualizza le recensioni di un cliente.
     * Utilizza il metodo già implementato nella classe Cliente.
     *
     * @param cliente    Cliente di cui visualizzare le recensioni.
     * @param ristoranti Lista di ristoranti su cui cercare le recensioni.
     */
    public static void visualizzaRecensioniCliente(Cliente cliente, ArrayList<Ristorante> ristoranti) {
        System.out.println("=== Recensioni di " + cliente.getUsername() + " ===");
        cliente.visualizzaRecensioni(ristoranti);
    }

    /**
     * Verifica se un cliente può aggiungere una recensione per un ristorante.
     * Utilizza il metodo già implementato nella classe Cliente.
     *
     * @param cliente    Cliente da verificare.
     * @param ristorante Ristorante da verificare.
     * @return {@code true} se il cliente può aggiungere la recensione, {@code false} altrimenti.
     */
    public static boolean puoAggiungereRecensione(Cliente cliente, Ristorante ristorante) {
        return !cliente.haRecensito(ristorante);
    }

    /**
     * Ottiene le statistiche delle recensioni per un ristorante.
     * Utilizza i metodi già implementati nella classe Ristorante.
     *
     * @param ristorante Ristorante di cui ottenere le statistiche.
     * @return Stringa contenente le statistiche formattate.
     */
    public static String getStatisticheRecensioni(Ristorante ristorante) {
        int numeroRecensioni = ristorante.getNumeroRecensioni();
        if (numeroRecensioni == 0) {
            return "Nessuna recensione presente per " + ristorante.getNome();
        }

        float mediaStelle = ristorante.getMediaStelle();

        StringBuilder stats = new StringBuilder();
        stats.append("=== Statistiche per ").append(ristorante.getNome()).append(" ===\n");
        stats.append("Numero recensioni: ").append(numeroRecensioni).append("\n");
        stats.append("Media stelle: ").append(String.format("%.2f", mediaStelle)).append("/5\n");

        // Conta recensioni per numero di stelle
        for (int i = 1; i <= 5; i++) {
            int count = ristorante.getRecensioniPerStelle(i).size();
            if (count > 0) {
                stats.append(i).append(" ★: ").append(count).append(" recensioni\n");
            }
        }

        return stats.toString();

    }
}