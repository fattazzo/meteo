/*
 * Project: meteo
 * File: BaseTest
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

package com.gmail.fattazzo.meteo;

import android.content.Context;

import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.Suppress;
import androidx.test.rule.ActivityTestRule;

import com.gmail.fattazzo.meteo.activity.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author fattazzo
 * <p/>
 * date: 04/03/16
 */
@RunWith(AndroidJUnit4.class)
public class BaseTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);
    private String[] menuEntry;
    private Context context;

    public Context getContext() {
        if (context == null) {
            context = InstrumentationRegistry.getTargetContext();
        }
        assertNotNull("Context nullo", context);

        return context;
    }

    public String[] getMenuEntry() {
        if (menuEntry == null) {
            //menuEntry = getContext().getResources().getStringArray(R.array.menu_title);
        }
        assertTrue("Menu entry non valide", menuEntry != null && menuEntry.length > 0);

        return menuEntry;
    }

    @Suppress
    @Test
    public void verificaMenuEntry() {

        assertTrue("Numero menu non esatto", getMenuEntry().length == AppMenu.values().length);
    }

    protected void selectMenu(AppMenu menu, AppMenuSwipeAction swipeAction) {

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        swipeAction.getAction().run();

        onView(withText(getMenuEntry()[menu.ordinal()])).check(matches(isDisplayed()));
        onView(withText(getMenuEntry()[menu.ordinal()])).perform(click());
    }

    protected void selectMenu(AppMenu menu) {
        selectMenu(menu, AppMenuSwipeAction.NONE);
    }

    public enum AppMenu {HOME, BOLL_PROB, BOLL_LOCALE, BOLL_SINTETICO, STAZIONI, NEVE_VALANGHE, RADAR, WEBCAM, IMPOSTAZIONI, GUIDA, ABOUT}

    public enum AppMenuSwipeAction {

        NONE(new Runnable() {
            @Override
            public void run() {
            }
        }), UP(new Runnable() {
            @Override
            public void run() {
                onView(withId(R.id.drawer_layout)).perform(swipeUp());
            }
        }), DOWN(new Runnable() {
            @Override
            public void run() {
                onView(withId(R.id.drawer_layout)).perform(swipeDown());
            }
        });

        private Runnable action;

        AppMenuSwipeAction(Runnable action) {
            this.action = action;
        }

        /**
         * @return the action
         */
        public Runnable getAction() {
            return action;
        }
    }
}
