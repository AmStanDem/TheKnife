import vista.MenuIniziale;

import java.util.Scanner;

/**
 * Punto di ingresso dell'applicazione.
 * @author Thomas Riotto
 * @author Antonio Pesavento
 * @author Alessandro Tullo
 * @author Marco Zaro
 * @version 1.0
 */
public final class TheKnife {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MenuIniziale menuIniziale = new MenuIniziale(scanner);
        menuIniziale.mostra();
        scanner.close();
    }
}