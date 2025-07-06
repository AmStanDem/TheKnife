package theknife.entita;
/*
 * Riotto Thomas 760981 VA
 * Pesavento Antonio 759933 VA
 * Tullo Alessandro 760760 VA
 * Zaro Marco 760194 VA
 */
/**
 * Eccezione personalizzata per errori relativi alla gestione delle recensioni.
 * Viene lanciata quando si verificano errori durante la creazione, modifica
 * o gestione di una recensione.
 *
 * @author Antonio Pesavento
 */
public class RecensioneException extends RuntimeException {

    /**
     * Costruisce una nuova RecensioneException con il messaggio specificato.
     *
     * @param message il messaggio di errore dettagliato
     */
    public RecensioneException(String message) {
        super(message);
    }
}