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

  public void setControl(PWMChannel channel, PWMControl control, boolean value) {
    if (value)
      setBits(PWM, PWM_CONTROL_REGISTER, control.values[channel.ordinal]);
    else
      clearMask(PWM, PWM_CONTROL_REGISTER, control.masks[channel.ordinal]);
  }

  public boolean getStatus(PWMChannel channel, PWMStatus pwmStatus) {
    return isBitSet(PWM, PWM_STATUS_REGISTER, pwmStatus.value);
  }

}
