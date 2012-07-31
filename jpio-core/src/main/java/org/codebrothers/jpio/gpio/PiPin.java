package org.codebrothers.jpio.gpio;

import org.codebrothers.jpio.pin.DigitalPin;

/**
 * An alternative to {@link GPIOPin} for obtaining pin instances. Mapped by the
 * pins position on the GPIO header and only exposing those pins.
 * 
 * @author Rick Watson
 */
public enum PiPin implements DigitalPin {

  /**
   * Pin 3 on the GPIO header, maps to GPIOPin.PIN0
   */
  PIN3(GPIOPin.PIN0),

  /**
   * Pin 5 on the GPIO header, maps to GPIOPin.PIN1
   */
  PIN5(GPIOPin.PIN1),

  /**
   * Pin 7 on the GPIO header, maps to GPIOPin.PIN4
   */
  PIN7(GPIOPin.PIN4),

  /**
   * Pin 8 on the GPIO header, maps to GPIOPin.PIN14
   */
  PIN8(GPIOPin.PIN14),

  /**
   * Pin 10 on the GPIO header, maps to GPIOPin.PIN15
   */
  PIN10(GPIOPin.PIN15),

  /**
   * Pin 11 on the GPIO header, maps to GPIOPin.PIN17
   */
  PIN11(GPIOPin.PIN17),

  /**
   * Pin 12 on the GPIO header, maps to GPIOPin.PIN18
   */
  PIN12(GPIOPin.PIN18),

  /**
   * Pin 13 on the GPIO header, maps to GPIOPin.PIN21
   */
  PIN13(GPIOPin.PIN21),

  /**
   * Pin 15 on the GPIO header, maps to GPIOPin.PIN22
   */
  PIN15(GPIOPin.PIN22),

  /**
   * Pin 16 on the GPIO header, maps to GPIOPin.PIN23
   */
  PIN16(GPIOPin.PIN23),

  /**
   * Pin 18 on the GPIO header, maps to GPIOPin.PIN24
   */
  PIN18(GPIOPin.PIN24),

  /**
   * Pin 19 on the GPIO header, maps to GPIOPin.PIN10
   */
  PIN19(GPIOPin.PIN10),

  /**
   * Pin 21 on the GPIO header, maps to GPIOPin.PIN9
   */
  PIN21(GPIOPin.PIN9),

  /**
   * Pin 22 on the GPIO header, maps to GPIOPin.PIN25
   */
  PIN22(GPIOPin.PIN25),

  /**
   * Pin 23 on the GPIO header, maps to GPIOPin.PIN11
   */
  PIN23(GPIOPin.PIN11),

  /**
   * Pin 24 on the GPIO header, maps to GPIOPin.PIN8
   */
  PIN24(GPIOPin.PIN8),

  /**
   * Pin 26 on the GPIO header, maps to GPIOPin.PIN7
   */
  PIN26(GPIOPin.PIN7);

  private final GPIOPin pin;

  private PiPin(final GPIOPin pin) {
    this.pin = pin;
  }

  @Override
  public Boolean getValue() {
    return GPIO.getPinValue(pin);
  }

  @Override
  public void setValue(Boolean value) {
    GPIO.setPinValue(pin, value);
  }

  /**
   * Sets the pins internal resistor to one of the three {@link Resistor}
   * states.
   * 
   * @param resistor
   *          The internal resistor mode to set for the pin.
   */
  public void setPinResistor(Resistor resistor) {
    GPIO.setPinResistor(pin, resistor);
  }

  /**
   * Sets the pins function to one of the {@link Function} types.
   * 
   * @param function
   *          The new function for the pin.
   */
  public void setFunction(Function function) {
    GPIO.setPinFunction(pin, function);
  }

}