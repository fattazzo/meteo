/*
 * Project: meteo
 * File: NumberPickerDialogPreference
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

package com.gmail.fattazzo.meteo.preferences.numberpicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.gmail.fattazzo.meteo.R;

/**
 *
 * @author fattazzo
 *
 *         date: 30/lug/2014
 *
 */
public class NumberPickerDialogPreference extends DialogPreference {

    /**
     *
     * @author fattazzo
     *
     *         date: 13/lug/2015
     *
     */
    private static class SavedState extends BaseSavedState {
        private int minValue;
        private int maxValue;
        private int value;

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

            minValue = source.readInt();
            maxValue = source.readInt();
            value = source.readInt();
        }

        /**
         * @param superState
         *            state
         */
        public SavedState(final Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);

            dest.writeInt(minValue);
            dest.writeInt(maxValue);
            dest.writeInt(value);
        }
    }

    private static final int DEFAULT_MIN_VALUE = 0;
    private static final int DEFAULT_MAX_VALUE = 100;

    private static final int DEFAULT_VALUE = 0;
    private int mMinValue;
    private int mMaxValue;

    private int mValue;

    private NumberPicker mNumberPicker;

    /**
     * Costruttore.
     *
     * @param context
     *            context
     */
    public NumberPickerDialogPreference(final Context context) {
        this(context, null);
    }

    /**
     * Costruttore.
     *
     * @param context
     *            context
     * @param attrs
     *            attributi
     */
    public NumberPickerDialogPreference(final Context context, final AttributeSet attrs) {
        super(context, attrs);

        // get attributes specified in XML
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.NumberPickerDialogPreference, 0, 0);
        try {
            setMinValue(a.getInteger(R.styleable.NumberPickerDialogPreference_min, DEFAULT_MIN_VALUE));
            setMaxValue(a.getInteger(R.styleable.NumberPickerDialogPreference_android_max, DEFAULT_MAX_VALUE));
        } finally {
            a.recycle();
        }

        // set layout
        setDialogLayoutResource(R.layout.preference_number_picker_dialog);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
        setDialogIcon(null);
    }

    /**
     * @return the mMaxValue
     */
    public int getMaxValue() {
        return mMaxValue;
    }

    /**
     * @return the mMinValue
     */
    public int getMinValue() {
        return mMinValue;
    }

    /**
     * @return the mValue
     */
    public int getValue() {
        return mValue;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        TextView dialogMessageText = (TextView) view.findViewById(R.id.text_dialog_message);
        dialogMessageText.setText(getDialogMessage());

        mNumberPicker = (NumberPicker) view.findViewById(R.id.number_picker);
        mNumberPicker.setMinValue(mMinValue);
        mNumberPicker.setMaxValue(mMaxValue);
        mNumberPicker.setValue(mValue);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            int numberPickerValue = mNumberPicker.getValue();
            if (callChangeListener(numberPickerValue)) {
                setValue(numberPickerValue);
            }
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInt(index, DEFAULT_VALUE);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        // check whether we saved the state in onSaveInstanceState()
        if (state == null || !state.getClass().equals(SavedState.class)) {
            // didn't save the state, so call superclass
            super.onRestoreInstanceState(state);
            return;
        }

        // restore the state
        SavedState myState = (SavedState) state;
        setMinValue(myState.minValue);
        setMaxValue(myState.maxValue);
        setValue(myState.value);

        super.onRestoreInstanceState(myState.getSuperState());
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        // save the instance state so that it will survive screen orientation changes and other events that may
        // temporarily destroy it
        final Parcelable superState = super.onSaveInstanceState();

        // set the state's value with the class member that holds current setting value
        final SavedState myState = new SavedState(superState);
        myState.minValue = getMinValue();
        myState.maxValue = getMaxValue();
        myState.value = getValue();

        return myState;
    }

    @Override
    protected void onSetInitialValue(boolean restore, Object defaultValue) {
        setValue(restore ? getPersistedInt(DEFAULT_VALUE) : (Integer) defaultValue);
    }

    /**
     * @param maxValue
     *            setter of maxValue
     */
    public void setMaxValue(int maxValue) {
        mMaxValue = maxValue;
        setValue(Math.min(mValue, mMaxValue));
    }

    /**
     * @param minValue
     *            setter of minValue
     */
    public void setMinValue(int minValue) {
        mMinValue = minValue;
        setValue(Math.max(mValue, mMinValue));
    }

    /**
     * @param value
     *            setter of value
     */
    public void setValue(int value) {
        value = Math.max(Math.min(value, mMaxValue), mMinValue);

        if (value != mValue) {
            mValue = value;
            persistInt(value);
            notifyChanged();
        }
    }
}
