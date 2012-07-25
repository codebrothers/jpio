package org.codebrothers.jpio;

public enum GPClock {

  CLOCK0(28, 39), CLOCK1(30, 31), CLOCK2(32, 33);

  public final int controlRegister;
  public final int dividerRegister;

  private GPClock(int controlRegister, int dividerRegister) {
    this.controlRegister = controlRegister;
    this.dividerRegister = dividerRegister;
  }

}
