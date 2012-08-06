package org.codebrothers.jpio.spi;

import static org.codebrothers.jpio.JPIO.SPI0;
import static org.codebrothers.jpio.util.BitUtils.clearMask;
import static org.codebrothers.jpio.util.BitUtils.isBitClear;
import static org.codebrothers.jpio.util.BitUtils.setBits;
import static org.codebrothers.jpio.util.BitUtils.setMaskedValue;

import org.codebrothers.jpio.gpio.Function;
import org.codebrothers.jpio.gpio.GPIOPin;

public class SPI {

  private static final int SPI_CONTROL_STATUS_REGISTER = 0;
  private static final int SPI_FIFO_REGISTER = 1;
  private static final int SPI_DIVISOR_REGISTER = 2;

  private static final Function SPI_PIN_FUNCTION = Function.ALT0;

  /*
   * SPI Pins
   * 
   * MISO, MOSI, CLOCK, CHIP SELECT 0, CHIP SELECT 1
   */
  private static final GPIOPin[] SPI_PINS = { GPIOPin.PIN9, GPIOPin.PIN10, GPIOPin.PIN11, GPIOPin.PIN7, GPIOPin.PIN8 };

  /**
   * Enters SPI mode. Configures pins to their correct functions and clears any
   * existing FIFO or config data.
   */
  public static void enter() {
    // Set up ALT function for the SPI pins
    for (GPIOPin spiPin : SPI_PINS) {
      spiPin.setFunction(SPI_PIN_FUNCTION);
    }
    // Clear all bits on status/control register
    SPI0.put(SPI_CONTROL_STATUS_REGISTER, 0);
    // Clear FIFOs
    setClear(SPIClear.ALL);
  }

  /**
   * Exits the SPI mode. Puts all associated pins into input mode.
   */
  public static void exit() {
    for (GPIOPin spiPin : SPI_PINS) {
      spiPin.setFunction(Function.INPUT);
    }
  }

  public static void clearControl(SPIControl control) {
    clearMask(SPI0, SPI_CONTROL_STATUS_REGISTER, control.mask);
  }

  public static void setControl(SPIControl control) {
    setBits(SPI0, SPI_CONTROL_STATUS_REGISTER, control.value);
  }

  public static void setChipSelect(SPIChipSelect chipSelect) {
    setMaskedValue(SPI0, SPI_CONTROL_STATUS_REGISTER, SPIChipSelect.CHIP_SELECT_MASK, chipSelect.value);
  }

  public static void setDataMode(SPIDataMode dataMode) {
    setMaskedValue(SPI0, SPI_CONTROL_STATUS_REGISTER, SPIDataMode.DATA_MODE_MASK, dataMode.value);
  }

  public static void setClear(SPIClear clear) {
    setMaskedValue(SPI0, SPI_CONTROL_STATUS_REGISTER, SPIClear.CLEAR_MASK, clear.value);
  }

  public static void setDivisor(SPIDivisor divisor) {
    SPI0.put(SPI_DIVISOR_REGISTER, divisor.value);
  }

  /**
   * Transfer method
   */

  public static byte transfer(byte value) {
    // Clear FIFOs
    setClear(SPIClear.ALL);

    // Set TA = 1
    setControl(SPIControl.TRANSFER_ACTIVE);

    // Maybe wait for TXD
    while (isBitClear(SPI0, SPI_CONTROL_STATUS_REGISTER, SPIControl.TX_CAN_ACCEPT_DATA.value)) {
    }

    SPI0.put(SPI_FIFO_REGISTER, value);

    // Wait for DONE to be set
    while (isBitClear(SPI0, SPI_CONTROL_STATUS_REGISTER, SPIControl.TRANSFER_DONE.value)) {
    }

    // Read any byte sent back
    int returnValue = SPI0.get(SPI_FIFO_REGISTER);

    // Set TA = 0, and also set the barrier
    clearControl(SPIControl.TRANSFER_ACTIVE);

    // truncate and return
    return (byte) returnValue;
  }
}
