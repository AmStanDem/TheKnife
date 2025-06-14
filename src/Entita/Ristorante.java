package Entita;

import java.util.LinkedList;

/**
 * Questa classe rappresenta un ristorante nel sistema e contiene parametri essenziali affinchè la ricerca
 * dell'utente possa svolgersi in modo semplice ed efficace.
 * (nome, localita, tipoDiCucina, delivery, prenotazione, prezzoMedio, descrizione).
 *
 * @author Marco Zaro
 * @version 1.0
 */

public class Ristorante {
    private String nome;
    private Localita localita;
    private TipoCucina tipoDiCucina;
    private boolean delivery;
    private boolean prenotazione;
    private float prezzoMedio;
    private String descrizione;
    private Ristoratore proprietario;
    private LinkedList<Recensione> listaRecensioni;


    public Ristorante(String nome, Localita localita, TipoCucina tipoDiCucina, boolean delivery, boolean prenotazione, float prezzoMedio,  String descrizione, Ristoratore proprietario) {
        this.nome=nome;
        this.localita=localita;
        this.tipoDiCucina=tipoDiCucina;
        this.delivery=delivery;
        this.prenotazione=prenotazione;
        this.prezzoMedio=prezzoMedio;
        this.descrizione = descrizione;
        this.proprietario =proprietario;
        this.listaRecensioni = new LinkedList<>();

    }

    public Ristorante(String nome, Localita localita, TipoCucina tipoDiCucina, boolean delivery, boolean prenotazione, float prezzoMedio,  String descrizione) {
        this.nome=nome;
        this.localita=localita;
        this.tipoDiCucina=tipoDiCucina;
        this.delivery=delivery;
        this.prenotazione=prenotazione;
        this.prezzoMedio=prezzoMedio;
        this.descrizione = descrizione;
        this.listaRecensioni = new LinkedList<>();
    }

    public String getNome(){return nome;}
    public Localita getLocalita(){return localita;}
    public TipoCucina getTipoDiCucina(){return tipoDiCucina;}
    public boolean getDelivery(){return delivery;}
    public boolean getPrenotazione(){return prenotazione;}
    public float getPrezzoMedio(){return prezzoMedio;}
    public String getDescrizione(){return descrizione;}

    public Ristoratore getProprietario(){return proprietario;}

    public String getUsernameProprietario(){
        return proprietario.getUsername().trim();
    }

    public void setProprietario(Ristoratore proprietario){
        this.proprietario=proprietario;
    }

    public boolean appartieneA(Ristoratore proprietario){
        return this.proprietario.equals(proprietario);
    }

    public boolean appartieneA(String username) {
        return this.proprietario.getUsername().equals(username);
    }

    public void aggiungiRecensione(Recensione recensione){
        if(listaRecensioni.isEmpty()){
            listaRecensioni.add(recensione);
            System.out.println("\nRecensione aggiunta con successo");
        } else if(listaRecensioni.contains(recensione))
            System.out.println("\nRecensione gia' presente; recensione non aggiunta!");
        else {
            listaRecensioni.add(recensione);
            System.out.println("\nRecensione aggiunta con successo");
        }
    }

    public LinkedList<Recensione> getListaRecensioni(){
        return new LinkedList<>(listaRecensioni);
    }

    public void eliminaRecensione(Recensione recensione){
        if(listaRecensioni.remove(recensione))
            System.out.println("\nRecensione eliminata con successo");
        else
            System.out.println("\nNessuna recensione equivalente trovata; nessun effetto!");
    }

}
