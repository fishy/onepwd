package com.yhsif.onepwd;

import android.content.Intent;
import android.service.quicksettings.TileService;

public class QSTileService extends TileService {
  @Override
  public void onClick() {
    startActivityAndCollapse(new Intent(this, OnePwd.class));
  }
}
