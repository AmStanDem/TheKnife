package servizi;

import Entita.Cliente;
import Entita.Ristorante;
import Entita.Ristoratore;
import Entita.Utente;
import com.opencsv.exceptions.CsvException;
import io_file.GestoreFile;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Servizio per la gestione degli utenti e delle operazioni a essi correlate.
 * Funge da intermediario tra la logica di business e la persistenza dei dati.
 *
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
     * @throws IOException  Se si verifica un errore durante l'accesso al file.
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
     * @throws IOException  Se si verifica un errore durante l'accesso al file.
     * @throws CsvException Se si verifica un errore durante la gestione del CSV.
     */
    public static Utente autenticaUtente(String username, String password) throws IOException, CsvException {
        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            return null;
        }

        Utente utente = GestoreFile.verificaLogin(username.trim(), password);

        return switch (utente) {
            case null -> null;

            case Cliente c -> {
                ArrayList<Ristorante> preferiti = GestoreFile.caricaPreferiti(c.getUsername());

                if (!preferiti.isEmpty()) {
                    try {
                        RecensioneService.caricaRecensioniPerTuttiRistoranti(preferiti);
                    } catch (IOException | CsvException e) {
                        System.err.println("Errore nel caricamento delle recensioni dei preferiti: " + e.getMessage());
                    }
                }

                yield new Cliente(
                        c.getNome(),
                        c.getCognome(),
                        c.getUsername(),
                        c.getPassword(),
                        c.getDataNascita(),
                        c.getLuogoDomicilio(),
                        preferiti
                );
            }

            case Ristoratore r -> {
                // Carica i ristoranti del ristoratore
                ArrayList<Ristorante> ristoranti = GestoreFile.caricaRistoranti(r.getUsername());

                if (!ristoranti.isEmpty()) {
                    RecensioneService.caricaRecensioniPerTuttiRistoranti(ristoranti);
                }

                yield new Ristoratore(
                        r.getNome(),
                        r.getCognome(),
                        r.getUsername(),
                        r.getPassword(),
                        r.getDataNascita(),
                        r.getLuogoDomicilio(),
                        ristoranti
                );
            }

            default -> utente;
        };
    }

    /**
     * Aggiunge un ristorante alla lista dei preferiti di un cliente.
     *
     * @param cliente   Il cliente a cui aggiungere il preferito.
     * @param preferito Il ristorante da aggiungere ai preferiti.
     * @return {@code true} se l'aggiunta è avvenuta con successo, {@code false} altrimenti.
     * @throws IOException  Se si verifica un errore durante l'accesso al file.
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
     * @param cliente   Il cliente da cui rimuovere il preferito.
     * @param preferito Il ristorante da rimuovere dai preferiti.
     * @return {@code true} se la rimozione è avvenuta con successo, {@code false} altrimenti.
     * @throws IOException  Se si verifica un errore durante l'accesso al file.
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
}