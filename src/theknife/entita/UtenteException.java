package theknife.entita;
/*
 * Riotto Thomas 760981 VA
 * Pesavento Antonio 759933 VA
 * Tullo Alessandro 760760 VA
 * Zaro Marco 760194 VA
 */
/**
 * Eccezione personalizzata per errori relativi alla gestione degli utenti.
 * Viene lanciata quando si verificano errori durante la creazione, modifica
 * o gestione di un'utente.
 *
 * @author Thomas Riotto
 */
public class UtenteException extends RuntimeException {
    /**
     * Costruisce una nuova UtenteException con il messaggio specificato.
     *
     * @param message il messaggio di errore dettagliato
     */
    public UtenteException(String message) {
        super(message);
    }
}
