package theknife.servizi;

import theknife.entita.Cliente;
import theknife.entita.Ristorante;
import theknife.entita.Ristoratore;
import theknife.entita.Utente;
import com.opencsv.exceptions.CsvException;
import theknife.io_file.GestoreFile;

import java.io.IOException;
import java.util.ArrayList;
/*
 * Riotto Thomas 760981 VA
 * Pesavento Antonio 759933 VA
 * Tullo Alessandro 760760 VA
 * Zaro Marco 760194 VA
 */
/**
 * Servizio per la gestione degli utenti e delle operazioni a essi correlate.
 * Funge da intermediario tra la logica di business e la persistenza dei dati.
 *
 * @author Antonio Pesavento
 */
public final class UtenteService {

    /**
     * Costruttore privato della classe {@code UtenteService}.
     * <p>
     * Impedisce l’istanziamento diretto della classe che espone esclusivamente metodi statici.
     * L’uso di questo costruttore garantisce che {@code UtenteService} sia utilizzata solo come classe utility.
     */
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
        if (username == null || password == null ||
                username.isBlank() || password.isBlank()) {
            return null;
        }

        String cleanUsername = username.trim();

        Utente utente = GestoreFile.verificaLogin(cleanUsername, password);
        if (utente == null) {
            return null;
        }

        return switch (utente) {
            case Cliente c -> {
                try {
                    ArrayList<Ristorante> preferiti = GestoreFile.caricaPreferiti(c.getUsername());

                    // Carica recensioni solo se ci sono preferiti
                    if (!preferiti.isEmpty()) {
                        RecensioneService.caricaRecensioniPerTuttiRistoranti(preferiti);
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
                } catch (IOException | CsvException e) {
                    System.err.println("Errore nel caricamento dei preferiti: " + e.getMessage());

                    yield new Cliente(
                            c.getNome(),
                            c.getCognome(),
                            c.getUsername(),
                            c.getPassword(),
                            c.getDataNascita(),
                            c.getLuogoDomicilio(),
                            new ArrayList<>()
                    );
                }
            }
            case Ristoratore r -> {
                try {
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
                } catch (IOException | CsvException e) {
                    System.err.println("Errore nel caricamento dei ristoranti: " + e.getMessage());

                    yield new Ristoratore(
                            r.getNome(),
                            r.getCognome(),
                            r.getUsername(),
                            r.getPassword(),
                            r.getDataNascita(),
                            r.getLuogoDomicilio(),
                            new ArrayList<>()
                    );
                }
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