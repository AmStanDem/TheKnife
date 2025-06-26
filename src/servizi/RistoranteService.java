package servizi;
import Entita.Ristorante;
import Entita.Ristoratore;
import com.opencsv.exceptions.CsvException;
import io_file.GestoreFile;

import java.io.IOException;

public final class RistoranteService {
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

    public static void visualizzaRsitorante(Ristorante ristorante){
        System.out.println(ristorante);
    }

}
