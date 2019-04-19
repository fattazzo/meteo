/*
 * Project: meteo
 * File: ZoneWidgetPreference
 *
 * Created by fattazzo
 * Copyright © 2018 Gianluca Fattarsi. All rights reserved.
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

package com.gmail.fattazzo.meteo.preferences.imagewidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gmail.fattazzo.meteo.R;

/**
 * Classe che permette di selezionare e salvare nelle preference la zona di cui i widget visualizzeranno le
 * informazioni.
 *
 * @author fattazzo
 *
 *         date: 18/set/2014
 *
 */
public class ZoneWidgetPreference extends DialogPreference implements View.OnClickListener {

    /**
     *
     * @author fattazzo
     *
     *         date: 13/lug/2015
     *
     */
    private static class SavedState extends BaseSavedState {
        private String value;

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {

            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };

        /**
         * @param source
         *            source
         */
        public SavedState(final Parcel source) {
            super(source);
            value = source.readString();
        }

        /**
         * @param superState
         *            state
         */
        public SavedState(final Parcelable superState) {
            super(superState);
        }

        /**
         * @return the value
         */
        public String getValue() {
            return value;
        }

        /**
         * @param value
         *            the value to set
         */
        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(value);
        }
    }

    // nel caso non venga specifivato un valore di default sul file xml il valore sarà uguale a "S"
    public static final String DEFAULT_VALUE = "S";

    private String zoneValue;
    private String currentZoneValue;

    private RelativeLayout layoutNW;

    private RelativeLayout layoutNE;
    private RelativeLayout layoutS;

    /**
     * Costruttore.
     *
     * @param context
     *            context
     * @param attrs
     *            attribute
     */
    public ZoneWidgetPreference(final Context context, final AttributeSet attrs) {
        super(context, attrs);

        setDialogLayoutResource(R.layout.preference_zone_widget_layout);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);

        setDialogIcon(null);
    }

    /**
     * Bind colors.
     */
    private void bindValueView() {
        layoutNE.setBackgroundColor("NE".equals(currentZoneValue) ? 0x6600FF00 : Color.TRANSPARENT);
        layoutNW.setBackgroundColor("NW".equals(currentZoneValue) ? 0x6600FF00 : Color.TRANSPARENT);
        layoutS.setBackgroundColor("S".equals(currentZoneValue) ? 0x6600FF00 : Color.TRANSPARENT);

    }

    @Override
    public CharSequence getSummary() {
        return getContext().getString(R.string.widgets_pref_zone_summary) + " " + zoneValue;
    }

    /**
     * @return the zoneValue
     */
    public String getZoneValue() {
        return zoneValue;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        TextView description = (TextView) view.findViewById(R.id.preferences_zone_widget_description);
        description.setText(Html.fromHtml(view.getContext().getString(R.string.widgets_pref_zone_text)));

        layoutNW = (RelativeLayout) view.findViewById(R.id.preferences_zone_widget_NW);
        layoutNE = (RelativeLayout) view.findViewById(R.id.preferences_zone_widget_NE);
        layoutS = (RelativeLayout) view.findViewById(R.id.preferences_zone_widget_S);

        layoutNW.setOnClickListener(this);
        layoutNE.setOnClickListener(this);
        layoutS.setOnClickListener(this);

        bindValueView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.preferences_zone_widget_NW:
            currentZoneValue = "NW";
            break;
        case R.id.preferences_zone_widget_NE:
            currentZoneValue = "NE";
            break;
        case R.id.preferences_zone_widget_S:
            currentZoneValue = "S";
            break;
        default:
            break;
        }
        bindValueView();
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        // se è stato premuto il tasto ok salvo il valore della zona nelle preferences
        if (positiveResult) {
            zoneValue = currentZoneValue;
            persistString(zoneValue);
        } else {
            currentZoneValue = zoneValue;
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        String value = a.getString(index);
        return value != null ? value : DEFAULT_VALUE;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {

        if (state == null || !state.getClass().equals(SavedState.class)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());

        currentZoneValue = myState.getValue();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();

        if (isPersistent()) {
            return superState;
        }

        final SavedState myState = new SavedState(superState);
        myState.setValue(currentZoneValue);
        return myState;
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            // carico il valore impostato per la zona
            zoneValue = this.getPersistedString(DEFAULT_VALUE);
        } else {
            // Setta il valore di default impostato sull'attributo del file xml
            zoneValue = (String) defaultValue;
            persistString(zoneValue);
        }
        currentZoneValue = zoneValue;
    }

}
