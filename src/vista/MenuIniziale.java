package vista;

import java.time.LocalDate;
import java.time.LocalDateTime;
// import java.time.temporal.ValueRange;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import Entita.Cliente;
import Entita.Ristoratore;
import com.opencsv.exceptions.CsvException;
import servizi.GeocodingService;

import static io_file.GestoreFile.esisteUtente;

public class MenuIniziale extends Menu {

    @Override
    public void mostra(){
        var sc = new Scanner(System.in);
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Menu Iniziale: ");
        System.out.println("1. Login");
        System.out.println("2. Registrazione");
        System.out.println("3. Guest");
        System.out.println("4. Esci");

        int selezione = sc.nextInt();

        switch (selezione) {
            case 1:
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

        String nome,cognome,username,password,luogoDomicilio,via,civico,citta,anno,mese,giorno;
        final String stop="STOP";
        LocalDate dataDiNascita;
        int i,selezione, aaaa=0, mm=0 ,gg=0; // TMCH
        char c;
        boolean stato,bisestile=false;

        System.out.println("\nInserisci il nome del cliente");
        do {

            stato=true;
            System.out.print("Il nome deve essere composto da solo lettere: ");
            nome = sc.nextLine().strip();

            if(nome.equals(stop)) {
                System.out.println("\nInserito STOP; interruzione della registrazione.\n");
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

        System.out.println("\nInserisci il cognome del cliente");
        do {
            stato=true;

            System.out.print("Il cognome deve essere composto da solo lettere: ");
            cognome = sc.nextLine().strip();

            if(cognome.equals(stop)) {
                System.out.println("\nInserito STOP; interruzione della registrazione.\n");
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

        System.out.println("Inserisci l'username del cliente");
        do {
            stato=true;

            System.out.print("L'username deve essere composto da caratteri alfanumerici e lungo da 3 a 16 caratteri: ");
            username = sc.nextLine().strip();

            if(username.equals(stop)) {
                System.out.println("\nInserito STOP; interruzione della registrazione.\n");
                return;
            }

            if(username.isBlank()){
                System.out.println("\nL'username non puo' essere vuoto!");
                stato = false;
            }

            if(stato && (username.length()<2||username.length()>16)){
                System.out.println("\nL'username inserito non rispetta i caratteri di lunghezza");
                stato = false;
            }

            for(i=0; stato && i<username.length(); i++){
                c=username.charAt(i);
                if(!Character.isLetter(c)&&!Character.isDigit(c)&&c!='_'){
                    System.out.println("\nHai inserito un carattere che non e' alfanumerico: "+ c + " in posizione " + (i+1));
                    stato = false;
                    break;
                }
            }
            try {
                if(stato && esisteUtente(username)){
                    System.out.println("\nL'username inserito è gia' utilizzato!");
                    stato = false;
                }
            } catch (IOException | CsvException e) {
                throw new RuntimeException(e);
            }

        } while(username.isBlank() || !stato);

        System.out.println("Inserisci la password del cliente");
        do {
            stato = true;

            System.out.println("Inserisci la password del cliente:");
            password = sc.nextLine().strip();

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
                }
            }

        }while (!stato);

        System.out.println("Inserisci la data di nascita in numero a cifre");
        do {
            stato=true;

            System.out.print("Inserisci l'anno di nascita: ");
            anno = sc.nextLine().strip();

            if(anno.equals(stop)){
                System.out.println("\nInserito STOP; interruzione della registrazione.\n");
                return;
            }

            if(anno.isBlank()){
                System.out.println("\nL'anno di nascita non puo' essere vuoto!");
                stato = false;
            }

            for(i=0; stato && i<anno.length() ; i++){
                c=anno.charAt(i);

                if(!Character.isDigit(c)) {
                    System.out.println("\nHai inserito un carattere che non e' numerico: "+ c + " in posizione " + (i+1));
                    stato = false;
                    break;
                }
            }

            if(stato){
                aaaa = Integer.parseInt(anno);

                if (aaaa < 1900 || aaaa > LocalDateTime.now().getYear()) {
                    System.out.println("\nL'anno inserito e' invalido!");
                    stato = false;
                } else if (aaaa % 400 == 0 || (aaaa % 4 == 0 && aaaa % 100 != 0))
                    bisestile = true;

            }

        }while(!stato);

        do {
            stato=true;
            System.out.print("Inserisci il mese di nascita: ");
            mese = sc.nextLine().strip();

            if(mese.equals(stop)){
                System.out.println("\nInserito STOP; interruzione della registrazione.\n");
                return;
            }

            if(mese.isBlank()){
                System.out.println("Il mese di nascita non puo' essere vuoto!");
                stato = false;
            }

            for(i=0; stato && i<mese.length() ; i++){
                c=mese.charAt(i);
                if(!Character.isDigit(c)) {
                    System.out.println("\nHai inserito un carattere che non e' numerico: "+ c +" in posizione "+ (i+1));
                    stato = false;
                    break;
                }
            }

            if(stato){
                mm = Integer.parseInt(mese);

                if(mm<1 || mm>12){
                    System.out.println("\nIl mese inserito e' invalido!");
                    stato = false;
                }
            }

        } while(!stato);

        do {
            stato = true;
            System.out.print("Inserisci il giorno di nascita: ");
            giorno = sc.nextLine().strip();

            if(giorno.equals(stop)){
                System.out.println("\nInserito STOP; interruzione della registrazione.\n");
                return;
            }

            if(giorno.isBlank()){
                System.out.println("\nIl giorno di nascita non pu' essere vuoto!");
                stato = false;
            }

            for(i=0; stato && i<giorno.length() ; i++){
                c=giorno.charAt(i);
                if(!Character.isDigit(c)) {
                    System.out.println("\nHai inserito un carattere che non e' numerico: "+ c +" in posizione "+ (i+1));
                    stato = false;
                    break;
                }
            }

            if(stato){
                gg = Integer.parseInt(giorno);

                if(gg<1){
                    System.out.println("\nIl giorno inserito e' invalido!");
                    stato = false;
                } else if((mm==4 || mm==6 || mm == 9 || mm == 11) && gg > 30){
                    System.out.println("\nE' stata inserita la data '"+gg+"', ma il mese di nascita ha solo 30 giorni!");
                    stato = false;
                } else if(mm==2 && bisestile && gg > 29){
                    System.out.println("\nE' stata inserita la data '"+gg+"', ma il mese di febbraio bisestile ha solo 29 giorni!");
                    stato = false;
                } else if(mm==2 && !bisestile && gg > 28){
                    System.out.println("\nE' stata inserita la data '"+gg+"', ma il mese di febbraio ha solo 28 giorni!");
                    stato = false;
                } else if(gg > 31){
                    System.out.println("\nE' stata inserita la data '"+gg+"', ma il mese di nascita ha solo 31 giorni!");
                    stato = false;
                }
            }

        } while(!stato);
        dataDiNascita = LocalDate.of(aaaa,mm,gg);

        System.out.println("Inserisci il luogo di domicilio");
        do {
            do {
                System.out.print("\nInserisci la via di domicilio, senza numero civico e attenzione agli spazi: ");
                via = sc.nextLine().strip();

                if(via.equals(stop)){
                    System.out.println("\nInserito STOP; Interruzione della registrazione.\n");
                    return;
                }

                if(via.isBlank())
                    System.out.println("\nLa via di domicilio non puo' essere vuota!");

            } while (via.isBlank());


            System.out.print("\nInserisci il numero civico (non obbligatorio): ");
            do{
                stato = true;
                civico = sc.nextLine();

                if(civico.equals(stop)){
                    System.out.println("\nInserito STOP; Interruzione della registrazione.\n");
                    return;
                }

                if(civico.isBlank())
                    break;

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
                        stato = false;
                    }
                }

                if(stato && !numero.toString().isBlank() && Integer.parseInt(numero.toString())==0){
                    System.out.println("\nIl numero civico inserito non puo' essere zero!");
                    stato = false;
                }

                if(stato && civicoStrano){
                    System.out.println("\nIl numero civico che hai inserito sembra anomalo.\nSe sei sicuro di averlo" +
                            " scritto correttamente, scrivi 1, altrimenti reinseriscilo scrivendo 2.\n\n" +
                            "Civico inserito: "+civico);
                    do {
                        System.out.println("\nSelezione: ");
                        selezione = sc.nextInt();
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
                citta = sc.nextLine().strip();

                if(citta.equals(stop)){
                    System.out.println("\nInserito STOP; Interruzione della registrazione.\n");
                    return;
                }

                if(citta.isBlank())
                    System.out.println("\nLa citta' non puo' essere vuota!");

            } while(citta.isBlank());


            luogoDomicilio = (via+" "+civico+", "+citta).strip();
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

        System.out.println("\nTi registri da cliente o da ristoratore?\nInserisci 1 se cliente, 2 se ristoratore");
        do {
            System.out.println("Selezione: ");
            selezione = sc.nextInt();
            switch (selezione) {

                case 1:
                    new Cliente(nome, cognome, username, password, dataDiNascita, luogoDomicilio);
                    break;
                case 2:
                    new Ristoratore(nome, cognome, username, password, dataDiNascita, luogoDomicilio);
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

