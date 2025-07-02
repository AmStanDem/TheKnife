/**
 * Questo package contiene le entità principali del sistema gestionale TheKnife.
 * <p>
 * Le classi incluse modellano gli attori fondamentali dell'applicazione:
 * <ul>
 *   <li>{@code Utente}: classe astratta che rappresenta un generico utente (cliente o ristoratore)</li>
 *   <li>{@code Cliente}, {@code Ristoratore}: estensioni concrete dell'utente con ruoli specifici</li>
 *   <li>{@code Ristorante}: entità che gestisce dati e funzionalità relative ai locali registrati</li>
 *   <li>{@code Recensione}: rappresentazione di feedback e valutazioni lasciate dai clienti</li>
 * </ul>
 * <p>
 * Ogni classe segue criteri di incapsulamento, validazione e sicurezza (es. cifratura password via BCrypt).
 * <p>
 * Il package ha lo scopo di offrire una struttura modulare e coerente per la gestione degli oggetti
 * di dominio, facilitando lo sviluppo e il mantenimento dell'applicazione.
 */
package Entita;