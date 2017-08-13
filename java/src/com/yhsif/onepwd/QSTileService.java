package com.yhsif.onepwd;

import android.content.Intent;
import android.service.quicksettings.TileService;

public class QSTileService extends TileService {
  private static boolean added = false;

  @Override
  public void onClick() {
    startActivityAndCollapse(new Intent(this, OnePwd.class));
  }

  @Override
  public void onTileAdded() {
    added = true;
    NotificationService.stop(this);
  }

  @Override
  public void onTileRemoved() {
    added = false;
    NotificationService.run(this);
  }

  static boolean added() {
    return added;
  }
}
