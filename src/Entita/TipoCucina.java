package Entita;

/**
 * Rappresenta le diverse tipologie di cucina.
 * Enum semplificato con le tipologie più comuni e rappresentative.
 *
 * @author Alessandro Tullo
 */
public enum TipoCucina {
    ITALIANA("Italiana"),
    PIZZERIA("Pizzeria"),
    PESCE("Pesce"),
    MODERNA("Moderna"),

    GIAPPONESE("Giapponese"),
    CINESE("Cinese"),
    INDIANA("Indiana"),
    MESSICANA("Messicana"),
    FRANCESE("Francese"),
    AMERICANA("Americana"),
    GRECA("Greca"),
    SPAGNOLA("Spagnola"),
    THAILANDESE("Thailandese"),

    MEDITERRANEA("Mediterranea"),
    VEGETARIANA("Vegetariana"),
    VEGANA("Vegana"),
    FUSION("Fusion"),
    ETNICA("Etnica"),
    INTERNAZIONALE("Internazionale"),
    PANINOTECA("Paninoteca");

    private final String nome;

    TipoCucina(String nome) {
        this.nome = nome;
    }

    /**
     * Restituisce il nome descrittivo della tipologia di cucina.
     *
     * @return Il nome della tipologia di cucina.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Override del metodo toString per restituire il nome della tipologia di cucina.
     *
     * @return Il nome della tipologia di cucina.
     */
    @Override
    public String toString() {
        return nome;
    }
}