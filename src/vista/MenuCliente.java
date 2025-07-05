package vista;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import entita.*;
import com.opencsv.exceptions.CsvException;
import servizi.GeocodingService;
import servizi.RecensioneService;
import servizi.RistoranteService;
import servizi.UtenteService;

/**
 * Classe che rappresenta il menu principale per un cliente autenticato.
 * Fornisce varie opzioni per visualizzare ristoranti, gestire preferiti e recensioni.
 *
 * @author Thomas Riotto
 * @author Marco Zaro
 */
public final class MenuCliente extends Menu {

    /**
     * Scanner per la lettura dell'input da tastiera.
     */
    private final Scanner scanner;

    /**
     * Comando per uscire o interrompere operazioni.
     */
    private final String stop = "stop";

    /**
    /**
     * Cliente autenticato associato al menu.
     */
    private final Cliente cliente;

    /**
     * Costruttore del menu cliente.
     *
     * @param scanner Scanner per leggere input da tastiera.
     * @param cliente Cliente autenticato.
     * @throws IllegalArgumentException se scanner o cliente sono null.
     */
    public MenuCliente(Scanner scanner, Cliente cliente) {
        validaAttributi(scanner, cliente);
        this.scanner = scanner;
        this.cliente = cliente;
    }

    /**
     * Valida gli argomenti passati al costruttore.
     *
     * @param scanner Scanner per input.
     * @param cliente Cliente autenticato.
     * @throws IllegalArgumentException se uno degli argomenti è null.
     */
    private void validaAttributi(Scanner scanner, Cliente cliente) {
        StringBuilder errori = new StringBuilder();
        boolean errore = false;

        if (scanner == null) {
            errori.append("Impossibile leggere da terminale.\n");
            errore = true;
        }
        if (cliente == null) {
            errori.append("Il cliente deve essere valorizzato.\n");
            errore = true;
        }
        if (errore) {
            throw new IllegalArgumentException(errori.toString());
        }
    }

    /**
     * Mostra il menu cliente e gestisce l'interazione con l'utente.
     */
    @Override
    public void mostra() {
        System.out.println("Benvenuto " + cliente);
        int opzione;

        do {
            System.out.println("1. Visualizza i ristoranti vicini a te.");
            System.out.println("2. Cerca un ristorante.");
            System.out.println("3. Visualizza i tuoi ristoranti preferiti.");
            System.out.println("4. Aggiungi un ristorante ai preferiti.");
            System.out.println("5. Rimuovi un ristorante dai preferiti.");
            System.out.println("6. Visualizza i ristoranti recensiti.");
            System.out.println("7. Aggiungi una recensione a un ristorante.");
            System.out.println("8. Modifica una recensione di un ristorante.");
            System.out.println("9. Rimuovi una recensione da un ristorante.");
            System.out.println("10. Logout");

            System.out.print("Inserisci un'opzione: ");
            opzione = leggiInt();

            switch (opzione) {
                case 1:
                    visualizzaRistorantiVicini();
                    break;
                case 2:
                    cercaRistoranteAvanzata();
                    break;
                case 3:
                    visualizzaPreferiti();
                    break;
                case 4:
                    aggiungiAiPreferitiManuale();
                    break;
                case 5:
                    rimuoviPreferito();
                    break;
                case 6:
                    visualizzaRistorantiRecensiti();
                    break;
                case 7:
                    aggiungiRecensioneManuale();
                    break;
                case 8:
                    modificaRecensione();
                    break;
                case 9:
                    rimuoviRecensione();
                    break;
                case 10:
                    System.out.println("Arrivederci. :D");
                    break;
                default:
                    System.out.println("Operazione non valida.");
            }
        } while (opzione != 10);
    }

    /**
     * Visualizza i ristoranti preferiti del cliente.
     */
    private boolean visualizzaPreferiti() {
        System.out.println("I tuoi ristoranti preferiti:");
        var preferiti = cliente.getPreferiti();
        if (preferiti.isEmpty()) {
            System.out.println("Non hai nessun ristorante tra i preferiti!");
            return false;
        }
        for (int i = 0; i < preferiti.size(); i++) {
            System.out.println((i + 1) + ". " + preferiti.get(i));
        }
        return true;
    }

    /**
     * Visualizza i ristoranti vicini al domicilio del cliente.
     */
    private void visualizzaRistorantiVicini() {
        double[] coords = GeocodingService.geocodeAddress(cliente.getLuogoDomicilio());
        if (coords == null) coords = GeocodingService.chiediCoordinateManuali(scanner);
        Localita localita = new Localita(coords[0], coords[1]);
        try {
            var lista = RistoranteService.cercaRistorante(localita, 10.0);
            lista.forEach(System.out::println);
        } catch (IOException | CsvException e) {
            System.err.println("Errore nella ricerca dei ristoranti vicini.");
        }
    }

    /**
     * Permette di effettuare una ricerca avanzata di ristoranti.
     */
    private void cercaRistoranteAvanzata() {
        double[] coords = GeocodingService.geocodeAddress(cliente.getLuogoDomicilio());
        if (coords == null) coords = GeocodingService.chiediCoordinateManuali(scanner);
        Localita localita = new Localita(coords[0], coords[1]);
        try {
            /**
             * Comando per uscire o interrompere operazioni.
             */
            var risultati = RistoranteService.ricercaAvanzata(scanner, localita, stop);
            if (risultati.isEmpty()) {
                System.out.println("Nessun ristorante trovato. Prova a restringere la ricerca.");
            } else {
                gestisciRisultatiRicerca(risultati);
            }
        } catch (IOException | CsvException e) {
            System.err.println("Errore nella ricerca dei ristoranti.");
        }
    }

    /**
     * Gestisce le azioni possibili sui risultati di ricerca.
     *
     * @param risultati Lista di ristoranti trovati.
     */
    private void gestisciRisultatiRicerca(ArrayList<Ristorante> risultati) {
        System.out.println("\n=== AZIONI SUI RISULTATI ===");
        for (int i = 0; i < risultati.size(); i++) {
            System.out.println((i + 1) + ". " + risultati.get(i));
        }

        int scelta;

        do {
            System.out.println("\nCosa vuoi fare?");
            System.out.println("1. Visualizza dettagli di un ristorante");
            System.out.println("2. Aggiungi un ristorante ai preferiti");
            System.out.println("3. Aggiungi una recensione a un ristorante");
            System.out.println("4. Torna al menu principale");

            System.out.print("Scelta: ");
            scelta = leggiInt();

            switch (scelta) {
                case 1:
                    visualizzaDettagliRistorante(risultati);
                    break;
                case 2:
                    aggiungiAiPreferiti(risultati);
                    break;
                case 3:
                    aggiungiRecensioneDaRisultati(risultati);
                    break;
                case 4:
                    System.out.println("Torno al menu principale...");
                    break;
                default:
                    System.out.println("Opzione non valida.");
            }
        } while (scelta != 4);
    }

    /**
     * Visualizza i dettagli di un ristorante selezionato.
     *
     * @param risultati Lista dei ristoranti.
     */
    private void visualizzaDettagliRistorante(ArrayList<Ristorante> risultati) {
        int indice = selezionaRistorante(risultati);
        if (indice >= 0 && indice < risultati.size()) {
            System.out.println("\n=== DETTAGLI RISTORANTE ===");
            System.out.println(risultati.get(indice));
        }
    }

    /**
     * Aggiunge un ristorante selezionato dai risultati ai preferiti del cliente.
     *
     * @param risultati Lista dei ristoranti trovati.
     */
    private void aggiungiAiPreferiti(ArrayList<Ristorante> risultati) {
        int indice = selezionaRistorante(risultati);
        if (indice >= 0 && indice < risultati.size()) {
            Ristorante ristorante = risultati.get(indice);

            if (cliente.getPreferiti().contains(ristorante)) {
                System.out.println("Questo ristorante è già nei tuoi preferiti!");
                return;
            }

            try {
                if (UtenteService.aggiungiPreferito(cliente, ristorante)) {
                    System.out.println("Ristorante aggiunto ai preferiti con successo!");
                } else {
                    System.out.println("Errore nell'aggiunta ai preferiti.");
                }
            } catch (IOException | CsvException e) {
                System.err.println("Errore nell'aggiunta ai preferiti: " + e.getMessage());
            }
        }
    }

    /**
     * Permette di selezionare un ristorante da una lista.
     *
     * @param risultati Lista dei ristoranti.
     * @return indice selezionato o -1 se non valido.
     */
    private int selezionaRistorante(ArrayList<Ristorante> risultati) {
        System.out.print("\nSeleziona un ristorante (1-" + risultati.size() + "): ");

        try {
            int scelta = leggiInt() - 1;

            if (scelta >= 0 && scelta < risultati.size()) {
                return scelta;
            } else {
                System.out.println("Selezione non valida.");
                return -1;
            }
        } catch (Exception e) {
            scanner.nextLine();
            System.out.println("Input non valido.");
            return -1;
        }
    }

    /**
     * Messaggio per invitare a usare prima la ricerca per aggiungere preferiti.
     */
    private void aggiungiAiPreferitiManuale() {
        System.out.println("=== AGGIUNGI AI PREFERITI ===");
        System.out.println("Usa prima 'Cerca un ristorante' per selezionare.");
    }

    /**
     * Messaggio per invitare a usare prima la ricerca per aggiungere recensioni.
     */
    private void aggiungiRecensioneManuale() {
        System.out.println("=== AGGIUNGI RECENSIONE ===");
        System.out.println("Usa prima 'Cerca un ristorante' per selezionare.");
    }

    /**
     * Rimuove un ristorante dalla lista dei preferiti.
     */
    private void rimuoviPreferito() {
        boolean haPreferiti = visualizzaPreferiti();
        if (!haPreferiti)
            return;
        System.out.print("Inserisci il numero del preferito da rimuovere: ");
        int idx = leggiInt() - 1;
        if (idx < 0 || idx >= cliente.getPreferiti().size()) return;
        try {
            boolean successo = UtenteService.rimuoviPreferito(cliente, cliente.getPreferiti().get(idx));
            if (successo)
                System.out.println("Preferito rimosso.");
            else
                System.err.println("Errore nella rimozione del preferito!");
        } catch (IOException | CsvException e) {
            System.err.println("Errore nella rimozione del preferito.");
        }
    }

    /**
     * Visualizza i ristoranti recensiti dal cliente.
     */
    private void visualizzaRistorantiRecensiti() {
        try {
            RecensioneService.visualizzaRecensioniCliente(cliente);
        } catch (IOException | CsvException e) {
            System.err.println("Errore nella visualizzazione delle recensioni.");
        }
    }

    /**
     * Permette di modificare una recensione esistente.
     */
    private void modificaRecensione() {
        System.out.println("=== MODIFICA RECENSIONE ===");
        try {
            var recensioni = RecensioneService.getRecensioniCliente(cliente);
            if (recensioni.isEmpty())  {
                System.out.println("Non hai nessuna recensione!");
                return;
            }
            for (int i = 0; i < recensioni.size(); i++) {
                System.out.println((i + 1) + ". " + recensioni.get(i));
            }
            System.out.print("Seleziona recensione da modificare (premi 0 per annullare): ");
            int idx = leggiInt() - 1;
            if (idx < 0 || idx >= recensioni.size()) {
                System.out.println("Modifica della recensione annullata!");
                return;
            }
            var vecchia = recensioni.get(idx);

            System.out.print("Nuove stelle (1-5) o invio per mantenere: ");
            String s = scanner.nextLine().trim();
            int stelle = s.isEmpty() ? vecchia.getStelle() : Integer.parseInt(s);

            System.out.print("Nuovo testo o invio per mantenere: ");
            String msg = scanner.nextLine();
            if (!msg.isEmpty()) vecchia.setMessaggio(msg);
            if (!RecensioneService.modificaRecensione(cliente, vecchia.getRistorante(), new Recensione(cliente, vecchia.getRistorante(), stelle, vecchia.getMessaggio()))
            ) {
                System.err.println("Errore nell'aggiornamento della recensione.");
                return;
            }
            System.out.println("Recensione aggiornata.");
        } catch (IOException | CsvException e) {
            System.err.println("Errore modifica recensione.");
        }
    }

    /**
     * Permette di rimuovere una recensione.
     */
    private void rimuoviRecensione() {
        System.out.println("=== RIMUOVI RECENSIONE ===");
        try {
            var recensioni = RecensioneService.getRecensioniCliente(cliente);
            if (recensioni.isEmpty()) {
                System.out.println("Non hai nessuna recensione!");
                return;
            }
            for (int i = 0; i < recensioni.size(); i++) {
                System.out.println((i + 1) + ". " + recensioni.get(i));
            }
            System.out.print("Seleziona recensione da rimuovere (premi 0 per annullare): ");
            int idx = leggiInt() - 1;
            if (idx < 0 || idx >= recensioni.size()) {
                System.out.println("Cancellazione della recensione annullata!");
                return;
            }
            if (!RecensioneService.eliminaRecensione(cliente, recensioni.get(idx).getRistorante())) {
                System.err.println("Recensione non eliminata.");
                return;
            }
            System.out.println("Recensione rimossa.");
        } catch (IOException | CsvException e) {
            System.err.println("Errore rimozione recensione.");
        }
    }


    /**
     * Aggiunge una recensione a un ristorante scelto tra i risultati.
     *
     * @param risultati Lista dei ristoranti disponibili.
     */
    private void aggiungiRecensioneDaRisultati(ArrayList<Ristorante> risultati) {
        System.out.println("=== AGGIUNGI RECENSIONE ===");

        int indiceRistorante = selezionaRistorante(risultati);
        if (indiceRistorante < 0 || indiceRistorante > risultati.size()) {
            return;
        }

        Ristorante ristoranteSelezionato = risultati.get(indiceRistorante);

        try {
            // Verifica se l'utente ha già recensito questo ristorante
            if (!RecensioneService.puoAggiungereRecensione(cliente, ristoranteSelezionato)) {
                System.out.println("Hai già recensito questo ristorante. Usa l'opzione 'Modifica recensione' per modificarla.");
                return;
            }

            // Inserimento delle stelle
            System.out.print("Inserisci il numero di stelle (1-5): ");
            int stelle;
            try {
                stelle = leggiInt();
                if (stelle < 1 || stelle > 5) {
                    System.out.println("Il numero di stelle deve essere compreso tra 1 e 5.");
                    return;
                }
            } catch (Exception e) {
                System.out.println("Input non valido per le stelle.");
                return;
            }

            // Inserimento del testo della recensione
            System.out.print("Inserisci il testo della recensione (opzionale): ");
            String testoRecensione = scanner.nextLine().trim();

            Recensione recensione = new Recensione(cliente, ristoranteSelezionato, stelle, testoRecensione);

            // Aggiunta della recensione
            boolean successo = RecensioneService.aggiungiRecensione(
                    cliente, ristoranteSelezionato,recensione
            );

            if (successo) {
                System.out.println("Recensione aggiunta con successo!");
            } else {
                System.out.println("Errore nell'aggiunta della recensione.");
            }

        } catch (IOException | CsvException e) {
            System.err.println("Errore durante l'aggiunta della recensione: " + e.getMessage());
        }
    }

    /**
     * Legge un intero da console in modo sicuro.
     */
    private int leggiInt() {
        while (true) {

            try {
                String line = scanner.nextLine().trim();
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.print("Input non valido, riprova: ");
            }
        }
    }
}
