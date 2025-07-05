package entita;

/**
 * Rappresenta le varie tipologie di cucina disponibili nel sistema TheKnife.
 * <p>
 * Questa enumerazione è pensata per classificare i ristoranti secondo lo stile culinario,
 * semplificando la selezione da parte dei clienti. I valori disponibili includono
 * cucine internazionali, locali e alternative (es. vegetariana, vegana, fusion).
 * <p>
 * Ogni elemento dell'enumerazione è associato a una stringa descrittiva leggibile.
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

    /**
     * Nome leggibile e descrittivo della tipologia di cucina.
     * <p>
     * Usato per presentazioni in interfacce utente e stampe testuali.
     */
    private final String nome;

    /**
     * Costruttore interno dell'enum, utilizzato per inizializzare la stringa associata.
     *
     * @param nome Rappresentazione testuale della tipologia
     */
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
     * Rappresenta l'enumerazione come stringa leggibile.
     * <p>
     * Sovrascrive il comportamento predefinito di {@code toString()} per restituire
     * direttamente il nome leggibile della cucina.
     *
     * @return Nome della tipologia di cucina
     */
    @Override
    public String toString() {
        return nome;
    }
}