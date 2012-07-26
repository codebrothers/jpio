package org.codebrothers.jpio.gpclock;

import static org.codebrothers.jpio.JPIO.CLOCK;

import org.codebrothers.jpio.gpio.Function;
import org.codebrothers.jpio.gpio.GPIO;

public class GPClock {

  private static int BUSY_BIT = 1 << 7;

  private static int ENABLE_BIT = 1 << 4;

  private static int CLOCK_MANAGER_PASSWORD = 0x5A000000;

  private static Function PIN_CLOCK_FUNCTION = Function.ALT0;

  /**
   * Sets up the channel provided, with the specified settings
   */
  public static synchronized void enable(GPClockChannel channel, GPClockSource source, GPClockMash mash) {
    // configure channel's pin for GPClock
    GPIO.setPinFunction(channel.gpioPin, PIN_CLOCK_FUNCTION);
    // reset the channel
    resetChannel(channel);
    // set the source
    configureClockSource(channel, source);
    // set the mash
    configureMash(channel, mash);
    // set the frequency
    // setFrequency(channel, frequency);
    // enable the channel
    CLOCK.put(channel.controlRegister, CLOCK.get(channel.controlRegister) | ENABLE_BIT);
  }

  private static void disable(GPClockChannel channel) {
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
    CLOCK.put(channel.controlRegister,
    /* Clear the bits for the pin, ready for new value */
    (CLOCK.get(channel.controlRegister) & GPClockSource.SOURCE_MASK)
    /* Set the new value for specified source */
    | source.value);
  }

  private static void configureMash(final GPClockChannel channel, final GPClockMash mash) {
    CLOCK.put(channel.controlRegister,
    /* Clear the bits for the pin, ready for new value */
    (CLOCK.get(channel.controlRegister) & GPClockMash.MASH_MASK)
    /* Set the new value for specified mash */
    | mash.value);
  }

  // private static void setFrequency(GPClockChannel channel, GPClockFrequency
  // frequency) {
  // // TODO Auto-generated method stub
  //
  // }

}
