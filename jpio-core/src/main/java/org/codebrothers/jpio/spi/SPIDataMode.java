package org.codebrothers.jpio.spi;

/**
 * Constants for controlling the Clock Polarity and Clock Phase for SPI.
 * <p>
 * <strong>CPOL</strong> - Controls the resting state for the clock.
 * <ul>
 * <li>0 = Rest state of clock is low.
 * <li>1 = Rest state of clock is high.
 * </ul>
 * <strong>CPHA</strong> - Controls the clock phase,
 * <ul>
 * <li>0 = First SCLK transition at middle of data bit.
 * <li>1 = First SCLK transition at beginning of data bit.
 * </ul>
 * 
 * @author Rick Watson
 */
public enum SPIDataMode {

  /**
   * Sets the SPI mode as follows <strong>(default)</strong>:
   * <ul>
   * <li>CPOL = 0
   * <li>CPHA = 0
   * </ul>
   */
  MODE0(0b00),

  /**
   * Sets the SPI mode as follows:
   * <ul>
   * <li>CPOL = 0
   * <li>CPHA = 1
   * </ul>
   */
  MODE1(0b01),

  /**
   * Sets the SPI mode as follows:
   * <ul>
   * <li>CPOL = 1
   * <li>CPHA = 0
   * </ul>
   */
  MODE2(0b10),

  /**
   * Sets the SPI mode as follows:
   * <ul>
   * <li>CPOL = 1
   * <li>CPHA = 1
   * </ul>
   */
  MODE3(0b11);

  /*
   * The mask in order to clear the bits associated with SPIDataMode value.
   */
  public static int DATA_MODE_MASK = ~(0b11 << 2);

  /*
   * Register value for configuring the SPIDataMode.
   */
  public final int value;

  private SPIDataMode(int mode) {
    this.value = mode << 2;
  }

}
