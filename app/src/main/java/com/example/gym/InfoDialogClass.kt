package com.example.gym

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment

class InfoDialogClass: DialogFragment(R.layout.info_dialog_fragment) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val cancel_b : Button = view.findViewById(R.id.cancelBT);
        cancel_b.setOnClickListener(){
            dismiss()
        }

    }


}