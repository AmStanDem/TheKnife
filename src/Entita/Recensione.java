package Entita;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Recensione {

    private Cliente cliente;
    private int stelle;
    private String recensione;
    private LocalDateTime dataRecensione;

    public Recensione(Cliente cliente, int stelle, String recensione) {
        this.cliente = cliente;
        this.stelle = stelle;
        this.recensione = recensione;
        this.dataRecensione = LocalDateTime.now();
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String formattedDateTime = dataRecensione.format(formatter);
        return cliente.getUsername() + "(" + cliente.getCognome() + " " + cliente.getNome() + ")    Stelle: " +
                stelle + "\nRecensione: " + recensione + "\nData di recensione: " + formattedDateTime;
    }

    // TESTING
    public static void main(String[] args) {
        Recensione recensione1 = new Recensione(new Cliente("Antonio","Pesavento","apesavento",
                "ciao", LocalDate.of(2006,1,14),"Varese"),5,"ciao ciao");
        System.out.println(recensione1);
    }

}
