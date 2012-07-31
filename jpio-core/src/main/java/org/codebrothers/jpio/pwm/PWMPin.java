package org.codebrothers.jpio.pwm;

import org.codebrothers.jpio.gpio.Function;
import org.codebrothers.jpio.gpio.GPIOPin;

public enum PWMPin {

  /**
   * PWM Channel 0
   * <p>
   * GPIO 12 - not on header
   */
  PIN12(GPIOPin.PIN12, Function.ALT0, PWMChannel.PWM0),

  /**
   * PWM Channel 1
   * <p>
   * GPIO 13 - not on header
   */
  PIN13(GPIOPin.PIN13, Function.ALT0, PWMChannel.PWM1),

  /**
   * PWM Channel 0
   * <p>
   * GPIO 18 - Pin 12 on the GPIO header
   */
  PIN18(GPIOPin.PIN18, Function.ALT5, PWMChannel.PWM0),

  /**
   * PWM Channel 1
   * <p>
   * GPIO 19 - not on header
   */
  PIN19(GPIOPin.PIN19, Function.ALT5, PWMChannel.PWM1),

  /**
   * PWM Channel 0
   * <p>
   * GPIO 40 - not on header
   */
  PIN40(GPIOPin.PIN40, Function.ALT0, PWMChannel.PWM0),

  /**
   * PWM Channel 1
   * <p>
   * GPIO 41 - not on header
   */
  PIN41(GPIOPin.PIN41, Function.ALT0, PWMChannel.PWM1),

  /**
   * PWM Channel 1
   * <p>
   * GPIO 45 - not on header
   */
  PIN45(GPIOPin.PIN45, Function.ALT0, PWMChannel.PWM1),

  /**
   * PWM Channel 0
   * <p>
   * GPIO 52 - not on header
   */
  PIN52(GPIOPin.PIN52, Function.ALT1, PWMChannel.PWM0),

  /**
   * PWM Channel 1
   * <p>
   * GPIO 53 - not on header
   */
  PIN53(GPIOPin.PIN53, Function.ALT1, PWMChannel.PWM1);

  /*
   * The GPIOPin this pin maps to
   */
  public final GPIOPin pin;

  /*
   * The alternative function required to put this pin into PWM mode
   */
  public final Function function;

  /*
   * This channel this pin is on
   */
  public final PWMChannel channel;

  private PWMPin(final GPIOPin pin, final Function function, final PWMChannel channel) {
    this.pin = pin;
    this.function = function;
    this.channel = channel;
  }

}
