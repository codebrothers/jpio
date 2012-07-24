/*
 * Copyright (c) 2000-2012 Open Objects Software Ltd.
 * All Rights Reserved.
 */
package org.codebrothers.jpio;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

/**
 * The peripheral bus interface for the JPIO library.
 * <p>
 * <strong>Remember:</strong> You must call JPIO.init() before you can use any
 * of the peripheral functions.
 * <p>
 * 
 * @author: Rick Watson
 */
public class JPIO {

  private static IntBuffer GPIO;

  private static boolean initialized = false;

  private JPIO() {
    // cannot be constructed
  }

  /**
   * Configures JPIO by getting the JVM direct access to the peripheral bus.
   * <p>
   * Must be called before you can use any of the peripheral functions.
   */
  /*
   * Configures JPIO by getting the JVM direct access to the peripheral bus. The
   * JNI functions bind the required memory regions to ByteBuffers. <p> It might
   * be possible to get this to work with pure java using FileChannel but I've
   * not had any success yet. <p> Remember that the data structure we are
   * getting is going to be in little endian byte order. We must make the
   * ByteBuffer aware of this or the bytes will be mirrored and our IntBuffer
   * will inherit this!
   */
  public static void init() {
    if (!initialized) {
      System.loadLibrary("JPIO");
      final ByteBuffer buf = mapGPIO();
      buf.order(ByteOrder.LITTLE_ENDIAN);
      GPIO = buf.asIntBuffer();
      initialized = true;
    }
  }

  private static native ByteBuffer mapGPIO();

  /**
   * Sets a pin's function using the pin's function register.
   * 
   * @param pin
   *          The pin for which to set the function.
   * @param function
   *          The new function for the pin.
   */
  public static synchronized void setPinFunction(final GPIOPin pin, final Function function) {
    GPIO.put(pin.functionRegister,
    /* Clear the bits for the pin, ready for new value */
    (GPIO.get(pin.functionRegister) & pin.functionMask)
    /* Set the new value for specified function */
    | function.values[pin.functionOrdinal]);
  }

  /**
   * Sets or clears a pin's value using the pin's set or clear register.
   * 
   * @param pin
   *          The pin for which to set the value.
   * @param value
   *          The new value for the pin.
   */
  public static synchronized void setPinValue(final GPIOPin pin, final boolean value) {
    GPIO.put(value ? pin.setRegister : pin.clearRegister, pin.pinValue);
  }

  /**
   * Gets a pin's value using the pin's level register.
   * 
   * @param pin
   *          The pin for which to get the value.
   * @return The value of the pin.
   */
  public static synchronized boolean getPinValue(final GPIOPin pin) {
    return (GPIO.get(pin.levelRegister) & pin.pinValue) != 0;
  }

  /**
   * Sets a pin's internal resistor to one of the three {@link Resistor} states.
   * 
   * @param pin
   *          The pin for which to set the internal resistor mode.
   * @param resistor
   *          The internal resistor mode to set for the pin.
   */
  public static synchronized void setPinResistor(final GPIOPin pin, final Resistor resistor) {
    // See page 101 in the datasheet for details on how this is implemented.
    //
    // 250mhz is the speed of the peripheral bus clock, and the datasheet says
    // to sleep for 150 ticks. So we'll need to sleep for at least:
    //
    // 1x10&#x2079/(2.5x10&#x2078/150) = 600ns (1000ns will do!)
    //
    // set new up/down value
    GPIO.put(Resistor.VALUE_REGISTER, resistor.value);
    // provide the required set-up time for the control signal
    delayNs(1000);
    // to clock the control signal into the GPIO pads
    GPIO.put(pin.pullUpDownClockRegister, pin.pinValue);
    // required hold time for the control signal
    delayNs(1000);
    // take our values out of the registers
    GPIO.put(Resistor.VALUE_REGISTER, 0x00);
    GPIO.put(pin.pullUpDownClockRegister, 0x00);
  }

  /**
   * Quick and dirty millisecond delay using Thread.sleep.
   * 
   * @param delay
   *          the (minimum) amount of time to sleep in milliseconds.
   */
  public static void delayMs(long delay) {
    try {
      Thread.sleep(delay);
    } catch (InterruptedException e) {
      // TODO log?
      e.printStackTrace();
    }
  }

  /**
   * Spins a loop until the required number of nanoseconds have elapsed,
   * terribly wasteful, but useful if you need to sleep for a very small amount
   * of time!
   * 
   * @param delay
   *          the (minimum) amount of time to sleep in nanoseconds.
   */
  public static void delayNs(long delay) {
    final long delayUntil = System.nanoTime() + delay;
    while (System.nanoTime() < delayUntil) {
    }
  }

}