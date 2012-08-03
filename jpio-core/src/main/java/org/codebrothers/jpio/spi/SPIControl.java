package org.codebrothers.jpio.spi;

public enum SPIControl {

  CLKTRANS(0x4),

  CLK_IDLHI(0x8),

  CLRRXFIFO(0x10),

  CLRTXFIFO(0x20),

  CLRFIFOS(0x30),

  CS_POLARIT(0x40),

  ACTIVATE(0x80),

  DMA_ENABLE(0x100),

  DONE_IRQ(0x200),

  RX_IRQ(0x400),

  DEASRT_CS(0x800),

  MOSI_INPUT(0x1000),

  DONE(0x10000),

  RXFIFODATA(0x20000),

  TXFIFOSPCE(0x40000),

  RXFIFO3_4(0x80000),

  RXFIFOFULL(0x100000),

  CS0ACTHIGH(0x200000),

  CS1ACTHIGH(0x400000),

  CS2ACTHIGH(0x800000);

  public final int value;
  public final int mask;

  private SPIControl(int value) {
    this.value = value;
    this.mask = ~value;
  }

}
