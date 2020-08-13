package com.example.amasyasut

import android.app.Dialog
import android.content.Context
import android.graphics.Color


object LoadingDialog {

    fun startDialog(context: Context): Dialog? {
        val progressDialog = Dialog(context)

        progressDialog.let {
            it.show()
            // it.window?.setBackgroundDrawableResource(R.color.siyah)
            it.setContentView(R.layout.proggres_dialog)
            it.setCancelable(false)
            it.setCanceledOnTouchOutside(false)
            return it
        }
    }

    fun startDialog2(context: Context): Dialog? {
        val progressDialog = Dialog(context)

        progressDialog.let {
            it.show()
            // it.window?.setBackgroundDrawableResource(R.color.siyah)
            it.setContentView(R.layout.proggres_dialog)
            it.setCancelable(false)
            it.setCanceledOnTouchOutside(false)
            return it
        }
    }


}