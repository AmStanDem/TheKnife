package theknife;

import theknife.vista.MenuIniziale;

import java.util.Scanner;
/*
 * Riotto Thomas 760981 VA
 * Pesavento Antonio 759933 VA
 * Tullo Alessandro 760760 VA
 * Zaro Marco 760194 VA
 */
/**
 * Classe principale del progetto <strong>theknife.TheKnife</strong>.
 * <p>
 * Questa classe rappresenta il punto di ingresso dell'applicazione e gestisce
 * l'inizializzazione dei componenti principali, l'avvio dei theknife.servizi e la visualizzazione
 * dell'interfaccia utente.
 * <p>
 * Il progetto è pensato per facilitare l'interazione tra utenti e ristoratori,
 * offrendo funzionalità di ricerca, gestione e visualizzazione dei dati.
 *
 * <p><strong>Funzionalità principali:</strong>
 * <ul>
 *   <li>Avvio dell'applicazione</li>
 *   <li>Caricamento delle entità e dei file di configurazione</li>
 *   <li>Gestione dei theknife.servizi e della logica di business</li>
 *   <li>Visualizzazione dell'interfaccia utente</li>
 * </ul>
 * @author Thomas Riotto
 * @author Antonio Pesavento
 * @author Alessandro Tullo
 * @author Marco Zaro
 */
public final class TheKnife {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MenuIniziale menuIniziale = new MenuIniziale(scanner);
        menuIniziale.mostra();
        scanner.close();
    }
}