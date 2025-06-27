package vista;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import Entita.*;
import com.opencsv.exceptions.CsvException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import servizi.GeocodingService;
import servizi.RecensioneService;
import servizi.RistoranteService;
import servizi.UtenteService;

public class MenuCliente extends MenuIniziale {

    private final Scanner scanner;
    private final Cliente cliente;

    public MenuCliente(Scanner scanner, Cliente cliente) {
        validaAttributi(scanner, cliente);
        this.scanner = scanner;
        this.cliente = cliente;
        mostra();
    }

    private void validaAttributi(Scanner scanner, Cliente cliente) {
        StringBuilder errori = new StringBuilder();
        boolean errore = false;

        if (scanner == null) {
            String messaggio = "Impossibile leggere da terminale.\n";
            errori.append(messaggio);
            errore = true;
        }

        if (cliente == null) {
            String messaggio = "Il nome di un cliente deve essere valorizzato.\n";
            errori.append(messaggio);
            errore = true;
        }
        if (errore) {
            throw new IllegalArgumentException();
        }
    }

    private void visualizzaPreferiti() {

        System.out.println("I tuoi ristoranti preferiti: ");

        for (int i = 0; i < cliente.getPreferiti().size(); i++) {
            System.out.println((i + 1) + ". " + cliente.getPreferiti().get(i));
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

            System.out.println("Inserisci una opzione: ");
            opzione = scanner.nextInt();

            switch (opzione) {
                case 1:
                    visualizzaRistorantiVicini();
                    break;
                case 2:
                    cercaRistorante();
                    break;
                case 3:
                    visualizzaPreferiti();
                    break;
                case 4:
                    aggiungiAiPreferiti();
                    break;
                case 5:
                    rimuoviPreferito();
                    break;
                case 6:
                    visualizzaRistorantiRecensiti();
                    break;
                case 7:
                    aggiungiRecensione();
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
                    System.out.println("Operazione non ancora supportata.");
                    break;
            }
        } while (opzione != 10);
    }

    private void visualizzaRistorantiVicini() {
        double[] coords = GeocodingService.geocodeAddress(cliente.getLuogoDomicilio());
        double raggioKm = 10;
        Localita localita = new Localita(coords[0], coords[1]);
        try {
            var ristoranti = RistoranteService.cercaRistorante(localita, raggioKm);
            for (Ristorante ristorante : ristoranti) {
                RistoranteService.visualizzaRistorante(ristorante);
            }
        }
        catch (IOException | CsvException e) {
            System.err.println("Errore nella ricerca dei ristoranti vicini.");
        }
    }

    private void cercaRistorante() {
        System.out.println("=== RICERCA AVANZATA RISTORANTI ===");

        try {

            scanner.nextLine();

            TipoCucina tipoCucina = selezionaTipoCucina();

            double[] coords = GeocodingService.geocodeAddress(cliente.getLuogoDomicilio());
            Localita localita = new Localita(coords[0], coords[1]);

            Double raggioKm = inserisciRaggio();

            Float[] prezzi = inserisciFasciaPrezzo();
            Float prezzoMinimo = prezzi[0];
            Float prezzoMassimo = prezzi[1];

            Boolean delivery = inserisciServizio("delivery");
            Boolean prenotazione = inserisciServizio("prenotazione online");

            Float mediaStelle = inserisciMediaStelle();

            System.out.println("\nRicerca in corso...");
            var ristoranti = RistoranteService.cercaRistorante(
                    tipoCucina, localita, prezzoMinimo, prezzoMassimo,
                    delivery, prenotazione, mediaStelle, raggioKm
            );

            if (ristoranti.isEmpty()) {
                System.out.println("Nessun ristorante trovato con i criteri specificati.");
                System.out.println("Prova ad allargare i filtri di ricerca.");
            } else {
                System.out.println("\n=== RISULTATI RICERCA ===");
                System.out.println("Trovati " + ristoranti.size() + " ristoranti:");

                for (int i = 0; i < ristoranti.size(); i++) {
                    System.out.println("\n" + (i + 1) + ". ");
                    RistoranteService.visualizzaRistorante(ristoranti.get(i));
                }
                gestisciRisultatiRicerca(ristoranti);
            }

        } catch (IOException | CsvException e) {
            System.err.println("Errore durante la ricerca dei ristoranti.");
        } catch (IllegalArgumentException e) {
            System.err.println("Errore nei parametri di ricerca: " + e.getMessage());
        }
    }

    private TipoCucina selezionaTipoCucina() {
        System.out.println("\nSeleziona tipo di cucina (premi INVIO per saltare):");

        TipoCucina[] tipi = TipoCucina.values();
        for (int i = 0; i < tipi.length; i++) {
            System.out.println((i + 1) + ". " + tipi[i]);
        }
        System.out.println("0. Qualsiasi tipo");

        System.out.print("Scelta: ");
        String input = scanner.nextLine().trim();

        if (input.isEmpty() || input.equals("0")) {
            return null;
        }

        try {
            int scelta = Integer.parseInt(input);
            if (scelta >= 1 && scelta <= tipi.length) {
                return tipi[scelta - 1];
            } else {
                System.out.println("Scelta non valida, tipo di cucina ignorato.");
                return null;
            }
        } catch (NumberFormatException e) {
            System.out.println("Input non valido, tipo di cucina ignorato.");
            return null;
        }
    }

    private Double inserisciRaggio() {
        System.out.print("\nRaggio di ricerca in km (default: 10km, premi INVIO per default): ");
        String input = scanner.nextLine().trim();

        if (input.isEmpty()) {
            return 10.0;
        }

        try {
            double raggio = Double.parseDouble(input);
            if (raggio > 0) {
                return raggio;
            } else {
                System.out.println("Raggio non valido, utilizzato default 10km.");
                return 10.0;
            }
        } catch (NumberFormatException e) {
            System.out.println("Input non valido, utilizzato default 10km.");
            return 10.0;
        }
    }

    private Float[] inserisciFasciaPrezzo() {
        System.out.println("\nFascia di prezzo:");

        System.out.print("Prezzo minimo in € (premi INVIO per saltare): ");
        String minInput = scanner.nextLine().trim();
        Float prezzoMinimo = null;

        if (!minInput.isEmpty()) {
            try {
                prezzoMinimo = Float.parseFloat(minInput);
                if (prezzoMinimo < 0) {
                    System.out.println("Prezzo non valido, filtro prezzo minimo ignorato.");
                    prezzoMinimo = null;
                }
            } catch (NumberFormatException e) {
                System.out.println("Input non valido, filtro prezzo minimo ignorato.");
                prezzoMinimo = null;
            }
        }

        System.out.print("Prezzo massimo in € (premi INVIO per saltare): ");
        String maxInput = scanner.nextLine().trim();
        Float prezzoMassimo = null;

        if (!maxInput.isEmpty()) {
            try {
                prezzoMassimo = Float.parseFloat(maxInput);
                if (prezzoMassimo < 0) {
                    System.out.println("Prezzo non valido, filtro prezzo massimo ignorato.");
                    prezzoMassimo = null;
                }
            } catch (NumberFormatException e) {
                System.out.println("Input non valido, filtro prezzo massimo ignorato.");
                prezzoMassimo = null;
            }
        }

        if (prezzoMinimo != null && prezzoMassimo != null && prezzoMinimo > prezzoMassimo) {
            System.out.println("Prezzo minimo maggiore del massimo, filtri prezzo ignorati.");
            prezzoMinimo = null;
            prezzoMassimo = null;
        }

        return new Float[]{prezzoMinimo, prezzoMassimo};
    }

    private Boolean inserisciServizio(String nomeServizio) {
        System.out.println("\nServizio " + nomeServizio + ":");
        System.out.println("1. Solo con " + nomeServizio);
        System.out.println("2. Solo senza " + nomeServizio);
        System.out.println("3. Indifferente (default)");

        System.out.print("Scelta: ");
        String input = scanner.nextLine().trim();

        return switch (input) {
            case "1" -> true;
            case "2" -> false;
            default -> null;
        };
    }

    private Float inserisciMediaStelle() {
        System.out.print("\nMedia stelle minima (1.0-5.0, premi INVIO per saltare): ");
        String input = scanner.nextLine().trim();

        if (input.isEmpty()) {
            return null;
        }

        try {
            float stelle = Float.parseFloat(input);
            if (stelle >= 1.0f && stelle <= 5.0f) {
                return stelle;
            } else {
                System.out.println("Valore non valido (deve essere tra 1.0 e 5.0), filtro ignorato.");
                return null;
            }
        } catch (NumberFormatException e) {
            System.out.println("Input non valido, filtro stelle ignorato.");
            return null;
        }
    }

    private void gestisciRisultatiRicerca(ArrayList<Ristorante> risultati) {
        System.out.println("\n=== AZIONI SUI RISULTATI ===");

        int scelta;
        do {
            System.out.println("\nCosa vuoi fare?");
            System.out.println("1. Visualizza dettagli di un ristorante");
            System.out.println("2. Aggiungi un ristorante ai preferiti");
            System.out.println("3. Aggiungi una recensione a un ristorante");
            System.out.println("4. Torna al menu principale");

            System.out.print("Scelta: ");
            scelta = scanner.nextInt();
            scanner.nextLine();

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
        } while (scelta != 3);
    }

    private void visualizzaDettagliRistorante(ArrayList<Ristorante> risultati) {
        int indice = selezionaRistorante(risultati);
        if (indice >= 0) {
            System.out.println("\n=== DETTAGLI RISTORANTE ===");
            RistoranteService.visualizzaRistorante(risultati.get(indice));
        }
    }

    private void aggiungiAiPreferiti(ArrayList<Ristorante> risultati) {
        int indice = selezionaRistorante(risultati);
        if (indice >= 0) {
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
            int scelta = scanner.nextInt() - 1;
            scanner.nextLine();

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

    private void rimuoviPreferito() {
        int indice;

        do {
            visualizzaPreferiti();
            System.out.println("Inserisci il numero del ristorante che vuoi rimuovere dai preferiti: ");
            indice = scanner.nextInt() - 1;
        } while (indice < 0 || indice > cliente.getPreferiti().size());

        Ristorante preferitoDaEliminare = cliente.getPreferiti().get(indice);

        try {
            if (!UtenteService.rimuoviPreferito(cliente, preferitoDaEliminare)) {
                System.err.println("Errore nella rimozione del ristorante dai preferiti");
            }
            System.out.println("Preferito rimosso con successo.");
        } catch (IOException | CsvException e) {
            System.err.println("Errore nella rimozione del ristorante dai preferiti");
        }
    }

    public void visualizzaRistorantiRecensiti() {
        try {
            RecensioneService.visualizzaRecensioniCliente(cliente);
        }
        catch (IOException | CsvException e) {
            System.err.println("Errore nella visualizzazione dei ristoranti recensiti.");
        }
    }

    private void aggiungiRecensione() {
        System.out.println("=== AGGIUNGI RECENSIONE ===");
        System.out.println("Per aggiungere una recensione, devi prima cercare i ristoranti.");
        System.out.println("Usa l'opzione 'Cerca un ristorante' e poi seleziona 'Aggiungi una recensione a un ristorante' dai risultati.");
    }

    private void aggiungiAiPreferiti() {
        System.out.println("=== AGGIUNGI PREFERITI ===");
        System.out.println("Per aggiungere un ristorante ai preferiti, devi prima cercare i ristoranti.");
        System.out.println("Usa l'opzione 'Cerca un ristorante' e poi seleziona 'Aggiungi un ristorante ai preferiti' dai risultati.");
    }

    /**
     * Modifica una recensione esistente dell'utente
     */
    private void modificaRecensione() {
        System.out.println("=== MODIFICA RECENSIONE ===");

        try {
            // Ottieni le recensioni dell'utente
            var recensioni = RecensioneService.getRecensioniCliente(cliente);

            if (recensioni.isEmpty()) {
                System.out.println("Non hai ancora inserito nessuna recensione.");
                return;
            }

            // Mostra le recensioni esistenti
            System.out.println("Le tue recensioni:");
            for (int i = 0; i < recensioni.size(); i++) {
                var recensione = recensioni.get(i);
                System.out.println((i + 1) + ". " + recensione);
            }

            // Selezione della recensione da modificare
            System.out.print("\nSeleziona la recensione da modificare (1-" + recensioni.size() + "): ");
            int indice;
            try {
                indice = scanner.nextInt() - 1;
                scanner.nextLine(); // Consuma il newline

                if (indice < 0 || indice >= recensioni.size()) {
                    System.out.println("Selezione non valida.");
                    return;
                }
            } catch (Exception e) {
                scanner.nextLine(); // Pulisce il buffer
                System.out.println("Input non valido.");
                return;
            }

            var recensioneDaModificare = recensioni.get(indice);

            // Modifica delle stelle
            System.out.println("\nStelle attuali: " + recensioneDaModificare.getStelle());
            System.out.print("Inserisci le nuove stelle (1-5, premi INVIO per mantenere le attuali): ");
            String inputStelle = scanner.nextLine().trim();

            int nuoveStelle = recensioneDaModificare.getStelle();
            if (!inputStelle.isEmpty()) {
                try {
                    nuoveStelle = Integer.parseInt(inputStelle);
                    if (nuoveStelle < 1 || nuoveStelle > 5) {
                        System.out.println("Numero di stelle non valido, mantengo le attuali.");
                        nuoveStelle = recensioneDaModificare.getStelle();
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Input non valido, mantengo le stelle attuali.");
                }
            }

            // Modifica del testo
            System.out.println("Testo attuale: \"" + recensioneDaModificare.getMessaggio() + "\"");
            System.out.print("Inserisci il nuovo testo (premi INVIO per mantenere l'attuale): ");
            String nuovoTesto = scanner.nextLine();

            if (nuovoTesto.isEmpty()) {
                nuovoTesto = recensioneDaModificare.getMessaggio();
            }

            Recensione recensione = new Recensione(cliente, recensioneDaModificare.getRistorante(), nuoveStelle, nuovoTesto);

            // Aggiornamento della recensione
            boolean successo = RecensioneService.modificaRecensione(
                    cliente, recensioneDaModificare.getRistorante(), recensione
            );

            if (successo) {
                System.out.println("Recensione modificata con successo!");
            } else {
                System.out.println("Errore nella modifica della recensione.");
            }

        } catch (IOException | CsvException e) {
            System.err.println("Errore durante la modifica della recensione: " + e.getMessage());
        }
    }

    /**
     * Rimuove una recensione dell'utente
     */
    private void rimuoviRecensione() {
        System.out.println("=== RIMUOVI RECENSIONE ===");

        try {
            // Ottieni le recensioni dell'utente
            var recensioni = RecensioneService.getRecensioniCliente(cliente);

            if (recensioni.isEmpty()) {
                System.out.println("Non hai ancora inserito nessuna recensione.");
                return;
            }

            // Mostra le recensioni esistenti
            System.out.println("Le tue recensioni:");
            for (int i = 0; i < recensioni.size(); i++) {
                var recensione = recensioni.get(i);
                System.out.println((i + 1) + ". " + recensione);
            }

            // Selezione della recensione da rimuovere
            System.out.print("\nSeleziona la recensione da rimuovere (1-" + recensioni.size() + "): ");
            int indice;
            try {
                indice = scanner.nextInt() - 1;
                scanner.nextLine();

                if (indice < 0 || indice >= recensioni.size()) {
                    System.out.println("Selezione non valida.");
                    return;
                }
            } catch (Exception e) {
                scanner.nextLine();
                System.out.println("Input non valido.");
                return;
            }

            var recensioneDaRimuovere = recensioni.get(indice);

            System.out.print("Sei sicuro di voler rimuovere la recensione per \"" +
                    recensioneDaRimuovere.getRistorante().getNome() + "\"? (s/N): ");
            String conferma = scanner.nextLine().trim().toLowerCase();

            if (!conferma.equals("s") && !conferma.equals("si")) {
                System.out.println("Rimozione annullata.");
                return;
            }

            // Rimozione della recensione
            boolean successo = RecensioneService.eliminaRecensione(
                    cliente, recensioneDaRimuovere.getRistorante()
            );

            if (successo) {
                System.out.println("Recensione rimossa con successo!");
            } else {
                System.out.println("Errore nella rimozione della recensione.");
            }

        } catch (IOException | CsvException e) {
            System.err.println("Errore durante la rimozione della recensione: " + e.getMessage());
        }
    }

    /**
     * Aggiunge una recensione dai risultati di ricerca
     */
    private void aggiungiRecensioneDaRisultati(ArrayList<Ristorante> risultati) {
        System.out.println("=== AGGIUNGI RECENSIONE ===");

        int indiceRistorante = selezionaRistorante(risultati);
        if (indiceRistorante < 0) {
            return;
        }

        Ristorante ristoranteSelezionato = risultati.get(indiceRistorante);

        try {
            // Verifica se l'utente ha già recensito questo ristorante
            if (RecensioneService.puoAggiungereRecensione(cliente, ristoranteSelezionato)) {
                System.out.println("Hai già recensito questo ristorante. Usa l'opzione 'Modifica recensione' per modificarla.");
                return;
            }

            // Inserimento delle stelle
            System.out.print("Inserisci il numero di stelle (1-5): ");
            int stelle;
            try {
                stelle = scanner.nextInt();
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

}
