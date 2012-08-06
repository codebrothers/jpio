package org.codebrothers.jpio.spi;

/**
 * Controls the Clock Polarity and Clock Phase
 * 
 * CPOL - controls the resting state for the clock.
 * 
 * 
 */
public enum SPIDataMode {

  /**
   * CPOL = 0, CPHA = 0
   */
  MODE0(0b00),

  /**
   * CPOL = 0, CPHA = 1
   */
  MODE1(0b01),

  /**
   * CPOL = 1, CPHA = 0
   */
  MODE2(0b10),

  /**
   * CPOL = 1, CPHA = 1
   */
  MODE3(0b11);

  public static int DATA_MODE_MASK = 0b11 << 2;
  public final int value;

  private SPIDataMode(int mode) {
    this.value = mode << 2;
  }

}
