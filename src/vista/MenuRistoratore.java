package vista;

import entita.*;
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
 * Permette di aggiungere ristoranti, visualizzare recensioni,
 * rispondere/modificare/eliminare risposte a recensioni e vedere riepiloghi.
 * Usa il terminale per l'interazione.
 *
 * @author Alessandro Tullo
 */
public final class MenuRistoratore extends Menu {
    /**
     * Scanner per la lettura da terminale.
     */
    private final Scanner scanner;
    /**
     * Il ristoratore autenticato.
     */
    private final Ristoratore ristoratore;
    private final String stop = "stop";

    /**
     * Crea un nuovo menu per il ristoratore.
     * @param scanner     Scanner per input da terminale.
     * @param ristoratore Oggetto Ristoratore autenticato.
     * @throws IllegalArgumentException se i parametri non sono validi.
     */
    public MenuRistoratore(Scanner scanner, Ristoratore ristoratore) {
        validaAttributi(scanner, ristoratore);
        this.scanner = scanner;
        this.ristoratore = ristoratore;
    }

    /**
     * Valida che gli attributi scanner e ristoratore non siano null.
     *
     * @param scanner     Scanner per input da terminale.
     * @param ristoratore Oggetto Ristoratore autenticato.
     * @throws IllegalArgumentException se uno dei due è null.
     */
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
     * Mostra il menu principale con le opzioni disponibili per il ristoratore.
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
            System.out.print("Selezione: ");

            opzione = leggiIntero();
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

    /**
     * Permette al ristoratore di inserire un nuovo ristorante,
     * chiedendo le informazioni necessarie e geocodificando l'indirizzo.
     */
    private void aggiungiRistorante() {
        System.out.println("\n=== AGGIUNGI NUOVO RISTORANTE ===");

        try {

            boolean stato;
            String nome;
            do {
                stato = true;

                System.out.print("Nome del ristorante: ");
                nome = scanner.nextLine().trim();

                if (nome.equalsIgnoreCase(stop)) {
                    System.out.println("\nInserito STOP; Inserimento nuovo ristorante interrotto\n");
                    return;
                }

                if (nome.isBlank()) {
                    System.out.println("Errore: Il nome del ristorante non può essere vuoto.");
                    stato = false;
                }

            } while(!stato);


            System.out.println("\n=== INFORMAZIONI LOCALITÀ ===");

            System.out.print("Nazione: ");
            String nazione = scanner.nextLine().trim();
            if (nazione.equalsIgnoreCase(stop)) {
                System.out.println("\nInserito STOP; Inserimento nuovo ristorante interrotto\n");
                return;
            }

            System.out.print("Città: ");
            String citta = scanner.nextLine().trim();
            if (citta.equalsIgnoreCase(stop)) {
                System.out.println("\nInserito STOP; Inserimento nuovo ristorante interrotto\n");
                return;
            }

            System.out.print("Indirizzo: ");
            String indirizzo = scanner.nextLine().trim();
            if (indirizzo.equalsIgnoreCase(stop)) {
                System.out.println("\nInserito STOP; Inserimento nuovo ristorante interrotto\n");
                return;
            }

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

            String sceltaCucina;
            do {
                stato = true;

                System.out.print("\nSeleziona il tipo di cucina (1-" + tipiCucina.length + "): ");
                sceltaCucina = scanner.nextLine().strip();

                if(sceltaCucina.equalsIgnoreCase(stop)) {
                    System.out.println("\nInserito STOP; Inserimento nuovo ristorante interrotto\n");
                    return;
                }

                for(char c : sceltaCucina.toCharArray()) {
                    if(!Character.isDigit(c)) {
                        System.out.println("Errore: inseriti caratteri anomali nella selezione");
                        stato = false;
                        break;
                    }
                }

                if (stato && (Integer.parseInt(sceltaCucina) < 1 || Integer.parseInt(sceltaCucina) > tipiCucina.length)) {
                    System.out.println("Errore: Selezione non valida.");
                    stato = false;
                }

            } while(!stato);

            TipoCucina tipoCucina = tipiCucina[Integer.parseInt(sceltaCucina) - 1];


            String prezzoMedio;
            do {
                stato = true;

                System.out.print("\nPrezzo medio (€): ");
                prezzoMedio = scanner.nextLine().strip();

                if(prezzoMedio.equalsIgnoreCase(stop)) {
                    System.out.println("\nInserito STOP; Inserimento nuovo ristorante interrotto\n");
                    return;
                }

                boolean virgola = false;
                for(int i=0; i<prezzoMedio.length(); i++) {
                    char c = prezzoMedio.charAt(i);
                    if(!Character.isDigit(c) && c != ',' && c != '.') {
                        System.out.println("Errore: inseriti caratteri anomali nel prezzo medio");
                        stato = false;
                        break;
                    } else if(c == ',') {
                        if(virgola) {
                            System.out.println("Errore: non ci possono essere piu' virgole nel prezzo medio");
                            stato = false;
                            break;
                        }
                        prezzoMedio = prezzoMedio.replace(',','.');
                        virgola = true;
                    } else if(c == '.') {
                        if(virgola) {
                            System.out.println("Errore: non ci possono essere piu' virgole nel prezzo medio");
                            stato = false;
                            break;
                        }
                        virgola = true;
                    }
                }

                if (stato && Float.parseFloat(prezzoMedio) <= 0) {
                    System.out.println("Errore: Il prezzo medio deve essere maggiore di zero.");
                    stato = false;
                }

            } while(!stato);


            // Inserimento servizi
            System.out.println("\n=== SERVIZI DISPONIBILI ===");
            System.out.print("Servizio delivery disponibile? (s/n): ");
            String inputDelivery = scanner.next().toLowerCase();

            if (inputDelivery.equalsIgnoreCase(stop)) {
                System.out.println("\nInserito STOP; Inserimento nuovo ristorante interrotto\n");
                return;
            }
            boolean delivery = inputDelivery.equals("s") || inputDelivery.equals("si") || inputDelivery.equals("sì");

            System.out.print("Prenotazione online disponibile? (s/n): ");
            String inputPrenotazione = scanner.next().toLowerCase();
            if (inputPrenotazione.equalsIgnoreCase(stop)) {
                System.out.println("\nInserito STOP; Inserimento nuovo ristorante interrotto\n");
                return;
            }
            boolean prenotazione = inputPrenotazione.equals("s") || inputPrenotazione.equals("si") || inputPrenotazione.equals("sì");

            System.out.print("\nDescrizione del ristorante (opzionale): ");
            String descrizione = scanner.nextLine().trim();
            if (descrizione.equalsIgnoreCase(stop)) {
                System.out.println("\nInserito STOP; Inserimento nuovo ristorante interrotto\n");
                return;
            }

            Ristorante nuovoRistorante = new Ristorante(
                    nome,
                    localita,
                    tipoCucina,
                    delivery,
                    prenotazione,
                    Float.parseFloat(prezzoMedio),
                    descrizione,
                    ristoratore
            );

            boolean successo = RistoranteService.aggiungiRistorante(ristoratore,  nuovoRistorante);

            if (successo) {
                System.out.println("\n✓ Ristorante '" + nome + "' aggiunto con successo!");
                System.out.println("Riepilogo:");
                System.out.println(nuovoRistorante);
            }
            else
                System.err.println("Errore durante l'inserimento di un ristorante!");

        } catch (IOException | CsvException e) {
            System.err.println("Errore durante la creazione del nuovo inserimento.");
        }
    }

    /**
     * Mostra i ristoranti con recensioni disponibili del ristoratore,
     * permettendo di selezionarne uno per approfondire.
     */
    private void visualizzaRecensioni() {
        System.out.println("\n=== RECENSIONI DEI TUOI RISTORANTI ===");

        ArrayList<Ristorante> ristoranti = ristoratore.getRistoranti();

        if (ristoranti == null || ristoranti.isEmpty()) {
            System.out.println("Non hai ancora ristoranti registrati.");
            return;
        }


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
        int scelta = leggiIntero();

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

    /**
     * Mostra le recensioni di un ristorante specifico, con varie opzioni
     * (filtraggio, risposta, modifica).
     *
     * @param ristorante Il ristorante selezionato.
     */
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
            opzione = leggiIntero();

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

    /**
     * Stampa una lista di recensioni a schermo, una per riga.
     *
     * @param recensioni Lista di recensioni da mostrare.
     */
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

    /**
     * Filtra le recensioni di un ristorante per numero di stelle.
     *
     * @param ristorante Il ristorante di cui filtrare le recensioni.
     */
    private void filtraRecensioniPerStelle(Ristorante ristorante) {
        boolean stato;
        int filtro;

        do{
            stato = true;

            System.out.print("Inserisci il numero di stelle (1-5): ");
            String stelle = scanner.nextLine().strip();

            if(stelle.equalsIgnoreCase(stop)) {
                System.out.println("\nInserito STOP; Filtro recensioni interrotto\n");
                return;
            }

            for(char c : stelle.toCharArray()) {
                if(!Character.isDigit(c)) {
                    System.out.println("Errore: inseriti caratteri anomali nel filtro");
                    stato = false;
                    break;
                }
            }
            filtro = Integer.parseInt(stelle);

            if (filtro < 1 || filtro > 5) {
                System.out.println("Numero di stelle non valido.");
                stato = false;
            }

        } while(!stato);


        ArrayList<Recensione> recensioniFiltrate = ristorante.getRecensioniPerStelle(filtro);

        if (recensioniFiltrate.isEmpty()) {
            System.out.println("Nessuna recensione trovata con " + filtro + " stelle.");
            return;
        }

        System.out.println("\nRecensioni con " + filtro + " stelle:");
        mostraRecensioni(recensioniFiltrate);
    }

    /**
     * Permette al ristoratore di rispondere a una recensione senza risposta.
     *
     * @param ristorante Il ristorante a cui appartiene la recensione.
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

        boolean stato;
        int scelta;
        do {
            stato = true;
            System.out.print("Seleziona il numero della recensione a cui rispondere (0 per annullare): ");
            scelta = leggiIntero();

            if (scelta == 0) {
                return;
            }

            if (scelta < 1 || scelta > recensioniSenzaRisposta.size()) {
                System.out.println("Selezione non valida.");
                stato = false;
            }

        } while(!stato);


        Recensione recensioneDaRispondere = recensioniSenzaRisposta.get(scelta - 1);

        System.out.println("\nRecensione selezionata:");
        System.out.println(formatRecensione(recensioneDaRispondere));

        System.out.print("\nInserisci la tua risposta: (STOP per uscire)");
        String testoRisposta = scanner.nextLine().trim();

        if (testoRisposta.equalsIgnoreCase("STOP")) {
            System.out.println("\nInserito STOP; Risposta alla recensione interrotta\n");
            return;
        }

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

    /**
     * Permette al ristoratore di modificare o eliminare una risposta a una recensione.
     *
     * @param ristorante Il ristorante a cui appartiene la recensione.
     */
    private void modificaRispostaRecensione(Ristorante ristorante) {

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

        int scelta;
        boolean stato;
        do {
            stato = true;
            System.out.print("Seleziona il numero della recensione di cui modificare la risposta (0 per annullare): ");
            scelta = leggiIntero();

            if (scelta == 0) {
                return;
            }

            if (scelta < 1 || scelta > recensioniConRisposta.size()) {
                System.out.println("Selezione non valida.");
                stato = false;
            }

        } while (!stato);

        Recensione recensioneDaModificare = recensioniConRisposta.get(scelta - 1);

        System.out.println("\nRecensione selezionata:");
        System.out.println(formatRecensione(recensioneDaModificare));

        System.out.println("\nRisposta attuale: " + recensioneDaModificare.getRispostaRistoratore());

        System.out.println("\nOpzioni:");
        System.out.println("1. Modifica il testo della risposta");
        System.out.println("2. Elimina la risposta");
        System.out.println("0. Annulla");

        int opzioneModifica;
        do {
            System.out.print("Scelta: ");
            opzioneModifica = leggiIntero();

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
        } while (opzioneModifica < 0 || opzioneModifica > 2);
    }

    /**
     * Modifica il testo della risposta del ristoratore a una recensione.
     *
     * @param ristorante     Il ristorante associato alla recensione.
     * @param recensione     La recensione da modificare.
     */
    private void modificaTestoRisposta(Ristorante ristorante, Recensione recensione) {
        System.out.print("\nInserisci il nuovo testo della risposta: ");
        String nuovoTestoRisposta = scanner.nextLine().trim();

        if (nuovoTestoRisposta.equalsIgnoreCase("STOP")) {
            System.out.println("\nInserito STOP; Modifica della risposta della recensione interrotta\n");
            return;
        }

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

    /**
     * Elimina la risposta del ristoratore da una recensione.
     *
     * @param ristorante Il ristorante associato.
     * @param recensione La recensione da cui rimuovere la risposta.
     */
    private void eliminaRispostaRecensione(Ristorante ristorante, Recensione recensione) {
        System.out.print("Sei sicuro di voler eliminare la risposta? (s/n): ");
        String conferma = scanner.nextLine().toLowerCase().strip();

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
     * Formatta una recensione in formato leggibile per il terminale.
     *
     * @param recensione La recensione da formattare.
     * @return Stringa formattata.
     */
    private String formatRecensione(Recensione recensione) {
        return recensione.toString();
    }

    private int leggiIntero() {
        while (true) {
            String line = scanner.nextLine().strip();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.print("Input non valido. Inserisci un numero!");

            }
        }
    }
}
