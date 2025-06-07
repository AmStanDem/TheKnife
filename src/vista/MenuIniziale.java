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

    private void modalitaGuest() {
    }

    public static void main(String[] args) {
        MenuIniziale menu = new MenuIniziale();
        menu.mostra();
    }

}
