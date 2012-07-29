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

  /*
   * Scaler: we can multiply a float's fractional part by this to convert it to
   * it's integer form.
   */
  private static int DIVISOR_FRACTION_SCALER = DIVISOR_COMPONENT + 1;

  /*
   * ALT0 assigns a pin to output according to it's appropriate GP clock!
   */
  private static Function PIN_CLOCK_FUNCTION = Function.ALT0;

  // /**
  // * Sets up the channel provided, with the specified settings
  // */
  // public static synchronized void configure(ClockChannel channel, ClockSource
  // source, ClockMash mash, float divisor) {
  // // configure channel's pin for GPClock
  // GPIO.setPinFunction(channel.gpioPin, PIN_CLOCK_FUNCTION);
  // // reset the channel
  // resetChannel(channel);
  // // set the source
  // configureClockSource(channel, source);
  // // set the mash
  // configureMash(channel, mash);
  // // set the divisor
  // configureDivisor(channel, divisor);
  // }

  /**
   * Enables the channel. The channel's pin will be automatically configured to
   * output the clock.
   * 
   * @param channel
   *          The channel to disable.
   */
  public static void enable(ClockChannel channel) {
    // configure channel's pin for GPClock
    GPIO.setPinFunction(channel.gpioPin, PIN_CLOCK_FUNCTION);
    // enable the channel
    setGPClockMaskedValue(CLOCK, channel.controlRegister, ENABLE_MASK, ENABLE_VALUE);
  }

  /**
   * Disables the channel, leaves it's other settings unchanged. The pin will be
   * reconfigured for input, and will therefore go low.
   * 
   * @param channel
   *          The channel to disable.
   */
  public static void disable(ClockChannel channel) {
    // configure channel's pin to input
    GPIO.setPinFunction(channel.gpioPin, Function.INPUT);
    // disable the clock
    setGPClockMaskedValue(CLOCK, channel.controlRegister, ENABLE_MASK, 0);
    // wait for the channel to go idle
    awaitIdle(channel);
  }

  /**
   * Disables the channel and resets it's settings. Waits for the channel to
   * become idle before returning.
   * 
   * @param channel
   *          The channel to reset.
   */
  public static void resetChannel(ClockChannel channel) {
    // disable the channel
    disable(channel);
    // reset both of the channel's registers
    CLOCK.put(channel.controlRegister, CLOCK_MANAGER_PASSWORD);
    CLOCK.put(channel.dividerRegister, CLOCK_MANAGER_PASSWORD);
  }

  /**
   * Configures the channel's clock source as specified.
   * 
   * @param channel
   *          The channel to configure.
   * @param source
   *          The clock source to use.
   */
  public static void configureSource(final ClockChannel channel, final ClockSource source) {
    setGPClockMaskedValue(CLOCK, channel.controlRegister, ClockSource.SOURCE_MASK, source.value);
  }

  /**
   * Configures the channel's MASH setting as specified.
   * 
   * @param channel
   *          The channel to configure.
   * @param mash
   *          The mash setting to apply.
   */
  public static void configureMash(final ClockChannel channel, final ClockMash mash) {
    setGPClockMaskedValue(CLOCK, channel.controlRegister, ClockMash.MASH_MASK, mash.value);
  }

  /**
   * Configures the channel's divisor, handling the conversion from float to
   * fixed point value.
   * 
   * @param channel
   *          The channel to configure.
   * @param divisor
   *          The divisor to use.
   * 
   * @throws IllegalArgumentException
   *           If the divisor is out of the supported range of the 12 bit
   *           integer/12 bit fractional fixed point value.
   */
  public static void configureDivisor(ClockChannel channel, float divisor) {
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

  /*
   * Spins a loop till busy bit goes clear
   */
  private static void awaitIdle(ClockChannel channel) {
    while (isBitSet(CLOCK, channel.controlRegister, BUSY_BIT)) {
    }
  }

  /*
   * Adds the password to the masked value we are inserting. Remember we
   * neededn't add the password bits to the mask as they always read zero/
   */
  private static void setGPClockMaskedValue(final IntBuffer buffer, final int index, final int mask, final int value) {
    setMaskedValue(buffer, index, mask, CLOCK_MANAGER_PASSWORD | value);
  }

}
