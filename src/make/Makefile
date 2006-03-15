BASE_DIR=../..
SRC_DIR=${BASE_DIR}/src/c
TARGET_DIR=${BASE_DIR}/target/native
DEBUG_DIR=${TARGET_DIR}/Debug
RELEASE_DIR=${TARGET_DIR}/Release

JAVA_DIR=/opt/blackdown-jdk-1.4.1

COMMON_CFLAGS=-fPIC -I$(JAVA_DIR)/include -I$(JAVA_DIR)/include/linux
RELEASE_CFLAGS=$(COMMON_CFLAGS) -g -mcpu=pentiumpro -O6 -ffast-math -funroll-loops -fomit-frame-pointer -fexpensive-optimizations
DEBUG_CFLAGS=$(COMMON_CFLAGS) -g

ifneq ($(RELEASE),TRUE)
CFLAGS=$(DEBUG_CFLAGS)
INT_DIR=$(DEBUG_DIR)
else
CFLAGS=$(RELEASE_CFLAGS)
INT_DIR=$(RELEASE_DIR)
endif

CC=gcc
LDFLAGS=-lpcap -shared

DO_CC=$(CC) $(CFLAGS) -o $@ -c $<

$(BASE_DIR)/libpacketspy.so: $(INT_DIR)/NativePacketCaptureDriver.o
	$(CC) $(CFLAGS) -o $@ $(LDFLAGS) $(INT_DIR)/NativePacketCaptureDriver.o

$(INT_DIR)/NativePacketCaptureDriver.o: $(SRC_DIR)/NativePacketCaptureDriver.cpp
	@mkdir -p $(INT_DIR)
	$(CC) $(CFLAGS) -o $@ -c $(SRC_DIR)/NativePacketCaptureDriver.cpp
