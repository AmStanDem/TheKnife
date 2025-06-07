package io_file;

import Entita.Ristorante;
import Entita.Cliente;
import Entita.TipoCucina;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


public class GestoreFile {
    private final static Path fileUtenti = Path.of("data", "Utenti.csv");
    private final static Path fileRistoranti = Path.of("data", "Ristoranti.csv");

    private GestoreFile() {}

    public static LinkedList<Ristorante> getRistoranti() throws IOException, CsvException {
        LinkedList<Ristorante> lista = new LinkedList<>();

        CSVReader reader = new CSVReader(new FileReader(fileRistoranti.toFile()));
        List<String[]> righe = reader.readAll();

        // Salta intestazione se presente
        for (int i = 1; i < righe.size(); i++) {
            String[] riga = righe.get(i);
            if (riga.length >= 3) {
                String nome = riga[0];
                String citta = riga[4];
                Ristorante r = new Ristorante(nome, citta);
                lista.add(r);
            }
        }
        reader.close();
        return lista;
    }


    public static void aggiungiRistorante(Ristorante nuovoRistorante) throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter(fileRistoranti.toFile(), true));
        // Scrive una nuova riga con i dati del ristorante
        String[] dati = new String[] {
                nuovoRistorante.getNome(),
                String.valueOf(nuovoRistorante.getPrezzoMedio()),
                String.valueOf(nuovoRistorante.getTipoDiCucina()),
                nuovoRistorante.getNazione(),
                nuovoRistorante.getCitta(),
                nuovoRistorante.getIndirizzo(),
                String.valueOf(nuovoRistorante.getLatitudine()),
                String.valueOf(nuovoRistorante.getLongitudine()),
                nuovoRistorante.getDescrizione(),
                (nuovoRistorante.getDelivery() ? "Sì" : "No"),
                (nuovoRistorante.getPrenotazione() ? "Sì" : "No")
        };
        writer.writeNext(dati);
        writer.close();
    }

    public static void aggiungiCliente(Cliente nuovoCliente) throws IOException, CsvException {
        // Controlla che non ci sia già un utente con lo stesso username
        if(getCliente(nuovoCliente.getUsername())!=null)
            System.out.println("\nEsiste già un utente con questo username; Azione interrotta");
        else {
            CSVWriter writer = new CSVWriter(new FileWriter(fileUtenti.toFile(), true));
            // Scrive una nuova riga con i dati del cliente
            String[] dati = new String[]{
                    nuovoCliente.getNome(),
                    nuovoCliente.getCognome(),
                    nuovoCliente.getUsername(),
                    nuovoCliente.getPassword(),
                    nuovoCliente.getDataDiNascita().format(DateTimeFormatter.ISO_DATE),
                    nuovoCliente.getLuogo()
            };
            writer.writeNext(dati);
            writer.close();
        }
    }

    public static Cliente getCliente(String username) throws IOException, CsvException {
        CSVReader reader = new CSVReader(new FileReader(fileUtenti.toFile()));
        List<String[]> righe = reader.readAll();

        for (int i = 1; i < righe.size(); i++) {
            String[] riga = righe.get(i);
            if(username.equals(riga[2])) { // Ricerca un cliente per username; se lo trova, lo copia
                String nome = riga[0];
                String cognome = riga[1];
                // String username2 = riga[2]; inutile perchè l'user è già nei parametri del metodo, e (in teoria) è uguale
                String password = riga[3];
                LocalDate dataDiNascita = LocalDate.parse(riga[4]);
                String luogo = riga[5];
                reader.close();
                return new Cliente(nome,cognome,username,password,dataDiNascita,luogo);
            }
        }
        reader.close();
        System.out.println("\nNon e' stato trovato nessun cliente corrispondente"); // se non trova ristoranti validi
        return null;
    }


    public static void main(String[] args) throws IOException, CsvException {
        //var dati = GestoreFile.getRistoranti();
        //System.out.printf(dati.toString());
        // var bob = new Ristorante("A","1","a","A,A",1,2, TipoCucina.AMERICANA,true,false,1.2f,"AA");
        // GestoreFile.aggiungiRistorante(bob);
        var ciccio = new Cliente("Antonio","Pesavento","apesavento", "ciao", LocalDate.of(2006,1,14),"Varese");
        GestoreFile.aggiungiCliente(ciccio);
        var ciccio2 = new Cliente("Alessandro","Tullo","atullo","bsgdfjsnhsdhj",LocalDate.of(2005,11,19),"Cittiglio");
        GestoreFile.aggiungiCliente(ciccio2);
        getCliente("apesavento");
        getCliente("atullo");
        getCliente("amstandem");

    }




    public static boolean Login(String username, String password) throws IOException, CsvException {
        //Cerca se l'utente che fa il login è già registrato nel file o no
        CSVReader reader = new CSVReader(new FileReader(fileUtenti.toFile()));
        List<String[]> righe = reader.readAll();
        for (int i=0; i<righe.size(); i++) {
            String[] riga = righe.get(i);
            String name = riga[2];
            String key = riga[3];
            if(name.equals(username)&& key.equals(password)){
                return true;
            }
        }
        return false;
    }


}
