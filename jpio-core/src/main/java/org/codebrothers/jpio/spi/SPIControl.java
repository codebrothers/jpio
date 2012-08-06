package org.codebrothers.jpio.spi;

public enum SPIControl {

  /**
   * Enable Long data word in Lossi mode if DMA_LEN is set
   * 
   * 0 = writing to the FIFO will write a single byte
   * 
   * 1 = writing to the FIFO will write a 32 bit word
   */
  ENABLE_LONG_DATA(0x02000000), // /< Enable Long data word in Lossi mode if

  /**
   * Enable DMA mode in Lossi mode
   */
  ENABLE_DMA(0x01000000),

  /**
   * Chip Select 2 Polarity
   * 
   * 0 = Chip select is active low.
   * 
   * 1 = Chip select is active high.
   */
  CS2_POLARITY(0x00800000),

  /**
   * Chip Select 1 Polarity
   * 
   * 0 = Chip select is active low.
   * 
   * 1 = Chip select is active high.
   */
  CS1_POLARITY(0x00400000),

  /**
   * Chip Select 0 Polarity
   * 
   * 0 = Chip select is active low.
   * 
   * 1 = Chip select is active high.
   */
  CS0_POLARITY(0x00200000),

  /**
   * RXF - RX FIFO Full
   * 
   * 0 = RXFIFO is not full.
   * 
   * 1 = RX FIFO is full. No further serial data will be sent/ received until
   * data is read from FIFO.
   */
  RXF(0x00100000),

  /**
   * RXR RX FIFO needs Reading
   * 
   * 0 = RX FIFO is less than full (or not active TA = 0).
   * 
   * 1 = RX FIFO is or more full. Cleared by reading sufficient data from the RX
   * FIFO or setting TA to 0.
   */
  RXR(0x00080000),

  /**
   * TXD TX FIFO can accept Data
   * 
   * 0 = TX FIFO is full and so cannot accept more data.
   * 
   * 1 = TX FIFO has space for at least 1 byte.
   */
  TX_CAN_ACCEPT_DATA(0x00040000),

  /**
   * RXD RX FIFO contains Data
   * 
   * 0 = RX FIFO is empty.
   * 
   * 1 = RX FIFO contains at least 1 byte.
   */
  RX_CONTAINS_DATA(0x00020000),

  /**
   * Done transfer
   * 
   * 0 = Transfer is in progress (or not active TA = 0).
   * 
   * 1 = Transfer is complete. Cleared by writing more data to the TX FIFO or
   * setting TA to 0.
   */
  TRANSFER_DONE(0x00010000),

  /**
   * LEN LoSSI enable
   * 
   * The serial interface is configured as a LoSSI master.
   * 
   * 0 = The serial interface will behave as an SPI master.
   * 
   * 1 = The serial interface will behave as a LoSSI master.
   */
  LEN_LOSSI_ENABLE(0x00002000),

  /**
   * REN Read Enable
   * 
   * Read enable if you are using bidirectional mode. If this bit is set, the
   * SPI peripheral will be able to send data to this device.
   * 
   * 0 = We intend to write to the SPI peripheral.
   * 
   * 1 = We intend to read from the SPI peripheral.
   */
  READ_ENABLE(0x00001000),

  /**
   * ADCS Automatically Deassert Chip Select (DMA Mode)
   * 
   * 0 = Don't automatically deassert chip select at the end of a DMA transfer
   * chip select is manually controlled by software.
   * 
   * 1 = Automatically deassert chip select at the end of a DMA transfer (as
   * determined by SPIDLEN)
   */
  DEASSERT_CHIP_SELECT(0x00000800),

  /**
   * INTR Interrupt on RXR
   * 
   * 0 = Don't generate interrupts on RX FIFO condition.
   * 
   * 1 = Generate interrupt when RX FIFO needs reading.
   */
  INTERRUPT_ON_RXR(0x00000400),

  /**
   * INTD Interrupt on Done
   * 
   * 0 = Don't generate interrupt on transfer complete.
   * 
   * 1 = Generate interrupt when DONE.
   */
  INTERRUPT_ON_DONE(0x00000200),

  /**
   * DMAEN DMA Enable
   * 
   * 0 = No DMA requests will be issued.
   * 
   * 1 = Enable DMA operation. Peripheral generates data requests. These will be
   * taken in four-byte words until the SPIDLEN has been reached.
   */
  DMA_ENABLE(0x00000100),

  /**
   * Transfer Active
   * 
   * 0 = Transfer not active./CS lines are all high (assuming CSPOL = 0). RXR
   * and DONE are 0. Writes to SPIFIFO write data into bits -0 of SPICS allowing
   * DMA data blocks to set mode before sending data.
   * 
   * 1 = Transfer active. /CS lines are set according to CS bits and CSPOL.
   * Writes to SPIFIFO write data to TX FIFO.TA is cleared by a dma_frame_end
   * pulse from the DMA controller.
   */
  TRANSFER_ACTIVE(0x00000080),

  /**
   * Chip Select Polarity
   * 
   * 0 = Chip select lines are active low
   * 
   * 1 = Chip select lines are active high
   */
  CHIP_SELECT_POLARITY(0x00000040),

  /**
   * Clock Polarity
   * 
   * 0 = Rest state of clock = low.
   * 
   * 1 = Rest state of clock = high.
   */
  CLOCK_POLARITY(0x00000008),

  /**
   * Clock Phase
   * 
   * 0 = First SCLK transition at middle of data bit.
   * 
   * 1 = First SCLK transition at beginning of data
   */
  CLOCK_PHASE(0x00000004);

  public final int value;
  public final int mask;

  private SPIControl(int value) {
    this.value = value;
    this.mask = ~value;
  }

}
