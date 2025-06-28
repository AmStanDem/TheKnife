package servizi;

import Entita.*;
import com.opencsv.exceptions.CsvException;
import io_file.GestoreFile;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Servizio per la gestione dei ristoranti e delle loro informazioni.
 * Tramite l'inserimento dei filtri la ricerca del ristorante diviene più specifica affinchè l'utente possa tovare
 * in modo ancor più veloce i luoghi più pertinenti alle sue esigenze.
 *
 * @author Marco Zaro
 */

public final class RistoranteService {


    /**
     * Cerca ristoranti applicando una combinazione di filtri specificati.
     * Tutti i parametri sono opzionali (possono essere {@code null}) tranne la localita che è obbligatoria.
     * Se un parametro è {@code null}, il relativo filtro non viene applicato.
     *
     * <p>I filtri vengono applicati in sequenza e un ristorante deve soddisfare
     * tutti i criteri specificati per essere incluso nei risultati.</p>
     *
     * @param tipoCucina    Tipo di cucina desiderato. Se {@code null}, non viene applicato il filtro per tipologia
     * @param localita      Localita di riferimento per la ricerca (obbligatorio). Non può essere {@code null}
     * @param prezzoMinimo  Prezzo minimo in euro. Se {@code null}, non viene applicato il filtro per prezzo minimo
     * @param prezzoMassimo Prezzo massimo in euro. Se {@code null}, non viene applicato il filtro per prezzo massimo
     * @param delivery      Disponibilità servizio delivery. Se {@code null}, non viene applicato il filtro delivery.
     *                      Se {@code true}, cerca solo ristoranti con delivery. Se {@code false}, cerca solo quelli senza
     * @param prenotazione  Disponibilità prenotazione online. Se {@code null}, non viene applicato il filtro prenotazione.
     *                      Se {@code true}, cerca solo ristoranti con prenotazione. Se {@code false}, cerca solo quelli senza
     * @param mediaStelle   Media minima delle stelle richiesta (da 1.0 a 5.0). Se {@code null}, non viene applicato il filtro stelle
     * @param raggioKm      Raggio di ricerca in chilometri dalla localita specificata. Se {@code null}, non viene applicata limitazione geografica
     * @return Lista di ristoranti che soddisfano tutti i criteri di ricerca specificati.
     * Può essere una lista vuota se nessun ristorante soddisfa i criteri
     * @throws IOException              Se si verifica un errore durante il caricamento dei dati dei ristoranti
     * @throws CsvException             Se si verifica un errore durante la lettura del file CSV
     * @throws IllegalArgumentException Se la localita è {@code null} o se i parametri numerici hanno valori non validi
     *                                  (es. prezzoMinimo > prezzoMassimo, mediaStelle non compresa tra 1.0 e 5.0)
     * @see TipoCucina
     * @see Localita
     * @see Ristorante
     */
    public static ArrayList<Ristorante> cercaRistorante(TipoCucina tipoCucina, Localita localita,
                                                        Float prezzoMinimo, Float prezzoMassimo, Boolean delivery, Boolean prenotazione,
                                                        Float mediaStelle, Double raggioKm) throws IOException, CsvException {

        // Validazione parametri obbligatori
        if (localita == null) {
            throw new IllegalArgumentException("La localita è un parametro obbligatorio e non può essere null");
        }

        // Validazioni parametri numerici
        if (prezzoMinimo != null && prezzoMinimo < 0) {
            throw new IllegalArgumentException("Il prezzo minimo non può essere negativo");
        }
        if (prezzoMassimo != null && prezzoMassimo < 0) {
            throw new IllegalArgumentException("Il prezzo massimo non può essere negativo");
        }
        if (prezzoMinimo != null && prezzoMassimo != null && prezzoMinimo > prezzoMassimo) {
            throw new IllegalArgumentException("Il prezzo minimo non può essere maggiore del prezzo massimo");
        }
        if (mediaStelle != null && (mediaStelle < 1.0f || mediaStelle > 5.0f)) {
            throw new IllegalArgumentException("La media stelle deve essere compresa tra 1.0 e 5.0");
        }
        if (raggioKm != null && raggioKm <= 0) {
            throw new IllegalArgumentException("Il raggio deve essere un valore positivo");
        }

        var ristoranti = GestoreFile.caricaRistoranti();
        var risultato = new ArrayList<Ristorante>();

        for (Ristorante ristorante : ristoranti) {
            if (!filtroTipoCucina(ristorante, tipoCucina)) {
                continue;
            }
            if (!filtroLocalita(ristorante, localita, raggioKm)) {
                continue;
            }
            if (!filtroPrezzoMinimo(ristorante, prezzoMinimo)) {
                continue;
            }
            if (!filtroPrezzoMassimo(ristorante, prezzoMassimo)) {
                continue;
            }
            if (!filtroDelivery(ristorante, delivery)) {
                continue;
            }
            if (!filtroPrenotazione(ristorante, prenotazione)) {
                continue;
            }
            if (!filtroMediaStelle(ristorante, mediaStelle)) {
                continue;
            }

            risultato.add(ristorante);
        }
        return risultato;
    }

    /**
     * Cerca ristoranti applicando una combinazione di filtri specificati.
     * Tutti i parametri sono opzionali (possono essere {@code null}) tranne la localita che è obbligatoria.
     * Se un parametro è {@code null}, il relativo filtro non viene applicato.
     *
     * <p>I filtri vengono applicati in sequenza e un ristorante deve soddisfare
     * tutti i criteri specificati per essere incluso nei risultati.</p>
     *
     * @param localita      Localita di riferimento per la ricerca (obbligatorio). Non può essere {@code null}
     * @param raggioKm      Raggio di ricerca in chilometri dalla localita specificata. Se {@code null}, non viene applicata limitazione geografica
     * @return Lista di ristoranti che soddisfano tutti i criteri di ricerca specificati.
     * Può essere una lista vuota se nessun ristorante soddisfa i criteri
     * @throws IOException              Se si verifica un errore durante il caricamento dei dati dei ristoranti
     * @throws CsvException             Se si verifica un errore durante la lettura del file CSV
     * @throws IllegalArgumentException Se la localita è {@code null} o se i parametri numerici hanno valori non validi
     *                                  (es. prezzoMinimo > prezzoMassimo, mediaStelle non compresa tra 1.0 e 5.0)
     */
    public static ArrayList<Ristorante> cercaRistorante(Localita localita, Double raggioKm) throws IOException, CsvException {
        return cercaRistorante(null, localita, null, null, null, null, null, raggioKm);
    }



    private static boolean filtroTipoCucina(Ristorante ristorante, TipoCucina tipoCucina) {
        if (tipoCucina == null) {
            return true;
        }
        return ristorante.getTipoDiCucina().equals(tipoCucina);
    }

    private static boolean filtroLocalita(Ristorante ristorante, Localita localita, Double raggioKm) {
        if (ristorante == null || localita == null) {
            return false;
        }
        Localita localitaRistorante = ristorante.getLocalita();

        if (localitaRistorante == null) {
            return false;
        }

        if (raggioKm != null) {
            if (raggioKm > 0 && localita.hasCoordinate() && localitaRistorante.hasCoordinate()) {
                double distanza = localitaRistorante.calcolaDistanza(localita);
                return distanza != -1 && distanza <= raggioKm;
            }
            return false;
        }

        return localita.stessaZonaGeografica(localitaRistorante);
    }

    private static boolean filtroPrezzoMinimo(Ristorante ristorante, Float prezzoMinimo) {
        if (prezzoMinimo == null) {
            return true;
        }
        return ristorante.getPrezzoMedio() >= prezzoMinimo;
    }

    private static boolean filtroPrezzoMassimo(Ristorante ristorante, Float prezzoMassimo) {
        if (prezzoMassimo == null) {
            return true;
        }
        return ristorante.getPrezzoMedio() <= prezzoMassimo;
    }

    private static boolean filtroPrenotazione(Ristorante ristorante, Boolean prenotazione) {
        if (prenotazione == null) {
            return true;
        }
        return ristorante.getPrenotazione() == prenotazione;
    }

    private static boolean filtroDelivery(Ristorante ristorante, Boolean delivery) {
        if (delivery == null) {
            return true;
        }
        return ristorante.getDelivery() == delivery;
    }

    private static boolean filtroMediaStelle(Ristorante ristorante, Float mediaStelle) {
        if (mediaStelle == null) {
            return true;
        }
        return ristorante.getMediaStelle() >= mediaStelle;
    }


    /**
     * Aggiunge un nuovo ristorante
     *
     * @param ristoratore Il ristoratore proprietario
     * @param ristorante  Il ristorante da aggiungere
     * @return {@code true} se il ristorante è stato aggiunto correttamente, {@code false} altrimenti
     * @throws IOException  Se si verifica un errore durante l'accesso al file
     * @throws CsvException Se si verifica un errore durante la gestione del CSV
     */
    public static boolean aggiungiRistorante(Ristoratore ristoratore, Ristorante ristorante)
            throws IOException, CsvException {

        if (ristoratore == null || ristorante == null) {
            return false;
        }

        // Verifica autorizzazione
        if (!ristorante.getProprietario().equals(ristoratore)) {
            return false;
        }
        if (!GestoreFile.aggiungiRistorante(ristorante)) {
            return false;
        }

        return ristoratore.aggiungiRistorante(ristorante);
    }


    public static ArrayList<Recensione> getRecensioniRistorante(Ristorante ristorante) throws IOException, CsvException {
        ArrayList<Recensione> recensioni = GestoreFile.caricaRecensioniRistorante(ristorante);
        ristorante.setRecensioni(recensioni);
        return recensioni;
    }

    /**
     * Aggiunta di un metodo che permette all'utente di visualizzare le informazioni dei ristoranti
     */
    public static void visualizzaRistorante(Ristorante ristorante) {
        System.out.println(ristorante);
    }

}
