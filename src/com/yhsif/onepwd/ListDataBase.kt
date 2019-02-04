package com.yhsif.onepwd

import android.content.Context

interface ListDataBase {
  fun getText(): String
  fun doRemove(ctx: Context)
}
