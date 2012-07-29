package org.codebrothers.jpio.examples;

import org.codebrothers.jpio.JPIO;
import org.codebrothers.jpio.gpclock.Clock;
import org.codebrothers.jpio.gpclock.ClockChannel;
import org.codebrothers.jpio.gpclock.ClockMash;
import org.codebrothers.jpio.gpclock.ClockSource;
import org.codebrothers.jpio.gpio.Function;
import org.codebrothers.jpio.gpio.GPIOPin;
import org.codebrothers.jpio.util.DelayUtil;

public class GPClk {
  public static void main(String[] args) {
    // Initialize the hardware
    JPIO.init();
    while (true) {
      // set up as oscillator
      Clock.configure(ClockChannel.CLOCK0, ClockSource.OSCILLATOR, ClockMash.INT, 0xFFF);
      Clock.enable(ClockChannel.CLOCK0);
      System.out.println("enabled");

      System.out.println("Enabled");
      JPIO.printCLOCK();
      DelayUtil.delayMs(5000);

      System.out.println("Disabling");
      Clock.disable(ClockChannel.CLOCK0);
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
