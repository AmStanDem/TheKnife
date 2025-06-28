package Entita;

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
 * @version 1.0
 */
public abstract class Utente {
    protected String nome;
    protected String cognome;
    protected String username;
    protected String password;
    protected LocalDate dataNascita;
    protected String luogoDomicilio;

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

    /** @return Il nome formattato dell’utente */
    public String getNome() { return nome; }

    /** @param nome Nuovo nome (verrà formattato automaticamente) */
    public void setNome(String nome) {
        if (nome != null && !nome.isEmpty()) {
            this.nome = formattaNome(nome);
        }
    }

    /** @return Il cognome formattato dell’utente */
    public String getCognome() { return cognome; }

    /** @param cognome Nuovo cognome (verrà formattato automaticamente) */
    public void setCognome(String cognome) {
        if (cognome != null && !cognome.isEmpty()) {
            this.cognome = formattaNome(cognome);
        }
    }

    /** @return Lo username dell’utente */
    public String getUsername() { return username; }

    /** @param username Nuovo username */
    public void setUsername(String username) {
        if (username != null && !username.isEmpty()) {
            this.username = username;
        }
    }

    /** @return La password cifrata dell’utente */
    public String getPassword() { return password; }

    /**
     * Imposta una nuova password per l’utente, cifrandola automaticamente.
     *
     * @param password Nuova password in chiaro
     */
    public void setPassword(String password) {
        if (password != null && !password.isEmpty()) {
            this.password = cifraPassword(password);
        }
    }

    /**
     * Imposta la password già cifrate per l'utente.
     * @param password Password cifrata
     */
    public void setPasswordCifrata(String password) {
        if (password != null && !password.isEmpty()) {
            this.password = password;
        }
    }

    /** @return La data di nascita dell’utente */
    public LocalDate getDataNascita() { return dataNascita; }

    /** @param dataNascita Nuova data di nascita */
    public void setDataNascita(LocalDate dataNascita) { this.dataNascita = dataNascita; }

    /** @return Il luogo di domicilio formattato */
    public String getLuogoDomicilio() { return luogoDomicilio; }

    /** @param luogo Luogo di domicilio (verrà formattato automaticamente) */
    public void setLuogoDomicilio(String luogo) {
        if (luogo != null && !luogo.isEmpty()) {
            this.luogoDomicilio = formattaNome(luogo);
        }
    }

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

    /*
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

    // Metodo privato di utilità: formattazione della stringa (singole e multiple parole)
    // con prima lettera maiuscola e il resto minuscolo.
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

    public static void main(String[] args) {
        System.out.println("TEST CLASSE UTENTE\n");

        String password1 = "minefraift?!";
        String password2 = "bruuuuh";

        Cliente cliente = new Cliente( // Per pochi
                "marco b",
                "porterai",
                "minefraft",
                password1,
                LocalDate.of(2004, 10, 9),
                "ehm, no"
        );

        // Test toString() e formattaNome
        System.out.println("➤ Dati iniziali:");
        System.out.println(cliente); // nome, cognome, username

        // Test getTipoUtente()
        System.out.println("Tipo utente: " + cliente.getTipoUtente());

        // Test verificaPassword()
        System.out.println("\n➤ Verifica password:");
        System.out.println("Password corretta? " + cliente.verificaPassword(password1)); // true
        System.out.println("Password errata? " + cliente.verificaPassword("sbagliata"));     // false

        // Test setters
        cliente.setNome("bruh");
        cliente.setCognome("bruh");
        cliente.setLuogoDomicilio("milano centrale");
        cliente.setUsername("bruh");
        cliente.setDataNascita(LocalDate.of(1999, 5, 20));
        cliente.setPassword(password2);

        System.out.println("\n➤ Dopo modifica dati:");
        System.out.println(cliente);
        System.out.println("\n➤ Test dei getters:");
        System.out.println("Nome: " + cliente.getNome()); // atteso: "Bruh"
        System.out.println("Cognome: " + cliente.getCognome()); // atteso: "Bruh"
        System.out.println("Username: " + cliente.getUsername()); // atteso: "bruh"
        System.out.println("Luogo di domicilio: " + cliente.getLuogoDomicilio()); // atteso: "Milano Centrale"
        System.out.println("Data di nascita: " + cliente.getDataNascita()); // atteso: 1999-05-20
        System.out.println("Password cifrata: " + cliente.getPassword()); // sarà un hash, non confrontabile

        System.out.println("Password nuova corretta? " + cliente.verificaPassword(password2));

        // Test equals()
        Cliente altro = new Cliente(
                "Cavallo",
                "Pazzo",
                "bruh",
                "Rabiot",
                LocalDate.of(2000, 1, 1),
                "Milano please, vieni al Milan"
        );

        System.out.println("\n➤ Test equals tra utenti con stesso username:");
        System.out.println("Sono uguali? " + cliente.equals(altro)); // true

        // Test hashCode
        System.out.println("\n➤ hashCode:");
        System.out.println("HashCode cliente: " + cliente.hashCode());
        System.out.println("HashCode altro:   " + altro.hashCode());

        String password = "Ciao";

        Utente u = new Ristoratore("A","A","A",password,null,"a");

        System.out.println(u.verificaPassword(password));
    }
}
