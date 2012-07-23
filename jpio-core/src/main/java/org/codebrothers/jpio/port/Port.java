package org.codebrothers.jpio.port;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.codebrothers.jpio.pin.BytePin;
import org.codebrothers.jpio.pin.DigitalPin;
import org.codebrothers.jpio.pin.Pin;

/**
 * A port which has outputs or inputs that can be controlled synchronously.
 * <p>
 * Designed to be efficient where synchronous access to a shift registers (or
 * other types of IO hardware) is required whilst still providing access at pin
 * granularity.
 * 
 * @author: Rick Watson
 */
public abstract class Port<T, P extends Pin<T>> implements Iterable<P> {

  private final ReentrantLock lock = new ReentrantLock();

  private final int size;

  private final T[] outputBuffer;

  private final List<P> pins;

  /**
   * Construct the port with a specific size, providing the PortPin type so the
   * pin instances can be created.
   * 
   * @param size
   *          The number of pins in the port instance.
   * @param portPinClass
   *          The PortPin type.
   */
  @SuppressWarnings("unchecked")
  public Port(int size, Class<? extends PortPin<T>> portPinClass) {
    try {
      this.size = size;
      // warning: cannot pass this as typed array outside here:
      // http://stackoverflow.com/questions/529085/java-how-to-generic-array-creation
      this.outputBuffer = (T[]) new Object[size];
      // construct immutable array list of pins
      final List<P> pins = new ArrayList<P>();
      for (int i = 0; i < size; i++) {
        final PortPin<T> portPin = portPinClass.newInstance();
        portPin.pin = i;
        portPin.port = this;
        pins.add((P) portPin);
      }
      this.pins = Collections.unmodifiableList(pins);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Provides an iterator for the ports pins.
   */
  @Override
  public Iterator<P> iterator() {
    return pins.iterator();
  }

  /**
   * Get the number of pins in this port.
   */
  public int getSize() {
    return size;
  }

  /**
   * PortPin type for a BytePort.
   */
  protected static Class<BytePortPin> BYTE = BytePortPin.class;

  /**
   * PortPin type for a DigitalPort.
   */
  protected static Class<DigitalPortPin> DIGITAL = DigitalPortPin.class;

  /*
   * Notes on Locking
   * 
   * Allows a single thread to gain exclusive access to the port in order to
   * make an atomic set of changes.
   * 
   * Whilst the locking is exclusive the writing is not restricted to the
   * holding thread, allowing multiple threads to write to a port during an
   * atomic process.
   * 
   * I may choose to extend locking to allow multiple locks to be held at once
   * if need arises. However I'd like to examine and understand a use case
   * before adding the complexity.
   */

  /**
   * Aborts the current atomic operation. Changes will not be written to the
   * port.
   * <p>
   * This can only be called from the thread which called {@link #beginAtomic()}
   */
  public synchronized void abortAtomic() {
    checkLockHeld();
    lock.unlock();
  }

  /**
   * Completes the current atomic operation, delegating for the changes to be
   * written by the port implementation.
   * <p>
   * This can only be called from the thread which called {@link #beginAtomic()}
   */
  public synchronized void completeAtomic() {
    checkLockHeld();
    try {
      applyChanges(outputBuffer);
    } finally {
      lock.unlock();
    }
  }

  /**
   * Starts an atomic operation, locking port.
   * <p>
   * Once called, reads and writes to the underlying port hardware will be held
   * until {@link #completeAtomic()} is called again by this thread.
   * <p>
   * Changes to the port can be prepared by any thread using either the pins or
   * the {@link #setPinValue(int, T)} method.
   */
  public synchronized boolean beginAtomic() {
    checkLockNotHeld();
    if (lock.tryLock()) {
      clearOutputBuffer();
      return true;
    }
    return false;
  }

  /**
   * Allows for a value to be written to a pin on the port. Lock may or may not
   * be held. Changes are held in a buffer if a atomic operation is open,
   * otherwise they are applied immediately.
   * 
   * @param pin
   *          The pin for which to set the value.
   * @param value
   *          The new value to set for the pin.
   */
  public synchronized void setPinValue(int pin, T value) {
    if (lock.isLocked()) {
      outputBuffer[pin] = value;
    } else {
      applyChange(pin, value);
      flushChanges();
    }
  }

  /**
   * Reads the current value from a pin.
   * 
   * @param pin
   *          The pin from which to get the value.
   */
  public abstract T getPinValue(int pin);

  /**
   * Get a {@link org.codebrothers.jpio.pin.Pin} instance for a particular pin
   * on this port.
   * 
   * @param pin
   *          The pin instance to return.
   */
  public P getPin(int pin) {
    return pins.get(pin);
  }

  /*
   * Clears the output buffer, used when an atomic operation is started.
   */
  private void clearOutputBuffer() {
    for (int i = 0; i < outputBuffer.length; i++) {
      outputBuffer[i] = null;
    }
  }

  /*
   * Ensures the current thread holds the lock
   */
  private void checkLockHeld() {
    if (!lock.isHeldByCurrentThread()) {
      throw new IllegalMonitorStateException("Current thread does not hold this lock.");
    }
  }

  /*
   * Ensures the current thread does not already hold the lock, prevents
   * multiple locks which is not the intended usage pattern.
   */
  private void checkLockNotHeld() {
    if (lock.isHeldByCurrentThread()) {
      throw new IllegalMonitorStateException("Current thread already holds this lock.");
    }
  }

  /*
   * Uses the implementation to apply the changes to the port
   */
  private void applyChanges(T[] outputBuffer) {
    boolean changes = false;
    for (int i = 0; i < outputBuffer.length; i++) {
      if (outputBuffer[i] != null && applyChange(i, outputBuffer[i])) {
        changes = true;
      }
    }
    // write changes to the hardware, some port types write out in batches!
    if (changes) {
      flushChanges();
    }
  }

  /**
   * Can be overridden if the implementation requires flushing after a change or
   * group of changes are made to the port.
   */
  protected void flushChanges() {
    // default implementation does nothing on flush changes
  }

  /*
   * To be implemented by the extending class.
   * 
   * Depending on the implementation this might go directly to the hardware, or
   * in the case of shift register ports this may be delayed until
   * flushChanges() is called.
   */
  protected abstract boolean applyChange(int pin, T value);

  static class BytePortPin extends PortPin<Byte> implements BytePin {

  }

  static class DigitalPortPin extends PortPin<Boolean> implements DigitalPin {

  }

  /*
   * An internal class, wraps a pin and ties it specifically to this port
   * instance. Therefore adopts the ports behaviour (locking etc)!
   */
  @SuppressWarnings("rawtypes")
  static class PortPin<L> implements Pin<L> {

    private int pin;
    private Port port;

    @SuppressWarnings("unchecked")
    public L getValue() {
      return (L) port.getPinValue(pin);
    }

    @SuppressWarnings("unchecked")
    public void setValue(L value) {
      port.setPinValue(pin, value);
    }

  }

}
