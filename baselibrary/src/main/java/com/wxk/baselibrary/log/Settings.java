package com.wxk.baselibrary.log;

/**
 * @author Orhan Obut
 */
public final class Settings {

  private boolean showThreadInfo = true;

  /**
   * Determines how logs will printed
   */

  public Settings hideThreadInfo() {
    showThreadInfo = false;
    return this;
  }

  public boolean isHideThreadInfo(){
	  return showThreadInfo;
  }
}
