package org.codebrothers.jpio.clock;

import org.codebrothers.jpio.gpio.Function;
import org.codebrothers.jpio.gpio.GPIO;
import org.codebrothers.jpio.gpio.GPIOPin;

/**
 * The three General Purpose Clock channels.
 * <p>
 * NB: CLOCK0 is the <strong>only</strong> channel directly exposed on the
 * Raspberry Pi's GPIO header. The other channels are listed for posterity's
 * sake.
 * 
 * @author Rick Watson
 */
public enum ClockPin {

  PIN4(GPIOPin.PIN4, ClockChannel.CLOCK0),

  PIN5(GPIOPin.PIN5, ClockChannel.CLOCK1),

  PIN6(GPIOPin.PIN6, ClockChannel.CLOCK2);

  /*
   * ALT0 assigns a pin to output according to it's appropriate GP clock!
   */
  private static Function PIN_CLOCK_FUNCTION = Function.ALT0;

  public final ClockChannel channel;
  public final GPIOPin pin;

  private ClockPin(GPIOPin pin, ClockChannel channel) {
    this.channel = channel;
    this.pin = pin;
  }

  /**
   * Enables the channel. The channel's pin will be automatically configured to
   * output the clock.
   */
  public void enable() {
    // configure channel's pin for GPClock
    GPIO.setPinFunction(pin, PIN_CLOCK_FUNCTION);
    // enable the clock channel
    Clock.enable(channel);
  }

  /**
   * Disables the channel, leaves it's other settings unchanged. The pin will be
   * reconfigured for input, and will therefore go low.
   */
  public void disable() {
    // configure channel's pin to input
    GPIO.setPinFunction(pin, Function.INPUT);
    // disable the clock channel
    Clock.disable(channel);
  }

  /**
   * Disables the channel and resets it's settings. Waits for the channel to
   * become idle before returning.
   */
  public void resetChannel() {
    Clock.resetChannel(channel);
  }

  /**
   * Configures the channel's clock source as specified.
   * 
   * @param source
   *          The clock source to use.
   */
  public void configureSource(final ClockSource source) {
    Clock.configureSource(channel, source);
  }

  /**
   * Configures the channel's MASH setting as specified.
   * 
   * @param mash
   *          The mash setting to apply.
   */
  public void configureMash(final ClockMash mash) {
    Clock.configureMash(channel, mash);
  }

  /**
   * Configures the channel's divisor, handling the conversion from float to
   * fixed point value.
   * 
   * @param divisor
   *          The divisor to use.
   * 
   * @throws IllegalArgumentException
   *           If the divisor is out of the supported range of the 12 bit
   *           integer/12 bit fractional fixed point value.
   */
  public void configureDivisor(float divisor) {
    Clock.configureDivisor(channel, divisor);
  }

}
