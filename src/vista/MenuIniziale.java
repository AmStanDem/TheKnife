package vista;

import java.time.temporal.ValueRange;
import java.util.Scanner;

public class MenuIniziale extends Menu {

    @Override
    public void mostra() {
        var sc = new Scanner(System.in);

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
                break;
            case 3:
                modalitaGuest();
                break;
            case 4:
                System.exit(0);
                break;
        }
    }

    private void modalitaGuest(Scanner sc) {
        System.out.println("\nSei ora in modalita' guest.\nInserisci la tua localita', cosi' da consigliarti i ristoranti nelle vicinanze:");
        System.out.print("\nLocalita':");
        String luogo=sc.nextLine();
        // metodo fittizio della ricerca dei ristoranti...
        int selezione = 0;
        while(selezione!=2){
            System.out.println("\nInserisci uno dei numeri per eseguire il comando:");
            System.out.println(" 1. Inserisci una nuova localita'");
            System.out.println(" 2. Esci dalla modalita' guest");
            System.out.print("\nComando: ");
            selezione = sc.nextInt();
            switch(selezione){
                case 1:
                    System.out.println("\nInserisci la tua localita', cosi' da consigliarti i ristoranti nelle vicinanze:");
                    System.out.print("\nLocalita':");
                    luogo=sc.nextLine();
                    // metodo fittizio della ricerca dei ristoranti...
                    break;
                case 2:
                    System.out.println("\nSei uscito dalla modalita' guest.");
                    break;
                default:
                    System.out.println("\nComando inserito non valido!");
        }
    }

    public static void main(String[] args) {
        MenuIniziale menu = new MenuIniziale();
        menu.mostra();
    }

}
