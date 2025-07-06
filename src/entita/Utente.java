package entita;

import org.mindrot.jbcrypt.BCrypt;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Rappresenta un utente del sistema, che può essere un cliente o un ristoratore.
 * <p>
 * Ogni utente possiede dati anagrafici di base (nome, cognome, username,
 * data di nascita e luogo di domicilio).
 * La classe è astratta e va estesa da classi concrete come {@code Cliente} o {@code Ristoratore}.
 * <p>
 * Le password vengono cifrate automaticamente tramite l’algoritmo BCrypt.
 * I nomi e i luoghi vengono formattati con la prima lettera maiuscola in ogni parola.
 *
 * @author Thomas Riotto
 * @author Antonio Pesavento
 * @author Alessandro Tullo
 * @author Marco Zaro
 */
public abstract class Utente {
    /** Nome dell’utente formattato correttamente */
    private final String nome;

    /** Cognome dell’utente formattato correttamente */
    private final String cognome;

    /** Identificativo unico scelto dall’utente per accedere al sistema */
    private final String username;

    /** Password cifrata dell’utente, generata tramite algoritmo BCrypt */
    private String password;

    /** Data di nascita dell’utente */
    private final LocalDate dataNascita;

    /** Luogo di domicilio formattato (prima lettera maiuscola) */
    private final String luogoDomicilio;


    /**
     * Crea un nuovo utente impostando i dati anagrafici e cifrando la password.
     * Nomi, cognome e luogo di domicilio vengono formattati correttamente.
     *
     * @param nome           Nome dell’utente (in chiaro, con qualsiasi formato)
     * @param cognome        Cognome dell’utente (in chiaro)
     * @param username       Username scelto per l’accesso
     * @param password       Password in chiaro da cifrare
     * @param dataNascita    Data di nascita dell’utente
     * @param luogoDomicilio Luogo di domicilio (in chiaro)
     * @throws UtenteException Se i dati inseriti sono invalidi.
     */
    public Utente(String nome, String cognome, String username, String password,
                  LocalDate dataNascita, String luogoDomicilio) {
        validaAttributi(nome, cognome, username, password, luogoDomicilio);
        this.nome = formattaNome(nome);
        this.cognome = formattaNome(cognome);
        this.username = username;
        this.password = cifraPassword(password);
        this.dataNascita = dataNascita;
        this.luogoDomicilio = formattaNome(luogoDomicilio);
    }

    //region --- Getters e Setters ---

    /**
     * Restituisce Il nome formattato dell’utente
     *
     * @return Il nome formattato dell’utente
     */
    public String getNome() { return nome; }

    /**
     * Restituisce il cognome formattato dell’utente
     *
     * @return Il cognome formattato dell’utente
     */
    public String getCognome() { return cognome; }

    /**
     * Restituisce l'username dell’utente
     *
     * @return Lo username dell’utente
     */
    public String getUsername() { return username; }

    /**
     * Restituisce la password cifrata dell’utente
     *
     * @return La password cifrata dell’utente
     */
    public String getPassword() { return password; }

    /**
     * Imposta la password già cifrata per l'utente.
     * @param password Password cifrata
     */
    public void setPasswordCifrata(String password) {
        if (password != null && !password.isEmpty()) {
            this.password = password;
        }
    }

    /** @return La data di nascita dell’utente */
    public LocalDate getDataNascita() { return dataNascita; }

    /** @return Il luogo di domicilio formattato */
    public String getLuogoDomicilio() { return luogoDomicilio; }

    //endregion

    //region --- Metodi di utilità ---

    /**
     * Ritorna il tipo concreto dell’utente.
     * @return Stringa che rappresenta il tipo utente ("Cliente" per {@code Cliente}, "Ristoratore" per {@code Ristoratore})
     */
    public String getTipoUtente() {
        return getClass().getSimpleName();
    }

    /**
     * Verifica che i dati siano valorizzati nel modo corretto
     * @param nome Nome dell'utente
     * @param cognome Cognome dell'utente
     * @param username Username dell'utente
     * @param password Password dell'utente
     * @param luogoDomicilio Luogo del domicilio dell'utente
     * @throws UtenteException Se i dati non sono valorizzati nel modo corretto.
     */
    private void validaAttributi(String nome, String cognome, String username, String password, String luogoDomicilio) {
        StringBuilder errori = new StringBuilder();
        boolean errore = false;
        if (nome == null || nome.isEmpty()) {
            String messaggio = "Il nome di un utente deve essere valorizzato.\n";
            errori.append(messaggio);
            errore = true;
        }
        if (cognome == null || cognome.isEmpty()) {
            String messaggio = "Il cognome di un utente deve essere valorizzato.\n";
            errori.append(messaggio);
            errore = true;
        }
        if (username == null || username.isEmpty()) {
            String messaggio = "L'username di un utente deve essere valorizzato.\n";
            errori.append(messaggio);
            errore = true;
        }
        if (password == null || password.isEmpty()) {
            String messaggio = "La password di un utente deve essere valorizzata.\n";
            errori.append(messaggio);
            errore = true;
        }
        if (luogoDomicilio == null || luogoDomicilio.isEmpty()) {
            String messaggio = "Il luogo del domicilio di un utente deve essere valorizzato.\n";
            errori.append(messaggio);
            errore = true;
        }

        if (errore) {
            throw new UtenteException(errori.toString());
        }
    }

    /**
     * Verifica che la password in chiaro corrisponda a quella salvata cifrata.
     *
     * @param password Password in chiaro da verificare
     * @return {@code true} se la password è corretta, {@code false} altrimenti
     */
    public boolean verificaPassword(String password) {
        return BCrypt.checkpw(password, this.password);
    }

    /**
     * Calcola l’hash BCrypt di una password in chiaro.
     */
    private String cifraPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * Effettua l’equivalenza tra utenti in base allo username.
     *
     * @param o Altro oggetto da confrontare
     * @return {@code true} se l’altro utente ha lo stesso username
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Utente utente)) return false;
        return username.equals(utente.username);
    }
    /**
     * Calcola l'hashCode basato sullo {@code username} dell'utente.
     * <p>
     * Questo garantisce coerenza con il metodo {@code equals()}, che si basa anch'esso
     * sullo {@code username}.
     *
     * @return Codice hash dell'utente
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(username);
    }

    /**
     * Rappresentazione testuale dell’utente, nel formato "Nome Cognome (username)".
     *
     * @return Stringa descrittiva dell’utente
     */
    @Override
    public String toString() {
        return String.format("%s %s (%s)", nome, cognome, username);
    }

    /**
     * Restituisce la stringa formattata con la prima lettera maiuscola per ogni parola
     * e il resto in minuscolo.
     * <p>
     * Utile per omogeneizzare nomi propri e località, indipendentemente dal formato
     * fornito in input. Eventuali spazi multipli vengono compressi.
     *
     * @param input Stringa da formattare (es. "mario rossi" → "Mario Rossi")
     * @return Stringa formattata con capitalizzazione corretta
     */
    private String formattaNome(String input) {
        if (input == null || input.isEmpty()) return input;

        String[] parole = input.trim().toLowerCase().split("\\s+");
        StringBuilder risultato = new StringBuilder();

        for (String parola : parole) {
            if (!parola.isEmpty()) {
                risultato.append(parola.substring(0, 1).toUpperCase());
                if (parola.length() > 1) {
                    risultato.append(parola.substring(1));
                }
                risultato.append(" ");
            }
        }

        return risultato.toString().trim();
    }

    //endregion
}
