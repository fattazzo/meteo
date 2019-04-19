/*
 * Project: meteo
 * File: MeteoDatabaseHandlerTest
 *
 * Created by fattazzo
 * Copyright © 2019 Gianluca Fattarsi. All rights reserved.
 *
 * MIT License
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.gmail.fattazzo.meteo.db.handlers;


import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.runner.RunWith;

/**
 * @author fattazzo
 * <p/>
 * date: 06/03/16
 */
@RunWith(AndroidJUnit4.class)
public class MeteoDatabaseHandlerTest {

    private static final int NUM_INSERT = 100;

    /**
     private MeteoDatabaseHandler dbHandler;

     @Before public void setUp() throws Exception {

     // test_ to prevent you from overwriting data that may have in the same simulator.
     Context mMockContext = new RenamingDelegatingContext(InstrumentationRegistry.getInstrumentation().getTargetContext(), "test_");

     dbHandler = MeteoDatabaseHandler.getTestInstance(mMockContext);
     Assert.assertNotNull("Null database handler", dbHandler);
     }

     @After public void tearDown() throws Exception {

     String[] createTables = MeteoDatabase.dropTablesSQL();
     for (String sql : createTables) {
     dbHandler.getWritableDatabase().execSQL(sql);
     }
     dbHandler.onCreate(dbHandler.getWritableDatabase());
     }**/

    /**
     * private void inserisciStazioniMeteo() {
     * <p>
     * List<StazioneMeteo> stazioni = new ArrayList<>();
     * for (int i = 1; i <= NUM_INSERT; i++) {
     * StazioneMeteo stazione = new StazioneMeteo();
     * stazione.setCodice("codice" + i);
     * stazione.setEst(0.1);
     * stazione.setLatitudine(0.1);
     * stazione.setLongitudine(0.1);
     * stazione.setNome("nome" + i);
     * stazione.setNomeBreve("nome breve" + i);
     * stazione.setNord(0.1);
     * stazione.setQuota(1);
     * stazioni.add(stazione);
     * }
     * //dbHandler.salvaStazioniMeteo(stazioni);
     * }
     **/

    /**
     private void inserisciStazioniValanghe() {

     List<StazioneValanghe> stazioni = new ArrayList<>();
     for (int i = 1; i <= NUM_INSERT; i++) {
     StazioneValanghe stazione = new StazioneValanghe();
     stazione.setQuota(1);
     stazione.setNomeBreve("nome breve" + i);
     stazione.setNome("nome" + i);
     stazione.setCodice("codice" + i);
     stazione.setLatitudine(0.1);
     stazione.setLongitudine(0.1);
     stazioni.add(stazione);
     }
     dbHandler.salvaStazioniValanghe(stazioni);
     }
     **/

    /**
     @Test public void testCancellaStazioniMeteo() throws Exception {

     List<StazioneMeteo> stazioni = dbHandler.caricaStazioniMeteo();
     Assert.assertTrue("Stazioni meteo presenti", stazioni.isEmpty());

     inserisciStazioniMeteo();

     dbHandler.cancellaStazioniMeteo();
     stazioni = dbHandler.caricaStazioniMeteo();
     Assert.assertTrue("Stazioni meteo presenti", stazioni.isEmpty());
     }

     @Test public void testCancellaStazioniValanghe() throws Exception {

     List<StazioneValanghe> stazioni = dbHandler.caricaStazioniValanghe();
     Assert.assertTrue("Stazioni valanghe presenti", stazioni.isEmpty());

     inserisciStazioniValanghe();

     dbHandler.cancellaStazioniValanghe();
     stazioni = dbHandler.caricaStazioniValanghe();
     Assert.assertTrue("Stazioni valanghe presenti", stazioni.isEmpty());
     }

     @Test public void testCaricaStazioneByCodice() throws Exception {

     inserisciStazioniMeteo();

     StazioneMeteo stazioneMeteo = dbHandler.caricaStazioneByCodice("codice1");
     Assert.assertNotNull("Stazione meteo 'codice1' non caricata", stazioneMeteo);

     stazioneMeteo = dbHandler.caricaStazioneByCodice("abcd");
     Assert.assertNull(stazioneMeteo);
     }

     @Test public void testCaricaStazioniMeteo() throws Exception {

     inserisciStazioniMeteo();

     List<StazioneMeteo> stazioni = dbHandler.caricaStazioniMeteo();
     Assert.assertNotNull("Stazioni meteo non caricate", stazioni);
     Assert.assertTrue("Caricate " + stazioni.size() + " stazioni meteo invece di " + NUM_INSERT, stazioni.size() == NUM_INSERT);
     }

     @Test public void testCaricaStazioniValanghe() throws Exception {

     inserisciStazioniValanghe();

     List<StazioneValanghe> stazioni = dbHandler.caricaStazioniValanghe();
     Assert.assertNotNull("Stazioni valanghe non caricate", stazioni);
     Assert.assertTrue("Caricate " + stazioni.size() + " stazioni valanghe invece di " + NUM_INSERT, stazioni.size() == NUM_INSERT);
     }

     @Test public void testGetCountStazioni() throws Exception {

     inserisciStazioniMeteo();

     int countStazioni = dbHandler.getCountStazioni();
     Assert.assertEquals(countStazioni, NUM_INSERT);
     }

     @Test public void testGetCountStazioniValanghe() throws Exception {

     inserisciStazioniValanghe();

     int countStazioniValanghe = dbHandler.getCountStazioniValanghe();
     Assert.assertEquals(countStazioniValanghe, NUM_INSERT);
     }

     @Test public void testOnCreate() throws Exception {

     SQLiteDatabase db = dbHandler.getWritableDatabase();

     String[] createTables = MeteoDatabase.dropTablesSQL();
     for (String sql : createTables) {
     db.execSQL(sql);
     }

     for (TableStructure<?> table : MeteoDatabase.tables) {
     try {
     db.execSQL("SELECT * FROM " + table.getTableName());
     } catch (SQLiteException e) {
     Assert.assertTrue(e.getMessage().contains("no such table"));
     }
     }

     dbHandler.onCreate(db);

     for (TableStructure<?> table : MeteoDatabase.tables) {
     db.execSQL("SELECT * FROM " + table.getTableName());
     }
     }

     @Test public void testSalvaStazioniMeteo() throws Exception {

     List<StazioneMeteo> stazioni = dbHandler.caricaStazioniMeteo();
     Assert.assertTrue("Stazioni meteo presenti", stazioni.isEmpty());

     inserisciStazioniMeteo();

     stazioni = dbHandler.caricaStazioniMeteo();
     Assert.assertTrue("Nessuna stazione meteo presente", !stazioni.isEmpty());
     Assert.assertTrue("Stazioni meteo presenti " + stazioni.size() + " non " + NUM_INSERT, stazioni.size() == NUM_INSERT);
     }

     @Test public void testSalvaStazioniValanghe() throws Exception {

     List<StazioneValanghe> stazioni = dbHandler.caricaStazioniValanghe();
     Assert.assertTrue("Stazioni valanghe presenti", stazioni.isEmpty());

     inserisciStazioniValanghe();

     stazioni = dbHandler.caricaStazioniValanghe();
     Assert.assertTrue("Nessuna stazione valanghe presente", !stazioni.isEmpty());
     Assert.assertTrue("Stazioni valanghe presenti " + stazioni.size() + " non " + NUM_INSERT, stazioni.size() == NUM_INSERT);
     }
     **/
}