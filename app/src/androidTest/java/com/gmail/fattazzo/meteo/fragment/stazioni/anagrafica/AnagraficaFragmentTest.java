/*
 * Project: meteo
 * File: AnagraficaFragmentTest
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

package com.gmail.fattazzo.meteo.fragment.stazioni.anagrafica;

import com.gmail.fattazzo.meteo.BaseTest;
import com.gmail.fattazzo.meteo.R;

import org.junit.Before;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.gmail.fattazzo.meteo.matcher.Matcher.withListNotEmpty;
import static com.gmail.fattazzo.meteo.matcher.Matcher.withListSize;

/**
 * @author fattazzo
 * <p/>
 * date: 05/03/16
 */
public class AnagraficaFragmentTest extends BaseTest {

    //private MeteoDatabaseHandler databaseHandler;

    @Before
    public void setUp() throws Exception {

        //databaseHandler = MeteoDatabaseHandler.getInstance(getContext());
        //assertNotNull("Context nullo", databaseHandler);
    }

    @Test
    public void caricaAnagraficaStazioni() {

        selectMenu(AppMenu.STAZIONI);

        // Mi sposto sulla sezione anagrafica
        String[] stringArray = getContext().getResources().getStringArray(R.array.stazioni_sezioni);
        onView(withText(stringArray[1])).perform(click());

        // Carico le stazioni presenti e verifico che tutte siano caricate nella ListView
        checkView();

        // Eseguo un refresh
        onView(withId(R.id.refreshButton)).perform(click());

        // Verifico di nuovo che tutto sia caricato
        checkView();

        // Cambio menu e ritorno per verificare che tutto funzioni
        selectMenu(AppMenu.HOME);
        selectMenu(AppMenu.STAZIONI);
        onView(withText(stringArray[1])).perform(click());

        checkView();
    }

    private void checkView() {
        int countStazioni = 0;//databaseHandler.getCountStazioni();
        onView(withId(R.id.anag_stazioni_list)).check(matches(withListNotEmpty()));
        onView(withId(R.id.anag_stazioni_list)).check(matches(withListSize(countStazioni)));
    }
}