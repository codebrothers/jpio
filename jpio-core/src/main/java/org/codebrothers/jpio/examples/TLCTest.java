package org.codebrothers.jpio.examples;

import static org.codebrothers.jpio.util.DelayUtil.delayMs;

import java.util.Random;

import org.codebrothers.jpio.JPIO;
import org.codebrothers.jpio.clock.ClockPin;
import org.codebrothers.jpio.clock.ClockSource;
import org.codebrothers.jpio.gpio.Function;
import org.codebrothers.jpio.gpio.PiPin;
import org.codebrothers.jpio.pin.DigitalPin;
import org.codebrothers.jpio.port.shift595.Shift595;

/**
 * Demonstrates how to use a 74HC595 (or similar) shift register with the
 * {@link org.codebrothers.jpio.port.shift595.Shift595} port implementation.
 * 
 * @author Rick Watson
 */
public class TLCTest {

  private static final ClockPin CLOCK_PIN = ClockPin.PIN4;
  private static final PiPin DATA_PIN = PiPin.PIN11;
  private static final PiPin REGISTER_CLOCK_PIN = PiPin.PIN15;
  private static final PiPin LATCH_PIN = PiPin.PIN16;

  public static void main(String[] args) {
    // Initialize the hardware
    JPIO.init();

    // Set up as oscillator
    CLOCK_PIN.configureSource(ClockSource.OSCILLATOR);
    CLOCK_PIN.configureDivisor(2);
    CLOCK_PIN.enable();

    // Set data, clock and latch pins to be outputs
    DATA_PIN.setFunction(Function.OUTPUT);
    REGISTER_CLOCK_PIN.setFunction(Function.OUTPUT);
    LATCH_PIN.setFunction(Function.OUTPUT);

    // Construct the shift port
    Shift595 shift595 = new Shift595(DATA_PIN, REGISTER_CLOCK_PIN, LATCH_PIN, 192 * 3);

    // Random to get us random boolean values
    Random random = new Random();

    while (true) {
      // Begin atomic change
      shift595.beginAtomic();
      try {
        // Change all 8 pins to random values!
        for (DigitalPin pin : shift595) {
          pin.setValue(random.nextBoolean());
        }
      } finally {
        // Complete (writes to the shift register)
        shift595.completeAtomic();
      }
      // wait a bit
      delayMs(500);
    }
  }

}
