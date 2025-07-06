package entita;

/**
 * Rappresenta una località geografica relativa a un ristorante o a un utente.<p>
 * Contiene informazioni su nazione, città, indirizzo e coordinate geografiche.<p>
 * Fornisce funzionalità per il calcolo della distanza tra località, utile per
 * visualizzare ristoranti vicini sulla piattaforma TheKnife.
 *
 * @author Thomas Riotto
 */

public final class Localita {

    /**
     * Nazione in cui si trova la località
     * (es. "Italia").
     */
    private final String nazione;

    /**
     * Città associata alla località
     * (es. "Milano").
     */
    private final String citta;

    /**
     * Indirizzo specifico della località
     * (es. "Via Roma 12").
     */
    private final String indirizzo;

    /**
     * Coordinata geografica nord-sud della località, espressa in gradi decimali.
     */
    private final double latitudine;

    /**
     * Coordinata geografica est-ovest della località, espressa in gradi decimali.
     */
    private final double longitudine;

    /**
     * Raggio medio della Terra in chilometri, utilizzato per il calcolo delle distanze geografiche.
     */
    private static final double RAGGIO_TERRA_KM = 6371.0;

    /**
     * Crea una nuova località impostando i dati geografici.
     *
     * @param nazione     La nazione della località.
     * @param citta       La città della località.
     * @param indirizzo   L'indirizzo della località.
     * @param latitudine  La latitudine della località.
     * @param longitudine La longitudine della località.
     */
    public Localita(String nazione, String citta, String indirizzo,
                    double latitudine, double longitudine) {
        this.nazione = nazione;
        this.citta = citta;
        this.indirizzo = indirizzo;
        this.latitudine = latitudine;
        this.longitudine = longitudine;
    }

    /**
     * Crea una nuova località impostando i dati geografici.
     *
     * @param latitudine  La latitudine della località.
     * @param longitudine La longitudine della località.
     */
    public Localita(double latitudine, double longitudine) {
        this.nazione = "";
        this.citta = "";
        this.indirizzo = "";
        this.latitudine = latitudine;
        this.longitudine = longitudine;
    }

    /**
     * Calcola la distanza in chilometri tra questa località e un'altra
     * utilizzando la formula di Haversine.
     *
     * @param altra l'altra località
     * @return distanza in chilometri, o -1 se una delle coordinate non è disponibile
     */
    public double calcolaDistanza(Localita altra) {
        if (!haCoordinate() || !altra.haCoordinate()) {
            return -1;
        }

        double lat1Rad = Math.toRadians(this.latitudine);
        double lat2Rad = Math.toRadians(altra.latitudine);
        double deltaLatRad = Math.toRadians(altra.latitudine - this.latitudine);
        double deltaLonRad = Math.toRadians(altra.longitudine - this.longitudine);

        // Formula di Haversine
        // a = sin²(Δφ/2) + cos φ1 ⋅ cos φ2 ⋅ sin²(Δλ/2)
        double sinDeltaLat = Math.sin(deltaLatRad / 2);
        double sinDeltaLon = Math.sin(deltaLonRad / 2);

        double a = sinDeltaLat * sinDeltaLat +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        sinDeltaLon * sinDeltaLon;

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Distanza in chilometri
        return RAGGIO_TERRA_KM * c;
    }

    /**
     * Verifica se la località ha coordinate geografiche valide.
     *
     * @return {@code true} se latitudine e longitudine sono diverse da zero.
     */

    public boolean haCoordinate() {
        return latitudine != 0.0 && longitudine != 0.0;
    }

    /**
     * Verifica se due località appartengono alla stessa zona geografica,
     * comparando città e nazione.
     *
     * @param altra L'altra località da confrontare.
     * @return {@code true} se città e nazione coincidono (case-insensitive), {@code false} altrimenti.
     */

    public boolean stessaZonaGeografica(Localita altra) {
        return this.citta.equalsIgnoreCase(altra.citta) &&
                this.nazione.equalsIgnoreCase(altra.nazione);
    }
    /**
     * Restituisce la nazione in cui si trova il ristorante.
     *
     * @return Nome della nazione
     */
    public String getNazione() {
        return nazione;
    }

    /**
     * Restituisce la città in cui si trova il ristorante.
     *
     * @return Nome della città associata al ristorante
     */
    public String getCitta() {
        return citta;
    }

    /**
     * Restituisce l'indirizzo completo del ristorante.
     * <p>
     * Può includere via, numero civico e altre specifiche locali.
     *
     * @return Indirizzo testuale del ristorante
     */
    public String getIndirizzo() {
        return indirizzo;
    }

    /**
     * Restituisce la coordinata geografica di latitudine.
     * <p>
     * Utilizzata per la geolocalizzazione e la mappatura del ristorante.
     *
     * @return Valore decimale della latitudine
     */
    public double getLatitudine() {
        return latitudine;
    }

    /**
     * Restituisce la coordinata geografica di longitudine.
     * <p>
     * Utilizzata insieme alla latitudine per identificare la posizione sulla mappa.
     *
     * @return Valore decimale della longitudine
     */
    public double getLongitudine() {
        return longitudine;
    }


    /**
     * Restituisce una rappresentazione testuale della località nel formato:
     * "indirizzo, città, nazione" se l'indirizzo è disponibile,
     * altrimenti "città, nazione".
     *
     * @return Stringa descrittiva della località.
     */
    @Override
    public String toString() {
        if (indirizzo != null && !indirizzo.isEmpty()) {
            return indirizzo + ", " + citta + ", " + nazione;
        }
        return citta + ", " + nazione;
    }

    /**
     * Determina se due oggetti Localita sono equivalenti.
     * Se entrambi hanno coordinate valide, il confronto avviene sulle coordinate con una tolleranza definita (0.0001).
     * In alternativa, confronta nazione, città e indirizzo (se disponibili).
     *
     * @param obj L'oggetto da confrontare con questa località.
     * @return {@code true} se le due località sono considerate equivalenti, {@code false} altrimenti.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Localita altra = (Localita) obj;

        // Se entrambe hanno coordinate valide, confronta con tolleranza
        if (this.haCoordinate() && altra.haCoordinate()) {
            final double tolleranza = 0.0001;
            return Math.abs(this.latitudine - altra.latitudine) < tolleranza &&
                    Math.abs(this.longitudine - altra.longitudine) < tolleranza;
        }

        // Altrimenti confronta tutti i dati testuali
        return this.nazione.equalsIgnoreCase(altra.nazione) &&
                this.citta.equalsIgnoreCase(altra.citta) &&
                ((this.indirizzo == null && altra.indirizzo == null) ||
                        (this.indirizzo != null && altra.indirizzo != null &&
                                this.indirizzo.equalsIgnoreCase(altra.indirizzo)));

    }
}