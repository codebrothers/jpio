package org.codebrothers.jpio.spi;

/**
 * Constants used for clearing the RX or TX FIFOs.
 * 
 * @author Rick Watson
 */
public enum SPIClear {

  /**
   * Clear the RX FIFO.
   */
  RX(0b01),

  /**
   * Clear the TX FIFO.
   */
  TX(0b10),

  /**
   * Clear both the RX and TX FIFOs.
   */
  ALL(0b11);

  /*
   * Register value for clearing the FIFOs.
   */
  public final int value;

  private SPIClear(int value) {
    this.value = value << 4;
  }

}
