package org.codebrothers.jpio;

import static org.codebrothers.jpio.StaticTest.moo;

public class StaticTest2 {
  public static void main(String[] args) {
    System.out.println(moo);
    StaticTest.init();
    System.out.println(moo);
  }
}
