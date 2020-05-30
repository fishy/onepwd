package com.yhsif.onepwd

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListViewHolder(val v: View) : RecyclerView.ViewHolder(v) {
    fun setText(text: String) {
        v.findViewById<TextView>(R.id.text).setText(text)
    }

    fun setBackground(i: Int) {
        if (i % 2 == 0) {
            v.setBackgroundColor(v.getContext().getColor(R.color.even_background))
        } else {
            v.setBackgroundColor(v.getContext().getColor(R.color.odd_background))
        }
    }
}
