package Entita;

/**
 * Eccezione personalizzata per errori relativi alla gestione delle recensioni.
 * Viene lanciata quando si verificano errori durante la creazione, modifica
 * o gestione di una recensione.
 *
 * @author Thomas Riotto
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