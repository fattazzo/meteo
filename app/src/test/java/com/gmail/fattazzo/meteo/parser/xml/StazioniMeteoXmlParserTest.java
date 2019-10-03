package com.gmail.fattazzo.meteo.parser.xml;

/**
 * @author fattazzo
 *         <p/>
 *         date: 01/03/16
 */
public class StazioniMeteoXmlParserTest {

    /**
     @Test public void testCaricaAnagrafica() throws Exception {

     StazioniMeteoXmlParser parser = new StazioniMeteoXmlParser();

     List<StazioneMeteo> stazioni = null;
     try {
     stazioni = parser.caricaAnagrafica();
     } catch (Exception e) {
     fail("Errore durante il caricamento delle stazioni meteo " + e.getMessage());
     }

     assertTrue("Nessuna stazione meteo caricata", stazioni != null && !stazioni.isEmpty());
     for (StazioneMeteo stazione : stazioni) {
     assertTrue("Codice stazione non presente", stazione.getCodice() != null && !stazione.getCodice().isEmpty());
     }
     }

     @Test public void testCaricaDatiStazione() throws Exception {

     StazioniMeteoXmlParser parser = new StazioniMeteoXmlParser();

     List<StazioneMeteo> stazioni = parser.caricaAnagrafica();

     for (StazioneMeteo stazione : stazioni) {
     try {
     parser.caricaDatiStazione(stazione.getCodice());
     } catch (Exception e) {
     fail("Errore durante il caricamento dei dati della stazione " + stazione.getCodice() + " " + e.getMessage());
     }
     }
     }
     **/
}