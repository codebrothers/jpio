package org.codebrothers.jpio;

import org.codebrothers.jpio.gpio.Function;
import org.codebrothers.jpio.gpio.GPIO;
import org.codebrothers.jpio.gpio.GPIOPin;

public class IOTest {

  private static final GPIOPin PIN = GPIOPin.PIN17;

  public static void main(String[] args) {
    JPIO.init();
    GPIO.setPinFunction(PIN, Function.OUTPUT);
    final long startedAt = System.currentTimeMillis();
    for (int i = 0; i < 10000000; i++) {
      GPIO.setPinValue(PIN, true);
      GPIO.setPinValue(PIN, false);
    }
    
    System.out.println("Took " + (System.currentTimeMillis() - startedAt));
  }

}
