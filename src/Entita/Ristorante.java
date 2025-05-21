package Entita;

public class Ristorante {
    private String nome;
    private String luogo;
    private String tipoDiCucina;
    private boolean delivery;
    private boolean prenotazione;
    private float prezzoMinimo;
    private float prezzoMassimo;

    public Ristorante(String nome, String luogo, String tipoDiCucina, boolean delivery, boolean prenotazione, float prezzoMinimo, float prezzoMassimo){
        this.nome=nome;
        this.luogo=luogo;
        this.tipoDiCucina=tipoDiCucina;
        this.delivery=delivery;
        this.prenotazione=prenotazione;
        this.prezzoMinimo=prezzoMinimo;
        this.prezzoMassimo=prezzoMassimo;
    }

    public String getNOme(){return nome;}
    public String getLuogo(){return luogo;}
    public String getTipoDiCucina(){return tipoDiCucina;}
    public boolean getDelivery(){return delivery;}
    public boolean getPrenotazione(){return prenotazione;}
    public float getPrezzoMinimo(){return prezzoMinimo;}
    public float getPrezzoMassimo(){return prezzoMassimo;}






}
