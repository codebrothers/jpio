package org.codebrothers.jpio.port;

import org.codebrothers.jpio.pin.BytePin;

/**
 * Extends {@link Port} with generics in order to produce an abstract BytePort.
 * <p>
 * Ports writing Byte levels to their pins can extend this class.
 * 
 * @author: Rick Watson
 */
public abstract class BytePort extends Port<Byte, BytePin> {

  /**
   * Construct the BytePort with a specific size.
   * 
   * @param size
   *          The number of pins in the port instance.
   */
  public BytePort(int size) {
    super(size, BYTE);
  }

}
