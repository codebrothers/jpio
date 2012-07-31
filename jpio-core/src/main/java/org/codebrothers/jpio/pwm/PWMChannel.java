package org.codebrothers.jpio.pwm;

/**
 * The two PWM channels on the Raspberry PI
 */
public enum PWMChannel {

  PWM0(4, 5), PWM1(8, 9);

  public final int ordinal;
  public final int rangeRegister;
  public final int dataRegister;

  private PWMChannel(final int rangeRegister, final int dataRegister) {
    this.ordinal = ordinal();
    this.rangeRegister = rangeRegister;
    this.dataRegister = dataRegister;
  }

}
