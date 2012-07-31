package org.codebrothers.jpio.pwm;

/**
 * Settings for the PWM channel's control register.
 * 
 * Values and masks for each PWM channel are calculated statically.
 * 
 * @author Rick Watson
 */
public enum PWMControl {

  /**
   * Channel enable state
   * <p>
   * Used to enable/disable the channel. Setting this bit to 1 enables the
   * channel and transmitter state machine.
   * <p>
   * All registers and FIFO are writable without setting this bit.
   * <ul>
   * <li>0: Channel is disabled
   * <li>1: Channel is enabled
   * </ul>
   */
  ENABLE(0x01),

  /**
   * Channel mode
   * <p>
   * Used to determine mode of operation.
   * <ul>
   * <li>0: PWM mode
   * <li>1: Serialiser mode
   * </ul>
   */
  MODE(0x02),

  /**
   * Channel repeat mode
   * <p>
   * Used to enable/disable repeating of the last data available in the FIFO
   * just before it empties. When this bit is 1 and FIFO is used, the last
   * available data in the FIFO is repeatedly sent. This may be useful in PWM
   * mode to avoid duty cycle gaps. If the FIFO is not used this bit does not
   * have any effect. Default operation is do-notrepeat.
   * <ul>
   * <li>0: Transmission interrupts when FIFO is empty
   * <li>1: Last data in FIFO is transmitted repeatedly until FIFO is not empty
   * </ul>
   */
  REPEAT_LAST(0x04),

  /**
   * Channel Silence bit
   * <p>
   * Defines the state of the output when no transmission takes place. It also
   * defines the zero polarity for the zero padding in serialiser mode.
   */
  SILENCE_BIT(0x08),

  /**
   * Channel Polarity
   * <p>
   * Used to configure the polarity of the output bit. When set to high the
   * final output is inverted.
   * <p>
   * Default operation is no inversion.
   * <ul>
   * <li>0: 0=low 1=high
   * <li>1: 1=low 0=high
   * </ul>
   */
  POLARITY(0x10),

  /**
   * Use FIFO for channel
   * <p>
   * Used to enable/disable FIFO transfer.
   * <ul>
   * <li>0: Data register is transmitted
   * <li>1: FIFO is used for transmission
   * </ul>
   */
  USE_FIFO(0x20),

  /**
   * Channels clear FIFO operation
   * <p>
   * Used to clear the FIFO. Writing a 1 to this bit clears the FIFO. Writing 0
   * has no effect. This is a single shot operation and reading the bit always
   * returns 0.
   * <ul>
   * <li>1: Clears FIFO
   * <li>0: Has no effect (single shot operation). This bit always reads 0
   * </ul>
   */
  CLEAR_FIFO(0x40),

  /**
   * Channels M/S enable state
   * <p>
   * Used to determine whether to use PWM algorithm or simple M/S ratio
   * transmission. When this bit is high M/S transmission is used.
   * <p>
   * This bit is zero as default. When serialiser mode is used, this bit has no
   * effect.
   * <ul>
   * <li>0: PWM algorithm is used
   * <li>1: M/S transmission is used.
   * </ul>
   */
  MS_ENABLE(0x80);

  /*
   * Shared PWM Control Register
   */
  public static final int PWM_CONTROL_REGISTER = 0;

  /*
   * Control values for the PWM channels, stored by channel ordinal.
   */
  public final int[] values = new int[2];

  /*
   * Control value masks for the PWM channels, stored by channel ordinal.
   */
  public final int[] masks = new int[2];

  private PWMControl(final int value) {
    values[0] = value;
    values[1] = value << 8;
    masks[0] = ~values[0];
    masks[1] = ~values[1];
  }

}
