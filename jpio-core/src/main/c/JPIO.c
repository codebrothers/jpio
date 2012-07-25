#include "JPIO.h"
#include <sys/mman.h>
#include <stdlib.h>
#include <stdint.h>
#include <stdio.h>
#include <errno.h>
#include <fcntl.h>
#include <jni.h>

#define PERI_BASE               0x20000000
#define GPIO_BASE               (PERI_BASE + 0x200000)
#define CLOCK_BASE				(PERI_BASE + 0x101000)
#define GPIO_PWM				(PERI_BASE + 0x20C000)

/// Size of memory page on RPi
#define PAGE_SIZE               (4*1024)

/// Size of memory block on RPi
#define BLOCK_SIZE              (4*1024)

static volatile uint32_t *gpio;
static volatile uint32_t *pwm;
static volatile uint32_t *clk;

JNIEXPORT jboolean JNICALL Java_org_codebrothers_jpio_JPIO_initialize(JNIEnv *env, jclass this) {

  int fd;
  uint8_t *gpioMem, *pwmMem, *clkMem;


  // Open the master /dev/memory device

  if ((fd = open("/dev/mem", O_RDWR | O_SYNC)) < 0) {
    fprintf(stderr, "jpio: Unable to open /dev/mem: %s\n", strerror(errno));
    return JNI_FALSE;
  }

  // GPIO

  if ((gpioMem = malloc(BLOCK_SIZE + (PAGE_SIZE - 1))) == NULL) {
    fprintf(stderr, "jpio: malloc failed: %s\n", strerror(errno));
    return JNI_FALSE;
  }


  if (((uint32_t) gpioMem % PAGE_SIZE) != 0) gpioMem += PAGE_SIZE - ((uint32_t) gpioMem % PAGE_SIZE);

  gpio = (uint32_t *) mmap((caddr_t) gpioMem, BLOCK_SIZE, PROT_READ | PROT_WRITE, MAP_SHARED | MAP_FIXED, fd, GPIO_BASE);

  if ((int32_t) gpio < 0) {
    fprintf(stderr, "jpio: mmap failed: %s\n", strerror(errno));
    return JNI_FALSE;
  }

  // PWM

  if ((pwmMem = malloc(BLOCK_SIZE + (PAGE_SIZE - 1))) == NULL) {
    fprintf(stderr, "jpio: pwmMem malloc failed: %s\n", strerror(errno));
    return JNI_FALSE;
  }

  if (((uint32_t) pwmMem % PAGE_SIZE) != 0) pwmMem += PAGE_SIZE - ((uint32_t) pwmMem % PAGE_SIZE);

  pwm = (uint32_t *) mmap(pwmMem, BLOCK_SIZE, PROT_READ | PROT_WRITE, MAP_SHARED | MAP_FIXED, fd, GPIO_PWM);

  if ((int32_t) pwm < 0) {
    fprintf(stderr, "jpio: mmap failed (pwm): %s\n", strerror(errno));
    return JNI_FALSE;
  }

  // Clock

  if ((clkMem = malloc(BLOCK_SIZE + (PAGE_SIZE - 1))) == NULL) {
    fprintf(stderr, "jpio: clkMem malloc failed: %s\n", strerror(errno));
    return JNI_FALSE;
  }

  if (((uint32_t) clkMem % PAGE_SIZE) != 0) clkMem += PAGE_SIZE - ((uint32_t) clkMem % PAGE_SIZE);

  clk = (uint32_t *) mmap(clkMem, BLOCK_SIZE, PROT_READ | PROT_WRITE, MAP_SHARED | MAP_FIXED, fd, CLOCK_BASE);

  if ((int32_t) clk < 0) {
    fprintf(stderr, "jpio: mmap failed (clk): %s\n", strerror(errno));
    return JNI_FALSE;
  }
  return JNI_TRUE;
}

JNIEXPORT jobject JNICALL Java_org_codebrothers_jpio_JPIO_getGPIO(JNIEnv *env, jclass this) {
  // 4 bytes per register, there are 41 GPIO registers
  return (*env)->NewDirectByteBuffer(env, (uint32_t *)gpio, 41*4);
}

// not sure exactly how many registers we'll need for these buffers...

JNIEXPORT jobject JNICALL Java_org_codebrothers_jpio_JPIO_getClock(JNIEnv *env, jclass this) {
  // 4 bytes per register, we need 45 registers
  return (*env)->NewDirectByteBuffer(env, (uint32_t *)clk, 45*4);
}

JNIEXPORT jobject JNICALL Java_org_codebrothers_jpio_JPIO_getPWM(JNIEnv *env, jclass this) {
  // 4 bytes per register, we need 45 registers
  return (*env)->NewDirectByteBuffer(env, (uint32_t *)pwm, 45*4);
}