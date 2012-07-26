package org.codebrothers.jpio.gpclock;

public enum GPClockSource {

  GND, OSCILLATOR, TEST0, TEST1, PLLA, PLLC, PLLD, HDMI;

  public static int SOURCE_MASK = ~(0b1111);
  public final int value;

  private GPClockSource() {
    this.value = ordinal();
  }

  public static void main(String[] args) {
    System.out.println(Integer.toBinaryString(SOURCE_MASK));
  }

}
