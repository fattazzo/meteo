/*
 * Project: meteo
 * File: BollettinoGeneraleTest
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

package com.gmail.fattazzo.meteo.fragment.home;


import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.gmail.fattazzo.meteo.BaseTest;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author fattazzo
 *         <p/>
 *         date: 29/02/16
 */
@RunWith(AndroidJUnit4.class)
public class BollettinoGeneraleTest extends BaseTest {

    @Test
    public void caricaBollettinoGenerale() {

        selectMenu(AppMenu.HOME);

        //onView(withId(R.id.home_layout_domani_include)).perform(ViewActions.scrollTo()).check(matches(isDisplayed()));
        //onView(withId(R.id.home_layout_dopodomani_include)).perform(ViewActions.scrollTo()).check(matches(isDisplayed()));
        //onView(withId(R.id.home_tendenza_label)).perform(ViewActions.scrollTo()).check(matches(isDisplayed()));

        //onView(withId(R.id.home_refresh_action)).perform(click());

        //onView(withId(R.id.home_layout_domani_include)).perform(ViewActions.scrollTo()).check(matches(isDisplayed()));
        //onView(withId(R.id.home_layout_dopodomani_include)).perform(ViewActions.scrollTo()).check(matches(isDisplayed()));
        //onView(withId(R.id.home_tendenza_label)).perform(ViewActions.scrollTo()).check(matches(isDisplayed()));

        // Cambio pagina e ritorno sulla home per verificare che il bollettino sia ancora caricato
        selectMenu(AppMenu.BOLL_LOCALE);
        selectMenu(AppMenu.HOME);

        //onView(withId(R.id.home_layout_domani_include)).perform(ViewActions.scrollTo()).check(matches(isDisplayed()));
        //onView(withId(R.id.home_layout_dopodomani_include)).perform(ViewActions.scrollTo()).check(matches(isDisplayed()));
        //onView(withId(R.id.home_tendenza_label)).perform(ViewActions.scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void infoDialogBollettinoGenerale() {

        selectMenu(AppMenu.HOME);

        /**
        // Dialog previsione
        //onView(withId(R.id.home_img_previsione_info)).perform(ViewActions.scrollTo()).perform(click());
        onView(withText("Informazioni previsione")).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(withId(R.id.prev_info_data_prev)).check(matches(isDisplayed()));
        onView(withText("Informazioni previsione")).inRoot(isDialog()).perform(pressBack());

        // Dialog oggi
        try {
            onView(withId(R.id.home_oggi_info)).check(matches(isDisplayed()));
            // View is displayed
            onView(withId(R.id.home_oggi_info)).perform(ViewActions.scrollTo()).perform(click());
            onView(withText("Informazioni dettagliate")).inRoot(isDialog()).check(matches(isDisplayed()));
            onView(withId(R.id.oggi_info_data)).check(matches(isDisplayed()));
            onView(withText("Informazioni dettagliate")).inRoot(isDialog()).perform(pressBack());
        } catch (AssertionFailedError e) {
            // View not displayed
        }

        // Dialog domani
        onView(withId(R.id.home_domani_info)).perform(ViewActions.scrollTo()).perform(click());
        onView(withText("Informazioni dettagliate")).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(withId(R.id.domani_info_data)).check(matches(isDisplayed()));
        onView(withText("Informazioni dettagliate")).inRoot(isDialog()).perform(pressBack());

        // Dialog dopodomani
        onView(withId(R.id.home_dopodomani_info)).perform(ViewActions.scrollTo()).perform(click());
        onView(withText("Informazioni dettagliate")).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(withId(R.id.domani_info_data)).check(matches(isDisplayed()));
        onView(withText("Informazioni dettagliate")).inRoot(isDialog()).perform(pressBack());

         **/
    }
}
