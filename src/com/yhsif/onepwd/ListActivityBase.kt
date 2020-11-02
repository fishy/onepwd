package com.yhsif.onepwd

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class ListActivityBase(val hintRes: Int) :
  AppCompatActivity(), View.OnClickListener {

  var adapter: ListAdapter? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.list)

    findViewById<TextView>(R.id.hint).setText(hintRes)

    adapter = ListAdapter(mutableListOf(), this)
    findViewById<RecyclerView>(R.id.list).let { rv ->
      adapter?.let { a ->
        rv.setAdapter(a)
      }
      rv.setLayoutManager(LinearLayoutManager(this))
    }
  }

  override fun onResume() {
    refreshData()
    super.onResume()
  }

  // for View.OnClickListener
  override fun onClick(v: View) {
    findViewById<RecyclerView>(R.id.list).let { rv ->
      val i = rv.getChildLayoutPosition(v)
      adapter?.let { a ->
        val data = a.list.get(i)
        AlertDialog.Builder(this)
          .setCancelable(true)
          .setIcon(R.mipmap.icon_round)
          .setTitle(R.string.dialog_title)
          .setMessage(getDialogMessage(data))
          .setNegativeButton(
            android.R.string.no,
            DialogInterface.OnClickListener() { dialog, _ ->
              dialog.dismiss()
            }
          )
          .setPositiveButton(
            android.R.string.yes,
            DialogInterface.OnClickListener() { dialog, _ ->
              data.doRemove(this)
              a.remove(i)
              dialog.dismiss()
            }
          )
          .create()
          .show()
      }
    }
  }

  abstract fun refreshData()
  abstract fun getDialogMessage(data: ListDataBase): String
}
