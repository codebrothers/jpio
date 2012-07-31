package org.codebrothers.jpio.util;

import java.nio.IntBuffer;

public class BitUtils {

  /**
   * Helper method used to clear a group of bits using a mask.
   * <p>
   * The mask is assumed to be inverse.
   * 
   * @param buffer
   *          The buffer containing the register we want to edit.
   * @param index
   *          The index of the register we are editing.
   * @param mask
   *          The inverse mask, used to clear.
   */
  public static void clearMask(final IntBuffer buffer, final int index, final int mask) {
    buffer.put(index, buffer.get(index) & mask);
  }

  /**
   * Helper method used to set a group of bits in a register using a mask.
   * <p>
   * The mask is assumed to be inverse.
   * 
   * @param buffer
   *          The buffer containing the register we want to edit.
   * @param index
   *          The index of the register we are editing.
   * @param mask
   *          The inverse mask, used to clear.
   * @param value
   *          The value to write to the register, any high bits will be set high
   *          in the register.
   */
  public static void setMaskedValue(final IntBuffer buffer, final int index, final int mask, final int value) {
    buffer.put(index,
    /* Clear the bits using the mask, ready for new value */
    (buffer.get(index) & mask)
    /* Set the new value for specified mash */
    | value);
  }

  /**
   * Helper method used to check if a particular bit is set in a register.
   * 
   * @param buffer
   *          The buffer containing the register we want to check.
   * @param index
   *          The index of the register we are checking.
   * @param bit
   *          The bit to check for, assumed to be pre-shifted.
   */
  public static boolean isBitSet(final IntBuffer buffer, final int index, final int bit) {
    return (buffer.get(index) & bit) != 0;
  }

  /**
   * Helper method used to set bits in a register.
   * 
   * @param buffer
   *          The buffer containing the register we are updating.
   * @param index
   *          The index of the register we are updating.
   * @param value
   *          The value to write to the register, any high bits will be set high
   *          in the register.
   */
  public static void setBits(final IntBuffer buffer, final int index, final int value) {
    buffer.put(index, buffer.get(index) | value);
  }

}
