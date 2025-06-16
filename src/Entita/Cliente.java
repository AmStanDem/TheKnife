package Entita;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class Cliente extends Utente {

    private final List<Ristorante> preferiti;

    public Cliente(String nome, String cognome, String username, String password, LocalDate dataDiNascita, String luogoDomicilio) {
        super(nome, cognome, username, password, dataDiNascita, luogoDomicilio);
        preferiti = new ArrayList<>();
    }

    public Cliente(String nome, String cognome, String username, String password, LocalDate dataDiNascita, String luogoDomicilio, ArrayList<Ristorante> preferiti, List<Recensione> recensioni) {
        super(nome, cognome, username, password, dataDiNascita, luogoDomicilio);
        this.preferiti = preferiti;
    }

    public List<Ristorante> getPreferiti() {
        return new ArrayList<>(preferiti);
    }

    public boolean aggiungiPreferito(Ristorante ristorante) {
        if (!this.preferiti.contains(ristorante)) {
            return preferiti.add(ristorante);
        }
        return false;
    }

    public boolean rimuoviPreferito(Ristorante ristorante) {
        return preferiti.remove(ristorante);
    }

    public ArrayList<Recensione> getRecensioni(ArrayList<Ristorante> ristoranti) {
        ArrayList<Recensione> listaRecensioni = new ArrayList<>();

        for (Ristorante ristorante: ristoranti) {
            Recensione recensione = ristorante.trovaRecensioneCliente(this);
            if (recensione != null) {
                listaRecensioni.add(recensione);
            }
        }
        return listaRecensioni;
    }

    public void visualizzaRecensioni(ArrayList<Ristorante> ristoranti) {
        ArrayList<Recensione> listaRecensioni = getRecensioni(ristoranti);

        StringBuilder stringBuilder = new StringBuilder();
        for (Recensione recensione: listaRecensioni) {
            stringBuilder.append(recensione).append("\n");
        }
        System.out.println(stringBuilder.toString());
    }

    public boolean aggiungiRecensione(Ristorante ristorante, Recensione recensione) {

        if (!recensione.getCliente().equals(this)) {
            return false;
        }

        if (haRecensito(ristorante)) {
            return false;
        }

        return ristorante.aggiungiRecensione(recensione);
    }

    public boolean rimuoviRecensione(Ristorante ristorante) {
        if (!haRecensito(ristorante))
            return false;

        Recensione recensione = ristorante.trovaRecensioneCliente(this);

        return ristorante.rimuoviRecensione(recensione);
    }

    public boolean modificaRecensione(Ristorante ristorante, Recensione nuovaRecensione) {
        if (!nuovaRecensione.getCliente().equals(this)) {
            return false;
        }

        Recensione recensione = ristorante.trovaRecensioneCliente(this);

        if (recensione == null) {
            return false;
        }

        if (ristorante.rimuoviRecensione(recensione)) {
            return ristorante.aggiungiRecensione(nuovaRecensione);
        }
        return false;
    }

    public boolean haRecensito(Ristorante ristorante) {
        return ristorante.trovaRecensioneCliente(this) != null;
    }



    public static void main(String[] args) {
        System.out.println(new Cliente
                ("admin",
                        "admin",
                        "admin",
                        "admin",
                        LocalDate.now(),
                        "admin")
                .getTipoUtente()); // Stampa Cliente
    }
}
