package vista;

import java.io.IOException;
import java.util.Scanner;

import Entita.Cliente;
import Entita.Localita;
import Entita.Ristorante;
import com.opencsv.exceptions.CsvException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import servizi.GeocodingService;
import servizi.RistoranteService;
import servizi.UtenteService;

public class MenuCliente extends Menu {

    private final Scanner scanner;
    private final Cliente cliente;

    public MenuCliente(Scanner scanner, Cliente cliente) {
        validaAttributi(scanner, cliente);
        this.scanner = scanner;
        this.cliente = cliente;
        mostra();
    }

    private void validaAttributi(Scanner scanner, Cliente cliente) {
        StringBuilder errori = new StringBuilder();
        boolean errore = false;

        if (scanner == null) {
            String messaggio = "Impossibile leggere da terminale.\n";
            errori.append(messaggio);
            errore = true;
        }

        if (cliente == null) {
            String messaggio = "Il nome di un cliente deve essere valorizzato.\n";
            errori.append(messaggio);
            errore = true;
        }
        if (errore) {
            throw new IllegalArgumentException();
        }
    }

    /*
     * Visualizza la lista dei ristoranti preferiti di un cliente.
     */
    private void visualizzaPreferiti() {

        System.out.println("I tuoi ristoranti preferiti: ");

        for (int i = 0; i < cliente.getPreferiti().size(); i++) {
            System.out.println((i + 1) + ". " + cliente.getPreferiti().get(i));
        }
    }

    @Override
    public void mostra() {
        System.out.println("Benvenuto " + cliente);
        int opzione;

        do {

            System.out.println("1. Ricerca i ristoranti vicini a te.");
            System.out.println("2. Cerca un ristorante.");
            System.out.println("3. Visualizza i tuoi ristoranti preferiti.");
            System.out.println("4. Aggiungi un ristorante ai preferiti.");
            System.out.println("5. Visualizza i ristoranti recensiti.");
            System.out.println("6. Rimuovi un ristorante dai preferiti.");
            System.out.println("7. Aggiungi una recensione a un ristorante.");
            System.out.println("8. Modifica una recensione di un ristorante.");
            System.out.println("9. Rimuovi una recensione da un ristorante.");
            System.out.println("10. Uscire dall'app.");

            System.out.println("Inserisci una opzione: ");
            opzione = scanner.nextInt();

            switch (opzione) {
                case 1:
                    double[] coords = GeocodingService.geocodeAddress(cliente.getLuogoDomicilio());
                    double raggioKm = 10;
                    Localita localita = new Localita(coords[0], coords[1]);
                    try {
                        var ristoranti = RistoranteService.cercaRistorante(localita, raggioKm);
                        for (Ristorante ristorante : ristoranti) {
                            System.out.println(ristorante);
                        }
                    }
                    catch (IOException | CsvException e) {
                        System.err.println("Errore nella ricerca dei ristoranti vicini.");
                    }
                    break;
                case 2:
                    break;
                case 3:
                    visualizzaPreferiti();
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:

                    int indice;

                    do {
                        visualizzaPreferiti();
                        System.out.println("Inserisci il numero del ristorante che vuoi rimuovere dai preferiti: ");
                        indice = scanner.nextInt() - 1;
                    } while (indice < 0 || indice > cliente.getPreferiti().size());

                    Ristorante preferitoDaEliminare = cliente.getPreferiti().get(indice);

                    try {
                        if (!UtenteService.rimuoviPreferito(cliente, preferitoDaEliminare)) {
                            System.err.println("Errore nella rimozione del ristorante dai preferiti");
                        }
                        System.out.println("Preferito rimosso con successo.");
                    } catch (IOException | CsvException e) {
                        System.err.println("Errore nella rimozione del ristorante dai preferiti");
                    }

                    break;
                case 7:
                    break;
                case 8:
                    break;
                case 9:
                    break;
                case 10:
                    System.out.println("Arrivederci. :D");
                    break;
                default:
                    System.out.println("Operazione non ancora supportata.");
                    break;
            }
        } while (opzione != 10);
    }
}
