package org.codebrothers.jpio;

/**
 * Function settings for GPIO pins.
 * 
 * @author: Rick Watson
 */
public enum Function {

  /**
   * The pin will be an input pin.
   */
  INPUT(0b000),

  /**
   * The pin will be an output pin.
   */
  OUTPUT(0b001),

  /**
   * The pin will take alternate function 0.
   */
  ALT0(0b100),

  /**
   * The pin will take alternate function 1.
   */
  ALT1(0b101),

  /**
   * The pin will take alternate function 2.
   */
  ALT2(0b110),

  /**
   * The pin will take alternate function 3.
   */
  ALT3(0b111),

  /**
   * The pin will take alternate function 4.
   */
  ALT4(0b011),

  /**
   * The pin will take alternate function 5.
   */
  ALT5(0b010);

  /*
   * Pre-shifted values (10 pins per register), saves shifting at runtime.
   */
  public final int[] values = new int[10];

  private Function(int value) {
    for (int i = 0; i < 10; i++) {
      values[i] = value << i * 3;
    }
  }

}
