package Entita;

import java.time.LocalDate;

public class Ristoratore extends Utente {
    public Ristoratore(String nome, String cognome, String username, String password, LocalDate dataDiNascita, String luogo) {
        super(nome, cognome, username, password, dataDiNascita, luogo);
    }
}
