/*
 * Project: meteo
 * File: DialogUtil
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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;

/**
 * @author fattazzo
 *         <p>
 *         date: 02/02/17
 */
public final class DialogUtil {

    /**
     * Crea il dialog con titolo e layout richiesto.
     *
     * @param title titolo
     * @param layout_id layout
     * @return dialogo creato
     */
    public static Dialog createDialog(String title, int layout_id, Context context) {
        Dialog dialog;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog = new AlertDialog.Builder(context)
                    .setTitle(title)
                    .setView(layout_id)
                    .create();
        } else {
            dialog = new Dialog(context);
            dialog.setContentView(layout_id);
            dialog.setTitle(title);
        }
        return dialog;
    }

    /**
     * Costruttore
     */
    private DialogUtil() {

    }
}
