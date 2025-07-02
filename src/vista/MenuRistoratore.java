package vista;

import Entita.*;
import com.opencsv.exceptions.CsvException;
import servizi.GeocodingService;
import servizi.RecensioneService;
import servizi.RistoranteService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Menu con le operazioni disponibili per un ristoratore.
 * @author Alessandro Tullo
 */
public final class MenuRistoratore extends Menu {
    private final Scanner scanner;
    private final Ristoratore ristoratore;

    /**
     * Crea un nuovo menu per il ristoratore.
     * @param scanner I/O su terminale.
     * @param ristoratore ristoratore registrato.
     * @throws IllegalArgumentException se i parametri non sono validi.
     */
    public MenuRistoratore(Scanner scanner, Ristoratore ristoratore) {
        validaAttributi(scanner, ristoratore);
        this.scanner = scanner;
        this.ristoratore = ristoratore;
    }

    private void validaAttributi(Scanner scanner, Ristoratore ristoratore) {
        StringBuilder errori = new StringBuilder();
        boolean errore = false;

        if (scanner == null) {
            String messaggio = "Impossibile leggere da terminale.\n";
            errori.append(messaggio);
            errore = true;
        }

        if (ristoratore == null) {
            String messaggio = "Il ristoratore deve essere valorizzato.\n";
            errori.append(messaggio);
            errore = true;
        }
        if (errore) {
            throw new IllegalArgumentException(errori.toString());
        }
    }

    /**
     * Mostra le opzioni disponibili a un ristoratore.
     */
    @Override
    public void mostra() {
        System.out.println("\n=== MENU RISTORATORE ===");
        System.out.println("Benvenuto " + ristoratore);

        int opzione;

        do {
            System.out.println("1. Aggiungere un nuovo ristorante.");
            System.out.println("2. Riepilogo delle recensioni dei ristoranti.");
            System.out.println("3. Visualizza le recensioni.");
            System.out.println("4. Logout.");

            opzione = scanner.nextInt();
            switch (opzione) {
                case 1:
                    aggiungiRistorante();
                    break;
                case 2:
                    System.out.println("\n=== RIEPILOGO ===");
                    ristoratore.visualizzaRiepilogo();
                    break;
                case 3:
                    visualizzaRecensioni();
                    break;
                case 4:
                    System.out.println("Logout effettuato. Tornando al menu principale...");
                    break;
            }
        } while (opzione != 4);
    }

    private void aggiungiRistorante() {
        System.out.println("\n=== AGGIUNGI NUOVO RISTORANTE ===");

        try {
            System.out.print("Nome del ristorante: ");
            scanner.nextLine();
            String nome = scanner.nextLine().trim();

            if (nome.isEmpty()) {
                System.out.println("Errore: Il nome del ristorante non può essere vuoto.");
                return;
            }

            System.out.println("\n=== INFORMAZIONI LOCALITÀ ===");
            System.out.print("Nazione: ");
            String nazione = scanner.nextLine().trim();

            System.out.print("Città: ");
            String citta = scanner.nextLine().trim();

            System.out.print("Indirizzo: ");
            String indirizzo = scanner.nextLine().trim();

            double[] coords = GeocodingService.geocodeAddress(nazione + " " + citta + " " + indirizzo);
            double latitudine, longitudine;

            if (coords == null) {
                coords = GeocodingService.chiediCoordinateManuali(scanner);
            }
            latitudine = coords[0];
            longitudine = coords[1];

            Localita localita = new Localita(nazione, citta, indirizzo, latitudine, longitudine);

            System.out.println("\n=== TIPO DI CUCINA ===");
            System.out.println("Tipi di cucina disponibili:");
            TipoCucina[] tipiCucina = TipoCucina.values();
            for (int i = 0; i < tipiCucina.length; i++) {
                System.out.println((i + 1) + ". " + tipiCucina[i]);
            }

            System.out.print("Seleziona il tipo di cucina (1-" + tipiCucina.length + "): ");
            int sceltaCucina = scanner.nextInt();

            if (sceltaCucina < 1 || sceltaCucina > tipiCucina.length) {
                System.out.println("Errore: Selezione non valida.");
                return;
            }

            TipoCucina tipoCucina = tipiCucina[sceltaCucina - 1];
            
            System.out.print("\nPrezzo medio (€): ");
            float prezzoMedio = scanner.nextFloat();

            if (prezzoMedio <= 0) {
                System.out.println("Errore: Il prezzo medio deve essere maggiore di zero.");
                return;
            }

            // Inserimento servizi
            System.out.println("\n=== SERVIZI DISPONIBILI ===");
            System.out.print("Servizio delivery disponibile? (s/n): ");
            String inputDelivery = scanner.next().toLowerCase();
            boolean delivery = inputDelivery.equals("s") || inputDelivery.equals("si") || inputDelivery.equals("sì");

            System.out.print("Prenotazione online disponibile? (s/n): ");
            String inputPrenotazione = scanner.next().toLowerCase();
            boolean prenotazione = inputPrenotazione.equals("s") || inputPrenotazione.equals("si") || inputPrenotazione.equals("sì");

            System.out.print("\nDescrizione del ristorante (opzionale): ");
            scanner.nextLine();
            String descrizione = scanner.nextLine().trim();

            Ristorante nuovoRistorante = new Ristorante(
                    nome,
                    localita,
                    tipoCucina,
                    delivery,
                    prenotazione,
                    prezzoMedio,
                    descrizione,
                    ristoratore
            );

            RistoranteService.aggiungiRistorante(ristoratore,  nuovoRistorante);

            System.out.println("\n✓ Ristorante '" + nome + "' aggiunto con successo!");
            System.out.println("Riepilogo:");
            System.out.println(nuovoRistorante);

        } catch (IOException | CsvException e) {
            System.err.println("Errore durante la creazione del nuovo inserimento.");
        }
    }

    private void visualizzaRecensioni() {
        System.out.println("\n=== RECENSIONI DEI TUOI RISTORANTI ===");

        ArrayList<Ristorante> ristoranti = ristoratore.getRistoranti();

        if (ristoranti == null || ristoranti.isEmpty()) {
            System.out.println("Non hai ancora ristoranti registrati.");
            return;
        }
//        try {
//            RecensioneService.caricaRecensioniPerTuttiRistoranti(ristoranti);
//        }
//        catch (IOException | CsvException e) {
//            System.err.println();
//        }


        List<Ristorante> ristorantiConRecensioni = new ArrayList<>();
        for (Ristorante ristorante : ristoranti) {
            if (ristorante.haRecensioni()) {
                ristorantiConRecensioni.add(ristorante);
            }
        }

        if (ristorantiConRecensioni.isEmpty()) {
            System.out.println("Nessuna recensione trovata per i tuoi ristoranti.");
            return;
        }

        System.out.println("Seleziona il ristorante di cui visualizzare le recensioni:");
        for (int i = 0; i < ristorantiConRecensioni.size(); i++) {
            Ristorante r = ristorantiConRecensioni.get(i);
            System.out.printf("%d. %s (%d recensioni, media: %.1f/5)%n",
                    i + 1, r.getNome(), r.getNumeroRecensioni(), r.getMediaStelle());
        }
        System.out.println("0. Torna al menu principale");

        System.out.print("Scelta: ");
        int scelta = scanner.nextInt();

        if (scelta == 0) {
            return;
        }

        if (scelta < 1 || scelta > ristorantiConRecensioni.size()) {
            System.out.println("Selezione non valida.");
            return;
        }

        Ristorante ristoranteSelezionato = ristorantiConRecensioni.get(scelta - 1);
        visualizzaRecensioniRistorante(ristoranteSelezionato);
    }

    private void visualizzaRecensioniRistorante(Ristorante ristorante) {
        int opzione;

        do {
            System.out.println("\n=== RECENSIONI DI: " + ristorante.getNome().toUpperCase() + " ===");
            System.out.printf("Media recensioni: %.1f/5 (%d recensioni totali)%n",
                    ristorante.getMediaStelle(), ristorante.getNumeroRecensioni());

            System.out.println("\n1. Visualizza tutte le recensioni");
            System.out.println("2. Visualizza solo recensioni senza risposta");
            System.out.println("3. Filtra recensioni per stelle");
            System.out.println("4. Rispondi a una recensione");
            System.out.println("5. Modifica la risposta a una recensione");
            System.out.println("0. Torna indietro");

            System.out.print("Scelta: ");
            opzione = scanner.nextInt();

            switch (opzione) {
                case 1:
                    mostraRecensioni(ristorante.getRecensioni());
                    break;
                case 2:
                    mostraRecensioni(ristorante.getRecensioniSenzaRisposta());
                    break;
                case 3:
                    filtraRecensioniPerStelle(ristorante);
                    break;
                case 4:
                    rispondiARecensione(ristorante);
                    break;
                case 5:
                    modificaRispostaRecensione(ristorante);
                    break;
                case 0:
                    System.out.println("Tornando al menu ristoranti...");
                    break;
                default:
                    System.out.println("Opzione non valida.");
            }
        } while (opzione != 0);
    }

    private void mostraRecensioni(List<Recensione> recensioni) {
        if (recensioni.isEmpty()) {
            System.out.println("Nessuna recensione da visualizzare.");
            return;
        }

        System.out.println("\n" + "=".repeat(60));
        for (int i = 0; i < recensioni.size(); i++) {
            Recensione r = recensioni.get(i);
            System.out.printf("%d. %s%n", i + 1, formatRecensione(r));
            System.out.println("-".repeat(60));
        }

        System.out.println("Premi INVIO per continuare...");
        scanner.nextLine();

    }

    private void filtraRecensioniPerStelle(Ristorante ristorante) {
        System.out.print("Inserisci il numero di stelle (1-5): ");
        int stelle = scanner.nextInt();

        if (stelle < 1 || stelle > 5) {
            System.out.println("Numero di stelle non valido.");
            return;
        }

        ArrayList<Recensione> recensioniFiltrate = ristorante.getRecensioniPerStelle(stelle);

        if (recensioniFiltrate.isEmpty()) {
            System.out.println("Nessuna recensione trovata con " + stelle + " stelle.");
            return;
        }

        System.out.println("\nRecensioni con " + stelle + " stelle:");
        mostraRecensioni(recensioniFiltrate);
    }

    /**
     * Gestisce la risposta a una recensione.
     */
    private void rispondiARecensione(Ristorante ristorante) {
        ArrayList<Recensione> recensioniSenzaRisposta = ristorante.getRecensioniSenzaRisposta();

        if (recensioniSenzaRisposta.isEmpty()) {
            System.out.println("Tutte le recensioni hanno già una risposta.");
            return;
        }

        System.out.println("\nRecensioni senza risposta:");
        for (int i = 0; i < recensioniSenzaRisposta.size(); i++) {
            Recensione r = recensioniSenzaRisposta.get(i);
            System.out.printf("%d. %s%n", i + 1, formatRecensione(r));
            System.out.println("-".repeat(40));
        }

        System.out.print("Seleziona il numero della recensione a cui rispondere (0 per annullare): ");
        int scelta = scanner.nextInt();

        if (scelta == 0) {
            return;
        }

        if (scelta < 1 || scelta > recensioniSenzaRisposta.size()) {
            System.out.println("Selezione non valida.");
            return;
        }

        Recensione recensioneDaRispondere = recensioniSenzaRisposta.get(scelta - 1);

        System.out.println("\nRecensione selezionata:");
        System.out.println(formatRecensione(recensioneDaRispondere));

        System.out.print("\nInserisci la tua risposta: ");
        scanner.nextLine();
        String testoRisposta = scanner.nextLine().trim();

        try {
            boolean successo = RecensioneService.rispondiARecensione(ristoratore, ristorante, recensioneDaRispondere, testoRisposta);
            if (successo) {
                System.out.println("✓ Risposta aggiunta con successo!");
            }
            else {
                System.err.println("Errore nell'aggiungere la risposta.");
            }
        } catch (Exception e) {
            System.err.println("Errore nell'aggiungere la risposta.");
        }
    }

    private void modificaRispostaRecensione(Ristorante ristorante) {
        try {
            RecensioneService.caricaRecensioniRistorante(ristorante);
        }
        catch (IOException | CsvException e ) {
            System.err.println("Errore nel caricamento delle recensioni.");
            return;
        }

        ArrayList<Recensione> recensioniConRisposta = ristorante.getRecensioniConRisposta();

        if (recensioniConRisposta.isEmpty()) {
            System.out.println("Nessuna recensione con risposta trovata per questo ristorante.");
            return;
        }

        System.out.println("\nRecensioni con risposta che puoi modificare:");
        for (int i = 0; i < recensioniConRisposta.size(); i++) {
            Recensione r = recensioniConRisposta.get(i);
            System.out.printf("%d. %s%n", i + 1, formatRecensione(r));
            System.out.println("-".repeat(40));
        }

        System.out.print("Seleziona il numero della recensione di cui modificare la risposta (0 per annullare): ");
        int scelta = scanner.nextInt();

        if (scelta == 0) {
            return;
        }

        if (scelta < 1 || scelta > recensioniConRisposta.size()) {
            System.out.println("Selezione non valida.");
            return;
        }

        Recensione recensioneDaModificare = recensioniConRisposta.get(scelta - 1);

        System.out.println("\nRecensione selezionata:");
        System.out.println(formatRecensione(recensioneDaModificare));

        System.out.println("\nRisposta attuale: " + recensioneDaModificare.getRispostaRistoratore());

        System.out.println("\nOpzioni:");
        System.out.println("1. Modifica il testo della risposta");
        System.out.println("2. Elimina la risposta");
        System.out.println("0. Annulla");

        System.out.print("Scelta: ");
        int opzioneModifica = scanner.nextInt();

        switch (opzioneModifica) {
            case 1:
                modificaTestoRisposta(ristorante, recensioneDaModificare);
                break;
            case 2:
                eliminaRispostaRecensione(ristorante, recensioneDaModificare);
                break;
            case 0:
                System.out.println("Operazione annullata.");
                break;
            default:
                System.out.println("Opzione non valida.");
        }
    }

    /**
     * Modifica il testo della risposta a una recensione.
     */
    private void modificaTestoRisposta(Ristorante ristorante, Recensione recensione) {
        System.out.print("\nInserisci il nuovo testo della risposta: ");
        scanner.nextLine();
        String nuovoTestoRisposta = scanner.nextLine().trim();

        if (nuovoTestoRisposta.isEmpty()) {
            System.out.println("Errore: Il testo della risposta non può essere vuoto.");
            return;
        }

        try {
            boolean successo = RecensioneService.modificaRispostaRecensione(
                    ristoratore, ristorante, recensione, nuovoTestoRisposta
            );

            if (successo) {
                System.out.println("✓ Risposta modificata con successo!");
                RecensioneService.caricaRecensioniRistorante(ristorante);
            } else {
                System.err.println("Errore nella modifica della risposta.");
            }
        } catch (IOException | CsvException e) {
            System.err.println("Errore durante la modifica della risposta: " + e.getMessage());
        }
    }

    private void eliminaRispostaRecensione(Ristorante ristorante, Recensione recensione) {
        System.out.print("Sei sicuro di voler eliminare la risposta? (s/n): ");
        String conferma = scanner.next().toLowerCase();

        if (!conferma.equals("s") && !conferma.equals("si") && !conferma.equals("sì")) {
            System.out.println("Operazione annullata.");
            return;
        }

        try {
            boolean successo = RecensioneService.modificaRispostaRecensione(
                    ristoratore, ristorante, recensione, ""
            );

            if (successo) {
                System.out.println("✓ Risposta eliminata con successo!");
                RecensioneService.caricaRecensioniRistorante(ristorante);
            } else {
                System.err.println("Errore nell'eliminazione della risposta.");
            }
        } catch (IOException | CsvException e) {
            System.err.println("Errore durante l'eliminazione della risposta.");
        }
    }

    /**
     * Formatta una recensione per la visualizzazione.
     */
    private String formatRecensione(Recensione recensione) {
        return recensione.toString();
    }
}
