package com.yhsif.onepwd

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View

import androidx.recyclerview.widget.RecyclerView

class ListAdapter(
  var list: MutableList<ListDataBase>,
  val listener: View.OnClickListener
) : RecyclerView.Adapter<ListViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, vt: Int): ListViewHolder {
    val v = LayoutInflater
      .from(parent.getContext())
      .inflate(R.layout.list_item, parent, false)
    v.setOnClickListener(listener)
    return ListViewHolder(v)
  }

  override fun onBindViewHolder(vh: ListViewHolder, i: Int) {
    vh.setText(list.get(i).getText())
    vh.setBackground(i)
  }

  override fun getItemCount(): Int = list.size

  override fun onAttachedToRecyclerView(rv: RecyclerView) {
    super.onAttachedToRecyclerView(rv)
  }

  fun remove(i: Int) {
    list.removeAt(i)
    notifyDataSetChanged()
  }
}
