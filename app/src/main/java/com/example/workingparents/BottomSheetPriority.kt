package com.example.workingparents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

//더보기 눌렀을 때 밑에 뜨는 bottomsheet.. 클릭되는게 itemClick으로 int 인지..
class BottomSheetPriority(val itemClick: (Int) -> Unit) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.board_more, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.latest).setOnClickListener {
            itemClick(0)
            dialog?.dismiss()
        }
        view.findViewById<Button>(R.id.best_cnt).setOnClickListener {
            itemClick(1)
            dialog?.dismiss()
        }
        view.findViewById<Button>(R.id.best_hcnt).setOnClickListener {
            itemClick(2)
            dialog?.dismiss()
        }
    }
    override fun getTheme(): Int = R.style.BottomSheetDialog

}
