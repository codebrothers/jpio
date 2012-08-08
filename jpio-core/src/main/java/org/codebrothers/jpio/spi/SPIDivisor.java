package org.codebrothers.jpio.spi;

/**
 * The divisor constants for hardware SPI, this divides the APB clock to define
 * the speed of the SPI.
 * <p>
 * Required to be a power of two, the maximum SPI clock rate is the rate of the
 * APB clock.
 * 
 * @author Rick Watson
 */
public enum SPIDivisor {

  /**
   * SPI clock will be APB/65536
   */
  DIVIDER_65536(0),

  /**
   * SPI clock will be APB/32768
   */
  DIVIDER_32768(32768),

  /**
   * SPI clock will be APB/16384
   */
  DIVIDER_16384(16384),

  /**
   * SPI clock will be APB/8192
   */
  DIVIDER_8192(8192),

  /**
   * SPI clock will be APB/4096
   */
  DIVIDER_4096(4096),

  /**
   * SPI clock will be APB/2048
   */
  DIVIDER_2048(2048),

  /**
   * SPI clock will be APB/1024
   */
  DIVIDER_1024(1024),

  /**
   * SPI clock will be APB/512
   */
  DIVIDER_512(512),

  /**
   * SPI clock will be APB/256
   */
  DIVIDER_256(256),

  /**
   * SPI clock will be APB/128
   */
  DIVIDER_128(128),

  /**
   * SPI clock will be APB/64
   */
  DIVIDER_64(64),

  /**
   * SPI clock will be APB/32
   */
  DIVIDER_32(32),

  /**
   * SPI clock will be APB/16
   */
  DIVIDER_16(16),

  /**
   * SPI clock will be APB/8
   */
  DIVIDER_8(8),

  /**
   * SPI clock will be APB/4
   */
  DIVIDER_4(4),

  /**
   * SPI clock will be APB/2
   */
  DIVIDER_2(2),

  /**
   * SPI clock will be APB
   */
  DIVIDER_1(1);

  /*
   * The divider value, to be put in the divider register.
   */
  public final int value;

  private SPIDivisor(int divider) {
    this.value = divider;
  }

}
