package com.ws.simplesegmentcalculator

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class DialogFragmentTotalLegth : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view: View = layoutInflater.inflate(R.layout.total_legth, null)
        val dialog = AlertDialog.Builder(requireActivity())
        dialog.setView(view)
        dialog.setMessage(R.string.title_total_legth)
            .setPositiveButton("Ok", DialogInterface.OnClickListener { dialogInterface, i ->
                Log.d("mylogs", "Пользователь прочитал что такое общая длина$i")
                dismiss()
            })
        return dialog.create()
    }

}
