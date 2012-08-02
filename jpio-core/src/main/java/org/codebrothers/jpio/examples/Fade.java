package org.codebrothers.jpio.examples;

import org.codebrothers.jpio.JPIO;
import org.codebrothers.jpio.clock.ClockChannel;
import org.codebrothers.jpio.clock.ClockSource;
import org.codebrothers.jpio.pwm.PWMPin;
import org.codebrothers.jpio.util.DelayUtil;

/**
 * An example of how to use the PWM pin.
 * 
 * The PWM clock must be configured first. Following that, the pin must have
 * it's range set before it is enabled. The data value can then be used to
 * modify the pin's output period.
 * 
 * @author Rick Watson
 */
public class Fade {

  // The PWM clock.
  private static final ClockChannel PWM_CLOCK = ClockChannel.PWM;

  // 19.2MHz / 10
  private static final int DIVISOR = 10;

  // The only available PWM pin GPIO18 (pin 14 on the header)
  private static final PWMPin PIN = PWMPin.PIN18;

  // Range of 0-1024
  private static final int RANGE = 1024;

  public static void main(String[] args) {
    // Initialize the hardware
    JPIO.init();

    // Set up the PWM clock and enable
    PWM_CLOCK.configureSource(ClockSource.OSCILLATOR);
    PWM_CLOCK.configureDivisor(DIVISOR);
    PWM_CLOCK.enable();

    // Set channels range and enable the output
    PIN.setRange(RANGE);
    PIN.enable();

    // Fade up, then down by adjusting the range
    while (true) {
      for (int i = 0; i <= RANGE; i++) {
        PIN.setData(i);
        DelayUtil.delayMs(10);
      }
      for (int i = RANGE; i >= 0; i--) {
        PIN.setData(i);
        DelayUtil.delayMs(10);
      }
    }
  }

}
