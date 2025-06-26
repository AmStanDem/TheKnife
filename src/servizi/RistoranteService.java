package servizi;
import Entita.*;
import com.opencsv.exceptions.CsvException;
import io_file.GestoreFile;

import java.io.IOException;
import java.util.ArrayList;

public final class RistoranteService {

    public static ArrayList<Ristorante> cercaRistorante(TipoCucina tipoCucina, Localita localita, Float prezzoMinimo, Float prezzoMassimo, Boolean delivery, Boolean prenotazione, Float mediaStelle) throws IOException, CsvException {
        var ristoranti = GestoreFile.caricaRistoranti();
        var risultato = new ArrayList<Ristorante>();

        for (Ristorante ristorante : ristoranti) {
            if (!filtroTipoCucina(ristorante, tipoCucina)) {
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

    public static boolean filtroPrezzoMinimo(Ristorante ristorante, Float prezzoMinimo){
        if(prezzoMinimo==null){
            return true;
        }
        return ristorante.getPrezzoMedio()>prezzoMinimo;
    }

    public static boolean filtroPrezzoMassimo(Ristorante ristorante, Float prezzoMassimo){
        if(prezzoMassimo==null){
            return true;
        }
        return ristorante.getPrezzoMedio()<prezzoMassimo;
    }

    public static boolean filtroPrenotazione(Ristorante ristorante, Boolean prenotazione){
        if(prenotazione==null){
            return true;
        }
        return ristorante.getPrenotazione()==prenotazione;
    }

    public static boolean filtroDelivery(Ristorante ristorante, Boolean delivery){
        if(delivery==null){
            return true;
        }
        return ristorante.getDelivery()==delivery;
    }

    public static boolean filtroMediaStelle(Ristorante ristorante, Float mediaStelle){
        if(mediaStelle==null){
            return true;
        }
        return ristorante.getMediaStelle()>=mediaStelle;
    }

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

    public static void visualizzaRistorante(Ristorante ristorante){
        System.out.println(ristorante);
    }

}
