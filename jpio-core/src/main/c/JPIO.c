#include "JPIO.h"
#include <sys/mman.h>
#include <stdlib.h>
#include <stdio.h>
#include <errno.h>
#include <fcntl.h>
#include <jni.h>

JNIEXPORT jobject JNICALL Java_org_codebrothers_jpio_JPIO_mapGPIO
  (JNIEnv *env, jclass this){


	uint8_t *gpioMem;
	int fd ;

	// Open the master /dev/memory device
	if ((fd = open("/dev/mem", O_RDWR | O_SYNC) ) < 0)
	{
	    fprintf(stderr, "mapGPIO: Unable to open /dev/mem: %s\n", strerror(errno)) ;
	    return 0;
	}

	// GPIO:
	// Allocate 2 pages - 1 ...
	if ((gpioMem = malloc(BLOCK_SIZE + (PAGE_SIZE - 1))) == NULL)
	{
	    fprintf(stderr, "mapGPIO: malloc failed: %s\n", strerror(errno)) ;
	    return 0;
	}

	// ... presumably to make sure we can round it up to a whole page size
	if (((uint32_t)gpioMem % PAGE_SIZE) != 0)
	    gpioMem += PAGE_SIZE - ((uint32_t)gpioMem % PAGE_SIZE) ;

	uint32_t *gpio = (uint32_t *)mmap((caddr_t)gpioMem, BLOCK_SIZE, PROT_READ|PROT_WRITE, MAP_SHARED|MAP_FIXED, fd, GPIO_BASE) ;

	if ((int32_t)gpio < 0)
	{
	    fprintf(stderr, "mapGPIO: mmap failed: %s\n", strerror(errno)) ;
	    return 0;
	}

	// 4 bytes per register
    return (*env)->NewDirectByteBuffer(env, gpio, 45*4);

}