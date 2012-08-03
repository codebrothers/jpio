package org.codebrothers.jpio.spi;

public enum SPIChipSelect {

  CS0(0b00), CS1(0b01), CS2(0b10), NONE(0b11);

  public static final int CHIP_SELECT_MASK = ~(0b11);

  public final int value;

  private SPIChipSelect(int value) {
    this.value = value;
  }

}
