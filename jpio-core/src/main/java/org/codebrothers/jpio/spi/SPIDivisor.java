package org.codebrothers.jpio.spi;

public enum SPIDivisor {

  DIVIDER_65536(0),

  DIVIDER_32768(32768),

  DIVIDER_16384(16384),

  DIVIDER_8192(8192),

  DIVIDER_4096(4096),

  DIVIDER_2048(2048),

  DIVIDER_1024(1024),

  DIVIDER_512(512),

  DIVIDER_256(256),

  DIVIDER_128(128),

  DIVIDER_64(64),

  DIVIDER_32(32),

  DIVIDER_16(16),

  DIVIDER_8(8),

  DIVIDER_4(4),

  DIVIDER_2(2),

  DIVIDER_1(1);

  public final int value;

  private SPIDivisor(int divider) {
    this.value = divider;
  }

}
