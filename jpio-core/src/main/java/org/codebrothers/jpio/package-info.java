/**
 * Provides the interfacing to the Raspberry Pi's various peripherals. Like with the C libraries, this is achieved by reading/writing directly on the peripheral bus, avoiding the file mapping or library wrapping approaches. 
 * <p>
 * This is therefore not wasteful, resulting in lower overheads when using the Raspberry Pi's peripheral bus from Java. In order to gain access to the Raspberry Pi's peripheral address space, a simple JNI library is employed.
 * <p>
 * 
 * @author: Rick Watson
 */
package org.codebrothers.jpio;