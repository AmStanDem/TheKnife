package Entita;

import java.time.LocalDate;

public class Cliente extends Utente{
    public Cliente(String nome, String cognome, String username, String password, LocalDate dataDiNascita, String luogo) {
        super(nome, cognome, username, password, dataDiNascita, luogo);
    }
}
