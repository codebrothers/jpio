package org.codebrothers.jpio.examples;

import org.codebrothers.jpio.Function;
import org.codebrothers.jpio.JPIO;
import org.codebrothers.jpio.PiPin;
import org.codebrothers.jpio.Resistor;

/**
 * Demonstrates reading the value from a pin with an internal pull-up enabled.
 * 
 * Connect up a button to ground pin 15 when pressed.
 * 
 * Pressing the button will sink the current from the internal resistor, you
 * should see the output change from High to Low
 * 
 * @author Rick Watson
 */
public class InputTest {

  private static final PiPin PIN = PiPin.PIN15;

  public static void main(String[] args) {
    // Initialize the hardware
    JPIO.init();

    // Set pin as input.
    PIN.setFunction(Function.INPUT);

    // Set internal resistor to pull-up.
    PIN.setPinResistor(Resistor.UP);

    // Output the value of the pin every 200ms
    while (true) {
      System.out.println(PIN.getValue());
      JPIO.delayMs(200);
    }
  }

}
