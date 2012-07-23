package org.codebrothers.jpio.examples;

import org.codebrothers.jpio.Function;
import org.codebrothers.jpio.JPIO;
import org.codebrothers.jpio.PiPin;

/**
 * The obligatory blink example.
 * 
 * @author Rick Watson
 */
public class Blink {

  private static final PiPin LED_PIN = PiPin.PIN11;

  public static void main(String[] args) {
    // Initialize the hardware
    JPIO.init();
    
    // Set pin as output
    LED_PIN.setFunction(Function.OUTPUT);
    
    // Bink forever!
    while (true) {
      LED_PIN.setValue(true);
      JPIO.delayMs(500);
      LED_PIN.setValue(false);
      JPIO.delayMs(500);
    }
  }

}
