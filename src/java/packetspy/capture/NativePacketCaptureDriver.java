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
package packetspy.capture;

import java.io.IOException;

/**
 * This class allows access to the native packet capture interface.
 * The native interface uses libpcap under *nix or winpcap under
 * windows.
 */
public class NativePacketCaptureDriver
{
    /**
     * The default length will ensure that the headers of most
     * protocols will be captured.
     */
    public static final int DEFAULT_SNAPSHOT_LENGTH = 96;

    /**
     * This length will ensure that the full packet is captured.
     */
    public static final int MAX_SNAPSHOT_LENGTH = 65536;

    /**
     * Capture timeout in milliseconds.
     */
    public static final int DEFAULT_TIMEOUT = 1000;

    static
    {
        //Make sure that the native library is loaded ...
        //Question - should this be versioned?
        System.loadLibrary( Version.LIBRARY );
    }

    /**
     * The handle for the underlying driver.
     */
    private final int m_handle;

    /**
     * Create NativePacketCaptureDriver and allocate native resources. 
     */
    public NativePacketCaptureDriver()
    {
        m_handle = NativePacketCaptureDriver.createDriver0();
    }

    /**
     * Make sure that the native driver is deallocated.
     */
    protected void finalize()
    {
        destroyDriver0( m_handle );
    }

    /**
     * Open a live network capture device and associate
     * it with the driver.
     *
     * @param device the name of the driver
     * @param snapLength the number of bytes to grab from each packet
     * @param promiscuous a flag indicating whether the sriver should
     *        be put into promiscuous mode.
     * @param timeout the timeout used during capture phase
     * @param filter the filter string used to select packets. May be null.
     */
    public void open( final String device,
                      final int snapLength,
                      final boolean promiscuous,
                      final int timeout,
                      final String filter )
        throws IOException
    {
        openLiveDevice0( m_handle, device, snapLength, promiscuous, timeout, filter );
    }

    /**
     * Capture packets associated with device.
     *
     * @param count the number of packets to wait for (unless timeout occurs).
     * @param handler the destination of packets
     * @throws IOException if unable to open capture device for any reason
     */
    public void capture( final int count, final RawPacketHandler handler )
        throws IOException
    {
        capture0( m_handle, count, handler, true );
    }

    /**
     * Return the number of packets dropped by device.
     *
     * @throws IOException if unable to determine packet count
     */
    public int getDroppedCount()
        throws IOException
    {
        return getDropCount0( m_handle );
    }

    /**
     * Return the number of packets received by device.
     *
     * @throws IOException if unable to determine packet count
     */
    public int getReceivedCount()
        throws IOException
    {
        return getReceiveCount0( m_handle );
    }

    /**
     * Close the device associated with driver if any.
     */
    public void close()
    {
        close0( m_handle );
    }

    /**
     * Return the names of all available native packet capture devices.
     *
     * @return the names of all available native packet capture devices.
     */
    public static String[] lookupDevices()
        throws IOException
    {
        return lookupAllDevices0();
    }

    /**
     * Allocate resources for a native driver
     * and return a handle.
     *
     * @return the drivers handle
     */
    private static native int createDriver0();

    /**
     * Deallocate resources for a native driver.
     *
     * @param handle the drivers handle
     */
    private static native void destroyDriver0( final int handle );

    /**
     * Return the names of all available native packet capture devices.
     *
     * @return the names of all available native packet capture devices.
     * @throws IOException if there is an error retrieving list
     */
    private static native String[] lookupAllDevices0()
        throws IOException;

    /**
     * Open a live network capture device and associate
     * it with the driver.
     *
     * @param handle the drivers handle
     * @param deviceName the name of the driver
     * @param snapLength the number of bytes to grab from each packet
     * @param promiscuous a flag indicating whether the sriver should
     *        be put into promiscuous mode.
     * @param timeout the timeout used during capture phase
     * @param filter the filter string used to select packets. May be null.
     * @return the link layer type.
     */
    private static native int openLiveDevice0( final int handle,
                                               final String deviceName,
                                               final int snapLength,
                                               final boolean promiscuous,
                                               final int timeout,
                                               final String filter )
        throws IOException;

    /**
     * Close the device associated with driver if any.
     *
     * @param handle the device
     */
    private static native void close0( final int handle );

    /**
     * Capture packets associated with device.
     *
     * @param handle the device
     * @param count the number of packets to wait for (unless timeout occurs).
     * @param handler the destination of packets
     * @param loop true to ignore timeout
     * @throws IOException if unable to open capture device for any reason
     */
    private static native void capture0( final int handle,
                                         final int count,
                                         final RawPacketHandler handler,
                                         final boolean loop )
        throws IOException;

    /**
     * Return the number of packets received by device.
     *
     * @param handle the driver handle
     * @return the number of packets received by device.
     * @throws IOException if unable to determine packet count
     */
    private static native int getReceiveCount0( final int handle )
        throws IOException;

    /**
     * Return the number of packets dropped by device.
     *
     * @param handle the driver handle
     * @return the number of packets dropped by device.
     * @throws IOException if unable to determine packet count
     */
    private static native int getDropCount0( final int handle )
        throws IOException;
}
