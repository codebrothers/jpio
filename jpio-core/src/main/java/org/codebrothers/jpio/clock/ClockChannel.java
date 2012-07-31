package org.codebrothers.jpio.clock;

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
public enum ClockChannel {

  /**
   * The only channel directly exposed on the Raspberry Pi's GPIO header. This
   * is exposed on GPIO4 (pin #7 on the header).
   */
  CLOCK0(28, 29, GPIOPin.PIN4),

  /**
   * Channel on GPIO5. This channel is not exposed on the GPIO header.
   */
  CLOCK1(30, 31, GPIOPin.PIN5),

  /**
   * Channel on GPIO6. This channel is not exposed on the GPIO header.
   */
  CLOCK2(32, 33, GPIOPin.PIN6);

  /*
   * The control register. For controlling source, mash and enable states. Also
   * used to assert the channel has gone idle after it has been disabled.
   */
  public final int controlRegister;

  /*
   * The divider register containing two 12 bit components, one integer, one
   * fractional.
   * 
   * Incoming source is divided by this to determining the final frequency.
   */
  public final int dividerRegister;

  /*
   * The GPIO pin for the channel, required in order to set it's function when
   * the channel is enabled.
   */
  public final GPIOPin gpioPin;

  private ClockChannel(final int controlRegister, final int dividerRegister, final GPIOPin gpioPin) {
    this.controlRegister = controlRegister;
    this.dividerRegister = dividerRegister;
    this.gpioPin = gpioPin;
  }

  /**
   * Enables the channel. The channel's pin will be automatically configured to
   * output the clock.
   */
  public void enable() {
    Clock.enable(this);
  }

  /**
   * Disables the channel, leaves it's other settings unchanged. The pin will be
   * reconfigured for input, and will therefore go low.
   * 
   */
  public void disable() {
    Clock.disable(this);
  }

  /**
   * Disables the channel and resets it's settings. Waits for the channel to
   * become idle before returning.
   */
  public void resetChannel() {
    Clock.resetChannel(this);
  }

  /**
   * Configures the channel's clock source as specified.
   * 
   * @param source
   *          The clock source to use.
   */
  public void configureSource(final ClockSource source) {
    Clock.configureSource(this, source);
  }

  /**
   * Configures the channel's MASH setting as specified.
   * 
   * @param mash
   *          The mash setting to apply.
   */
  public void configureMash(final ClockMash mash) {
    Clock.configureMash(this, mash);
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
    Clock.configureDivisor(this, divisor);
  }
}
