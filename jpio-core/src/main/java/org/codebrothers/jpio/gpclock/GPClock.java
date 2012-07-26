package org.codebrothers.jpio.gpclock;

import static org.codebrothers.jpio.JPIO.CLOCK;

import java.nio.IntBuffer;
import java.text.MessageFormat;

import org.codebrothers.jpio.gpio.Function;
import org.codebrothers.jpio.gpio.GPIO;

public class GPClock {

  private static int BUSY_BIT = 1 << 7;

  private static int ENABLE_BIT = 1 << 4;

  private static int CLOCK_MANAGER_PASSWORD = 0x5A000000;

  private static int DIVISOR_COMPONENT = 0xFFF;

  private static int DIVISOR_DECIMAL_SCALER = DIVISOR_COMPONENT + 1;

  private static int DIVISOR_MASK = ~(DIVISOR_COMPONENT << 12 | DIVISOR_COMPONENT);

  private static Function PIN_CLOCK_FUNCTION = Function.ALT0;

  public static void main(String[] args) {
    configureDivisor(null, 1.70068359375f);
  }

  // public static void main(String[] args) {
  //
  // System.out.println(Integer.toHexString(0b1111111111111111111111));
  // float f = 1.70068359375f;
  // int decimalPart = (int) f;
  // int fractionalPart = (int) ((f - decimalPart) * 0x1000);
  // System.out.println(decimalPart);
  // System.out.println(fractionalPart);
  // }

  /**
   * Sets up the channel provided, with the specified settings
   */
  public static synchronized void enable(GPClockChannel channel, GPClockSource source, GPClockMash mash, float divisor) {
    // configure channel's pin for GPClock
    GPIO.setPinFunction(channel.gpioPin, PIN_CLOCK_FUNCTION);
    // reset the channel
    resetChannel(channel);
    // set the source
    configureClockSource(channel, source);
    // set the mash
    configureMash(channel, mash);
    // set the divisor
    configureDivisor(channel, divisor);
    // enable the channel
    enable(channel);
  }

  public static void enable(GPClockChannel channel) {
    CLOCK.put(channel.controlRegister, CLOCK.get(channel.controlRegister) | ENABLE_BIT);
  }

  public static void disable(GPClockChannel channel) {
    CLOCK.put(channel.controlRegister, CLOCK.get(channel.controlRegister) & ~ENABLE_BIT);
    awaitIdle(channel);
  }

  /**
   * Resets the channel, waiting for idle before returning.
   */
  private static void resetChannel(GPClockChannel channel) {
    CLOCK.put(channel.controlRegister, CLOCK_MANAGER_PASSWORD);
    CLOCK.put(channel.dividerRegister, CLOCK_MANAGER_PASSWORD);
    awaitIdle(channel);
  }

  private static void awaitIdle(GPClockChannel channel) {
    while ((CLOCK.get(channel.controlRegister) & BUSY_BIT) > 0) {
    }
  }

  private static void configureClockSource(final GPClockChannel channel, final GPClockSource source) {
    setMaskedValue(CLOCK, channel.controlRegister,  GPClockSource.SOURCE_MASK, source.value);
  }

  private static void configureMash(final GPClockChannel channel, final GPClockMash mash) {
    setMaskedValue(CLOCK, channel.controlRegister, GPClockMash.MASH_MASK, mash.value);
  }

  private static void setMaskedValue(IntBuffer buffer, int register, int mask, int value) {
    buffer.put(register,
    /* Clear the bits using the mask, ready for new value */
    (buffer.get(register) & mask)
    /* Set the new value for specified mash */
    | value);
  }

  private static void configureDivisor(GPClockChannel channel, float divisor) {
    final int integerPart = (int) divisor;
    if (integerPart < 0 && integerPart > DIVISOR_COMPONENT) {
      throw new RuntimeException(MessageFormat.format("Integer part of divisor out of range. Recieved {0}, max {1}.",
          integerPart, DIVISOR_COMPONENT));
    }
    final int fractionalPart = (int) ((divisor - integerPart) * DIVISOR_DECIMAL_SCALER);
    if (fractionalPart < 0 && fractionalPart > DIVISOR_COMPONENT) {
      throw new RuntimeException(MessageFormat.format(
          "Fractional part of divisor out of range. Recieved {0}, max {1}.", fractionalPart, DIVISOR_COMPONENT));
    }
    setMaskedValue(CLOCK, channel.dividerRegister, DIVISOR_MASK, integerPart << 12 | fractionalPart);
  }

}
