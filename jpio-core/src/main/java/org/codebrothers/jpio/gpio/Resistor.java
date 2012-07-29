package org.codebrothers.jpio.gpio;

/**
 * Enumeration to control the state of the internal resistors for GPIO pins.
 * <p>
 * Useful when using a pin as an input as it avoids the need for external
 * resistors.
 * 
 * @author: Rick Watson
 */
public enum Resistor {

  /**
   * Turn off the internal resistor, the pin will float (high impedance)
   */
  OFF(0b00),

  /**
   * The pin will be pulled down (0v)
   */
  DOWN(0b01),

  /**
   * The pin will be pulled high (3.3v)
   */
  UP(0b10);

  /*
   * The register which lets us configure a resistor for a pin.
   */
  public static final int VALUE_REGISTER = 37;

  /*
   * The pre-shifted resistor value.
   */
  public final int value;

  private Resistor(int value) {
    this.value = value;
  }

}
