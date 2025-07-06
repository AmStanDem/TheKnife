package servizi;

import entita.*;
import com.opencsv.exceptions.CsvException;
import io_file.GestoreFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Servizio per la gestione dei ristoranti e delle loro informazioni.
 * Tramite l'inserimento dei filtri la ricerca del ristorante diviene più specifica affinchè l'utente possa tovare
 * in modo ancor più veloce i luoghi più pertinenti alle sue esigenze.
 *
 * @author Marco Zaro
 * @author Thomas Riotto
 */

public final class RistoranteService {

    /**
     * Flag di controllo per interrompere l'esecuzione di operazioni.
     * <p>
     * Può essere utilizzato per segnalare che un processo in corso deve essere fermato,
     * ad esempio durante il caricamento dei dati o l'elaborazione di file.
     */
    public static boolean interrotto = false;

    /**
     * Costruttore privato che impedisce l'istanziamento della classe {@code RistoranteService}.
     * <p>
     * Tutti i metodi sono statici, quindi non è prevista la creazione di oggetti.
     */
    private RistoranteService() {

    }


    /**
     * Cerca ristoranti applicando una combinazione di filtri specificati.
     * Tutti i parametri sono opzionali (possono essere {@code null}) tranne la localita che è obbligatoria.
     * Se un parametro è {@code null}, il relativo filtro non viene applicato.
     *
     * <p>I filtri vengono applicati in sequenza e un ristorante deve soddisfare
     * tutti i criteri specificati per essere incluso nei risultati.</p>
     *
     * @param tipoCucina    Tipo di cucina desiderato. Se {@code null}, non viene applicato il filtro per tipologia
     * @param localita      Localita di riferimento per la ricerca (obbligatorio). Non può essere {@code null}
     * @param prezzoMinimo  Prezzo minimo in euro. Se {@code null}, non viene applicato il filtro per prezzo minimo
     * @param prezzoMassimo Prezzo massimo in euro. Se {@code null}, non viene applicato il filtro per prezzo massimo
     * @param delivery      Disponibilità servizio delivery. Se {@code null}, non viene applicato il filtro delivery.
     *                      Se {@code true}, cerca solo ristoranti con delivery. Se {@code false}, cerca solo quelli senza
     * @param prenotazione  Disponibilità prenotazione online. Se {@code null}, non viene applicato il filtro prenotazione.
     *                      Se {@code true}, cerca solo ristoranti con prenotazione. Se {@code false}, cerca solo quelli senza
     * @param mediaStelle   Media minima delle stelle richiesta (da 1.0 a 5.0). Se {@code null}, non viene applicato il filtro stelle
     * @param raggioKm      Raggio di ricerca in chilometri dalla localita specificata. Se {@code null}, non viene applicata limitazione geografica
     * @return Lista di ristoranti che soddisfano tutti i criteri di ricerca specificati.
     * Può essere una lista vuota se nessun ristorante soddisfa i criteri
     * @throws IOException              Se si verifica un errore durante il caricamento dei dati dei ristoranti
     * @throws CsvException             Se si verifica un errore durante la lettura del file CSV
     * @throws IllegalArgumentException Se la localita è {@code null} o se i parametri numerici hanno valori non validi
     *                                  (es. prezzoMinimo > prezzoMassimo, mediaStelle non compresa tra 1.0 e 5.0)
     * @see TipoCucina
     * @see Localita
     * @see Ristorante
     */
    public static ArrayList<Ristorante> cercaRistorante(TipoCucina tipoCucina, Localita localita,
                                                        Float prezzoMinimo, Float prezzoMassimo, Boolean delivery, Boolean prenotazione,
                                                        Float mediaStelle, Double raggioKm) throws IOException, CsvException {

        // Validazione parametri obbligatori
        if (localita == null) {
            throw new IllegalArgumentException("La localita è un parametro obbligatorio e non può essere null");
        }

        // Validazioni parametri numerici
        if (prezzoMinimo != null && prezzoMinimo < 0) {
            throw new IllegalArgumentException("Il prezzo minimo non può essere negativo");
        }
        if (prezzoMassimo != null && prezzoMassimo < 0) {
            throw new IllegalArgumentException("Il prezzo massimo non può essere negativo");
        }
        if (prezzoMinimo != null && prezzoMassimo != null && prezzoMinimo > prezzoMassimo) {
            throw new IllegalArgumentException("Il prezzo minimo non può essere maggiore del prezzo massimo");
        }
        if (mediaStelle != null && (mediaStelle < 1.0f || mediaStelle > 5.0f)) {
            throw new IllegalArgumentException("La media stelle deve essere compresa tra 1.0 e 5.0");
        }
        if (raggioKm != null && raggioKm <= 0) {
            throw new IllegalArgumentException("Il raggio deve essere un valore positivo");
        }

        var ristoranti = GestoreFile.caricaRistoranti();
        var risultato = new ArrayList<Ristorante>();

        for (Ristorante ristorante : ristoranti) {
            if (!filtroTipoCucina(ristorante, tipoCucina)) {
                continue;
            }
            if (!filtroLocalita(ristorante, localita, raggioKm)) {
                continue;
            }
            if (!filtroPrezzoMinimo(ristorante, prezzoMinimo)) {
                continue;
            }
            if (!filtroPrezzoMassimo(ristorante, prezzoMassimo)) {
                continue;
            }
            if (!filtroDelivery(ristorante, delivery)) {
                continue;
            }
            if (!filtroPrenotazione(ristorante, prenotazione)) {
                continue;
            }
            if (!filtroMediaStelle(ristorante, mediaStelle)) {
                continue;
            }

            risultato.add(ristorante);
        }
        return risultato;
    }


    /**
     * Cerca ristoranti applicando una combinazione di filtri specificati.
     * Tutti i parametri sono opzionali (possono essere {@code null}) tranne la localita che è obbligatoria.
     * Se un parametro è {@code null}, il relativo filtro non viene applicato.
     *
     * <p>I filtri vengono applicati in sequenza e un ristorante deve soddisfare
     * tutti i criteri specificati per essere incluso nei risultati.</p>
     *
     * @param localita      Localita di riferimento per la ricerca (obbligatorio). Non può essere {@code null}
     * @param raggioKm      Raggio di ricerca in chilometri dalla localita specificata. Se {@code null}, non viene applicata limitazione geografica
     * @return Lista di ristoranti che soddisfano tutti i criteri di ricerca specificati.
     * Può essere una lista vuota se nessun ristorante soddisfa i criteri
     * @throws IOException              Se si verifica un errore durante il caricamento dei dati dei ristoranti
     * @throws CsvException             Se si verifica un errore durante la lettura del file CSV
     * @throws IllegalArgumentException Se la localita è {@code null} o se i parametri numerici hanno valori non validi
     *                                  (es. prezzoMinimo > prezzoMassimo, mediaStelle non compresa tra 1.0 e 5.0)
     */
    public static ArrayList<Ristorante> cercaRistorante(Localita localita, Double raggioKm) throws IOException, CsvException {
        return cercaRistorante(null, localita, null, null, null, null, null, raggioKm);
    }


    /**
     * Filtra per tipologia di cucina
     * @param ristorante ristorante su cui effettuare il filtro
     * @param tipoCucina tipo di cucina
     * @return {@code true} se il filtro è passato {@code false} altrimenti
     */
    private static boolean filtroTipoCucina(Ristorante ristorante, TipoCucina tipoCucina) {
        if (tipoCucina == null) {
            return true;
        }
        return ristorante.getTipoDiCucina().equals(tipoCucina);
    }

    /**
     * Filtra per la località
     * @param ristorante ristorante su cui effettuare il filtro
     * @param localita località
     * @param raggioKm raggio km
     * @return {@code true} se il filtro è passato {@code false} altrimenti
     */
    private static boolean filtroLocalita(Ristorante ristorante, Localita localita, Double raggioKm) {
        if (ristorante == null || localita == null) {
            return false;
        }
        Localita localitaRistorante = ristorante.getLocalita();

        if (localitaRistorante == null) {
            return false;
        }

        if (raggioKm != null) {
            if (raggioKm > 0 && localita.haCoordinate() && localitaRistorante.haCoordinate()) {
                double distanza = localitaRistorante.calcolaDistanza(localita);
                return distanza != -1 && distanza <= raggioKm;
            }
            return false;
        }

        return localita.stessaZonaGeografica(localitaRistorante);
    }

    /**
     * Filtra per il prezzo minimo
     * @param ristorante ristorante su cui effettuare il filtro
     * @param prezzoMinimo prezzoMinimo
     * @return {@code true} se il filtro è passato {@code false} altrimenti
     */
    private static boolean filtroPrezzoMinimo(Ristorante ristorante, Float prezzoMinimo) {
        if (prezzoMinimo == null) {
            return true;
        }
        return ristorante.getPrezzoMedio() >= prezzoMinimo;
    }

    /**
     * Filtra per il prezzo massimo
     * @param ristorante ristorante su cui effettuare il filtro
     * @param prezzoMassimo prezzoMassimo
     * @return {@code true} se il filtro è passato {@code false} altrimenti
     */
    private static boolean filtroPrezzoMassimo(Ristorante ristorante, Float prezzoMassimo) {
        if (prezzoMassimo == null) {
            return true;
        }
        return ristorante.getPrezzoMedio() <= prezzoMassimo;
    }

    /**
     * Filtra per la prenotazione
     * @param ristorante ristorante su cui effettuare il filtro
     * @param prenotazione prenotazione
     * @return {@code true} se il filtro è passato {@code false} altrimenti
     */
    private static boolean filtroPrenotazione(Ristorante ristorante, Boolean prenotazione) {
        if (prenotazione == null) {
            return true;
        }
        return ristorante.getPrenotazione() == prenotazione;
    }

    /**
     * Filtra per il delivery
     * @param ristorante ristorante su cui effettuare il filtro
     * @param delivery delivery
     * @return {@code true} se il filtro è passato {@code false} altrimenti
     */
    private static boolean filtroDelivery(Ristorante ristorante, Boolean delivery) {
        if (delivery == null) {
            return true;
        }
        return ristorante.getDelivery() == delivery;
    }

    /**
     * Filtra per la media delle stelle
     * @param ristorante ristorante su cui effettuare il filtro
     * @param mediaStelle mediaStelle
     * @return {@code true} se il filtro è passato {@code false} altrimenti
     */
    private static boolean filtroMediaStelle(Ristorante ristorante, Float mediaStelle) {
        if (mediaStelle == null) {
            return true;
        }
        return ristorante.getMediaStelle() >= mediaStelle;
    }


    /**
     * Aggiunge un nuovo ristorante
     *
     * @param ristoratore Il ristoratore proprietario
     * @param ristorante  Il ristorante da aggiungere
     * @return {@code true} se il ristorante è stato aggiunto correttamente, {@code false} altrimenti
     * @throws IOException  Se si verifica un errore durante l'accesso al file
     * @throws CsvException Se si verifica un errore durante la gestione del CSV
     */
    public static boolean aggiungiRistorante(Ristoratore ristoratore, Ristorante ristorante)
            throws IOException, CsvException {

        if (ristoratore == null || ristorante == null) {
            return false;
        }

        // Verifica autorizzazione
        if (!ristorante.getProprietario().equals(ristoratore)) {
            return false;
        }
        if (!GestoreFile.aggiungiRistorante(ristorante)) {
            return false;
        }

        return ristoratore.aggiungiRistorante(ristorante);
    }


    /**
     * Recupera le recensioni di un ristorante
     * @param ristorante ristorante
     * @return Le recensioni del ristorante
     * @throws IOException Errore durante la lettura del file
     * @throws CsvException Errore nel parsing del file CSV
     */
    public static ArrayList<Recensione> getRecensioniRistorante(Ristorante ristorante) throws IOException, CsvException {
        return GestoreFile.caricaRecensioniRistorante(ristorante);
    }

    /**
     * Effettua la ricerca dei ristoranti interagendo con l'utente
     * @param scanner Scanner per I/O
     * @param localita Località
     * @param stop stop
     * @return I ristoranti desiderati
     * @throws IOException Errore nella lettura del file
     * @throws CsvException Errore nel parsing CSV
     */
    public static ArrayList<Ristorante> ricercaAvanzata(Scanner scanner, Localita localita, String stop) throws IOException, CsvException {
        interrotto = false;

        System.out.println("=== RICERCA AVANZATA RISTORANTI ===");

        TipoCucina tipoCucina = selezionaTipoCucina(scanner, stop);
        if (interrotto) {
            System.out.println("\nInserito STOP; Ricerca interrotta!\n");
            return new ArrayList<>();
        }

        Double raggioKm = inserisciRaggio(scanner, stop);
        if (interrotto) {
            System.out.println("\nInserito STOP; Ricerca interrotta!\n");
            return new ArrayList<>();
        }

        Float[] prezzi = inserisciFasciaPrezzo(scanner, stop);
        if (interrotto) {
            System.out.println("\nInserito STOP; Ricerca interrotta!\n");
            return new ArrayList<>();
        }

        Boolean delivery = inserisciServizio(scanner, "delivery", stop);
        if (interrotto) {
            System.out.println("\nInserito STOP; Ricerca interrotta!\n");
            return new ArrayList<>();
        }

        Boolean prenotazione = inserisciServizio(scanner, "prenotazione online", stop);
        if (interrotto) {
            System.out.println("\nInserito STOP; Ricerca interrotta!\n");
            return new ArrayList<>();
        }

        Float mediaStelle = inserisciMediaStelle(scanner, stop);
        if (interrotto) {
            System.out.println("\nInserito STOP; Ricerca interrotta!\n");
            return new ArrayList<>();
        }

        System.out.println("Ricerca in corso...");

        return RistoranteService.cercaRistorante(
                tipoCucina, localita, prezzi[0], prezzi[1],
                delivery, prenotazione, mediaStelle, raggioKm
        );
    }




    /**
     * Recupera il tipo cucina dall'utente
     * @param scanner scanner
     * @param stop stop per fermare la ricerca
     * @return Il tipocucina desiderato
     */
    private static TipoCucina selezionaTipoCucina(Scanner scanner, String stop) {
        System.out.println("\nSeleziona tipo di cucina (premi INVIO per saltare):");
        TipoCucina[] tipi = TipoCucina.values();
        for (int i = 0; i < tipi.length; i++) {
            System.out.println((i + 1) + ". " + tipi[i]);
        }
        System.out.println("0. Qualsiasi tipo");

        System.out.print("\nScelta: ");
        String input = scanner.nextLine().trim();

        if (input.equalsIgnoreCase(stop)) {
            interrotto = true;
            return null;
        }

        if (input.isBlank() || input.equals("0")) {
            return null;
        }

        try {
            int scelta = Integer.parseInt(input);
            if (scelta >= 1 && scelta <= tipi.length) {
                return tipi[scelta - 1];
            }
        } catch (NumberFormatException ignored) {}

        System.out.println("Scelta non valida, tipo di cucina ignorato.");
        return null;
    }

    /**
     * Recupera il raggio in km dall'utente
     * @param scanner scanner
     * @param stop stop per fermare la ricerca
     * @return Il raggio in km desiderato
     */
    private static Double inserisciRaggio(Scanner scanner, String stop) {
        System.out.print("\nRaggio di ricerca in km (default: 25km, massimo 25 km, premi INVIO per default): ");
        String input = scanner.nextLine().trim();

        if (input.equalsIgnoreCase(stop)) {
            interrotto = true;
            return null;
        }

        if (input.isEmpty()) return 25.0;

        try {
            double raggio = Double.parseDouble(input);
            return (raggio > 0 && raggio < 25.0) ? raggio : 25.0;
        } catch (NumberFormatException e) {
            System.out.println("Input non valido, utilizzato default 25km.");
            return 25.0;
        }
    }

    /**
     * Recupera la fascia di prezzo dall'utente
     * @param scanner scanner
     * @param stop stop per fermare la ricerca
     * @return La fascia di prezzo desiderata
     */
    private static Float[] inserisciFasciaPrezzo(Scanner scanner, String stop) {
        System.out.println("\nFascia di prezzo:");
        Float prezzoMinimo = null, prezzoMassimo = null;

        System.out.print("Prezzo minimo in € (premi INVIO per saltare): ");
        String minInput = scanner.nextLine().trim();
        if (minInput.equalsIgnoreCase(stop)) {
            interrotto = true;
            return null;
        }

        if (!minInput.isEmpty()) {
            try {
                prezzoMinimo = Float.parseFloat(minInput);
                if (prezzoMinimo < 0) prezzoMinimo = null;
            } catch (NumberFormatException ignored) {}
        }

        System.out.print("Prezzo massimo in € (premi INVIO per saltare): ");
        String maxInput = scanner.nextLine().trim();
        if (maxInput.equalsIgnoreCase(stop)) {
            interrotto = true;
            return null;
        }

        if (!maxInput.isEmpty()) {
            try {
                prezzoMassimo = Float.parseFloat(maxInput);
                if (prezzoMassimo < 0) prezzoMassimo = null;
            } catch (NumberFormatException ignored) {}
        }

        if (prezzoMinimo != null && prezzoMassimo != null && prezzoMinimo > prezzoMassimo) {
            System.out.println("Prezzo minimo maggiore del massimo, filtri ignorati.");
            prezzoMinimo = prezzoMassimo = null;
        }

        return new Float[]{prezzoMinimo, prezzoMassimo};
    }

    /**
     * Recupera il servizio voluto dall'utente
     * @param scanner scanner
     * @param nomeServizio Il nome del servizio
     * @param stop stop per fermare la ricerca
     * @return Il servizio desiderato
     */
    private static Boolean inserisciServizio(Scanner scanner, String nomeServizio, String stop) {
        System.out.println("\nServizio " + nomeServizio + ":");
        System.out.println("1. Solo con " + nomeServizio);
        System.out.println("2. Solo senza " + nomeServizio);
        System.out.println("3. Indifferente (default)");

        System.out.print("Scelta: ");
        String input = scanner.nextLine().trim();

        if (input.equalsIgnoreCase(stop)) {
            interrotto = true;
            return null;
        }

        return switch (input) {
            case "1" -> true;
            case "2" -> false;
            default -> null;
        };
    }

    /**
     * Recupera la media delle stelle dall'utente
     * @param scanner scanner
     * @param stop stop per fermare la ricerca
     * @return La media delle stelle
     */
    private static Float inserisciMediaStelle(Scanner scanner, String stop) {
        System.out.print("\nMedia stelle minima (1.0-5.0, premi INVIO per saltare): ");
        String input = scanner.nextLine().trim();

        if (input.equalsIgnoreCase(stop)) {
            interrotto = true;
            return null;
        }

        if (input.isEmpty()) return null;

        try {
            float stelle = Float.parseFloat(input);
            if (stelle >= 1.0f && stelle <= 5.0f) {
                return stelle;
            }
        } catch (NumberFormatException ignored) {}

        System.out.println("Input non valido, filtro stelle ignorato.");
        return null;
    }
}
