package org.codebrothers.jpio;

public enum PWMChannel {

  PWM0(4, 5), PWM1(8, 9);

  public static final int PWM_CONTROL_REGISTER = 0;
  public static final int PWM_STATUS_REGISTER = 1;
  
  public int rangeRegister;
  public int dataRegister;

  private PWMChannel(int rangeRegister, int dataRegister) {
    this.rangeRegister = rangeRegister;
    this.dataRegister = dataRegister;
  }

}
