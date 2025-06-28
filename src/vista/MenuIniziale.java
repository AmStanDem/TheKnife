package vista;

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import Entita.Cliente;
import Entita.Localita;
import Entita.Utente;
import Entita.Ristoratore;
import com.opencsv.exceptions.CsvException;
import servizi.GeocodingService;
import servizi.RistoranteService;
import servizi.UtenteService;

import static io_file.GestoreFile.esisteUtente;
import static servizi.UtenteService.autenticaUtente;

public class MenuIniziale extends Menu {
    //campi
    private final RegistrazioneService registrazioneService;
    private final Scanner scanner;
    //costruttore
    public MenuIniziale(Scanner scanner) {
       this.scanner = scanner;
       this.registrazioneService = new RegistrazioneService(scanner);
    }

    @Override
    public void mostra(){
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        int selezione;
        stampaBanner();
        do {
            System.out.println("=== MENU INIZIALE ===");
            System.out.println("1. Login");
            System.out.println("2. Registrazione");
            System.out.println("3. Guest");
            System.out.println("4. Esci");
            System.out.println("Inserisci pure la funzionalità che desideri: ");

            selezione = scanner.nextInt();

            switch (selezione) {
                case 1:
                    Utente utente = login(scanner);
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
                    modalitaGuest(scanner, r);
                    break;
                case 4:
                    System.out.println("Arrivederci.");
                    break;
                default:
                    System.out.println("Scelta non valida, riprova");
            }
        } while(selezione != 4);

    }
    
    
    public static Utente login(Scanner sc) {
        String username,password;
        final String stop="STOP";
        boolean corretto;
        
        /*
            Controllo dell'username inserito dall'utente
         */
        do {
            corretto = true;
            System.out.print("\nInserisci l'username per fare il login:  ");
            username = sc.next().strip();

            if(username.equals(stop)){
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
        
        /*
            Controllo della password inserita dall'utente
         */
        do {
            corretto = true;
            System.out.print("\nStai facendo il login con l'username "+username+"\nInserisci la password:  ");
            password = sc.next().strip();

            if(password.equals(stop)){
                System.out.println("\nInserito STOP; Interruzione del login.n\n");
                return null;
            }

            if(password.isBlank()){
                System.out.println("\nLa password non puo' essere vuota!");
                corretto = false;
            }

            try {
                if(corretto && autenticaUtente(username, password) == null) {
                    System.out.println("\nPassword errata!");
                    corretto = false;
                }
            } catch (IOException | CsvException e) {
                throw new RuntimeException(e);
            }
        } while(!corretto);
        try {
            return autenticaUtente(username, password);
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
    }

    private void registrazione() throws IOException, CsvException {
        Utente nuovoUtente = registrazioneService.registraUtente();
        if (nuovoUtente != null && !registrazioneService.STOP.equals(nuovoUtente.getUsername())) {
            System.out.println(" Registrazione completata con successo!");
            System.out.println("Benvenuto, " + nuovoUtente.getUsername() + "!");
        } else {
            System.out.println(" Registrazione annullata.");
        }
        if (nuovoUtente != null) {
            try {
                UtenteService.registraUtente(nuovoUtente);
            } catch (IOException | CsvException e) {
                System.out.println("Errore durante il salvataggio dell'utente.");
                e.printStackTrace();
                return;
            }
            if (nuovoUtente instanceof Cliente cliente) {
                MenuCliente menuCliente = new MenuCliente(scanner, cliente);
            }
            else if (nuovoUtente instanceof Ristoratore ristoratore) {
                MenuRistoratore menuRistoratore = new MenuRistoratore(scanner, ristoratore);
            }
        }
    }

    public void modalitaGuest(Scanner sc, BufferedReader r) {

        System.out.println("\nSei ora in modalita' guest.\nInserisci la tua localita', cosi' da consigliarti i ristoranti nelle vicinanze:");
        String luogo = registrazioneService.chiediDomicilio();
        if(luogo == null) {
            System.out.print("\nOperazione interrotta con STOP");
            return;
        }
        double[] coords = GeocodingService.geocodeAddress(luogo);
        Localita localita = new Localita(coords[0], coords[1]);
        try {
            RistoranteService.cercaRistorante(localita, 10.0);
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
        int selezione;
        do {
            System.out.println("\nInserisci uno dei numeri per eseguire il comando:");
            System.out.println(" 1. Inserisci una nuova localita'");
            System.out.println(" 2. Esci dalla modalita' guest");
            System.out.print("\nComando: ");
            selezione = sc.nextInt();
            switch (selezione) {
                case 1:
                    System.out.println("\nInserisci la tua localita', cosi' da consigliarti i ristoranti nelle vicinanze:");
                    System.out.print("\nLocalita':");
                    luogo = registrazioneService.chiediDomicilio();
                    if(luogo == null) {
                        System.out.print("\nOperazione interrotta con STOP");
                        return;
                    }
                    coords = GeocodingService.geocodeAddress(luogo);
                    localita = new Localita(coords[0], coords[1]);

                    break;
                case 2:
                    System.out.println("\nSei uscito dalla modalita' guest.");
                    break;
                default:
                    System.out.println("\nComando inserito non valido!");
            }
        } while(selezione != 2);

    }
}