package org.codebrothers.jpio.port.shift595;

import org.codebrothers.jpio.pin.DigitalPin;
import org.codebrothers.jpio.port.DigitalPort;

/**
 * A port for bit banging a single or daisy chained 74HC595 via 3 or 4
 * {@link DigitalPin} instances.
 * 
 * Can be configured to any size and to use any digital pins.
 * 
 * @author Rick Watson
 */
public class Shift595 extends DigitalPort {

  private final DigitalPin data;
  private final DigitalPin clock;
  private final DigitalPin latch;
  private final DigitalPin clear;
  private final Boolean[] values;

  /**
   * Constructs the Shift595 port using the provided digital pins for bit
   * banging.
   * 
   * @param data
   *          The data pin on the 595
   * @param clock
   *          The clock pin on the 595
   * @param latch
   *          The latch pin on the 595
   * @param bits
   *          The number of bits in the 595, if setting to a value less than the
   *          number of outputs on the 595, a clear pin needs to be provided.
   */
  public Shift595(DigitalPin data, DigitalPin clock, DigitalPin latch, int bits) {
    this(data, clock, latch, null, bits);
  }

  /**
   * Constructs the Shift595 port using the provided digital pins for bit
   * banging.
   * 
   * @param data
   *          The data pin on the 595
   * @param clock
   *          The clock pin on the 595
   * @param latch
   *          The latch pin on the 595
   * @param clear
   *          The clear pin on the 595.
   * @param bits
   *          The number of bits in the 595, if setting to a value less than the
   *          number of outputs on the 595, a clear pin needs to be provided.
   */
  public Shift595(DigitalPin data, DigitalPin clock, DigitalPin latch, DigitalPin clear, int bits) {
    super(bits);
    this.data = data;
    this.clock = clock;
    this.latch = latch;
    this.clear = clear;
    // assign defaults?
    this.values = new Boolean[bits];
    for (int i = 0; i < bits; i++)
      this.values[i] = false;
  }

  /**
   * Applies an incoming value to the values array, ready for the next call to
   * {@link #flushChanges()}.
   * 
   * @param pin
   *          The pin to apply the change to *
   * @param value
   *          The new value to apply to the pin.
   * 
   * @return true if a new value was written, false if no change detected.
   * 
   */
  @Override
  public boolean applyChange(int pin, Boolean value) {
    if (!values[pin].equals(value)) {
      values[pin] = value;
      return true;
    }
    return false;
  }

  /**
   * Flush the changes. Bit bangs all of the values out to the 595 shift
   * registers!
   */
  @Override
  protected void flushChanges() {
    // only clear if a clear pin has been given
    if (clear != null) {
      clear.setValue(false);
      clear.setValue(true);
    }
    for (Boolean value : values) {
      // Write next value to data pin.
      data.setValue(value);
      // Pulse clock input to write next bit.
      clock.setValue(false);
      clock.setValue(true);
    }
    // Pulse latch to transfer data from shift to storage registers.
    latch.setValue(false);
    latch.setValue(true);
  }

  /**
   * Returns the value for the specified pin.
   * <p>
   * This will be the most recent value applied to the the port and may not have
   * been flushed yet.
   * 
   * @param pin
   *          The pin to return the value for.
   * 
   * @return The pins value.
   */
  @Override
  public Boolean getPinValue(int pin) {
    return values[pin];
  }

}
