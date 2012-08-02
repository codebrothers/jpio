package org.codebrothers.jpio.gpio;

import org.codebrothers.jpio.pin.DigitalPin;

/**
 * This class deals with the GPIO pins as they appear in the datasheet. It makes
 * no attempt to map them to their relevant pin numbers on the Raspberry Pi's
 * GPIO header.
 * <p>
 * See the {@link PiPin} enum if you would prefer to access pins in that manner.
 * <p>
 * When instantiated, this enum sets up data for the GPIO Pins calculating their
 * registers, offsets, masks and shift values. This approach consumes a little
 * memory in the JVM but results in less shifting/calculation when performing
 * GPIO operations.
 * 
 * @author: Rick Watson
 */
public enum GPIOPin implements DigitalPin {

  // 0-9 (GPFSEL0)
  PIN0, PIN1, PIN2, PIN3, PIN4, PIN5, PIN6, PIN7, PIN8, PIN9,

  // 10-19 (GPFSEL1)
  PIN10, PIN11, PIN12, PIN13, PIN14, PIN15, PIN16, PIN17, PIN18, PIN19,

  // 20-29 (GPFSEL2)
  PIN20, PIN21, PIN22, PIN23, PIN24, PIN25, PIN26, PIN27, PIN28, PIN29,

  // 30-39 (GPFSEL3)
  PIN30, PIN31, PIN32, PIN33, PIN34, PIN35, PIN36, PIN37, PIN38, PIN39,

  // 40-49 (GPFSEL4)
  PIN40, PIN41, PIN42, PIN43, PIN44, PIN45, PIN46, PIN47, PIN48, PIN49,

  // 50-53 (GPFSEL5)
  PIN50, PIN51, PIN52, PIN53;

  // the pin's number, calculated from ordinal()
  final int ordinal;

  // function register, offset and mask
  public final int functionOrdinal;
  public final int functionValueOffset;
  public final int functionRegister;
  public final int functionMask;

  // "pin" registers, offset and mask
  public final int pinOrdinal;
  public final int pinValueOffset;
  public final int setRegister;
  public final int clearRegister;
  public final int levelRegister;
  public final int pullUpDownClockRegister;
  public final int pinValue;
  public final int pinMask;

  // Notes on register and register offset calculations
  // ==================================================
  //
  // The Pi's registers are 32 bits long, mapping nicely to an integer in
  // Java. Therefore we bind the address space as an IntegerBuffer. An offset
  // of 1 represents one register (4 bytes) in the PI's address space.
  //
  // These offsets are relative to the base GPIO address 0x22000000
  //
  private GPIOPin() {

    this.ordinal = ordinal();

    // Calculate function register offset and mask
    // ===========================================
    //
    // Pin function registers are split up as follows:
    //
    // - 5 registers
    // - 3 bits per pin
    // - up to 10 pins per register.
    // - registers are contiguous, starting with an offset of 0
    //
    this.functionOrdinal = (this.ordinal % 10);
    this.functionRegister = this.ordinal / 10;
    this.functionValueOffset = this.functionOrdinal * 3;
    this.functionMask = ~(7 << functionValueOffset);

    // Calculate "pin" registers, offset and mask
    // ==========================================
    //
    // There are 3 types of "pin" register: clear, set and level. There are 2
    // registers of each type: GPCLR0, GPCLR1 GPSET0, GPSET1, GPLEV0 and GPLEV1.
    //
    // Each GPIO pin has one bit in each type of register. As is desirable, the
    // pins span the registers in order and registers are contiguous so the
    // registers and offsets can easily be easily calculated be / and % on 32.
    //
    this.pinOrdinal = ordinal / 32;
    this.pinValueOffset = ordinal % 32;
    this.setRegister = 7 + pinOrdinal;
    this.clearRegister = 10 + pinOrdinal;
    this.levelRegister = 13 + pinOrdinal;
    this.pullUpDownClockRegister = 38 + pinOrdinal;
    this.pinValue = 1 << pinValueOffset;
    this.pinMask = ~pinValue;
  }

  @Override
  public Boolean getValue() {
    return GPIO.getPinValue(this);
  }

  @Override
  public void setValue(Boolean value) {
    GPIO.setPinValue(this, value);
  }

  /**
   * Sets the pins internal resistor to one of the three {@link Resistor}
   * states.
   * 
   * @param resistor
   *          The internal resistor mode to set for the pin.
   */
  public void setPinResistor(Resistor resistor) {
    GPIO.setPinResistor(this, resistor);
  }

  /**
   * Sets the pins function to one of the {@link Function} types.
   * 
   * @param function
   *          The new function for the pin.
   */
  public void setFunction(Function function) {
    GPIO.setPinFunction(this, function);
  }

}
