package org.codebrothers.jpio;

import org.codebrothers.jpio.Function;
import org.codebrothers.jpio.GPIOPin;
import org.codebrothers.jpio.JPIO;

public class IOTest {

  private static final GPIOPin PIN = GPIOPin.PIN17;

  public static void main(String[] args) {
    JPIO.init();
    JPIO.setPinFunction(PIN, Function.OUTPUT);
    final long startedAt = System.currentTimeMillis();
    for (int i = 0; i < 10000000; i++) {
      JPIO.setPinValue(PIN, true);
      JPIO.setPinValue(PIN, false);
    }
    
    System.out.println("Took " + (System.currentTimeMillis() - startedAt));
  }

}
