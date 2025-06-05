package Entita;

public class Ristorante {
    private String nome;
    private String nazione;
    private String citta;
    private String indirizzo;
    private float latitudine;
    private float longitudine;
    private TipoCucina tipoDiCucina;
    private boolean delivery;
    private boolean prenotazione;
    private float prezzoMedio;
    private String descrizione;


    public Ristorante(String nome, String nazione, String citta, String indirizzo, float latitudine, float longitudine, TipoCucina tipoDiCucina, boolean delivery, boolean prenotazione, float prezzoMedio,  String descrizione) {
        this.nome=nome;
        this.nazione =nazione;
        this.citta = citta;
        this.indirizzo = indirizzo;
        this.latitudine = latitudine;
        this.longitudine = longitudine;
        this.tipoDiCucina=tipoDiCucina;
        this.delivery=delivery;
        this.prenotazione=prenotazione;
        this.prezzoMedio=prezzoMedio;
        this.descrizione = descrizione;
    }

    public Ristorante(String nome, String citta) {
        this.nome=nome;
        this.citta = citta;
    }

    public String getNome(){return nome;}
    public String getNazione(){return nazione;}
    public  String getCitta(){return citta;}
    public String getIndirizzo(){return indirizzo;}
    public float getLatitudine(){return latitudine;}
    public float getLongitudine(){return longitudine;}
    public TipoCucina getTipoDiCucina(){return tipoDiCucina;}
    public boolean getDelivery(){return delivery;}
    public boolean getPrenotazione(){return prenotazione;}
    public float getPrezzoMedio(){return prezzoMedio;}
    public String getDescrizione(){return descrizione;}





}
