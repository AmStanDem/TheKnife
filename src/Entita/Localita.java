package Entita;

/**
 * Rappresenta una località geografica.
 * @author Thomas Riotto
 * @version 1.0
 */
public final class Localita {
    private String nazione;
    private String citta;
    private String indirizzo;
    private double latitudine;
    private double longitudine;

    private static final double RAGGIO_TERRA_KM = 6371.0;

    /**
     * Crea una nuova località impostando i dati geografici.
     * @param nazione La nazione della località.
     * @param citta La città della località.
     * @param indirizzo L'indirizzo della località.
     * @param latitudine La latitudine della località.
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
     * Calcola la distanza in chilometri tra questa località e un'altra
     * utilizzando la formula di Haversine.
     *
     * @param altra l'altra località
     * @return distanza in chilometri, o -1 se una delle coordinate non è disponibile
     */
    public double calcolaDistanza(Localita altra) {
        if (!hasCoordinate() || !altra.hasCoordinate()) {
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
     * Verifica se la località ha coordinate geografiche valide
     */
    public boolean hasCoordinate() {
        return latitudine != 0.0 && longitudine != 0.0;
    }

    /**
     * Calcola la distanza approssimativa basata solo su città
     * (da usare quando le coordinate precise non sono disponibili)
     * @return {@code true} se città e nazione corrispondono, {@code false} altrimenti.
     */
    public boolean stessaZonaGeografica(Localita altra) {
        return this.citta.equalsIgnoreCase(altra.citta) &&
                this.nazione.equalsIgnoreCase(altra.nazione);
    }

    // Getters e Setters
    public String getNazione() { return nazione; }
    public void setNazione(String nazione) { this.nazione = nazione; }

    public String getCitta() { return citta; }
    public void setCitta(String citta) { this.citta = citta; }

    public String getIndirizzo() { return indirizzo; }
    public void setIndirizzo(String indirizzo) { this.indirizzo = indirizzo; }

    public double getLatitudine() { return latitudine; }
    public void setLatitudine(double latitudine) { this.latitudine = latitudine; }

    public double getLongitudine() { return longitudine; }
    public void setLongitudine(double longitudine) { this.longitudine = longitudine; }

    @Override
    public String toString() {
        if (indirizzo != null && !indirizzo.isEmpty()) {
            return indirizzo + ", " + citta + ", " + nazione;
        }
        return citta + ", " + nazione;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Localita altra = (Localita) obj;

        // Se entrambe hanno coordinate valide, confronta con tolleranza
        if (this.hasCoordinate() && altra.hasCoordinate()) {
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


    // Esempio di utilizzo
    public static void main(String[] args) {
        // Milano
        Localita milano = new Localita("Italia", "Milano", "Duomo", 45.4642, 9.1900);
        System.out.println(milano);

        // Roma
        Localita roma = new Localita("Italia", "Roma", "Colosseo", 41.8902, 12.4922);
        System.out.println(roma);

        double distanza = milano.calcolaDistanza(roma);
        System.out.println("Distanza Milano-Roma: " + String.format("%.2f", distanza) + " km");
        // Output atteso: circa 477 km
    }
}