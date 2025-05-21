/**
 * Rappresenta le diverse tipologie di cucina (elenco esteso).
 */
public enum TipoCucina {
    ITALIANA("Italiana"),
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
    VIETNAMITA("Vietnamita"),
    COREANA("Coreana"),
    TURCA("Turca"),
    LIBANESE("Libanese"),
    MAROCCHINA("Marocchina"),
    PERUVIANA("Peruviana"),
    BRASILIANA("Brasiliana"),
    ARGENTINA("Argentina"),
    CUBANA("Cubana"),
    ETIOPE("Etiope"),
    NIGERIANA("Nigeriana"),
    POLACCA("Polacca"),
    UNGHERESE("Ungherese"),
    RUSSA("Russa"),
    SUD_EST_ASIATICA("Sud-Est Asiatica"),
    CARAIBICA("Caraibica"),
    AUSTRALIANA("Australiana"),
    SVIZZERA("Svizzera"),
    AUSTRIACA("Austriaca"),
    BELGA("Belga");

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