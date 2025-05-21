package Entita;

public class Ristorante {
    private String nome;
    private String luogo;
    private String tipoDiCucina;
    private boolean delivery;
    private boolean prenotazione;
    private float prezzoMedio;


    public Ristorante(String nome, String luogo, String tipoDiCucina, boolean delivery, boolean prenotazione, float prezzoMedio){
        this.nome=nome;
        this.luogo=luogo;
        this.tipoDiCucina=tipoDiCucina;
        this.delivery=delivery;
        this.prenotazione=prenotazione;
        this.prezzoMedio=prezzoMedio;
    }

    public String getNOme(){return nome;}
    public String getLuogo(){return luogo;}
    public String getTipoDiCucina(){return tipoDiCucina;}
    public boolean getDelivery(){return delivery;}
    public boolean getPrenotazione(){return prenotazione;}
    public float getPrezzoMedio(){return prezzoMedio;}






}
