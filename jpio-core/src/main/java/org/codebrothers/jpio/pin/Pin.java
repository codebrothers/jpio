package org.codebrothers.jpio.pin;

/**
 * An interface which allows a single pin to be addressed directly.
 * <p>
 * Abstracted so port implementations (shift registers, PWM controllers etc) can
 * provide access to their input/outputs with pin granularity.
 * 
 * @author: Rick Watson
 */
public interface Pin<T extends Object> {

  /**
   * Get the value of the pin.
   * 
   * @return The current value of the pin.
   */
  public T getValue();

  /**
   * Set the value of the pin.
   * 
   * @param value
   *          The value to set.
   */
  public void setValue(T value);

}
