package org.codebrothers.jpio.examples;

import org.codebrothers.jpio.JPIO;
import org.codebrothers.jpio.clock.ClockPin;
import org.codebrothers.jpio.clock.ClockSource;
import org.codebrothers.jpio.gpio.Function;
import org.codebrothers.jpio.gpio.GPIOPin;
import org.codebrothers.jpio.util.DelayUtil;

public class Clock {

  private static final ClockPin CLOCK_PIN = ClockPin.PIN4;

  public static void main(String[] args) {
    // Initialize the hardware
    JPIO.init();
    while (true) {
      // set up as oscillator
      CLOCK_PIN.configureSource(ClockSource.OSCILLATOR);
      CLOCK_PIN.configureDivisor(0xFFF);
      CLOCK_PIN.enable();
      System.out.println("Enabled");

      // wait for 5 secs then disable
      DelayUtil.delayMs(5000);
      CLOCK_PIN.disable();
      System.out.println("Disabled");

      // convert to an output pin and flash
      GPIOPin pin4 = GPIOPin.PIN4;
      pin4.setFunction(Function.OUTPUT);
      for (int i = 0; i < 10; i++) {
        pin4.setValue(false);
        DelayUtil.delayMs(100);
        pin4.setValue(true);
        DelayUtil.delayMs(100);
      }
    }
  }
}
