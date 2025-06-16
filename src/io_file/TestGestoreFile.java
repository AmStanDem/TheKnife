package io_file;

import Entita.*;
import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;

/**
 * Classe di testing per verificare tutte le operazioni del GestoreFile.
 *
 * @author Test Suite
 * @version 1.0
 */
public class TestGestoreFile {

    private static int testsEseguiti = 0;
    private static int testsPassati = 0;

    public static void main(String[] args) {
        System.out.println("=== INIZIO TESTING GESTORE FILE ===\n");

        try {
            // Test delle operazioni sui ristoranti
            testOperazioniRistoranti();

            // Test delle operazioni sugli utenti
            testOperazioniUtenti();

            // Test delle operazioni sulle recensioni
            testOperazioniRecensioni();

            // Test delle operazioni sui preferiti
            testOperazioniPreferiti();

            // Test di integrazione
            testIntegrazione();

        } catch (Exception e) {
            System.err.println("ERRORE DURANTE I TEST: " + e.getMessage());
            e.printStackTrace();
        }

        // Risultati finali
        System.out.println("\n=== RISULTATI FINALI ===");
        System.out.println("Test eseguiti: " + testsEseguiti);
        System.out.println("Test passati: " + testsPassati);
        System.out.println("Test falliti: " + (testsEseguiti - testsPassati));
        System.out.println("Percentuale successo: " +
                (testsEseguiti > 0 ? (testsPassati * 100.0 / testsEseguiti) : 0) + "%");
    }

    // ================ TEST OPERAZIONI RISTORANTI ================

    private static void testOperazioniRistoranti() throws IOException, CsvException {
        System.out.println("--- TEST OPERAZIONI RISTORANTI ---");

        // Crea ristoranti di test
        Localita localita1 = new Localita("Italia", "Roma", "Via del Corso 123", 41.9028, 12.4964);
        Localita localita2 = new Localita("Italia", "Milano", "Via Montenapoleone 456", 45.4642, 9.1900);

        Ristorante ristorante1 = new Ristorante("Da Mario", localita1, TipoCucina.ITALIANA,
                true, true, 25.50f, "Autentica cucina romana");
        Ristorante ristorante2 = new Ristorante("Sushi Zen", localita2, TipoCucina.GIAPPONESE,
                false, true, 45.00f, "Sushi fresco e di qualità");

        // Test 1: Caricamento ristoranti iniziale
        testCaricamentoRistorantiIniziale();

        // Test 2: Aggiunta ristorante
        testAggiuntaRistorante(ristorante1);

        // Test 3: Controllo duplicati
        testControlloDuplicatiRistorante(ristorante1);

        // Test 4: Ricerca ristorante
        testRicercaRistorante(ristorante1);

        // Test 5: Aggiornamento ristorante
        testAggiornamentoRistorante(ristorante1, ristorante2);

        // Test 6: Conteggio ristoranti
        testConteggioRistoranti();

        // Test 7: Eliminazione ristorante
        testEliminazioneRistorante(ristorante2);

        System.out.println();
    }

    private static void testCaricamentoRistorantiIniziale() throws IOException, CsvException {
        try {
            LinkedList<Ristorante> ristoranti = GestoreFile.caricaRistoranti();
            assertTrue("Caricamento ristoranti iniziale", ristoranti != null);
            System.out.println("✓ Ristoranti caricati: " + ristoranti.size());
        } catch (Exception e) {
            assertFalse("Caricamento ristoranti iniziale", "Eccezione: " + e.getMessage());
        }
    }

    private static void testAggiuntaRistorante(Ristorante ristorante) throws IOException, CsvException {
        boolean aggiunto = GestoreFile.aggiungiRistorante(ristorante);
        assertTrue("Aggiunta ristorante", aggiunto);
    }

    private static void testControlloDuplicatiRistorante(Ristorante ristorante) throws IOException, CsvException {
        boolean duplicato = GestoreFile.aggiungiRistorante(ristorante);
        assertFalse("Controllo duplicati ristorante", duplicato);
    }

    private static void testRicercaRistorante(Ristorante ristorante) throws IOException, CsvException {
        Ristorante trovato = GestoreFile.cercaRistorante(ristorante.getNome(), ristorante.getLocalita());
        assertTrue("Ricerca ristorante", trovato != null && trovato.getNome().equals(ristorante.getNome()));
    }

    private static void testAggiornamentoRistorante(Ristorante vecchio, Ristorante nuovo) throws IOException, CsvException {
        // Modifica il nuovo ristorante per avere la stessa località del vecchio
        Ristorante nuovoConStessaLocalita = new Ristorante(
                vecchio.getNome(),
                vecchio.getLocalita(),
                nuovo.getTipoDiCucina(),
                nuovo.getDelivery(),
                nuovo.getPrenotazione(),
                nuovo.getPrezzoMedio() + 10, // Prezzo diverso
                "Descrizione aggiornata"
        );

        boolean aggiornato = GestoreFile.aggiornaRistorante(vecchio, nuovoConStessaLocalita);
        assertTrue("Aggiornamento ristorante", aggiornato);
    }

    private static void testConteggioRistoranti() throws IOException, CsvException {
        int count = GestoreFile.contaRistoranti();
        assertTrue("Conteggio ristoranti", count >= 0);
        System.out.println("✓ Numero ristoranti nel database: " + count);
    }

    private static void testEliminazioneRistorante(Ristorante ristorante) throws IOException, CsvException {
        // Prima trova un ristorante esistente da eliminare
        LinkedList<Ristorante> ristoranti = GestoreFile.caricaRistoranti();
        if (!ristoranti.isEmpty()) {
            Ristorante daEliminare = ristoranti.getFirst();
            boolean eliminato = GestoreFile.eliminaRistorante(daEliminare);
            assertTrue("Eliminazione ristorante", eliminato);
        } else {
            System.out.println("⚠ Nessun ristorante da eliminare per il test");
        }
    }

    // ================ TEST OPERAZIONI UTENTI ================

    private static void testOperazioniUtenti() throws IOException, CsvException {
        System.out.println("--- TEST OPERAZIONI UTENTI ---");

        // Crea utenti di test
        Cliente cliente = new Cliente("Mario", "Rossi", "mario.rossi", "password123",
                LocalDate.of(1990, 5, 15), "Roma");
        Ristoratore ristoratore = new Ristoratore("Giuseppe", "Bianchi", "giuseppe.bianchi", "password456",
                LocalDate.of(1985, 8, 20), "Milano");

        // Test caricamento utenti
        testCaricamentoUtenti();

        // Test aggiunta utenti
        testAggiuntaUtente(cliente);
        testAggiuntaUtente(ristoratore);

        // Test controllo duplicati utenti
        testControlloDuplicatiUtente(cliente);

        // Test ricerca utenti
        testRicercaUtente(cliente, ristoratore);

        // Test login
        testLogin(cliente);

        // Test caricamento per tipologia
        testCaricamentoPerTipologia();

        System.out.println();
    }

    private static void testCaricamentoUtenti() throws IOException, CsvException {
        LinkedList<Utente> utenti = GestoreFile.caricaUtenti();
        assertTrue("Caricamento utenti", utenti != null);
        System.out.println("✓ Utenti caricati: " + utenti.size());
    }

    private static void testAggiuntaUtente(Utente utente) throws IOException, CsvException {
        boolean aggiunto = GestoreFile.aggiungiUtente(utente);
        assertTrue("Aggiunta utente " + utente.getTipoUtente(), aggiunto);
    }

    private static void testControlloDuplicatiUtente(Utente utente) throws IOException, CsvException {
        boolean duplicato = GestoreFile.aggiungiUtente(utente);
        assertFalse("Controllo duplicati utente", duplicato);
    }

    private static void testRicercaUtente(Cliente cliente, Ristoratore ristoratore) throws IOException, CsvException {
        Utente clienteTrovato = GestoreFile.cercaUtente(cliente.getUsername());
        assertTrue("Ricerca cliente", clienteTrovato instanceof Cliente);

        Utente ristoratoreTrovato = GestoreFile.cercaUtente(ristoratore.getUsername());
        assertTrue("Ricerca ristoratore", ristoratoreTrovato instanceof Ristoratore);
    }

    private static void testLogin(Cliente cliente) throws IOException, CsvException {
        Utente loginValido = GestoreFile.verificaLogin(cliente.getUsername(), "password123");
        assertTrue("Login valido", loginValido != null);

        Utente loginInvalido = GestoreFile.verificaLogin(cliente.getUsername(), "passwordSbagliata");
        assertTrue("Login invalido", loginInvalido == null);
    }

    private static void testCaricamentoPerTipologia() throws IOException, CsvException {
        LinkedList<Cliente> clienti = GestoreFile.caricaClienti();
        LinkedList<Ristoratore> ristoratori = GestoreFile.caricaRistoratori();

        assertTrue("Caricamento clienti", clienti != null);
        assertTrue("Caricamento ristoratori", ristoratori != null);

        System.out.println("✓ Clienti: " + clienti.size() + ", Ristoratori: " + ristoratori.size());
    }

    // ================ TEST OPERAZIONI RECENSIONI ================

    private static void testOperazioniRecensioni() throws IOException, CsvException {
        System.out.println("--- TEST OPERAZIONI RECENSIONI ---");

        // Prima ottieni un cliente e un ristorante esistenti
        LinkedList<Cliente> clienti = GestoreFile.caricaClienti();
        LinkedList<Ristorante> ristoranti = GestoreFile.caricaRistoranti();

        if (clienti.isEmpty() || ristoranti.isEmpty()) {
            System.out.println("⚠ Impossibile testare recensioni: mancano clienti o ristoranti");
            return;
        }

        Cliente cliente = clienti.getFirst();
        Ristorante ristorante = ristoranti.getFirst();

        // Crea recensione di test
        Recensione recensione = new Recensione(cliente, ristorante, 4,
                "Ottimo ristorante, ci tornerò sicuramente!", LocalDateTime.now());

        // Test caricamento recensioni
        testCaricamentoRecensioni();

        // Test aggiunta recensione
        testAggiuntaRecensione(recensione);

        // Test controllo duplicati recensione
        testControlloDuplicatiRecensione(recensione);

        // Test caricamento recensioni per cliente
        testCaricamentoRecensioniCliente(cliente);

        // Test caricamento recensioni per ristorante
        testCaricamentoRecensioniRistorante(ristorante);

        // Test ricerca recensione specifica
        testRicercaRecensione(cliente, ristorante);

        System.out.println();
    }

    private static void testCaricamentoRecensioni() throws IOException, CsvException {
        LinkedList<Recensione> recensioni = GestoreFile.caricaRecensioni();
        assertTrue("Caricamento recensioni", recensioni != null);
        System.out.println("✓ Recensioni caricate: " + recensioni.size());
    }

    private static void testAggiuntaRecensione(Recensione recensione) throws IOException, CsvException {
        boolean aggiunta = GestoreFile.aggiungiRecensione(recensione);
        assertTrue("Aggiunta recensione", aggiunta);
    }

    private static void testControlloDuplicatiRecensione(Recensione recensione) throws IOException, CsvException {
        boolean duplicata = GestoreFile.aggiungiRecensione(recensione);
        assertFalse("Controllo duplicati recensione", duplicata);
    }

    private static void testCaricamentoRecensioniCliente(Cliente cliente) throws IOException, CsvException {
        LinkedList<Recensione> recensioni = GestoreFile.caricaRecensioniCliente(cliente.getUsername());
        assertTrue("Caricamento recensioni cliente", recensioni != null);
        System.out.println("✓ Recensioni del cliente " + cliente.getUsername() + ": " + recensioni.size());
    }

    private static void testCaricamentoRecensioniRistorante(Ristorante ristorante) throws IOException, CsvException {
        LinkedList<Recensione> recensioni = GestoreFile.caricaRecensioniRistorante(ristorante);
        assertTrue("Caricamento recensioni ristorante", recensioni != null);
        System.out.println("✓ Recensioni del ristorante " + ristorante.getNome() + ": " + recensioni.size());
    }

    private static void testRicercaRecensione(Cliente cliente, Ristorante ristorante) throws IOException, CsvException {
        Recensione trovata = GestoreFile.cercaRecensione(cliente, ristorante);
        assertTrue("Ricerca recensione specifica", trovata != null);
    }

    // ================ TEST OPERAZIONI PREFERITI ================

    private static void testOperazioniPreferiti() throws IOException, CsvException {
        System.out.println("--- TEST OPERAZIONI PREFERITI ---");

        // Ottieni un cliente e un ristorante esistenti
        LinkedList<Cliente> clienti = GestoreFile.caricaClienti();
        LinkedList<Ristorante> ristoranti = GestoreFile.caricaRistoranti();

        if (clienti.isEmpty() || ristoranti.isEmpty()) {
            System.out.println("⚠ Impossibile testare preferiti: mancano clienti o ristoranti");
            return;
        }

        Cliente cliente = clienti.getFirst();
        Ristorante ristorante = ristoranti.getFirst();

        // Test caricamento preferiti
        testCaricamentoPreferiti(cliente);

        // Test aggiunta preferito
        testAggiuntaPreferito(cliente, ristorante);

        // Test controllo duplicati preferito
        testControlloDuplicatiPreferito(cliente, ristorante);

        // Test esistenza preferito
        testEsistenzaPreferito(cliente, ristorante);

        // Test conteggio preferiti
        testConteggioPreferiti(cliente);

        // Test rimozione preferito
        //testRimozionePreferito(cliente, ristorante);

        System.out.println();
    }

    private static void testCaricamentoPreferiti(Cliente cliente) throws IOException, CsvException {
        LinkedList<Ristorante> preferiti = GestoreFile.caricaPreferiti(cliente.getUsername());
        assertTrue("Caricamento preferiti", preferiti != null);
        System.out.println("✓ Preferiti del cliente " + cliente.getUsername() + ": " + preferiti.size());
    }

    private static void testAggiuntaPreferito(Cliente cliente, Ristorante ristorante) throws IOException, CsvException {
        boolean aggiunto = GestoreFile.aggiungiPreferito(cliente, ristorante);
        assertTrue("Aggiunta preferito", aggiunto);
    }

    private static void testControlloDuplicatiPreferito(Cliente cliente, Ristorante ristorante) throws IOException, CsvException {
        boolean duplicato = GestoreFile.aggiungiPreferito(cliente, ristorante);
        assertFalse("Controllo duplicati preferito", duplicato);
    }

    private static void testEsistenzaPreferito(Cliente cliente, Ristorante ristorante) throws IOException, CsvException {
        boolean esistente = GestoreFile.esistePreferito(cliente.getUsername(), ristorante);
        assertTrue("Esistenza preferito", esistente);
    }

    private static void testConteggioPreferiti(Cliente cliente) throws IOException, CsvException {
        int count = GestoreFile.contaPreferiti(cliente.getUsername());
        assertTrue("Conteggio preferiti", count >= 0);
        System.out.println("✓ Numero preferiti: " + count);
    }

    private static void testRimozionePreferito(Cliente cliente, Ristorante ristorante) throws IOException, CsvException {
        boolean rimosso = GestoreFile.rimuoviPreferito(cliente, ristorante);
        assertTrue("Rimozione preferito", rimosso);
    }

    // ================ TEST DI INTEGRAZIONE ================

    private static void testIntegrazione() throws IOException, CsvException {
        System.out.println("--- TEST DI INTEGRAZIONE ---");

        // Test workflow completo: cliente registra, cerca ristorante, aggiunge preferito, recensisce
        testWorkflowCompleto();

        // Test coerenza dati
        testCoerenzaDati();

        System.out.println();
    }

    private static void testWorkflowCompleto() throws IOException, CsvException {
        try {
            // 1. Nuovo cliente
            Cliente nuovoCliente = new Cliente("Test", "User", "test.integration", "testpass",
                    LocalDate.of(1995, 1, 1), "Test City");
            boolean clienteAggiunto = GestoreFile.aggiungiUtente(nuovoCliente);

            // 2. Nuovo ristorante
            Localita testLocalita = new Localita("Test Country", "Test City", "Test Address 1", 0.0, 0.0);
            Ristorante nuovoRistorante = new Ristorante("Test Restaurant", testLocalita,
                    TipoCucina.ITALIANA, true, true, 30.0f, "Test description");
            boolean ristoranteAggiunto = GestoreFile.aggiungiRistorante(nuovoRistorante);

            // 3. Aggiunta preferito
            boolean preferitoAggiunto = GestoreFile.aggiungiPreferito(nuovoCliente, nuovoRistorante);

            // 4. Aggiunta recensione
            Recensione testRecensione = new Recensione(nuovoCliente, nuovoRistorante, 5,
                    "Test review", LocalDateTime.now());
            boolean recensioneAggiunta = GestoreFile.aggiungiRecensione(testRecensione);

            boolean workflowCompleto = clienteAggiunto && ristoranteAggiunto &&
                    preferitoAggiunto && recensioneAggiunta;

            assertTrue("Workflow completo", workflowCompleto);

            // Cleanup
            GestoreFile.eliminaRistorante(nuovoRistorante);
            GestoreFile.rimuoviPreferito(nuovoCliente, nuovoRistorante);

        } catch (Exception e) {
            assertFalse("Workflow completo", "Eccezione: " + e.getMessage());
        }
    }

    private static void testCoerenzaDati() throws IOException, CsvException {
        // Verifica che tutti i dati caricati siano coerenti
        LinkedList<Utente> utenti = GestoreFile.caricaUtenti();
        LinkedList<Ristorante> ristoranti = GestoreFile.caricaRistoranti();
        LinkedList<Recensione> recensioni = GestoreFile.caricaRecensioni();

        boolean coerenza = true;

        // Verifica che tutte le recensioni abbiano clienti e ristoranti validi
        for (Recensione recensione : recensioni) {
            boolean clienteValido = utenti.stream()
                    .anyMatch(u -> u instanceof Cliente && u.getUsername().equals(recensione.getCliente().getUsername()));
            boolean ristoranteValido = ristoranti.stream()
                    .anyMatch(r -> r.getNome().equals(recensione.getRistorante().getNome()));

            if (!clienteValido || !ristoranteValido) {
                coerenza = false;
                break;
            }
        }

        assertTrue("Coerenza dati", coerenza);
    }

    // ================ UTILITY METODI DI ASSERZIONE ================

    private static void assertTrue(String testName, boolean condition) {
        testsEseguiti++;
        if (condition) {
            testsPassati++;
            System.out.println("✓ " + testName + " - PASSATO");
        } else {
            System.out.println("✗ " + testName + " - FALLITO");
        }
    }

    private static void assertFalse(String testName, boolean condition) {
        assertTrue(testName, !condition);
    }

    private static void assertFalse(String testName, String errorMessage) {
        testsEseguiti++;
        System.out.println("✗ " + testName + " - FALLITO: " + errorMessage);
    }
}