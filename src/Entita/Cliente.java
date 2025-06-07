package Entita;

import java.time.LocalDate;

public class Cliente extends Utente {
    private String luogo;
    public Cliente(String nome, String cognome, String username, String password, LocalDate dataDiNascita, String luogo) {
        super(nome, cognome, username, password, dataDiNascita);
        this.luogo=luogo;
    }

    public String getLuogo() {
        return luogo;
    }
}
