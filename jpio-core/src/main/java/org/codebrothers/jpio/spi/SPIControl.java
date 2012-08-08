package org.codebrothers.jpio.spi;

public enum SPIControl {

  /**
   * Enable Long data word in Lossi mode if DMA_LEN is set
   * <p>
   * <ul>
   * <li>0 = writing to the FIFO will write a single byte
   * <li>1 = writing to the FIFO will write a 32 bit word
   * </ul>
   */
  ENABLE_LONG_DATA(0x2000000), // /< Enable Long data word in Lossi mode if

  /**
   * Enable DMA mode in Lossi mode
   */
  ENABLE_DMA(0x1000000),

  /**
   * Chip Select 2 Polarity
   * <p>
   * <ul>
   * <li>0 = Chip select is active low.
   * <li>1 = Chip select is active high.
   * </ul>
   */
  CS2_POLARITY(0x800000),

  /**
   * Chip Select 1 Polarity
   * <p>
   * <ul>
   * <li>0 = Chip select is active low.
   * <li>1 = Chip select is active high.
   * </ul>
   */
  CS1_POLARITY(0x400000),

  /**
   * Chip Select 0 Polarity
   * <p>
   * <ul>
   * <li>0 = Chip select is active low.
   * <li>1 = Chip select is active high.
   * </ul>
   */
  CS0_POLARITY(0x200000),

  /**
   * RXF - RX FIFO Full
   * <p>
   * <ul>
   * <li>0 = RX FIFO is not full.
   * <li>1 = RX FIFO is full. No further serial data will be sent/ received
   * until data is read from FIFO.
   * </ul>
   */
  RXF(0x100000),

  /**
   * RXR RX FIFO needs Reading
   * <p>
   * <ul>
   * <li>0 = RX FIFO is less than full (or not active TA = 0).
   * <li>1 = RX FIFO is or more full. Cleared by reading sufficient data from
   * the RX FIFO or setting TA to 0.
   * </ul>
   */
  RXR(0x80000),

  /**
   * TXD TX FIFO can accept Data
   * <p>
   * <ul>
   * <li>0 = TX FIFO is full and so cannot accept more data.
   * <li>1 = TX FIFO has space for at least 1 byte.
   * </ul>
   */
  TX_CAN_ACCEPT_DATA(0x40000),

  /**
   * RXD RX FIFO contains Data
   * <p>
   * <ul>
   * <li>0 = RX FIFO is empty.
   * <li>1 = RX FIFO contains at least 1 byte.
   * </ul>
   */
  RX_CONTAINS_DATA(0x20000),

  /**
   * Done transfer
   * <p>
   * <ul>
   * <li>0 = Transfer is in progress (or not active TA = 0).
   * <li>1 = Transfer is complete. Cleared by writing more data to the TX FIFO
   * or setting TA to 0.
   * </ul>
   */
  TRANSFER_DONE(0x10000),

  /**
   * LEN LoSSI enable
   * <p>
   * <ul>
   * <li>0 = The serial interface will behave as an SPI master.
   * <li>1 = The serial interface will behave as a LoSSI master.
   * </ul>
   */
  LEN_LOSSI_ENABLE(0x2000),

  /**
   * REN Read Enable
   * <p>
   * Read enable if you are using bidirectional mode. If this bit is set, the
   * SPI peripheral will be able to send data to this device. *
   * <p>
   * <ul>
   * <li>0 = We intend to write to the SPI peripheral.
   * <li>1 = We intend to read from the SPI peripheral.
   * </ul>
   */
  READ_ENABLE(0x1000),

  /**
   * ADCS Automatically Deassert Chip Select (DMA Mode)
   * <p>
   * <ul>
   * <li>0 = Don't automatically deassert chip select at the end of a DMA
   * transfer chip select is manually controlled by software.
   * <li>1 = Automatically deassert chip select at the end of a DMA transfer (as
   * determined by SPIDLEN)
   * </ul>
   */
  DEASSERT_CHIP_SELECT(0x800),

  /**
   * INTR Interrupt on RXR
   * <p>
   * <ul>
   * <li>0 = Don't generate interrupts on RX FIFO condition.
   * <li>1 = Generate interrupt when RX FIFO needs reading.
   * </ul>
   */
  INTERRUPT_ON_RXR(0x400),

  /**
   * INTD Interrupt on Done
   * <p>
   * <ul>
   * <li>0 = Don't generate interrupt on transfer complete.
   * <li>1 = Generate interrupt when DONE.
   * </ul>
   */
  INTERRUPT_ON_DONE(0x200),

  /**
   * DMAEN DMA Enable
   * <p>
   * <ul>
   * <li>0 = No DMA requests will be issued.
   * <li>1 = Enable DMA operation. Peripheral generates data requests. These
   * will be taken in four-byte words until the SPIDLEN has been reached.
   * </ul>
   */
  DMA_ENABLE(0x100),

  /**
   * Transfer Active
   * <p>
   * <ul>
   * <li>0 = Transfer not active./CS lines are all high (assuming CSPOL = 0).
   * RXR and DONE are 0. Writes to SPIFIFO write data into bits -0 of SPICS
   * allowing DMA data blocks to set mode before sending data.
   * <li>1 = Transfer active. /CS lines are set according to CS bits and CSPOL.
   * Writes to SPIFIFO write data to TX FIFO.TA is cleared by a dma_frame_end
   * pulse from the DMA controller.
   * </ul>
   */
  TRANSFER_ACTIVE(0x80),

  /**
   * Chip Select Polarity
   * <p>
   * <ul>
   * <li>0 = Chip select lines are active low
   * <li>1 = Chip select lines are active high
   * </ul>
   */
  CHIP_SELECT_POLARITY(0x40),

  /**
   * Clock Polarity
   * <p>
   * <ul>
   * <li>0 = Rest state of clock = low.
   * <li>1 = Rest state of clock = high.
   * </ul>
   */
  CLOCK_POLARITY(0x8),

  /**
   * Clock Phase
   * <p>
   * <ul>
   * <li>0 = First SCLK transition at middle of data bit.
   * <li>1 = First SCLK transition at beginning of data
   * </ul>
   */
  CLOCK_PHASE(0x4);

  /*
   * Inverted mask used to clear the SPIControl bit.
   */
  public final int mask;

  /*
   * Register value for setting the SPIControl bit.
   */
  public final int value;

  private SPIControl(int value) {
    this.value = value;
    this.mask = ~value;
  }

}
