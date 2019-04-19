package com.gmail.fattazzo.meteo.parser.xml;


/**
 * @author fattazzo
 *         <p/>
 *         date: 01/03/16
 */
public class StazioniValangheXmlParserTest {

    /**
    @Test
    public void testCaricaAnagrafica() throws Exception {

        StazioniValangheXmlParser parser = new StazioniValangheXmlParser();

        List<StazioneValanghe> stazioni = null;
        try {
            stazioni = parser.caricaAnagrafica();
        } catch (Exception e) {
            fail("Errore durante il caricamento delle stazioni valanghe " + e.getMessage());
        }

        assertTrue("Nessuna stazione valanghe caricata", stazioni != null && !stazioni.isEmpty());
        for (StazioneValanghe stazione : stazioni) {
            assertTrue("Codice stazione valanghe assente", stazione.getCodice() != null && !stazione.getCodice().isEmpty());
        }
    }

    **/
}