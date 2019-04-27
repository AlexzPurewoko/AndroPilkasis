/**
 * Copyright @2019 by Alexzander Purwoko Widiantoro <purwoko908@gmail.com>
 * @author APWDevs
 *
 * Licensed under GNU GPLv3
 *
 * This module is provided by "AS IS" and if you want to take
 * a copy or modifying this module, you must include this @author
 * Thanks! Happy Coding!
 */

package id.apwdevs.andropilkasis.plugin

import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import id.apwdevs.andropilkasis.R

class SimpleProgressDialog(
    ctx: Context
) {
    private val alertDialog: AlertDialog
    private val textState: TextView

    var stateTextContent: String = ""
        get() = textState.text.toString()
        set(value) {
            textState.text = value
            field = value
        }

    init {
        // initialize the view
        val view = LayoutInflater.from(ctx).inflate(R.layout.adapter_simple_loading, null, false)
        textState = view.findViewById(R.id.adapter_simple_loading_text)
        val alertBuilder = AlertDialog.Builder(ctx)
        alertBuilder.setView(view)
        alertBuilder.setCancelable(false)
        alertDialog = alertBuilder.create()
    }

    fun showDialog() {
        alertDialog.show()
    }

    fun hideDialog() {
        alertDialog.dismiss()
    }

}