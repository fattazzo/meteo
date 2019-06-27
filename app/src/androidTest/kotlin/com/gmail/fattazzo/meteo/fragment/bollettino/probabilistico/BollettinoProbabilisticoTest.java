/*
 * Project: meteo
 * File: BollettinoProbabilisticoTest
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

package com.gmail.fattazzo.meteo.fragment.bollettino.probabilistico;


import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.gmail.fattazzo.meteo.BaseTest;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author fattazzo
 * <p/>
 * date: 29/02/16
 */
@RunWith(AndroidJUnit4.class)
public class BollettinoProbabilisticoTest extends BaseTest {

    @Test
    public void caricaBollettinoProbabilistico() {

        /**
         // Apro la sezione del bollettino probabilistico
         selectMenu(AppMenu.BOLL_PROB);

         // Verifico che la previsione si stata caricata
         onView(withId(R.id.boll_prob_layout)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

         // Vado sulla home e ritorno sul bollettino probabilistico per verificare che sia ancora caricata la previsione
         selectMenu(AppMenu.HOME);
         selectMenu(AppMenu.BOLL_PROB);
         onView(withId(R.id.boll_prob_layout)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
         **/
    }
}
