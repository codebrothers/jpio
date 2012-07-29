package org.codebrothers.jpio.gpclock;

/**
 * MASH noise-shaping, can push the jitter out of the audio band if required.
 * 
 * Clock mash is controlled by two bits in the channel's respective control
 * register: bits 9-10.
 * 
 * The mask for resetting the bits is therefore ~(0b11<<9) and the values are
 * also shifted by 9.
 * 
 * @author Rick Watson
 */
public enum ClockMash {

  /**
   * Integer division
   */
  INT(0),

  /**
   * 1-stage MASH (equivalent to non-MASH dividers)
   */
  STAGE1(1),

  /**
   * 2-stage MASH
   */
  STAGE2(2),

  /**
   * 3-stage MASH
   */
  STAGE3(3);

  /*
   * The mask for the mash value.
   */
  public static final int MASH_MASK = ~(0b11 << 9);

  /*
   * The pre-shifted mash value.
   */
  public final int value;

  private ClockMash(int value) {
    this.value = value << 9;
  }

}
