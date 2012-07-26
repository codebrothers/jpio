package org.codebrothers.jpio.gpclock;

import org.codebrothers.jpio.gpio.GPIOPin;

public enum GPClockChannel {

  CLOCK0(28, 39, GPIOPin.PIN4), CLOCK1(30, 31, GPIOPin.PIN5), CLOCK2(32, 33, GPIOPin.PIN6);

  public final int controlRegister;
  public final int dividerRegister;
  public final GPIOPin gpioPin;

  private GPClockChannel(int controlRegister, int dividerRegister, GPIOPin gpioPin) {
    this.controlRegister = controlRegister;
    this.dividerRegister = dividerRegister;
    this.gpioPin = gpioPin;
  }

}
