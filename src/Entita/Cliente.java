package Entita;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Il Cliente è l'utente del sistema che ha i suoi ristoranti preferiti
 * e può recensire i ristoranti.
 */

public final class Cliente extends Utente {

    private final List<Ristorante> preferiti;

    /**
     * Costruisce un nuovo cliente.
     *
     * @param nome           Nome del cliente.
     * @param cognome        Cognome del cliente.
     * @param username       Username del cliente.
     * @param password       Password del cliente.
     * @param dataDiNascita  Data di nascita del cliente.
     * @param luogoDomicilio Luogo del domicilio del cliente.
     * @throws UtenteException Se i dati inseriti sono invalidi.
     */
    public Cliente(String nome, String cognome, String username, String password, LocalDate dataDiNascita, String luogoDomicilio) {
        super(nome, cognome, username, password, dataDiNascita, luogoDomicilio);
        preferiti = new ArrayList<>();
    }

    /**
     * Costruisce un nuovo cliente.
     *
     * @param nome           Nome del cliente.
     * @param cognome        Cognome del cliente.
     * @param username       Username del cliente.
     * @param password       Password del cliente.
     * @param dataDiNascita  Data di nascita del cliente.
     * @param luogoDomicilio Luogo del domicilio del cliente.
     * @param preferiti      Ristoranti preferiti del cliente.
     * @throws UtenteException Se i dati inseriti sono invalidi.
     */
    public Cliente(String nome, String cognome, String username, String password, LocalDate dataDiNascita, String luogoDomicilio, ArrayList<Ristorante> preferiti) {
        super(nome, cognome, username, password, dataDiNascita, luogoDomicilio);
        this.preferiti = preferiti;
    }

    /**
     * Restituisce i ristoranti preferiti del cliente.
     *
     * @return I ristoranti preferiti del cliente.
     */
    public List<Ristorante> getPreferiti() {
        return new ArrayList<>(preferiti);
    }

    /**
     * Aggiunge un nuovo ristorante preferito ai preferiti.
     *
     * @param preferito Il nuovo ristorante da aggiungere ai preferiti.
     * @return {@code true} se il nuovo ristorante preferito è stato aggiunto, {@code false} altrimenti.
     */
    public boolean aggiungiPreferito(Ristorante preferito) {
        if (preferito == null) {
            return false;
        }
        if (!this.preferiti.contains(preferito)) {
            return preferiti.add(preferito);
        }
        return false;
    }

    /**
     * Rimuove il ristorante preferito dai preferiti del cliente.
     *
     * @param preferito Il ristorante preferito da rimuovere.
     * @return {@code true} se il ristorante preferito è stato rimosso correttamente, {@code false} altrimenti.
     */
    public boolean rimuoviPreferito(Ristorante preferito) {
        if (preferito == null) {
            return false;
        }
        return preferiti.remove(preferito);
    }

    /**
     * Restituisce la recensione del cliente per ogni ristorante.
     *
     * @param ristoranti I ristoranti su cui cercare le recensioni.
     * @return Le recensioni del cliente.
     */
    public ArrayList<Recensione> getRecensioni(ArrayList<Ristorante> ristoranti) {
        ArrayList<Recensione> listaRecensioni = new ArrayList<>();

        for (Ristorante ristorante : ristoranti) {
            Recensione recensione = ristorante.trovaRecensioneCliente(this);
            if (recensione != null) {
                listaRecensioni.add(recensione);
            }
        }
        return listaRecensioni;
    }

    /**
     * Consente la visualizzazione delle proprie recensioni su dei ristoranti.
     *
     * @param ristoranti I ristoranti su cui effettuare la visualizzazione delle recensioni.
     */
    public void visualizzaRecensioni(ArrayList<Ristorante> ristoranti) {
        ArrayList<Recensione> listaRecensioni = getRecensioni(ristoranti);

        StringBuilder stringBuilder = new StringBuilder();
        for (Recensione recensione : listaRecensioni) {
            stringBuilder.append(recensione).append("\n");
        }
        System.out.println(stringBuilder.toString());
    }

    /**
     * Aggiunge una recensione a un ristorante.
     *
     * @param ristorante Il ristorante da recensire.
     * @param recensione La recensione da aggiungere.
     * @return {@code true} se la recensione è stata aggiunta correttamente, {@code false} altrimenti.
     */
    public boolean aggiungiRecensione(Ristorante ristorante, Recensione recensione) {

        if (ristorante == null) {
            return false;
        }

        if (recensione == null) {
            return false;
        }

        if (!recensione.getCliente().equals(this)) {
            return false;
        }

        if (haRecensito(ristorante)) {
            return false;
        }

        return ristorante.aggiungiRecensione(recensione);
    }

    /**
     * Rimuove la recensione effettuata dal cliente su un ristorante.
     *
     * @param ristorante Il ristorante su cui rimuovere la recensione.
     * @return {@code true} se la recensione è stata rimossa correttamente, {@code false} altrimenti
     */
    public boolean rimuoviRecensione(Ristorante ristorante) {

        if (ristorante == null) {
            return false;
        }

        if (!haRecensito(ristorante))
            return false;

        Recensione recensione = ristorante.trovaRecensioneCliente(this);

        return ristorante.rimuoviRecensione(recensione);
    }

    /**
     * Modifica una recensione esistente su un ristorante con una nuova.
     *
     * @param ristorante      Il ristorante su cui effettuare la modifica della recensione.
     * @param nuovaRecensione La nuova recensione da inserire al posto di quella vecchia.
     * @return {@code true} se la modifica è avvenuta con successo, {@code false} altrimenti.
     */
    public boolean modificaRecensione(Ristorante ristorante, Recensione nuovaRecensione) {

        if (ristorante == null) {
            return false;
        }

        if (nuovaRecensione == null) {
            return false;
        }

        if (!nuovaRecensione.getCliente().equals(this)) {
            return false;
        }

        Recensione vecchiaRecensione = ristorante.trovaRecensioneCliente(this);

        if (vecchiaRecensione == null) {
            return false;
        }

        if (ristorante.rimuoviRecensione(vecchiaRecensione)) {
            return ristorante.aggiungiRecensione(nuovaRecensione);
        }
        return false;
    }

    /**
     * Verifica se un utente ha recensito un ristorante.
     *
     * @param ristorante Il ristorante su cui verificare l'esistenza della recensione del cliente.
     * @return {@code true} se il cliente ha recensito il ristorante, {@code false} altrimenti.
     */
    public boolean haRecensito(Ristorante ristorante) {
        if (ristorante == null) {
            return false;
        }
        return ristorante.trovaRecensioneCliente(this) != null;
    }
}
