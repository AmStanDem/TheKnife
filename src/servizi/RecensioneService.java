package servizi;

import Entita.Cliente;
import Entita.Recensione;
import Entita.Ristorante;
import com.opencsv.exceptions.CsvException;
import io_file.GestoreFile;

import java.io.IOException;

/**
 * Servizio per la gestione delle recensioni e delle operazioni a essi correlate.
 * Funge da intermediario tra la logica di business e la persistenza dei dati e tra ristoratori e clienti.
 * @author Thomas Riotto
 */
public final class RecensioneService {
    /**
     * Aggiunge una recensione a un ristorante.
     * @param ristorante Ristorante su cui aggiungere la recensione.
     * @param recensione Recensione da aggiungere.
     * @return {@code true} se la recensione è stata aggiunta correttamente, {@code false} altrimenti.
     * @throws IOException Se si verifica un errore durante l'accesso al file.
     * @throws CsvException Se si verifica un errore durante la gestione del CSV.
     */
    public static boolean aggiungiRecensione(Ristorante ristorante, Recensione recensione) throws IOException, CsvException {
        if (!GestoreFile.aggiungiRecensione(recensione)) {
            return false;
        }
        return ristorante.aggiungiRecensione(recensione);
    }

    public static boolean eliminaRecensione(Recensione recensione, Ristorante ristorante) throws IOException, CsvException {
        if(!GestoreFile.eliminaRecensione(recensione)){
            return false;
        }
        return ristorante.rimuoviRecensione(recensione);
    }

    public static void visualizzaRecensioni(Ristorante ristorante){
        for(Recensione recensione : ristorante.getListaRecensioni()){
            System.out.println(recensione);
        }
    }
}
