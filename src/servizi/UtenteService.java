package servizi;

import Entita.Cliente;
import Entita.Ristorante;
import Entita.Utente;
import com.opencsv.exceptions.CsvException;
import io_file.GestoreFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servizio per la gestione degli utenti e delle operazioni a essi correlate.
 * Funge da intermediario tra la logica di business e la persistenza dei dati.
 * @author Thomas Riotto
 */
public final class UtenteService {


    private UtenteService() {
    }

    /**
     * Registra un nuovo utente nel sistema.
     *
     * @param utente L'utente da registrare.
     * @return {@code true} se la registrazione è avvenuta con successo, {@code false} altrimenti.
     * @throws IOException Se si verifica un errore durante l'accesso ai file.
     * @throws CsvException Se si verifica un errore durante la gestione del CSV.
     */
    public static boolean registraUtente(Utente utente) throws IOException, CsvException {
        if (utente == null) {
            return false;
        }

        return GestoreFile.aggiungiUtente(utente);
    }

    /**
     * Autentica un utente nel sistema verificando username e password.
     *
     * @param username L'username dell'utente.
     * @param password La password dell'utente.
     * @return L'utente autenticato se le credenziali sono corrette, {@code null} altrimenti.
     * @throws IOException Se si verifica un errore durante l'accesso al file.
     * @throws CsvException Se si verifica un errore durante la gestione del CSV.
     */
    public static Utente autenticaUtente(String username, String password) throws IOException, CsvException {
        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            return null;
        }

        return GestoreFile.verificaLogin(username.trim(), password);
    }

    /**
     * Aggiunge un ristorante alla lista dei preferiti di un cliente.
     *
     * @param cliente Il cliente a cui aggiungere il preferito.
     * @param preferito Il ristorante da aggiungere ai preferiti.
     * @return {@code true} se l'aggiunta è avvenuta con successo, {@code false} altrimenti.
     * @throws IOException Se si verifica un errore durante l'accesso al file.
     * @throws CsvException Se si verifica un errore durante la gestione del CSV.
     */
    public static boolean aggiungiPreferito(Cliente cliente, Ristorante preferito) throws IOException, CsvException {
        if (cliente == null || preferito == null) {
            return false;
        }

        if (!GestoreFile.aggiungiPreferito(cliente, preferito)) {
            return false;
        }

        return cliente.aggiungiPreferito(preferito);
    }

    /**
     * Rimuove un ristorante dalla lista dei preferiti di un cliente.
     *
     * @param cliente Il cliente da cui rimuovere il preferito.
     * @param preferito Il ristorante da rimuovere dai preferiti.
     * @return {@code true} se la rimozione è avvenuta con successo, {@code false} altrimenti.
     * @throws IOException Se si verifica un errore durante l'accesso al file.
     * @throws CsvException Se si verifica un errore durante la gestione del CSV.
     */
    public static boolean rimuoviPreferito(Cliente cliente, Ristorante preferito) throws IOException, CsvException {
        if (cliente == null || preferito == null) {
            return false;
        }

        if (!GestoreFile.rimuoviPreferito(cliente, preferito)) {
            return false;
        }

        return cliente.rimuoviPreferito(preferito);
    }

    /**
     * Visualizza la lista dei ristoranti preferiti di un cliente.
     *
     * @param cliente Il cliente di cui visualizzare i preferiti.
     * @return Lista dei ristoranti preferiti, lista vuota se il cliente è null.
     */
    public static List<Ristorante> visualizzaPreferiti(Cliente cliente) {
        if (cliente == null) {
            return new ArrayList<>();
        }
        return cliente.getPreferiti();
    }
}