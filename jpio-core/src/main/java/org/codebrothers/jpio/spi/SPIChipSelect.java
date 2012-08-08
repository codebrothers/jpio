package org.codebrothers.jpio.spi;

/**
 * Constants for controlling which chip is selected on the SPI.
 * 
 * @author Rick Watson
 */
public enum SPIChipSelect {

  /**
   * Chip Select 0
   */
  CS0(0b00),

  /**
   * Chip Select 1
   */
  CS1(0b01),

  /**
   * Chip Select 2 (0 and 1 asserted)
   */
  CS2(0b10),

  /**
   * No CS. Use GPIO Pin or another means of selecting the slave.
   */
  NONE(0b11);

  /*
   * Inverted mask used to clear the bits associated with SPIChipSelect value.
   */
  public static final int CHIP_SELECT_MASK = ~(0b11);

  /*
   * Register value choosing which chip to select.
   */
  public final int value;

  private SPIChipSelect(int value) {
    this.value = value;
  }

}
