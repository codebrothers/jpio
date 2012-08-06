package org.codebrothers.jpio.examples;

import org.codebrothers.jpio.JPIO;
import org.codebrothers.jpio.spi.SPI;
import org.codebrothers.jpio.spi.SPIChipSelect;
import org.codebrothers.jpio.spi.SPIDataMode;
import org.codebrothers.jpio.spi.SPIDivisor;

public class SPIRead {

  public static void main(String[] args) {
    if (args.length != 1) {
      throw new RuntimeException("One argument please!");
    }
    // Initialize the hardware
    JPIO.init();
    try {
      SPI.enter();
      SPI.setDataMode(SPIDataMode.MODE0);
      SPI.setDivisor(SPIDivisor.DIVIDER_65536);
      SPI.setChipSelect(SPIChipSelect.CS0);
      // System.out.println(SPI.transfer((byte) 128));

      final String toWrite = args[0];
      System.out.println("Sending: '" + toWrite + "'");
      byte[] bytes = toWrite.getBytes();
      byte[] echobytes = new byte[bytes.length];
      for (int i = 0; i < bytes.length; i++) {
        final byte echo = SPI.transfer(bytes[i]);
        if (i > 0)
          echobytes[i - 1] = echo;
      }
      echobytes[bytes.length - 1] = SPI.transfer((byte) 0x00);
      System.out.println("Echoed back: '" + new String(echobytes) + "'");
    } finally {
      SPI.exit();
    }
  }

}
