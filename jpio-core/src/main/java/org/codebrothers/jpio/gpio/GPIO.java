package org.codebrothers.jpio.gpio;

import static org.codebrothers.jpio.JPIO.GPIO;
import static org.codebrothers.jpio.util.DelayUtil.delayNs;

public class GPIO {

  /**
   * Sets a pin's function using the pin's function register.
   * 
   * @param pin
   *          The pin for which to set the function.
   * @param function
   *          The new function for the pin.
   */
  public static synchronized void setPinFunction(final GPIOPin pin, final Function function) {
    GPIO.put(pin.functionRegister,
    /* Clear the bits for the pin, ready for new value */
    (GPIO.get(pin.functionRegister) & pin.functionMask)
    /* Set the new value for specified function */
    | function.values[pin.functionOrdinal]);
  }

  /**
   * Sets or clears a pin's value using the pin's set or clear register.
   * 
   * @param pin
   *          The pin for which to set the value.
   * @param value
   *          The new value for the pin.
   */
  public static synchronized void setPinValue(final GPIOPin pin, final boolean value) {
    GPIO.put(value ? pin.setRegister : pin.clearRegister, pin.pinValue);
  }

  /**
   * Gets a pin's value using the pin's level register.
   * 
   * @param pin
   *          The pin for which to get the value.
   * @return The value of the pin.
   */
  public static synchronized boolean getPinValue(final GPIOPin pin) {
    return (GPIO.get(pin.levelRegister) & pin.pinValue) != 0;
  }

  /**
   * Sets a pin's internal resistor to one of the three {@link Resistor} states.
   * 
   * @param pin
   *          The pin for which to set the internal resistor mode.
   * @param resistor
   *          The internal resistor mode to set for the pin.
   */
  public static synchronized void setPinResistor(final GPIOPin pin, final Resistor resistor) {
    // See page 101 in the datasheet for details on how this is implemented.
    //
    // 250mhz is the speed of the peripheral bus clock, and the datasheet says
    // to sleep for 150 ticks. So we'll need to sleep for at least:
    //
    // 1x10&#x2079/(2.5x10&#x2078/150) = 600ns (1000ns will do!)
    //
    // set new up/down value
    GPIO.put(Resistor.VALUE_REGISTER, resistor.value);
    // provide the required set-up time for the control signal
    delayNs(1000);
    // to clock the control signal into the GPIO pads
    GPIO.put(pin.pullUpDownClockRegister, pin.pinValue);
    // required hold time for the control signal
    delayNs(1000);
    // take our values out of the registers
    GPIO.put(Resistor.VALUE_REGISTER, 0x00);
    GPIO.put(pin.pullUpDownClockRegister, 0x00);
  }

}
