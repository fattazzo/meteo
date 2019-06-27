/*
 * Project: meteo
 * File: RilevazioniFragmentTest
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

package com.gmail.fattazzo.meteo.fragment.stazioni.rilevazioni;

import android.view.View;
import android.widget.Spinner;

import androidx.test.espresso.matcher.BoundedMatcher;

import com.gmail.fattazzo.meteo.BaseTest;
import com.gmail.fattazzo.meteo.db.StazioneMeteo;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * @author fattazzo
 * <p/>
 * date: 05/03/16
 */
public class RilevazioniFragmentTest extends BaseTest {

    /**
     @Test public void expandCollapsData() {

     selectMenu(AppMenu.STAZIONI);

     // Mi sposto sull'anagrafica per assicurarmi che le stazioni vengano caricate
     String[] stringArray = getContext().getResources().getStringArray(R.array.stazioni_sezioni);
     onView(withText(stringArray[1])).perform(click());
     onView(withText(stringArray[0])).perform(click());

     //Seleziono la prima stazione
     onView(withId(R.id.dati_stazione_spinner)).perform(click());
     onData(anything()).atPosition(1).perform(click());

     onView(withId(R.id.anag_stazione_controls_layout)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

     onView(withId(R.id.dati_stazione_hide_panel_img)).perform(click());
     onView(withId(R.id.anag_stazione_controls_layout)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));

     onView(withId(R.id.dati_stazione_hide_panel_img)).perform(click());
     onView(withId(R.id.anag_stazione_controls_layout)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
     }

     @Test public void caricaRilevazioniStazioni() {

     selectMenu(AppMenu.STAZIONI);

     // Mi sposto sull'anagrafica per assicurarmi che le stazioni vengano caricate
     String[] stringArray = getContext().getResources().getStringArray(R.array.stazioni_sezioni);
     onView(withText(stringArray[1])).perform(click());
     onView(withText(stringArray[0])).perform(click());

     // All'inizio non devo avere nulla di caricato
     checkEmptyView();

     // Per le prime 10 stazioni provo a caricare i dati
     for (int i = 1; i < 11; i++) {
     onView(withId(R.id.dati_stazione_spinner)).perform(click());
     onData(anything()).atPosition(i).perform(click());
     checkView();

     // Riseleziono la prima stazione per svuotare le view per il prossimo controllo
     onView(withId(R.id.dati_stazione_spinner)).perform(click());
     onData(anything()).atPosition(0).perform(click());
     checkEmptyView();
     }


     }

     private void checkView() {

     onView(withId(R.id.dati_stazione_spinner)).check(matches(withStazioniSpinnerText(not(""))));

     String[] stringArray = getContext().getResources().getStringArray(R.array.tipo_dato_stazione);

     // Seleziono i dati delle precipitazioni
     onView(withId(R.id.dati_stazione_tipo_dato)).perform(click());
     onData(allOf(is(instanceOf(String.class)), is(stringArray[1]))).perform(click());
     onView(withId(R.id.dati_stazione_tipo_dato)).check(matches(withSpinnerText(containsString(stringArray[1]))));

     onView(withId(R.id.dati_stazione_visualizzazione_grafico)).perform(click());
     onView(withId(R.id.dati_stazione_visualizzazione_tabella)).perform(click());

     onView(withId(R.id.dati_stazione_visualizzazione_layout)).check(matches(com.gmail.fattazzo.meteo.matcher.Matcher.childCount(1 )));

     // Seleziono i dati delle temperature
     onView(withId(R.id.dati_stazione_tipo_dato)).perform(click());
     onData(allOf(is(instanceOf(String.class)), is(stringArray[0]))).perform(click());
     onView(withId(R.id.dati_stazione_tipo_dato)).check(matches(withSpinnerText(containsString(stringArray[0]))));

     onView(withId(R.id.dati_stazione_visualizzazione_grafico)).perform(click());
     onView(withId(R.id.dati_stazione_visualizzazione_tabella)).perform(click());

     onView(withId(R.id.dati_stazione_visualizzazione_layout)).check(matches(com.gmail.fattazzo.meteo.matcher.Matcher.childCount(1 )));
     }

     private void checkEmptyView() {

     onView(withId(R.id.dati_stazione_spinner)).check(matches(withStazioniSpinnerText(is(""))));

     String[] stringArray = getContext().getResources().getStringArray(R.array.tipo_dato_stazione);
     onView(withId(R.id.dati_stazione_tipo_dato)).check(matches(withSpinnerText(stringArray[0])));

     onView(withId(R.id.dati_stazione_visualizzazione_layout)).check(matches(com.gmail.fattazzo.meteo.matcher.Matcher.childCount(0)));
     }
     **/

    /**
     * Returns a matcher that matches {@link Spinner}s based on {@link StazioneMeteo#getNome()} value of the selected item.
     *
     * @param stringMatcher <code>Matcher</code></a>
     */
    private static Matcher<View> withStazioniSpinnerText(final Matcher<String> stringMatcher) {
        return new BoundedMatcher<View, Spinner>(Spinner.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("with text: ");
                stringMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(Spinner spinner) {
                StazioneMeteo stazione = (StazioneMeteo) spinner.getSelectedItem();
                return stringMatcher.matches(stazione.getNome());
            }
        };
    }
}