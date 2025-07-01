package vista;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import Entita.*;
import com.opencsv.exceptions.CsvException;
import servizi.GeocodingService;
import servizi.RecensioneService;
import servizi.RistoranteService;
import servizi.UtenteService;

/**
 * Menu con le operazioni disponibili per un cliente.
 * @author Marco Zaro
 */
public final class MenuCliente extends Menu {

    private final Scanner scanner;
    private final Cliente cliente;
    private final String stop = "stop";

    /**
     * Crea un nuovo menu cliente
     * @param scanner I/O su terminale.
     * @param cliente Cliente registrato.
     * @throws IllegalArgumentException se i parametri sono invalidi.
     */
    public MenuCliente(Scanner scanner, Cliente cliente) {
        validaAttributi(scanner, cliente);
        this.scanner = scanner;
        this.cliente = cliente;
    }

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
            System.out.println("10. Uscire dall'app.");

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

    private void visualizzaPreferiti() {
        System.out.println("I tuoi ristoranti preferiti:");
        var preferiti = cliente.getPreferiti();
        for (int i = 0; i < preferiti.size(); i++) {
            System.out.println((i + 1) + ". " + preferiti.get(i));
        }
    }

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

    private void cercaRistoranteAvanzata() {
        double[] coords = GeocodingService.geocodeAddress(cliente.getLuogoDomicilio());
        if (coords == null) coords = GeocodingService.chiediCoordinateManuali(scanner);
        Localita localita = new Localita(coords[0], coords[1]);
        try {
            var risultati = RistoranteService.ricercaAvanzata(scanner, localita, stop);
            if (risultati.isEmpty()) {
                System.out.println("Nessun ristorante trovato.");
            } else {
                gestisciRisultatiRicerca(risultati);
            }
        } catch (IOException | CsvException e) {
            System.err.println("Errore nella ricerca dei ristoranti.");
        }
    }

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

    private void visualizzaDettagliRistorante(ArrayList<Ristorante> risultati) {
        int indice = selezionaRistorante(risultati);
        if (indice >= 0 && indice < risultati.size()) {
            System.out.println("\n=== DETTAGLI RISTORANTE ===");
            System.out.println(risultati.get(indice));
        }
    }

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

    private void aggiungiAiPreferitiManuale() {
        System.out.println("=== AGGIUNGI AI PREFERITI ===");
        System.out.println("Usa prima 'Cerca un ristorante' per selezionare.");
    }

    private void aggiungiRecensioneManuale() {
        System.out.println("=== AGGIUNGI RECENSIONE ===");
        System.out.println("Usa prima 'Cerca un ristorante' per selezionare.");
    }

    private void rimuoviPreferito() {
        visualizzaPreferiti();
        System.out.print("Inserisci il numero del preferito da rimuovere: ");
        int idx = leggiInt() - 1;
        if (idx < 0 || idx >= cliente.getPreferiti().size()) return;
        try {
            UtenteService.rimuoviPreferito(cliente, cliente.getPreferiti().get(idx));
            System.out.println("Preferito rimosso.");
        } catch (IOException | CsvException e) {
            System.err.println("Errore nella rimozione del preferito.");
        }
    }

    private void visualizzaRistorantiRecensiti() {
        try {
            RecensioneService.visualizzaRecensioniCliente(cliente);
        } catch (IOException | CsvException e) {
            System.err.println("Errore nella visualizzazione delle recensioni.");
        }
    }

    private void modificaRecensione() {
        System.out.println("=== MODIFICA RECENSIONE ===");
        try {
            var recensioni = RecensioneService.getRecensioniCliente(cliente);
            if (recensioni.isEmpty()) return;
            for (int i = 0; i < recensioni.size(); i++) {
                System.out.println((i + 1) + ". " + recensioni.get(i));
            }
            System.out.print("Seleziona recensione da modificare: ");
            int idx = leggiInt() - 1;
            if (idx < 0 || idx >= recensioni.size()) return;
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

    private void rimuoviRecensione() {
        System.out.println("=== RIMUOVI RECENSIONE ===");
        try {
            var recensioni = RecensioneService.getRecensioniCliente(cliente);
            if (recensioni.isEmpty()) return;
            for (int i = 0; i < recensioni.size(); i++) {
                System.out.println((i + 1) + ". " + recensioni.get(i));
            }
            System.out.print("Seleziona recensione da rimuovere: ");
            int idx = leggiInt() - 1;
            if (idx < 0 || idx >= recensioni.size()) return;
            if (!RecensioneService.eliminaRecensione(cliente, recensioni.get(idx).getRistorante())) {
                System.err.println("Recensione non eliminata.");
                return;
            }
            System.out.println("Recensione rimossa.");
        } catch (IOException | CsvException e) {
            System.err.println("Errore rimozione recensione.");
        }
    }

    private void aggiungiRecensioneDaRisultati(ArrayList<Ristorante> risultati) {
        System.out.println("=== AGGIUNGI RECENSIONE ===");

        int indiceRistorante = selezionaRistorante(risultati);
        if (indiceRistorante < 0) {
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
                scanner.nextLine(); // Consuma il newline

                if (stelle < 1 || stelle > 5) {
                    System.out.println("Il numero di stelle deve essere compreso tra 1 e 5.");
                    return;
                }
            } catch (Exception e) {
                scanner.nextLine(); // Pulisce il buffer
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
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.print("Input non valido, riprova: ");
            }
        }
    }
}
