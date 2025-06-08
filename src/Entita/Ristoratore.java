package Entita;

import java.time.LocalDate;

public class Ristoratore extends Utente {

    public Ristoratore(String nome, String cognome, String username, String password, LocalDate dataDiNascita, String luogoDomicilio) {
        super(nome, cognome, username, password, dataDiNascita, luogoDomicilio);
    }

    public static void main(String[] args) {
        System.out.println(new Ristoratore(
                "a",
                "a",
                "a",
                "a",
                LocalDate.now(),
                "a"
        ).getTipoUtente()); // Stampa Ristoratore
    }
}
