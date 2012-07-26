package org.codebrothers.jpio.examples;

import static org.codebrothers.jpio.util.DelayUtil.delayMs;

import org.codebrothers.jpio.JPIO;
import org.codebrothers.jpio.gpclock.GPClock;
import org.codebrothers.jpio.gpclock.GPClockChannel;
import org.codebrothers.jpio.gpclock.GPClockMash;
import org.codebrothers.jpio.gpclock.GPClockSource;
import org.codebrothers.jpio.gpio.Function;
import org.codebrothers.jpio.gpio.PiPin;

public class GPClk {
  public static void main(String[] args) {
    // Initialize the hardware
    JPIO.init();
    
    // Output the value of the pin every 200ms
    while (true) {
      GPClock.configure(GPClockChannel.CLOCK0, GPClockSource.OSCILLATOR, GPClockMash.INT, 200);
      GPClock.enable(GPClockChannel.CLOCK0);
      System.out.println("enabled");
      delayMs(500);

      GPClock.disable(GPClockChannel.CLOCK0);
      System.out.println("disabled");
      delayMs(500);

      PiPin.PIN7.setFunction(Function.OUTPUT);
      PiPin.PIN7.setValue(true);
      System.out.println("no");
      delayMs(500);
    }
  }
}
