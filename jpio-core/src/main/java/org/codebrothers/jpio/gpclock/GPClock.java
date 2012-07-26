package org.codebrothers.jpio.gpclock;

import static org.codebrothers.jpio.JPIO.CLOCK;
import static org.codebrothers.jpio.util.BitUtils.isBitSet;
import static org.codebrothers.jpio.util.BitUtils.setMaskedValue;

import java.text.MessageFormat;

import org.codebrothers.jpio.gpio.Function;
import org.codebrothers.jpio.gpio.GPIO;

public class GPClock {

  /*
   * Enable the clock generator:
   * 
   * This requests the clock to start or stop without glitches. The output clock
   * will not stop immediately because the cycle must be allowed to complete to
   * avoid glitches. The BUSY flag will go low when the final cycle is
   * completed.
   */
  private static int ENABLE_BIT = 1 << 4;

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
   * 11 bit divisor component size.
   * 
   * (11 bits).(11 bits)
   */
  private static int DIVISOR_COMPONENT = 0xFFF;

  /*
   * Scaler: we can multiply a float's fractional part by this to convert it to
   * it's integer form.
   */
  private static int DIVISOR_FRACTION_SCALER = DIVISOR_COMPONENT + 1;

  /*
   * ALT0 maps a pin to it's appropriate GP Clock's Output!
   */
  private static Function PIN_CLOCK_FUNCTION = Function.ALT0;

  /**
   * Sets up the channel provided, with the specified settings
   */
  public static synchronized void configure(GPClockChannel channel, GPClockSource source, GPClockMash mash, float divisor) {
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
    while (isBitSet(CLOCK, channel.controlRegister, BUSY_BIT)) {
      // spin the loop till busy bit is clear
    }
  }

  private static void configureClockSource(final GPClockChannel channel, final GPClockSource source) {
    setMaskedValue(CLOCK, channel.controlRegister, GPClockSource.SOURCE_MASK, source.value);
  }

  private static void configureMash(final GPClockChannel channel, final GPClockMash mash) {
    setMaskedValue(CLOCK, channel.controlRegister, GPClockMash.MASH_MASK, mash.value);
  }

  private static void configureDivisor(GPClockChannel channel, float divisor) {
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
