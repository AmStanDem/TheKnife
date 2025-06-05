package Entita;

import java.time.LocalDate;

/**
 * Classe che rappresenta un Entita.Utente del sistema.
 * Un utente può essere un cliente o un ristoratore.
 * Un utente ha dei dati anagrafici di base.
 * @author Thomas Riotto
 */

public abstract class Utente {

    //region Attributi
    private String nome;
    private String cognome;
    private String username;
    private String password;
    private LocalDate dataDiNascita;
     // Da vedere dopo
    //endregion

    //region Costruttore
    public Utente(String nome, String cognome, String username, String password, LocalDate dataDiNascita) {
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.password = password;
        this.dataDiNascita = dataDiNascita;

    }
    //endregion

    //region Metodi
    public String getCognome() {
        return cognome;
    }

    public LocalDate getDataDiNascita() {
        return dataDiNascita;
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
