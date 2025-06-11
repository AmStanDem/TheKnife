package Entita;

public class Ristorante {
    private String nome;
    private Localita localita;
    private TipoCucina tipoDiCucina;
    private boolean delivery;
    private boolean prenotazione;
    private float prezzoMedio;
    private String descrizione;


    public Ristorante(String nome, Localita localita, TipoCucina tipoDiCucina, boolean delivery, boolean prenotazione, float prezzoMedio,  String descrizione) {
        this.nome=nome;
        this.localita=localita;
        this.tipoDiCucina=tipoDiCucina;
        this.delivery=delivery;
        this.prenotazione=prenotazione;
        this.prezzoMedio=prezzoMedio;
        this.descrizione = descrizione;
    }

    public String getNome(){return nome;}
    public Localita getLocalita(){return localita;}
    public TipoCucina getTipoDiCucina(){return tipoDiCucina;}
    public boolean getDelivery(){return delivery;}
    public boolean getPrenotazione(){return prenotazione;}
    public float getPrezzoMedio(){return prezzoMedio;}
    public String getDescrizione(){return descrizione;}





}
