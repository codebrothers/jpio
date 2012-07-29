package org.codebrothers.jpio.gpclock;

import static org.codebrothers.jpio.JPIO.CLOCK;
import static org.codebrothers.jpio.util.BitUtils.isBitSet;
import static org.codebrothers.jpio.util.BitUtils.setMaskedValue;

import java.nio.IntBuffer;
import java.text.MessageFormat;

import org.codebrothers.jpio.gpio.Function;
import org.codebrothers.jpio.gpio.GPIO;

/**
 * Allows you to control the General Purpose Clock channels on the Raspberry Pi.
 * <p>
 * Each channel has source and divisor properties which can be used to assign a
 * frequency to the clock. The divisor divides the incoming frequency to decide
 * the final frequency of the clock.
 * <p>
 * Each clock channel maps to a single output on the Raspberry Pi, and only one
 * of them is directly exposed on the GPIO header.
 * 
 * @author Rick Watson
 */
public class Clock {

  /*
   * Enable the clock generator:
   * 
   * This requests the clock to start or stop without glitches. The output clock
   * will not stop immediately because the cycle must be allowed to complete to
   * avoid glitches. The BUSY flag will go low when the final cycle is
   * completed.
   */
  private static int ENABLE_VALUE = 1 << 4;
  private static int ENABLE_MASK = ~ENABLE_VALUE;

  /*
   * Clock generator is running:
   * 
   * Indicates the clock generator is running. To avoid glitches and lock-ups,
   * clock sources and setups must not be changed while this flag is set.
   */
  private static int BUSY_BIT = 1 << 7;

  /*
   * Clock manager password
   * 
   * Must be set on bits 31-24 in the control and divisor registers.
   */
  private static int CLOCK_MANAGER_PASSWORD = 0x5A000000;

  /*
   * 12 bit divisor component size.
   * 
   * (12 bits).(12 bits)
   */
  private static int DIVISOR_COMPONENT = 0xFFF;

  public static void main(String[] args) {
    System.out.println(Integer.toBinaryString(0xFFF));
  }

  /*
   * Scaler: we can multiply a float's fractional part by this to convert it to
   * it's integer form.
   */
  private static int DIVISOR_FRACTION_SCALER = DIVISOR_COMPONENT + 1;

  /*
   * ALT0 assigns a pin to output according to it's appropriate GP clock!
   */
  private static Function PIN_CLOCK_FUNCTION = Function.ALT0;

  /**
   * Sets up the channel provided, with the specified settings
   */
  public static synchronized void configure(ClockChannel channel, ClockSource source, ClockMash mash, float divisor) {
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
  }

  public static void enable(ClockChannel channel) {
    setGPClockMaskedValue(CLOCK, channel.controlRegister, ENABLE_MASK, ENABLE_VALUE);
  }

  public static void disable(ClockChannel channel) {
    setGPClockMaskedValue(CLOCK, channel.controlRegister, ENABLE_MASK, 0);
    awaitIdle(channel);
  }

  /**
   * Resets the channel's settings. Waits for the channel to become idle before
   * returning.
   */
  private static void resetChannel(ClockChannel channel) {
    CLOCK.put(channel.controlRegister, CLOCK_MANAGER_PASSWORD);
    CLOCK.put(channel.dividerRegister, CLOCK_MANAGER_PASSWORD);
    awaitIdle(channel);
  }

  private static void awaitIdle(ClockChannel channel) {
    // spin a loop till busy bit goes clear
    while (isBitSet(CLOCK, channel.controlRegister, BUSY_BIT)) {
    }
  }

  private static void configureClockSource(final ClockChannel channel, final ClockSource source) {
    setGPClockMaskedValue(CLOCK, channel.controlRegister, ClockSource.SOURCE_MASK, source.value);
  }

  private static void configureMash(final ClockChannel channel, final ClockMash mash) {
    setGPClockMaskedValue(CLOCK, channel.controlRegister, ClockMash.MASH_MASK, mash.value);
  }

  /*
   * Adds the password to the masked value we are inserting. Remember we
   * neededn't add the password bits to the mask as they always read zero/
   */
  private static void setGPClockMaskedValue(final IntBuffer buffer, final int index, final int mask, final int value) {
    setMaskedValue(buffer, index, mask, CLOCK_MANAGER_PASSWORD | value);
  }

  private static void configureDivisor(ClockChannel channel, float divisor) {
    final int integerPart = (int) divisor;
    if (integerPart < 0 && integerPart > DIVISOR_COMPONENT) {
      throw new RuntimeException(MessageFormat.format("Integer part of divisor out of range. Recieved {0}, max {1}.",
          integerPart, DIVISOR_COMPONENT));
    }
    final int fractionalPart = (int) ((divisor - integerPart) * DIVISOR_FRACTION_SCALER);
    if (fractionalPart < 0 && fractionalPart > DIVISOR_COMPONENT) {
      throw new RuntimeException(MessageFormat.format(
          "Fractional part of divisor out of range. Recieved {0}, max {1}.", fractionalPart, DIVISOR_COMPONENT));
    }
    CLOCK.put(channel.dividerRegister, CLOCK_MANAGER_PASSWORD | integerPart << 12 | fractionalPart);
  }

}
