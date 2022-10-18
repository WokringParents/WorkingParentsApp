package com.example.workingparents

import android.app.Person
import android.text.method.TextKeyListener
import android.text.method.TextKeyListener.clear
import androidx.recyclerview.widget.DiffUtil
import java.util.*
import java.util.Collections.addAll
import kotlin.collections.ArrayList

class DiffUtilCallback(private val oldList: List<Notice>, private val newList: List<Notice>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return if (oldItem is Notice && newItem is Notice) {
            oldItem.nid==newItem.nid
        } else {
            false
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]

}



