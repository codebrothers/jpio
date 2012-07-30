package org.codebrothers.jpio.pwm;

public enum PWMChannel {

  PWM0(4, 5), PWM1(8, 9);

  public static final int PWM_CONTROL_REGISTER = 0;
  public static final int PWM_STATUS_REGISTER = 1;

  public final int ordinal;
  public final int rangeRegister;
  public final int dataRegister;

  private PWMChannel(int rangeRegister, int dataRegister) {
    this.ordinal = ordinal();
    this.rangeRegister = rangeRegister;
    this.dataRegister = dataRegister;
  }

}
