/*
 * Copyright (c) 2000-2012 Open Objects Software Ltd.
 * All Rights Reserved.
 */
package org.codebrothers.jpio;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

/**
 * The core JNI based mapping code for the JPIO library.
 * <p>
 * <strong>Remember:</strong> You must call JPIO.init() before you can use any
 * of the peripheral functions.
 * <p>
 * 
 * @author: Rick Watson
 */
public class JPIO {

  // to prevent multiple instantiations
  private static boolean initialized = false;

  // The GPIO registers, starting from 0x20200000
  public static IntBuffer GPIO;

  // The clock registers, starting from 0x20101000
  public static IntBuffer CLOCK;

  // The PWM registers, starting from 0x2020C000
  public static IntBuffer PWM;
  
  // The PWM registers, starting from 0x2020C000
  public static IntBuffer SPI0;

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
   * not had any success yet.
   */
  public static synchronized void init() {
    if (!initialized) {
      // we only attempt to initialize once.
      initialized = true;
      // load native library
      System.loadLibrary("JPIO");
      // try and initialize
      if (!initialize()) {
        throw new RuntimeException("Failed to initilize.");
      }
      // fetch and wrap as int buffers
      GPIO = wrapAsIntBuffer(getGPIO());
      CLOCK = wrapAsIntBuffer(getClock());
      PWM = wrapAsIntBuffer(getPWM());
    }
  }

  public static synchronized void initDebug() {
    if (!initialized) {
      // we only attempt to initialize once.
      initialized = true;
      // fetch and wrap as int buffers
      GPIO = wrapAsIntBuffer(ByteBuffer.allocate(45 * 4));
      CLOCK = wrapAsIntBuffer(ByteBuffer.allocate(45 * 4));
      PWM = wrapAsIntBuffer(ByteBuffer.allocate(45 * 4));
    }
  }

  public static void printGPIO() {
    printIntBuffer(GPIO);
  }

  public static void printCLOCK() {
    printIntBuffer(CLOCK);
  }

  public static void printPWM() {
    printIntBuffer(PWM);
  }

  private static void printIntBuffer(IntBuffer intBuff) {
    for (int i = 0; i < intBuff.capacity(); i++) {
      final StringBuilder sb = new StringBuilder(Integer.toBinaryString(intBuff.get(i)));
      while (sb.length() < 32)
        sb.insert(0, "0");
      System.out.println("Offset "+i+":\t" + sb);
    }
  }

  /*
   * Remember that the data structure we are getting is going to be in little
   * endian byte order. We must make the ByteBuffer aware of this or the bytes
   * will be mirrored and our IntBuffer will inherit this!
   */
  private static IntBuffer wrapAsIntBuffer(ByteBuffer buf) {
    buf.order(ByteOrder.LITTLE_ENDIAN);
    return buf.asIntBuffer();
  }

  private static native boolean initialize();

  private static native ByteBuffer getGPIO();

  private static native ByteBuffer getClock();

  private static native ByteBuffer getPWM();

}