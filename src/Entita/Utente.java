package Entita;

import java.time.LocalDate;

/**
 * Classe che rappresenta un Entita.Utente del sistema.
 * Un utente può essere un cliente o un ristoratore.
 * Un utente ha dei dati anagrafici di base.
 * @author Thomas Riotto
 */

public class Utente {

    //region Attributi
    private String nome;
    private String cognome;
    private String username;
    private String password;
    private LocalDate dataDiNascita;
    private String luogo; // Da vedere dopo
    //endregion

    //region Costruttore
    public Utente(String nome, String cognome, String username, String password, LocalDate dataDiNascita, String luogo) {
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.password = password;
        this.dataDiNascita = dataDiNascita;
        this.luogo = luogo;
    }
    //endregion

    //region Metodi
    public String getCognome() {
        return cognome;
    }

    public LocalDate getDataDiNascita() {
        return dataDiNascita;
    }

    public String getLuogo() {
        return luogo;
    }

    public String getNome() {
        return nome;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
    //endregion
    //
}
