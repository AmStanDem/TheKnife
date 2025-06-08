package Entita;

import java.time.LocalDate;

public class Cliente extends Utente {
    public Cliente(String nome, String cognome, String username, String password, LocalDate dataDiNascita, String luogoDomicilio) {
        super(nome, cognome, username, password, dataDiNascita, luogoDomicilio);
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
