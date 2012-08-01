package org.codebrothers.jpio.pwm;

import static org.codebrothers.jpio.JPIO.PWM;
import static org.codebrothers.jpio.util.BitUtils.clearMask;
import static org.codebrothers.jpio.util.BitUtils.isBitSet;
import static org.codebrothers.jpio.util.BitUtils.setBits;

public class PWM {

  /*
   * Shared PWM Control Register
   */
  private static final int PWM_CONTROL_REGISTER = 0;

  /*
   * Shared PWM Status Register
   */
  public static final int PWM_STATUS_REGISTER = 1;

  public static void setControlValue(PWMChannel channel, PWMControl control, boolean value) {
    if (value)
      setControl(channel, control);
    else
      clearControl(channel, control);
  }

  public static void clearControl(PWMChannel channel, PWMControl control) {
    clearMask(PWM, PWM_CONTROL_REGISTER, control.masks[channel.ordinal]);
  }

  public static void setControl(PWMChannel channel, PWMControl control) {
    setBits(PWM, PWM_CONTROL_REGISTER, control.values[channel.ordinal]);
  }

  public static boolean getStatus(PWMChannel channel, PWMStatus pwmStatus) {
    return isBitSet(PWM, PWM_STATUS_REGISTER, pwmStatus.value);
  }

  public static void setRange(PWMChannel channel, int range) {
    PWM.put(channel.rangeRegister, range);
  }

  public static void setData(PWMChannel channel, int data) {
    PWM.put(channel.dataRegister, data);
  }

}
