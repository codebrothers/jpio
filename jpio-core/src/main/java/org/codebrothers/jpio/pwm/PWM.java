package org.codebrothers.jpio.pwm;

import static org.codebrothers.jpio.JPIO.PWM;
import static org.codebrothers.jpio.util.BitUtils.setMaskedValue;

public class PWM {

  // in clock package? refactor? just one for all channels?
  public static final int PWM_CLOCK_CONTROL_REGISTER = 40;
  public static final int PWM_CLOCK_DIVIDER_REGISTER = 41;

  // TODO really need to pre-shift? Could implement a setBitValue(buf, index,
  // mask, value) in BitUtils
  public void setControl(PWMChannel channel, PWMControl control, boolean value) {
    if (value)
      setMaskedValue(PWM, PWM_CLOCK_CONTROL_REGISTER, control.masks[channel.ordinal], control.values[channel.ordinal]);
    else
      setMaskedValue(PWM, PWM_CLOCK_CONTROL_REGISTER, control.masks[channel.ordinal], 0);
  }

}
