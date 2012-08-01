package org.codebrothers.jpio.examples;

import org.codebrothers.jpio.JPIO;
import org.codebrothers.jpio.clock.ClockChannel;
import org.codebrothers.jpio.clock.ClockSource;
import org.codebrothers.jpio.pwm.PWMPin;
import org.codebrothers.jpio.util.DelayUtil;

public class Fade {

  private static final PWMPin PIN = PWMPin.PIN18;

  private static final ClockChannel PWM_CLOCK = ClockChannel.PWM;

  public static void main(String[] args) {
    // Initialize the hardware
    JPIO.init();
    while (true) {
      // set up the PWM clock and enable
      PWM_CLOCK.configureSource(ClockSource.OSCILLATOR);
      PWM_CLOCK.configureDivisor(10);
      PWM_CLOCK.enable();

      // set channels range and enable the output
      final int range = 1024;
      PIN.setRange(range);
      PIN.enable();

      // fade forward and backwards
      while (true) {
        for (int i = 0; i <= range; i++) {
          PIN.setData(i);
          DelayUtil.delayMs(1);
        }
        for (int i = range; i >= 0; i--) {
          PIN.setData(i);
          DelayUtil.delayMs(1);
        }
      }
    }
  }
}
