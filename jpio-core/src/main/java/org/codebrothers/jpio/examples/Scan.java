package org.codebrothers.jpio.examples;

import static org.codebrothers.jpio.util.DelayUtil.delayMs;

import org.codebrothers.jpio.JPIO;
import org.codebrothers.jpio.gpio.Function;
import org.codebrothers.jpio.gpio.PiPin;
import org.codebrothers.jpio.port.shift595.Shift595;

/**
 * Demonstrates how to use a 74HC595 (or similar) shift register with the
 * {@link org.codebrothers.jpio.port.shift595.Shift595} port implementation.
 * 
 * @author Rick Watson
 */
public class Scan {

  private static final PiPin DATA_PIN = PiPin.PIN11;
  private static final PiPin CLOCK_PIN = PiPin.PIN15;
  private static final PiPin LATCH_PIN = PiPin.PIN16;
  private static final int NUM_BITS = 24;

  public static void main(String[] args) {
    // Initialize the hardware
    JPIO.init();

    // Set data, clock and latch pins to be outputs
    DATA_PIN.setFunction(Function.OUTPUT);
    CLOCK_PIN.setFunction(Function.OUTPUT);
    LATCH_PIN.setFunction(Function.OUTPUT);

    // Construct the shift port
    Shift595 shift595 = new Shift595(DATA_PIN, CLOCK_PIN, LATCH_PIN, NUM_BITS);

    boolean reverse = true;
    int i = 1;
    while (true) {
      // Begin atomic change
      shift595.beginAtomic();
      try {
        shift595.setPinValue(i, false);
        i += reverse ? -1 : 1;
        shift595.setPinValue(i, true);
      } finally {
        shift595.completeAtomic();
      }
      if (i == 0 || i == (NUM_BITS - 1))
        reverse = !reverse;
      // wait a bit
      delayMs(50);
    }

  }
}
