package org.codebrothers.jpio.pwm;

/**
 * Status register flags.
 * <p>
 * An enumeration of the various flags which can be read from the PWM status
 * register.
 * <p>
 * Some of these flag can be set to clear error states and resume transmission
 * where required.
 * 
 * @author Rick Watson
 */
public enum PWMStatus {

  /**
   * FIFO Empty Flag
   * <p>
   * This flag indicates the full status of the FIFO. If this flag is high FIFO
   * is full.
   */
  FIFO_FULL(0x1),

  /**
   * FIFO Empty Flag
   * <p>
   * This flag indicates the empty status of the FIFO. If this bit is high FIFO
   * is empty.
   */
  FIFO_EMPTY(0x2),

  /**
   * FIFO Write Error Flag
   * <p>
   * This flag goes high when a write when full error occurs.
   * <p>
   * Software must clear this bit by writing 1. Writing 0 to this bit has no
   * effect.
   */
  FIFO_WRITE_ERROR(0x4),

  /**
   * FIFO Read Error Flag
   * <p>
   * This flag goes high when a read when empty error occurs.
   * <p>
   * Software must clear this bit by writing 1. Writing 0 to this bit has no
   * effect.
   */
  FIFO_READ_ERROR(0x8),

  /**
   * Channel 1 Gap Occurred Flag
   * <p>
   * This flag goes high when there has been a gap between transmission of two
   * consecutive data from FIFO. This may happen when FIFO gets empty after
   * state machine has sent a word and waits for the next. If
   * Control.REPEAT_LAST is set to high this event will not occur.
   * <p>
   * Software must clear this bit by writing 1. Writing 0 to this bit has no
   * effect.
   */
  CHANNEL1_GAP(0x10),

  /**
   * Channel 2 Gap Occurred Flag
   * <p>
   * This flag goes high when there has been a gap between transmission of two
   * consecutive data from FIFO. This may happen when FIFO gets empty after
   * state machine has sent a word and waits for the next. If
   * Control.REPEAT_LAST is set to high this event will not occur.
   * <p>
   * Software must clear this bit by writing 1. Writing 0 to this bit has no
   * effect.
   */
  CHANNEL2_GAP(0x20),

  /**
   * Bus Error Flag
   * <p>
   * This flag goes high when an error has occurred while writing to registers
   * via APB. *
   * <p>
   * This may happen if the bus tries to write successively to same set of
   * registers faster than the synchroniser block can cope with. Multiple
   * switching may occur and contaminate the data during synchronisation.
   * <p>
   * Software should clear this bit by writing 1. Writing 0 to this bit has no
   * effect.
   */
  BUS_ERROR(0x100),

  /**
   * Channel 1 State
   * <p>
   * This flag indicates the current state of the channel which is useful for
   * debugging purposes.
   * <ul>
   * <li>0: the channel is not currently transmitting
   * <li>1: the channel is transmitting data
   * </ul>
   */
  CHANNEL1_STATE(0x200),

  /**
   * Channel 2 State
   * <p>
   * This flag indicates the current state of the channel which is useful for
   * debugging purposes.
   * <ul>
   * <li>0: the channel is not currently transmitting
   * <li>1: the channel is transmitting data
   * </ul>
   */
  CHANNEL2_STATE(0x400);

  public final int value;
  public final int mask;

  private PWMStatus(final int value) {
    this.value = value;
    this.mask = ~value;
  }

}
