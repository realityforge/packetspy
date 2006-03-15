/*
 * Copyright (c) 2004 Peter Donald. All rights reserved
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   1. Redistributions of source code must retain the above copyright
 *      notice, this list of conditions and the following disclaimer.
 *   2. Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in
 *      the documentation and/or other materials provided with the
 *      distribution.
 *   3. The names of the authors may not be used to endorse or promote
 *      products derived from this software without specific prior
 *      written permission.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */
#include <stdlib.h>
#include <pcap.h>
#include <jni.h>

#ifndef TRUE
#define TRUE 1
#endif

struct Driver
{
	char	                 magic[6]; //'M','A','G','I','C', 0
	pcap_t *                 pDevice; 
	int                      linkType;
};

typedef struct Driver * DriverPtr;
typedef JNIEnv * JavaEnvPtr;

const char *HANDLER_METHOD_NAME = "handlePacket";
const char *HANDLER_METHOD_SIGNATURE = "(IIIII[B)V";

const char *ILLEGAL_STATE_EXCEPTION = "java/lang/IllegalStateException";
const char *IO_EXCEPTION = "java/io/IOException";

static void closeDevice( DriverPtr pDriver );

/**
* Throw an exception of specified type with specified message.
*/
static void throwException( JavaEnvPtr pEnv, const char *pType, const char *pMessage ) 
{
	const jclass exception = pEnv->FindClass( pType );
	if( NULL != exception )
	{
		pEnv->ThrowNew( exception, pMessage );
		pEnv->DeleteLocalRef( exception );
	}
}

static void throwBadDriverException( JavaEnvPtr pEnv ) 
{
	throwException( pEnv, ILLEGAL_STATE_EXCEPTION, "Invalid Driver ID" ); 
}

static void throwBadHandlerException( JavaEnvPtr pEnv ) 
{
	throwException( pEnv, ILLEGAL_STATE_EXCEPTION, "Unable to locate call back method for handler" ); 
}

static void throwIOException( JavaEnvPtr pEnv, const char *pMessage ) 
{
	throwException( pEnv, ILLEGAL_STATE_EXCEPTION, pMessage ); 
}

static void throwNullPointerException( JavaEnvPtr pEnv, const char *pMessage ) 
{
	throwException( pEnv, ILLEGAL_STATE_EXCEPTION, pMessage ); 
}

static DriverPtr createDriver()
{
	const DriverPtr pDriver = (DriverPtr)malloc( sizeof(struct Driver) );
	pDriver->magic[ 0 ] = 'M';
	pDriver->magic[ 1 ] = 'A';
	pDriver->magic[ 2 ] = 'G';
	pDriver->magic[ 3 ] = 'I';
	pDriver->magic[ 4 ] = 'C';
	pDriver->magic[ 5 ] = 0;
	pDriver->pDevice = NULL;

	return pDriver;
}

static bool isValidDriver( const DriverPtr pDriver )
{
	if( NULL == pDriver )
	{
		return false;
	}
	else
	{
		return 
			pDriver->magic[ 0 ] == 'M' &&
			pDriver->magic[ 1 ] == 'A' &&
			pDriver->magic[ 2 ] == 'G' &&
			pDriver->magic[ 3 ] == 'I' &&
			pDriver->magic[ 4 ] == 'C' &&
			pDriver->magic[ 5 ] == 0;
	}
}

static void destroyDriver( const DriverPtr pDriver )
{
	if( isValidDriver( pDriver ) )
	{
		closeDevice( pDriver );
		pDriver->magic[ 0 ] = 'D';
		pDriver->magic[ 1 ] = 'E';
		pDriver->magic[ 2 ] = 'A';
		pDriver->magic[ 3 ] = 'D';
		pDriver->magic[ 4 ] = '!';
		pDriver->magic[ 5 ] = 0;
		free( pDriver );
	}
}

static bool isValidDevice( const DriverPtr pDriver )
{
	if( !isValidDriver( pDriver ) || NULL == pDriver->pDevice )
	{
		return false;
	}
	else
	{
		return true;
	}
}

static void closeDevice( DriverPtr pDriver )
{
	if( isValidDevice( pDriver ) )
	{
		pcap_close( pDriver->pDevice );
		pDriver->pDevice = NULL;
	}
}

static bool setupFilter( JavaEnvPtr pEnv, DriverPtr pDriver, const char *filter )
{
	struct bpf_program bpp;

	if( NULL == filter )
	{
		return true;
	}

	if( -1 == pcap_compile( pDriver->pDevice, &bpp, (char *)filter, 1, 0 ) ) 
	{
		throwIOException( pEnv, pcap_geterr( pDriver->pDevice ) );
		closeDevice( pDriver );
		return false;
	}

	const int result = pcap_setfilter( pDriver->pDevice, &bpp );
	pcap_freecode( &bpp );
	if( -1 == result )
	{
		throwIOException( pEnv, pcap_geterr( pDriver->pDevice ) );
		closeDevice( pDriver );
		return false;
	}

	return true;
}

static bool openLiveDevice( JavaEnvPtr pEnv, 
						   DriverPtr pDriver, 
						   const char *pDeviceName,
						   int snaplen, 
						   bool promiscuous, 
						   int timeout, 
						   const char *filter )
{
	char error[ PCAP_ERRBUF_SIZE ];

	if( !isValidDriver( pDriver ) ) 
	{ 
		throwBadDriverException( pEnv );
		return false; 
	}

	if( isValidDevice( pDriver ) )
	{
		closeDevice( pDriver );
	}

	pDriver->pDevice =
		pcap_open_live( pDeviceName, snaplen, promiscuous, timeout, error );

	if( NULL == pDriver->pDevice ) 
	{
		throwIOException( pEnv, error );
		return false;
	}

	if( -1 == pcap_setnonblock( pDriver->pDevice, TRUE, error ) )
	{
		throwIOException( pEnv, error );
		closeDevice( pDriver );
		return false;
	}

	pDriver->linkType = pcap_datalink( pDriver->pDevice );
	return setupFilter( pEnv, pDriver, filter );
}

static int getDropCount( JavaEnvPtr pEnv, DriverPtr pDriver )
{
	if( !isValidDriver( pDriver ) ) 
	{ 
		throwBadDriverException( pEnv );
		return -1; 
	}

	struct pcap_stat stats;
	if( -1 == pcap_stats( pDriver->pDevice, &stats ) ) 
	{
		throwIOException( pEnv, pcap_geterr( pDriver->pDevice ) );
		return -1;
	}
	else 
	{
		return stats.ps_drop;
	}
}

static int getReceiveCount( JavaEnvPtr pEnv, DriverPtr pDriver )
{
	if( !isValidDriver( pDriver ) ) 
	{ 
		throwBadDriverException( pEnv );
		return -1; 
	}

	struct pcap_stat stats;
	if( -1 == pcap_stats( pDriver->pDevice, &stats ) ) 
	{
		throwIOException( pEnv, pcap_geterr( pDriver->pDevice ) );
		return -1;
	}
	else 
	{
		return stats.ps_recv;
	}
}

static jobjectArray lookupAllDevices( JavaEnvPtr pEnv )
{
	char error[ PCAP_ERRBUF_SIZE ];
	pcap_if_t *    pDevices;

	if( -1 == pcap_findalldevs( &pDevices, error ) ) 
	{
		throwIOException( pEnv, error );
		return NULL;
	}
	else 
	{
		int count = 0;
		pcap_if_t *counter = pDevices;
		while( NULL != counter )
		{
			count++;
			counter = counter->next;
		}

		jclass stringClass = pEnv->FindClass("java/lang/String");
		jobjectArray result = 
			pEnv->NewObjectArray( (jsize)count, stringClass, NULL );

		counter = pDevices;
		for( int i = 0 ; i < count; i++ ) 
		{
			jstring name = pEnv->NewStringUTF( counter->name );
			pEnv->SetObjectArrayElement( result, i, name );
			counter = counter->next;
		}

		pcap_freealldevs( pDevices );
		return result;
	}
}

struct HandlerData
{
	JavaEnvPtr    pEnv;
	DriverPtr     pDriver;
	jobject       pHandler;
	jmethodID     pMethod;  
};

static void processPacket( u_char *pUserData, 
						  const struct pcap_pkthdr *pHeader, 
						  const u_char *pPayLoad ) 
{
	const HandlerData *pData = (HandlerData *) pUserData;

	if( pData->pEnv->ExceptionOccurred() )
	{
		return;
	}

	jbyteArray javaPayLoad = pData->pEnv->NewByteArray( pHeader->caplen );

	pData->pEnv->SetByteArrayRegion( javaPayLoad, 0, pHeader->caplen, (jbyte *)pPayLoad );

	//Invoke metho on handler
	pData->pEnv->CallVoidMethod( pData->pHandler, pData->pMethod,
		pData->pDriver->linkType,
		pHeader->len, 
		pHeader->caplen, 
		pHeader->ts.tv_sec, 
		pHeader->ts.tv_usec,
		javaPayLoad );

	pData->pEnv->DeleteLocalRef( javaPayLoad );
}

static void capturePackets( JavaEnvPtr pEnv, DriverPtr pDriver, int count, jobject handler, bool loop )
{
	HandlerData data;
	
	if( !isValidDevice( pDriver ) )
	{
		throwIOException( pEnv, "No device created" );
		return;
	}

	jclass handlerClass = pEnv->GetObjectClass( handler );

	data.pDriver = pDriver;
	data.pEnv = pEnv;
	data.pHandler = handler;
	data.pMethod = 
		pEnv->GetMethodID( handlerClass, HANDLER_METHOD_NAME, HANDLER_METHOD_SIGNATURE );
	if( NULL == data.pMethod )
	{
		throwBadHandlerException( pEnv );
		return;
	}

	if( loop )
	{
		if( -1 == pcap_loop( pDriver->pDevice, count, processPacket, (u_char *)&data ) ) 
		{
			throwIOException( pEnv, pcap_geterr( pDriver->pDevice ) );  
		}
	}
	else
	{
		if( -1 == pcap_dispatch( pDriver->pDevice, count, processPacket, (u_char *)&data ) ) 
		{
			throwIOException( pEnv, pcap_geterr( pDriver->pDevice ) );  
		}
	}
}

#include "../../generated/include/packetspy_capture_NativePacketCaptureDriver.h"

static DriverPtr toDriver( jint handle )
{
	return (DriverPtr)((void *)handle);
}

JNIEXPORT jint JNICALL 
Java_packetspy_capture_NativePacketCaptureDriver_createDriver0
  (JNIEnv *, jclass)
{
	const DriverPtr pDriver = createDriver();
	return (jint)((void *)pDriver);
}

JNIEXPORT void JNICALL 
Java_packetspy_capture_NativePacketCaptureDriver_destroyDriver0
  (JNIEnv *, jclass, jint handle)
{
	DriverPtr pDriver = toDriver( handle );
	destroyDriver( pDriver );
}

JNIEXPORT jint JNICALL 
Java_packetspy_capture_NativePacketCaptureDriver_openLiveDevice0
  (JNIEnv *pEnv, jclass, jint handle, jstring deviceName, jint snapLength, jboolean promiscuous, jint timeout, jstring filter)
{
	DriverPtr pDriver = toDriver( handle );
	const char *pDeviceName = pEnv->GetStringUTFChars( deviceName, 0 );
	const char *pFilter = (NULL != filter)?pEnv->GetStringUTFChars( filter, 0 ):NULL;
  
	if( NULL == deviceName )
	{
		throwNullPointerException( pEnv, "deviceName" );
		return 0;
	}

	return openLiveDevice( pEnv, 
						   pDriver, 
						   pDeviceName,
						   snapLength, 
						   JNI_TRUE == promiscuous, 
						   timeout, 
						   pFilter );
}

JNIEXPORT void JNICALL 
Java_packetspy_capture_NativePacketCaptureDriver_close0
  (JNIEnv *, jclass, jint handle)
{
	DriverPtr pDriver = toDriver( handle );
  	closeDevice( pDriver );
}

JNIEXPORT jint JNICALL 
Java_packetspy_capture_NativePacketCaptureDriver_getReceiveCount0
  (JNIEnv *pEnv, jclass, jint handle)
{
	DriverPtr pDriver = toDriver( handle );
  	return getReceiveCount( pEnv, pDriver );
}

JNIEXPORT jint JNICALL Java_packetspy_capture_NativePacketCaptureDriver_getDropCount0
  (JNIEnv *pEnv, jclass, jint handle)
{
	DriverPtr pDriver = toDriver( handle );
  	return getDropCount( pEnv, pDriver );
}

JNIEXPORT void JNICALL 
Java_packetspy_capture_NativePacketCaptureDriver_capture0
  (JNIEnv *pEnv, jclass, jint handle, jint count, jobject handler, jboolean loop )
{
	DriverPtr pDriver = toDriver( handle );
  	capturePackets( pEnv, pDriver, count, handler, loop == JNI_TRUE );
}

JNIEXPORT jobjectArray JNICALL 
Java_packetspy_capture_NativePacketCaptureDriver_lookupAllDevices0
  (JNIEnv *pEnv, jclass)
{
	return lookupAllDevices( pEnv );
}
