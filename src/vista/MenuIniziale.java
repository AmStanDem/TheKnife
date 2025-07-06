package vista;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;

import entita.*;
import com.opencsv.exceptions.CsvException;
import servizi.GeocodingService;
import servizi.RecensioneService;
import servizi.RistoranteService;
import servizi.UtenteService;

import static io_file.GestoreFile.esisteUtente;
import static servizi.UtenteService.autenticaUtente;

/**
 * Menù iniziale all'avvio del programma.
 * Permette login, registrazione, accesso guest o uscita.
 * Gestisce il flusso principale iniziale dell'applicazione.
 *
 * @author Antonio Pesavento
 * @author Alessandro Tullo
 */
public final class MenuIniziale extends Menu {

    /**
     * Servizio per la registrazione degli utenti.
     */
    private final RegistrazioneService registrazioneService;

    /**
     * Scanner per l'interazione con l'utente via terminale.
     */
    private final Scanner scanner;

    /**
     * Crea un nuovo menù iniziale.
     *
     * @param scanner I/O su terminale.
     */
    public MenuIniziale(Scanner scanner) {
        this.scanner = scanner;
        this.registrazioneService = new RegistrazioneService(scanner);
    }

    /**
     * Mostra il menù iniziale all'avvio del programma e gestisce le scelte dell'utente.
     */
    @Override
    public void mostra(){
        int selezione;
        stampaBanner();
        do {
            System.out.println("=== MENU INIZIALE ===");
            System.out.println("1. Login");
            System.out.println("2. Registrazione");
            System.out.println("3. Guest");
            System.out.println("4. Esci\n");
            System.out.println("Puoi scrivere STOP in qualsiasi momento per concludere il programma");
            System.out.println("Inserisci la funzionalità che desideri: ");

            selezione = leggiIntero();

            switch (selezione) {
                case 1:
                    Utente utente = login();
                    if (utente != null) {
                        if (utente instanceof Cliente cliente) {
                            MenuCliente menuCliente = new MenuCliente(scanner, cliente);
                            menuCliente.mostra();
                        }
                        else if (utente instanceof Ristoratore ristoratore) {
                            MenuRistoratore menuRistoratore = new MenuRistoratore(scanner, ristoratore);
                            menuRistoratore.mostra();
                        }
                    }
                    break;
                case 2:
                    try {
                        registrazione();
                    } catch (IOException | CsvException e) {
                        System.err.println("Errore durante la registrazione.");
                    }
                    break;
                case 3:
                    modalitaGuest();
                    break;
                case 4:
                    System.out.println("Arrivederci.");
                    break;
                default:
                    System.out.println("Scelta non valida, riprova");
            }
        } while(selezione != 4);
    }

    /**
     * Effettua il login chiedendo username e password, con gestione errori e interruzione.
     *
     * @return Utente autenticato oppure null se il login è interrotto o fallito.
     */
    private Utente login() {
        String username,password;
        final String stop="STOP";
        boolean corretto;
        Utente utente = null;

        do {
            corretto = true;
            System.out.print("\nInserisci l'username per fare il login:  ");
            username = scanner.nextLine().strip();

            if(username.equalsIgnoreCase(stop)){
                System.out.println("\nInserito STOP; Interruzione del login.\n");
                return null;
            }

            if(username.isBlank()) {
                System.out.println("\nL'username non puo' essere vuoto!");
                corretto = false;
            }

            try {
                if(corretto && !esisteUtente(username)){
                    System.out.println("\nNon esiste utente con questo username.");
                    corretto = false;
                }
            } catch (IOException | CsvException e) {
                throw new RuntimeException(e);
            }
        } while(!corretto);

        do {
            corretto = true;
            System.out.print("\nStai facendo il login con l'username "+username+"\nInserisci la password:  ");
            password = scanner.nextLine().strip();

            if(password.equalsIgnoreCase(stop)){
                System.out.println("\nInserito STOP; Interruzione del login.n\n");
                return null;
            }

            if(password.isBlank()){
                System.out.println("\nLa password non puo' essere vuota!");
                corretto = false;
            }

            try {
                utente = autenticaUtente(username, password);
                if(corretto &&  utente == null) {
                    System.out.println("\nPassword errata!");
                    corretto = false;
                }
            } catch (IOException | CsvException _) {
                System.err.println("Errore durante la login.");
            }
        } while(!corretto);
        return utente;
    }

    /**
     * Effettua la registrazione di un nuovo utente e salva i dati.
     *
     * @throws IOException se avviene un errore di I/O.
     * @throws CsvException se avviene un errore di lettura/scrittura CSV.
     */
    private void registrazione() throws IOException, CsvException {
        Utente nuovoUtente = registrazioneService.registraUtente();
        if (nuovoUtente == null) {
            System.out.println("Registrazione annullata.");
            return;
        }
        try {
            boolean successo = UtenteService.registraUtente(nuovoUtente);
            if (!successo) {
                System.err.println("Registrazione non riuscita!");
                return;
            }
        } catch (IOException | CsvException e) {
            System.err.println("Errore durante il salvataggio dell'utente.");
            return;
        }
        System.out.println("Registrazione completata con successo!\n");
        if (nuovoUtente instanceof Cliente cliente) {
            MenuCliente menuCliente = new MenuCliente(scanner, cliente);
            menuCliente.mostra();
        }
        else if (nuovoUtente instanceof Ristoratore ristoratore) {
            MenuRistoratore menuRistoratore = new MenuRistoratore(scanner, ristoratore);
            menuRistoratore.mostra();
        }
    }

    /**
     * Modalità guest: consente ricerca di ristoranti, recensioni anonime e cambio località.
     * Gestisce l'interazione da guest tramite menù testuale.
     */
    public void modalitaGuest() {
        System.out.println("\nSei ora in modalità guest.");
        String luogo = registrazioneService.chiediDomicilio();
        if (luogo == null) {
            System.out.println("Operazione interrotta con STOP");
            return;
        }
        double[] coords = GeocodingService.geocodeAddress(luogo);
        if (coords == null) {
            coords = GeocodingService.chiediCoordinateManuali(scanner);
            if (coords == null) {
                return;
            }
        }
        Localita localita = new Localita(coords[0], coords[1]);

        ArrayList<Ristorante> ultimiRisultati = new ArrayList<>();
        int scelta;
        do {
            System.out.println("\n--- MENU GUEST ---");
            System.out.println("1. Visualizza ristoranti vicini");
            System.out.println("2. Cerca ristorante (ricerca avanzata)");
            System.out.println("3. Cambia località");
            System.out.println("4. Visualizza recensioni ristorante");
            System.out.println("5. Esci modalità guest");
            System.out.print("Comando: ");
            scelta = leggiIntero();

            switch (scelta) {
                case 1 -> {
                    try {
                        ultimiRisultati = RistoranteService.cercaRistorante(localita, 25.0);
                        if (ultimiRisultati.isEmpty()) System.out.println("Nessun ristorante entro 25 km.");
                        else {
                            RecensioneService.caricaRecensioniPerTuttiRistoranti(ultimiRisultati);
                            ultimiRisultati.forEach(System.out::println);
                        }
                    }
                    catch (IOException | CsvException e) {
                        System.err.println("Errore durante la ricerca dei ristoranti.");
                    }
                }
                case 2 -> {
                    try {
                        ArrayList<Ristorante> ris = RistoranteService.ricercaAvanzata(scanner, localita, "stop");
                        if (ris.isEmpty()) System.out.println("Nessun ristorante trovato.");
                        else {
                            ultimiRisultati = ris;
                            RecensioneService.caricaRecensioniPerTuttiRistoranti(ultimiRisultati);
                            System.out.println("Risultati:");
                            for (int i = 0; i < ris.size(); i++) {
                                System.out.printf("%d. %s%n", i+1, ris.get(i).getNome());
                            }
                        }
                    } catch (IOException | CsvException e) {
                        System.err.println("Errore nella ricerca avanzata.");
                    }
                }
                case 3 -> {
                    System.out.print("Nuova località: ");
                    luogo = registrazioneService.chiediDomicilio();
                    if (luogo == null) {
                        System.out.println("Operazione interrotta con STOP");
                        return;
                    }
                    coords = GeocodingService.geocodeAddress(luogo);
                    if (coords == null) {
                        coords = GeocodingService.chiediCoordinateManuali(scanner);
                        if (coords == null) {
                            return;
                        }
                    }
                    localita = new Localita(coords[0], coords[1]);
                }
                case 4 -> {
                    if (ultimiRisultati.isEmpty()) {
                        System.out.println("Prima effettua una ricerca o mostra vicini.");
                    } else {
                        boolean stato;
                        System.out.print("Seleziona ristorante (1-" + ultimiRisultati.size() + "): ");
                        do {
                            stato = true;
                            int idx = leggiIntero() - 1;
                            if (idx < 0 || idx >= ultimiRisultati.size()) {
                                System.out.println("Scelta non valida. Inserire un numero: ");
                                stato = false;
                            } else {
                                try {
                                    RecensioneService.visualizzaRecensioniAnonime(ultimiRisultati.get(idx));
                                } catch (IOException | CsvException e) {
                                    System.err.println("Errore durante la visualizzazione delle recensioni in forma anonima.");
                                }
                            }
                        } while (!stato);
                    }
                }
                case 5 -> System.out.println("Sei uscito dalla modalità guest.");
                default -> System.out.println("Comando non valido.");
            }
        } while (scelta != 5);
    }

    /**
     * Legge un numero intero da terminale, consumando l'intera riga.
     *
     * @return Intero inserito correttamente.
     */
    private int leggiIntero() {
        while (true) {
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.print("Input non valido. Inserisci un numero: ");
            }
        }
    }
}
