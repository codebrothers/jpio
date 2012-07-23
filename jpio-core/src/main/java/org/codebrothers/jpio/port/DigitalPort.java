package org.codebrothers.jpio.port;

import org.codebrothers.jpio.pin.DigitalPin;

/**
 * Extends {@link Port} with generics in order to produce an abstract
 * DigitalPort.
 * <p>
 * Ports writing Boolean levels to their pins can extend this class without the
 * need for the generics.
 * 
 * @author: Rick Watson
 */
public abstract class DigitalPort extends Port<Boolean, DigitalPin> {

  /**
   * Construct the DigitalPort with a specific size.
   * 
   * @param size
   *          The number of pins in the port instance.
   */
  public DigitalPort(int size) {
    super(size, DIGITAL);
  }

}
