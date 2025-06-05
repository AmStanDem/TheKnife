package Entita;

import java.time.LocalDate;

public class Ristoratore extends Utente {
    public Ristoratore(String nome, String cognome, String username, String password, LocalDate dataDiNascita) {
        super(nome, cognome, username, password, dataDiNascita);
    }
}
