package com.example.workingparents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment(val content: String, val itemClick: (Int) -> Unit) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.sharing_list_more, container, false)
        val contentTV: TextView = view.findViewById(R.id.moreContentTV)
        contentTV.text=content

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.moreModifyBtn).setOnClickListener {
            itemClick(0)
            dialog?.dismiss()
        }
        view.findViewById<Button>(R.id.moreDeleteBtn).setOnClickListener {
            itemClick(1)
            dialog?.dismiss()
        }
    }

    override fun getTheme(): Int = R.style.BottomSheetDialog

}
