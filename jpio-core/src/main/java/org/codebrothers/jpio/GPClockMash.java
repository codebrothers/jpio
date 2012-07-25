package org.codebrothers.jpio;

/**
 * Clock mash is controlled by two bits in the control register, bits 9-10.
 * 
 * The mask for resetting the bits is therefore ~(0b11<<9) and the values are
 * shifted by 9 too.
 */
public enum GPClockMash {

  INT(0), STAGE1(1), STAGE2(2), STAGE3(3);

  public static final int MASH_MASK = ~(0b11 << 9);
  public int value;

  private GPClockMash(int value) {
    this.value = value << 9;
  }

}
