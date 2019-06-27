package com.gmail.fattazzo.meteo.matcher

import android.view.View
import android.view.ViewGroup
import android.widget.ListView

import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

/**
 * @author fattazzo
 *
 *
 * date: 02/03/16
 */
object Matcher {

    /**
     * Esegue il match sul per una ViewGroup non vuota.
     *
     * @return Matcher
     */
    val isNotEmpty: org.hamcrest.Matcher<View>
        get() = object : TypeSafeMatcher<View>() {

            public override fun matchesSafely(view: View): Boolean {
                return view is ViewGroup && view.childCount > 0
            }

            override fun describeTo(description: Description) {
                description.appendText("ViewGroup is not empty")
            }
        }

    /**
     * Esegue il match dell'esatto numero di children.
     *
     * @param size numero di children da matchare
     * @return Matcher
     */
    fun childCount(size: Int): org.hamcrest.Matcher<View> {
        return object : TypeSafeMatcher<View>() {

            public override fun matchesSafely(view: View): Boolean {
                return view is ViewGroup && view.childCount == size
            }

            override fun describeTo(description: Description) {
                description.appendText("ViewGroup should have $size items")
            }
        }
    }

    /**
     * Esegue il match sul per una ViewGroup non vuota.
     *
     * @return Matcher
     */
    fun minChildrenCount(minChildren: Int): org.hamcrest.Matcher<View> {
        return object : TypeSafeMatcher<View>() {

            public override fun matchesSafely(view: View): Boolean {
                return view is ViewGroup && view.childCount >= minChildren
            }

            override fun describeTo(description: Description) {
                description.appendText("ViewGroup should have at least $minChildren items")
            }
        }
    }

    /**
     * Esegue il match sul numero di child di una ListView.
     *
     * @param size numero di child
     * @return Matcher
     */
    fun withListSize(size: Int): org.hamcrest.Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            public override fun matchesSafely(view: View): Boolean {
                return (view as ListView).childCount == size
            }

            override fun describeTo(description: Description) {
                description.appendText("ListView should have $size items")
            }
        }
    }

    /**
     * Esegue il match sul per una ListView non vuota.
     *
     * @return Matcher
     */
    fun withListNotEmpty(): org.hamcrest.Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            public override fun matchesSafely(view: View): Boolean {
                return (view as ListView).childCount > 0
            }

            override fun describeTo(description: Description) {
                description.appendText("ListView is not empty")
            }
        }
    }
}
