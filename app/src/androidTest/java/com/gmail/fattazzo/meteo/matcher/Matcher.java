package com.gmail.fattazzo.meteo.matcher;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 * @author fattazzo
 *         <p/>
 *         date: 02/03/16
 */
public class Matcher {

    /**
     * Esegue il match dell'esatto numero di children.
     *
     * @param size numero di children da matchare
     * @return Matcher
     */
    public static org.hamcrest.Matcher<View> childCount(final int size) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(final View view) {
                return view instanceof ViewGroup && ((ViewGroup) view).getChildCount() == size;
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("ViewGroup should have " + size + " items");
            }
        };
    }

    /**
     * Esegue il match sul numero di child di una ListView.
     *
     * @param size numero di child
     * @return Matcher
     */
    public static org.hamcrest.Matcher<View> withListSize (final int size) {
        return new TypeSafeMatcher<View> () {
            @Override public boolean matchesSafely (final View view) {
                return ((ListView) view).getChildCount() == size;
            }

            @Override public void describeTo (final Description description) {
                description.appendText ("ListView should have " + size + " items");
            }
        };
    }

    /**
     * Esegue il match sul per una ListView non vuota.
     *
     * @return Matcher
     */
    public static org.hamcrest.Matcher<View> withListNotEmpty () {
        return new TypeSafeMatcher<View> () {
            @Override public boolean matchesSafely (final View view) {
                return ((ListView) view).getChildCount () > 0;
            }

            @Override public void describeTo (final Description description) {
                description.appendText ("ListView is not empty");
            }
        };
    }
}
