/*
 * Project: meteo
 * File: LoadBitmapTask
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

package com.gmail.fattazzo.meteo.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author fattazzo
 *
 *         date: 03/set/2014
 *
 */
public class LoadBitmapTask extends AsyncTask<String, Void, Bitmap> {

    private Options options = null;

    /**
     * Costruttore.
     */
    public LoadBitmapTask() {
        this(null);
    }

    /**
     * Costruttore.
     *
     * @param options
     *            decode options
     */
    public LoadBitmapTask(final BitmapFactory.Options options) {
        super();
        this.options = options;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        URL newurl = null;
        Bitmap bitmap = null;
        try {
            newurl = new URL(params[0]);
            bitmap = BitmapFactory.decodeStream(newurl.openConnection().getInputStream(), null, options);
            Log.e("TASK", "Load " + newurl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
