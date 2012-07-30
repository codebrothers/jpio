package org.codebrothers.jpio.examples;

import org.codebrothers.jpio.JPIO;
import org.codebrothers.jpio.clock.ClockChannel;
import org.codebrothers.jpio.clock.ClockSource;
import org.codebrothers.jpio.gpio.Function;
import org.codebrothers.jpio.gpio.GPIOPin;
import org.codebrothers.jpio.util.DelayUtil;

public class GPClk {

  private static final ClockChannel CHANNEL = ClockChannel.CLOCK0;

  public static void main(String[] args) {
    // Initialize the hardware
    JPIO.init();
    while (true) {
      // set up as oscillator
      CHANNEL.configureSource(ClockSource.OSCILLATOR);
      CHANNEL.configureDivisor(0xFFF);
      CHANNEL.enable();
      System.out.println("Enabled");

      // wait for 5 secs then disable
      DelayUtil.delayMs(5000);
      CHANNEL.disable();
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
