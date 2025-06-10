package Entita;

/**
 * Eccezione personalizzata per errori relativi alla gestione degli utenti.
 * Viene lanciata quando si verificano errori durante la creazione, modifica
 * o gestione di un'utente.
 *
 * @author Thomas Riotto
 */
public class UtenteException extends RuntimeException {
    public UtenteException(String message) {
        super(message);
    }
}
