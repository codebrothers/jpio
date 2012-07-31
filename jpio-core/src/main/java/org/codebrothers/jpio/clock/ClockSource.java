package org.codebrothers.jpio.clock;

/**
 * The options for choosing the source for a clock.
 * 
 * @author Rick Watson
 */
public enum ClockSource {

  /**
   * Ground (no oscillator).
   */
  GND,

  /**
   * The on board crystal oscillator. Runs at a frequency of 19.2Mhz.
   */
  OSCILLATOR,

  /**
   * Test (debug 0)
   */
  TEST0,

  /**
   * Test (debug 1)
   */
  TEST1,

  /**
   * PLLA
   */
  PLLA,

  /**
   * PLLC
   */
  PLLC,

  /**
   * PLLD
   */
  PLLD,

  /**
   * HDMI auxiliary
   */
  HDMI;

  /*
   * The first 4 bits in the control register assign the clock's source.
   */
  public static int SOURCE_MASK = ~(0b1111);

  /*
   * The pre-shifted source value, simply the ordinal!
   */
  public final int value;

  private ClockSource() {
    this.value = ordinal();
  }

}
