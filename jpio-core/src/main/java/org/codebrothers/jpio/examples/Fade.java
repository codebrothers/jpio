package org.codebrothers.jpio.examples;

import org.codebrothers.jpio.JPIO;
import org.codebrothers.jpio.clock.ClockChannel;
import org.codebrothers.jpio.clock.ClockSource;
import org.codebrothers.jpio.pwm.PWMPin;
import org.codebrothers.jpio.util.DelayUtil;

public class Fade {

  private static final ClockChannel PWM_CLOCK = ClockChannel.PWM;

  private static final PWMPin PIN = PWMPin.PIN18;

  // Range of 0-1024
  private static final int RANGE = 1024;

  // 19.2MHz / 10
  private static final int DIVISOR = 10;

  public static void main(String[] args) {
    // Initialize the hardware
    JPIO.init();
    while (true) {
      // set up the PWM clock and enable
      PWM_CLOCK.configureSource(ClockSource.OSCILLATOR);
      PWM_CLOCK.configureDivisor(DIVISOR);
      PWM_CLOCK.enable();

      // set channels range and enable the output
      PIN.setRange(RANGE);
      PIN.enable();

      // fade forward and backwards
      while (true) {
        for (int i = 0; i <= RANGE; i++) {
          PIN.setData(i);
          DelayUtil.delayMs(1);
        }
        for (int i = RANGE; i >= 0; i--) {
          PIN.setData(i);
          DelayUtil.delayMs(1);
        }
      }
    }
  }
}
