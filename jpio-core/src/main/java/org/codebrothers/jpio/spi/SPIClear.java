package org.codebrothers.jpio.spi;

public enum SPIClear {

  RX(0b01), TX(0b10), ALL(0x11);

  public final int value;

  public static final int CLEAR_MASK = ~(0x11);

  private SPIClear(int value) {
    this.value = value << 4;
  }

}
