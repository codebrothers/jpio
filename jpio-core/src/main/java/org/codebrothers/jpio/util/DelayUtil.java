package org.codebrothers.jpio.util;

/**
 * Simple delay utility. Recommended usage is through static import, see
 * examples.
 * 
 * @author Rick Watson
 */
public class DelayUtil {

  /**
   * Quick and dirty millisecond delay using Thread.sleep.
   * 
   * @param delay
   *          the (minimum) amount of time to sleep in milliseconds.
   */
  public static void delayMs(long delay) {
    try {
      Thread.sleep(delay);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * Spins a loop until the required number of nanoseconds have elapsed,
   * terribly wasteful, but useful if you need to sleep for a very small amount
   * of time!
   * 
   * @param delay
   *          the (minimum) amount of time to sleep in nanoseconds.
   */
  public static void delayNs(long delay) {
    final long delayUntil = System.nanoTime() + delay;
    while (System.nanoTime() < delayUntil) {
    }
  }

}
