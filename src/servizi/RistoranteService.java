package servizi;
import Entita.*;
import com.opencsv.exceptions.CsvException;
import io_file.GestoreFile;

import java.io.IOException;
import java.util.ArrayList;

public final class RistoranteService {

    public static ArrayList<Ristorante> cercaRistorante(TipoCucina tipoCucina, Localita localita, float prezzoMinimo, float prezzoMassimo, boolean delivery, boolean prenotazione, float mediaStelle) throws IOException, CsvException {
        var ristoranti = GestoreFile.caricaRistoranti();

        var risultato = new ArrayList<Ristorante>();

        for (Ristorante ristorante : ristoranti) {
            if (!filtroTipoCucina(ristorante, tipoCucina)) {
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
