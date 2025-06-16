package Entita;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Ristoratore extends Utente {

    private List<Ristorante> ristoranti;

    public Ristoratore(String nome, String cognome, String username, String password, LocalDate dataDiNascita, String luogoDomicilio) {
        super(nome, cognome, username, password, dataDiNascita, luogoDomicilio);
        ristoranti = new ArrayList<>();
    }

    public Ristoratore(String nome, String cognome, String username, String password, LocalDate dataDiNascita, String luogoDomicilio, ArrayList<Ristorante> ristoranti) {
        super(nome, cognome, username, password, dataDiNascita, luogoDomicilio);
        this.ristoranti = new ArrayList<>(ristoranti);

        for (Ristorante ristorante : this.ristoranti) {
            ristorante.setProprietario(this);
        }
    }

    public List<Ristorante> getRistoranti() {
        return new ArrayList<>(ristoranti);
    }

    public boolean aggiungiRistorante(Ristorante ristorante) {
        if (ristoranti.contains(ristorante)) {
            return false;
        }
        ristorante.setProprietario(this);
        return ristoranti.add(ristorante);
    }

    public boolean rimuoviRistorante(Ristorante ristorante) {
        return ristoranti.remove(ristorante);
    }

    public int getNumeroRistoranti() {
        return ristoranti.size();
    }

    public void visualizzaRiepilogo() {
        int totaleRecensioni = 0;
        float sommaStelle = 0;

        for (Ristorante ristorante : ristoranti) {
            int numRecensioni = ristorante.getNumeroRecensioni();
            totaleRecensioni += numRecensioni;
            sommaStelle += ristorante.getMediaStelle() * numRecensioni;
        }

        if (totaleRecensioni == 0) {
            System.out.println("Nessuna recensione disponibile per i tuoi ristoranti.");
        } else {
            float mediaGlobale = sommaStelle / totaleRecensioni;
            System.out.printf("Hai %d recensioni totali. Media stelle complessiva: %.2f%n", totaleRecensioni, mediaGlobale);
        }
    }

    public static void main(String[] args) {
        // Creo un ristoratore
        Ristoratore ristoratore = new Ristoratore(
                "Mario",
                "Rossi",
                "mrossi",
                "password123",
                LocalDate.of(1980, 3, 15),
                "Milano"
        );

        // Creo un ristorante
        Localita loc1 = new Localita("Italia", "Milano", "Via Roma 10", 45.46, 9.19);
        Ristorante r1 = new Ristorante("La Trattoria", loc1, TipoCucina.ITALIANA, true, true, 35.0f, "Cucina tipica italiana");

        // Creo un cliente e due recensioni
        Cliente c1 = new Cliente("Luca", "Verdi", "lverdi", "pass", LocalDate.of(1995, 5, 12), "Como");
        Cliente c2 = new Cliente("Anna", "Bianchi", "abianchi", "pass", LocalDate.of(1992, 7, 25), "Varese");

        Recensione rec1 = new Recensione(c1, r1, 4, "Buono!");
        Recensione rec2 = new Recensione(c2, r1, 5, "Eccellente!");

        // Aggiungo recensioni al ristorante
        r1.aggiungiRecensione(rec1);
        r1.aggiungiRecensione(rec2);

        // Aggiungo il ristorante al ristoratore
        ristoratore.aggiungiRistorante(r1);

        // Stampo tipo utente
        System.out.println("Tipo utente: " + ristoratore.getTipoUtente());

        // Visualizzo riepilogo recensioni
        ristoratore.visualizzaRiepilogo();
    }
}
