/*
 * Project: meteo
 * File: BaseTest.kt
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

package com.gmail.fattazzo.meteo

import android.content.Context
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerMatchers
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.gmail.fattazzo.meteo.activity.MainActivity_
import org.junit.Assert
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author fattazzo
 *
 *
 * date: 04/03/16
 */
@RunWith(AndroidJUnit4::class)
open class BaseTest {

    @get:Rule
    var mActivityRule = ActivityTestRule(MainActivity_::class.java)

    protected val context: Context by lazy {
        val contextTmp = mActivityRule.activity
        assertNotNull("Context nullo", contextTmp)
        contextTmp
    }

    @Test
    fun verificaMenuEntry() {

        AppMenuConfig.values().forEach {
            try {
                context.resources.getString(it.resTextId)
            } catch (e: Exception) {
                Assert.fail("Risorsa del testo non trovata per il menu ${it.name}")
            }
        }
    }

    @JvmOverloads
    protected fun selectMenu(menu: AppMenuConfig, swipeAction: AppMenuSwipeAction = AppMenuSwipeAction.NONE) {

        waitView(R.id.drawer_layout)
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open())
                .check(matches(DrawerMatchers.isOpen()))

        //onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        //onView(withId(R.id.drawer_layout)).check(matches(isDisplayed()))

        //onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT))).perform(DrawerActions.open())
        //swipeAction.action.invoke()

        Thread.sleep(500L)
        onView(withId(R.id.nav_view)).check(matches(isDisplayed()))
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(menu.resId))
    }

    fun waitView(viewId: Int) {
        var timeout = 0L
        var viewFound = false
        while(timeout < 10000) {
            try {
                onView(withId(viewId)).check(matches(isDisplayed()))
                viewFound = true
                break
            } catch (e: Exception) {
                Thread.sleep(1000L)
                timeout += 1000L
            }
        }
        assertTrue("View not found!",viewFound)
    }

    enum class AppMenuSwipeAction(val action: () -> Unit) {

        NONE({}),
        UP({ onView(withId(R.id.drawer_layout)).perform(swipeUp()) }),
        DOWN({ onView(withId(R.id.drawer_layout)).perform(swipeDown()) })
    }
}
