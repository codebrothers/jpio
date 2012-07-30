package org.codebrothers.jpio.pwm;

/**
 * Settings for the PWM channel's control register.
 * 
 * Values for each PWM channel are calculated statically.
 * 
 * @author Rick Watson
 */
public enum PWMControl {

  /**
   * Channel enable state
   * 
   * <ul>
   * <li>0: Channel is disabled
   * <li>1: Channel is enabled
   * </ul>
   */
  ENABLE(0x01),

  /**
   * Channel mode
   * 
   * <ul>
   * <li>0: PWM mode
   * <li>1: Serialiser mode
   * </ul>
   */
  MODE(0x02),

  /**
   * Channel repeat mode
   * 
   * <ul>
   * <li>0: Transmission interrupts when FIFO is empty
   * <li>1: Last data in FIFO is transmitted repeatedly until FIFO is not empty
   * </ul>
   */
  REPEAT_LAST(0x04),

  /**
   * Channel Silence bit
   * 
   * <p>
   * Defines the state of the output when no transmission takes place
   */
  SILENCE_BIT(0x08),

  /**
   * Channel Polarity
   * 
   * <ul>
   * <li>0: 0=low 1=high
   * <li>1: 1=low 0=high
   * </ul>
   */
  POLARITY(0x10),

  /**
   * Use FIFO for channel
   * 
   * <ul>
   * <li>0: Data register is transmitted
   * <li>1: FIFO is used for transmission
   * </ul>
   */
  USE_FIFO(0x20),

  /**
   * Channels clear FIFO operation
   * 
   * <ul>
   * <li>1: Clears FIFO
   * <li>0: Has no effect (single shot operation). This bit always reads 0
   * </ul>
   */
  CLEAR_FIFO(0x40),

  /**
   * Channels M/S enable state
   * 
   * <ul>
   * <li>0: PWM algorithm is used
   * <li>1: M/S transmission is used.
   * </ul>
   */
  MS_ENABLE(0x80);

  public final int[] values = new int[2];
  public final int[] masks = new int[2];

  private PWMControl(int value) {
    values[0] = value;
    values[1] = value << 8;
    masks[0] = ~values[0];
    masks[1] = ~values[1];
  }

}
