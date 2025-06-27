package vista;

import java.time.LocalDate;
import java.time.LocalDateTime;
// import java.time.temporal.ValueRange;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import Entita.Cliente;
import Entita.Utente;
import Entita.Ristoratore;
import com.opencsv.exceptions.CsvException;
import servizi.GeocodingService;
import servizi.UtenteService;

import static io_file.GestoreFile.esisteUtente;
import static servizi.UtenteService.autenticaUtente;

public class MenuIniziale extends Menu {
    //campi
    private RegistrazioneService registrazioneService;
    private Scanner sc;
    //costruttore
    public MenuIniziale() {
       this.sc = new Scanner(System.in);
       this.registrazioneService = new RegistrazioneService(sc);
    }

    @Override
    public void mostra(){
        var sc = new Scanner(System.in);
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        int selezione;
        do {
            selezione = 0;
            System.out.println("Menu Iniziale: ");
            System.out.println("1. Login");
            System.out.println("2. Registrazione");
            System.out.println("3. Guest");
            System.out.println("4. Esci");

            selezione = sc.nextInt();

            switch (selezione) {
                case 1:
                    login(sc);
                    break;
                case 2:
                    registrazione(sc);
                    break;
                case 3:
                    modalitaGuest(sc, r);
                    break;
                case 4:
                    System.exit(0);
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
    }

    public static void registrazione(Scanner sc) {
        /*
        *nome – Nome dell’utente (in chiaro, con qualsiasi formato)
        cognome – Cognome dell’utente (in chiaro)
        username – Username scelto per l’accesso
        password – Password in chiaro da cifrare
        dataNascita – Data di nascita dell’utente
        luogoDomicilio – Luogo di domicilio (in chiaro)
        * */
        
        /*
            _____________________________________________________________________________________________
            Variabili varie
         */
        String nome,cognome,username,password,luogoDomicilio,via,civico,citta,anno,mese,giorno;
        final String stop="STOP";
        LocalDate dataDiNascita;
        int i,selezione, aaaa=0, mm=0 ,gg=0; // TMCH
        char c;
        boolean stato,bisestile=false;
        
        // Inizio
        System.out.println("\nBenvenuto nella registrazione utente.\nIn ogni momento puoi digitare STOP per" +
                " interrompere il processo.");
        
        /*
            _____________________________________________________________________________________________
            Inserimento del nome nella registrazione e controllo dei parametri
         */
        System.out.println("\nInserisci il nome del cliente");
        do {

            stato=true;
            System.out.print("Il nome deve essere composto da solo lettere: ");
            nome = sc.next().strip(); // Input

            // Se l'utente scrive STOP nel campo input, la registrazione si interromperà;
            // Nessun dato rimarrà salvato e tutte le informazioni precedentemente inserite saranno scartate
            if(nome.equals(stop)) {
                System.out.println("\nInserito STOP; Interruzione della registrazione.\n");
                return;
            }

            if(nome.isBlank()){
                System.out.println("\nIl nome non puo' essere vuoto!");
                stato = false;
            }

            for (i = 0; i < nome.length(); i++) {
                c = nome.charAt(i);
                if (!Character.isLetter(c)){
                    System.out.println("\nHai inserito un carattere che non e' una lettera: "+ c + " in posizione " + (i+1));
                    stato = false;
                    break;
                }
            }

        } while (!stato);
        
        /*
            _____________________________________________________________________________________________
            Inserimento del cognome nella registrazione e controllo dei parametri
         */
        System.out.println("\nInserisci il cognome del cliente");
        do {
            stato=true;

            System.out.print("Il cognome deve essere composto da solo lettere: ");
            cognome = sc.next().strip();

            // Se l'utente scrive STOP nel campo input, la registrazione si interromperà
            if(cognome.equals(stop)) {
                System.out.println("\nInserito STOP; Interruzione della registrazione.\n");
                return;
            }

            if(cognome.isBlank()){
                System.out.println("\nIl cognome non puo' essere vuoto!");
                stato = false;
            }

            for(i=0; i < cognome.length(); i++){
                c=cognome.charAt(i);
                if(!Character.isLetter(c)){
                    System.out.println("\nHai inserito un carattere che non e' una lettera: "+ c + " in posizione " + (i+1));
                    stato = false;
                    break;
                }
            }

        } while (!stato);

        /*
            _____________________________________________________________________________________________
            Inserimento dell'username nella registrazione e controllo dei parametri
         */
        System.out.println("Inserisci l'username del cliente");
        do {
            stato=true;

            System.out.print("L'username deve essere composto da caratteri alfanumerici e lungo da 3 a 16 caratteri: ");
            username = sc.next().strip(); // Input

            // Se l'utente scrive STOP nel campo input, la registrazione si interromperà
            if(username.equals(stop)) {
                System.out.println("\nInserito STOP; Interruzione della registrazione.\n");
                return;
            }

            if(username.isBlank()){
                System.out.println("\nL'username non puo' essere vuoto!");
                stato = false;
            }

            // L'username deve essere lungo almeno 2 caratteri e massimo 16 caratteri
            if(stato && (username.length()<2||username.length()>16)){
                System.out.println("\nL'username inserito non rispetta i caratteri di lunghezza");
                stato = false;
            }

            // L'username deve avere solo caratteri alfanumerici e underscore
            for(i=0; stato && i<username.length(); i++){
                c=username.charAt(i);
                if(!Character.isLetterOrDigit(c)&&c!='_'){
                    System.out.println("\nHai inserito un carattere che non e' alfanumerico: "+ c + " in posizione " + (i+1));
                    stato = false;
                    break;
                }
            }

            // Ricerca di un username già esistente
            try {
                if(stato && esisteUtente(username)){
                    System.out.println("\nL'username inserito è gia' utilizzato!");
                    stato = false;
                }
            } catch (IOException | CsvException e) {
                throw new RuntimeException(e);
            }

        } while(!stato);

        /*
            _____________________________________________________________________________________________
            Inserimento della password nella registrazione e controllo dei parametri
         */
        System.out.println("Inserisci la password del cliente");
        do {
            stato = true;

            System.out.println("Inserisci la password del cliente:");
            password = sc.next().strip();

            // Se l'utente scrive STOP nel campo input, la registrazione si interromperà
            if (password.equals(stop)) {
                System.out.println("\nInserito STOP; interruzione della registrazione.\n");
                return;
            }

            if (password.isBlank()) {
                System.out.println("La password non può essere vuota.");
                stato = false;
            }

            if (stato && (password.length() < 8 || password.length() > 16)) {
                System.out.println("La password deve essere lunga tra 8 e 16 caratteri.");
                stato = false;
            }

            /*
                La password deve contenere almeno una lettera, un numero, un carattere speciale e
                non contenere spazi, altrimenti verrà rifiutata
             */
            if (stato) {
                boolean contieneLettera = false;
                boolean contieneNumero = false;
                boolean contieneSpeciale = false;
                boolean contieneSpazio = false;

                for (char ch : password.toCharArray()) {
                    if (Character.isLetter(ch)) contieneLettera = true;
                    if (Character.isDigit(ch)) contieneNumero = true;
                    if (!Character.isLetterOrDigit(ch) && !Character.isWhitespace(ch)) contieneSpeciale = true;
                    if (Character.isWhitespace(ch)) contieneSpazio = true;

                    if (!contieneLettera) {
                        System.out.println("La password deve contenere almeno una lettera.");
                        stato = false;
                    }

                    if (!contieneNumero) {
                        System.out.println("La password deve contenere almeno un numero.");
                        stato = false;
                    }

                    if (!contieneSpeciale) {
                        System.out.println("La password deve contenere almeno un carattere speciale.");
                        stato = false;
                    }

                    if (contieneSpazio) {
                        System.out.println("La password non deve contenere spazi.");
                        stato = false;
                    }

                    if(!stato)
                        break;
                }
            }

        }while (!stato);

        /*
            _____________________________________________________________________________________________
            Inserimento della data di nascita nella registrazione
         */
        System.out.println("Inserisci la data di nascita in numero a cifre");

        /*
            Inserimento dell'anno
         */
        do {
            stato=true;

            System.out.print("Inserisci l'anno di nascita: ");
            anno = sc.next().strip();

            // Se l'utente scrive STOP nel campo input, la registrazione si interromperà
            if(anno.equals(stop)){
                System.out.println("\nInserito STOP; interruzione della registrazione.\n");
                return;
            }

            if(anno.isBlank()){
                System.out.println("\nL'anno di nascita non puo' essere vuoto!");
                stato = false;
            }

            // Controllo per caratteri non numerici
            for(i=0; stato && i<anno.length() ; i++){
                c=anno.charAt(i);

                if(!Character.isDigit(c)) {
                    System.out.println("\nHai inserito un carattere che non e' numerico: "+ c + " in posizione " + (i+1));
                    stato = false;
                    break;
                }
            }

            // Controllo di validità età e se l'anno è bisestile
            if(stato){
                aaaa = Integer.parseInt(anno);

                if (aaaa < 1900 || aaaa > LocalDateTime.now().getYear()) {
                    System.out.println("\nL'anno inserito e' invalido!");
                    stato = false;
                } else if (aaaa % 400 == 0 || (aaaa % 4 == 0 && aaaa % 100 != 0))
                    bisestile = true;

            }

        }while(!stato);

        /*
            Inserimento del mese di nascita
         */
        do {
            stato=true;
            System.out.print("Inserisci il mese di nascita: ");
            mese = sc.next().strip();

            // Se l'utente scrive STOP nel campo input, la registrazione si interromperà
            if(mese.equals(stop)){
                System.out.println("\nInserito STOP; interruzione della registrazione.\n");
                return;
            }

            if(mese.isBlank()){
                System.out.println("Il mese di nascita non puo' essere vuoto!");
                stato = false;
            }

            // Controllo di caratteri non numerici
            for(i=0; stato && i<mese.length() ; i++){
                c=mese.charAt(i);
                if(!Character.isDigit(c)) {
                    System.out.println("\nHai inserito un carattere che non e' numerico: "+ c +" in posizione "+ (i+1));
                    stato = false;
                    break;
                }
            }

            // Controllo di validità del mese
            if(stato){
                mm = Integer.parseInt(mese);

                if(mm<1 || mm>12){
                    System.out.println("\nIl mese inserito e' invalido!");
                    stato = false;
                }
            }

        } while(!stato);

        /*
            Inserimento del giorno di nascita
         */
        do {
            stato = true;
            System.out.print("Inserisci il giorno di nascita: ");
            giorno = sc.next().strip();

            // Se l'utente scrive STOP nel campo input, la registrazione si interromperà
            if(giorno.equals(stop)){
                System.out.println("\nInserito STOP; interruzione della registrazione.\n");
                return;
            }

            if(giorno.isBlank()){
                System.out.println("\nIl giorno di nascita non pu' essere vuoto!");
                stato = false;
            }

            // Controllo di caratteri non numerici
            for(i=0; stato && i<giorno.length() ; i++){
                c=giorno.charAt(i);
                if(!Character.isDigit(c)) {
                    System.out.println("\nHai inserito un carattere che non e' numerico: "+ c +" in posizione "+ (i+1));
                    stato = false;
                    break;
                }
            }

            // Controllo della validità del giorno di nascita in base al mese e se l'anno è bisestile
            if(stato){
                gg = Integer.parseInt(giorno);

                if(gg<1){
                    System.out.println("\nIl giorno inserito e' invalido!");
                    stato = false;
                } else if((mm==4 || mm==6 || mm == 9 || mm == 11) && gg > 30){
                    System.out.println("\nE' stata inserita la data '"+gg+"', ma il mese di nascita ha solo 30 giorni!");   // Mese di 30 giorni
                    stato = false;
                } else if(mm==2 && bisestile && gg > 29){
                    System.out.println("\nE' stata inserita la data '"+gg+"', ma il mese di febbraio bisestile ha solo 29 giorni!"); // Febbraio bisestile, 29 giorni
                    stato = false;
                } else if(mm==2 && !bisestile && gg > 28){
                    System.out.println("\nE' stata inserita la data '"+gg+"', ma il mese di febbraio ha solo 28 giorni!");  // Febbraio non bisestile, 28 giorni
                    stato = false;
                } else if(gg > 31){
                    System.out.println("\nE' stata inserita la data '"+gg+"', ma il mese di nascita ha solo 31 giorni!");   // Mese di 31 giorni
                    stato = false;
                }
            }

        } while(!stato);
        // Costruzione della data di nascita in formato LocalDate dopo tutti i controlli
        dataDiNascita = LocalDate.of(aaaa,mm,gg);


        /*
            _____________________________________________________________________________________________
            Inserimento del luogo di domicilio nella registrazione e controllo dei parametri
         */
        System.out.println("Inserisci il luogo di domicilio");

        /*
            Inserimento della via di domicilio, senza numero civico
         */
        do {
            do {
                System.out.print("\nInserisci la via di domicilio, senza numero civico e attenzione agli spazi: ");
                via = sc.next().strip();

                // Se l'utente scrive STOP nel campo input, la registrazione si interromperà
                if(via.equals(stop)){
                    System.out.println("\nInserito STOP; Interruzione della registrazione.\n");
                    return;
                }

                if(via.isBlank())
                    System.out.println("\nLa via di domicilio non puo' essere vuota!");

            } while (via.isBlank());

            /*
                Inserimento del numero civico
                Sono ammessi numeri civici misti (Es. 1A, 5B, ecc...), ma in caso ci sia un numero misto
                "irregolare", cioè con numeri anche dopo la lettera (Es. 15E9), sarà richiesta la conferma
                dell'utente
             */
            System.out.print("\nInserisci il numero civico (non obbligatorio): ");
            do{
                stato = true;
                civico = sc.next();

                // Se l'utente scrive STOP nel campo input, la registrazione si interromperà
                if(civico.equals(stop)){
                    System.out.println("\nInserito STOP; Interruzione della registrazione.\n");
                    return;
                }

                if(civico.isBlank())
                    break;

                // Il civico è in stringa per permettere l'inserimento di numeri civici misti
                StringBuilder numero = new StringBuilder();
                boolean civicoStrano = false;
                for(i=0; i < civico.length() ; i++){
                    c=civico.charAt(i);
                    if(Character.isDigit(c))
                        numero.append(c);
                    else if(Character.isLetter(c)){
                        if(i+1 < civico.length() && Character.isDigit(civico.charAt(i+1))){
                            while(!civicoStrano){
                                System.out.println("\nNumero civico anomalo: sei sicuro di averlo scritto giusto?");
                                civicoStrano = true;
                            }
                        }
                    } else {
                        System.out.println("\nHai inserito un carattere anomalo: "+ c +" in posizione "+ (i+1));
                        stato = false; // Il numero civico non permette caratteri al di fuori di lettere e numeri
                    }
                }

                // Controllo che il numero civico costruito non sia zero o inesistente
                if(stato && numero.toString().isBlank()){
                    System.out.println("\nIl numero civico non puo' non avere numeri!");
                    stato = false;
                } else if(stato && Integer.parseInt(numero.toString())==0){
                    System.out.println("\nIl numero civico non puo' essere zero!");
                    stato = false;
                }

                // Richiesta di conferma all'utente in caso il numero civico sia irregolare
                if(stato && civicoStrano){
                    System.out.println("\nIl numero civico che hai inserito sembra anomalo.\nSe sei sicuro di averlo" +
                            " scritto correttamente, scrivi 1, altrimenti reinseriscilo scrivendo 2.\n\n" +
                            "Civico inserito: "+civico);
                    do {
                        System.out.println("\nSelezione: ");
                        selezione = sc.nextInt(); // Scelta
                        switch(selezione){
                            case 1:
                                System.out.println("\nNumero civico confermato.");
                                break;
                            case 2:
                                System.out.println("\nNumero civico rifiutato. Reinserimento.");
                                stato = false;
                                break;
                            default:
                                System.out.println("\nComando inserito non valido!");
                        }
                    } while(selezione != 1 && selezione != 2);
                }
            } while(!stato);

            do {
                System.out.print("\nInserisci la citta' di domicilio: ");
                citta = sc.next().strip();

                // Se l'utente scrive STOP nel campo input, la registrazione si interromperà
                if(citta.equals(stop)){
                    System.out.println("\nInserito STOP; Interruzione della registrazione.\n");
                    return;
                }

                if(citta.isBlank())
                    System.out.println("\nLa citta' non puo' essere vuota!");

            } while(citta.isBlank());

            // Costruzione del luogo di domicilio così che GeocodingService lo capisca
            luogoDomicilio = (via+" "+civico+", "+citta).strip();

            // Conferma del luogo di domicilio da parte dell'utente
            System.out.println("\nIl luogo di domicilio inserito e':  "+luogoDomicilio+"\nRitieni sia corretto?");

            do {
                System.out.print("Scrivi 1 per dire si, scrivi 2 per dire no: ");
                selezione = sc.nextInt();
                switch (selezione) {
                    case 1:
                        System.out.println("\nRitieni sia corretto; ricerca del luogo di domicilio...");
                        double[] coords = GeocodingService.geocodeAddress(luogoDomicilio);
                        System.out.println("Latitudine: " + coords[0]);
                        System.out.println("Longitudine: " + coords[1]);
                        // PARLARE CON THOMAS
                        break;
                    case 2:
                        System.out.println("\nNon ritieni sia corretto; reinserimento del luogo di domicilio\n");
                        stato = false;
                        break;
                    default:
                        System.out.println("\nInserito valore non valido!\n");
                }
            }while(selezione != 1 && selezione != 2);

        } while(!stato);

        /*
            Registrazione finale dell'utente
         */
        System.out.println("\nTi registri da cliente o da ristoratore?\nInserisci 1 se cliente, 2 se ristoratore");
        Utente utente;
        do {
            System.out.println("Selezione: ");
            selezione = sc.nextInt();
            switch (selezione) {

                case 1:
                    utente = new Cliente(nome, cognome, username, password, dataDiNascita, luogoDomicilio);
                    break;
                case 2:
                    utente = new Ristoratore(nome, cognome, username, password, dataDiNascita, luogoDomicilio);
                    break;
                default:
                    System.out.println("\nComando di selezione non valido!");
            }
        } while(selezione < 1 || selezione > 2);
    }

    public static void modalitaGuest(Scanner sc, BufferedReader r) {
        System.out.println("\nSei ora in modalita' guest.\nInserisci la tua localita', cosi' da consigliarti i ristoranti nelle vicinanze:");
        System.out.print("\nLocalita':");
        String luogo;
        // metodo fittizio della ricerca dei ristoranti...
        try {
            luogo = r.readLine();
            double[] coords = GeocodingService.geocodeAddress(luogo);
                System.out.println("Latitudine: " + coords[0]);
                System.out.println("Longitudine: " + coords[1]);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }int selezione;
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
                    // metodo fittizio della ricerca dei ristoranti...
                    try {
                        luogo = r.readLine();
                        double[] coords = GeocodingService.geocodeAddress(luogo);
                        System.out.println("Latitudine: " + coords[0]);
                        System.out.println("Longitudine: " + coords[1]);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 2:
                    System.out.println("\nSei uscito dalla modalita' guest.");
                    break;
                default:
                    System.out.println("\nComando inserito non valido!");
            }
        } while(selezione != 2);

    }

    public static void main(String[] args) {
        MenuIniziale menuIniziale = new MenuIniziale();
        menuIniziale.mostra();
    }
}

