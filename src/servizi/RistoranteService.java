package servizi;
import Entita.*;
import com.opencsv.exceptions.CsvException;
import io_file.GestoreFile;
import java.io.IOException;
import java.util.ArrayList;

/*
 * Servizio per la gestione dei ristoranti e delle loro informazioni.
 * Tramite l'inserimento dei filtri la ricerca del ristorante diviene più specifica affinchè l'utente possa tovare
 * in modo ancor più veloce i luoghi più pertinenti alle sue esigenze.
 * @author Marco Zaro
 */

public final class RistoranteService {


    /*
     * Aggiunta di un metodo che permette all'utente, tramite dei filtri, di avviare la ricerca del ristorante
     */
    public static ArrayList<Ristorante> cercaRistorante(TipoCucina tipoCucina, Localita localita, Float prezzoMinimo, Float prezzoMassimo, Boolean delivery, Boolean prenotazione, Float mediaStelle, Double raggioKm) throws IOException, CsvException {
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

        if  (localitaRistorante == null) {
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

    private static boolean filtroPrezzoMinimo(Ristorante ristorante, Float prezzoMinimo){
        if(prezzoMinimo==null){
            return true;
        }
        return ristorante.getPrezzoMedio()>prezzoMinimo;
    }

    private static boolean filtroPrezzoMassimo(Ristorante ristorante, Float prezzoMassimo){
        if(prezzoMassimo==null){
            return true;
        }
        return ristorante.getPrezzoMedio()<prezzoMassimo;
    }

    private static boolean filtroPrenotazione(Ristorante ristorante, Boolean prenotazione){
        if(prenotazione==null){
            return true;
        }
        return ristorante.getPrenotazione()==prenotazione;
    }

    private static boolean filtroDelivery(Ristorante ristorante, Boolean delivery){
        if(delivery==null){
            return true;
        }
        return ristorante.getDelivery()==delivery;
    }

    private static boolean filtroMediaStelle(Ristorante ristorante, Float mediaStelle){
        if(mediaStelle==null){
            return true;
        }
        return ristorante.getMediaStelle()>=mediaStelle;
    }


    /*
     * Aggiunta di un metodo che permette al ristoratore di aggiungere la sua attività all'interno dell'applicazione
     */
    public static boolean aggiungiRistorante(Ristoratore ristoratore, Ristorante ristorante) throws IOException, CsvException {
        if(ristoratore==null){
            return false;
        }
        if(ristorante==null){
            return false;
        }
        if(!GestoreFile.aggiungiRistorante(ristorante)){
            return false;
        }
        return ristoratore.aggiungiRistorante(ristorante);
    }


    /*
     * Aggiunta di un metodo che permette all'utente di visualizzare le informazioni dei ristoranti
     */
    public static void visualizzaRistorante(Ristorante ristorante){
        System.out.println(ristorante);
    }

}
