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
package packetspy.model;

/**
 * The raw packet data captured from device.
 */
public class RawPacket
    extends Packet
{
    /**
     * Ethernet.
     */
    public static final int TYPE_802_3 = 1;

    /**
     * The link type.
     * Supported types specified by TYPE_*.
     */
    private final int m_linkType;

    /**
     * The length of the packet in bytes.
     */
    private final int m_length;

    /**
     * The number of bytes captured.
     */
    private final int m_capturedLength;

    /**
     * The time (seconds field) when was packet captured.
     */
    private final int m_seconds;

    /**
     * The time (microseconds field) when was packet captured.
     */
    private final int m_useconds;

    /**
     * The data captured.
     */
    private final byte[] m_data;

    /**
     * Create raw packet.
     *
     * @param linkType the link type
     * @param length the length of packet
     * @param capturedLength the number of bytes captured
     * @param seconds the time in seconds of capture
     * @param useconds the time in micro seconds of capture
     * @param data the packet data
     */
    public RawPacket( final int linkType,
                      final int length,
                      final int capturedLength,
                      final int seconds,
                      final int useconds,
                      final byte[] data )
    {
        m_linkType = linkType;
        m_length = length;
        m_capturedLength = capturedLength;
        m_seconds = seconds;
        m_useconds = useconds;
        m_data = data;
    }

    /**
     * Return the link type.
     *
     * @return the link type.
     */
    public int getLinkType()
    {
        return m_linkType;
    }

    /**
     * Return the packet size in bytes.
     *
     * @return the packet size in bytes.
     */
    public int getLength()
    {
        return m_length;
    }

    /**
     * Return the number of bytes captured.
     *
     * @return the number of bytes captured.
     */
    public int getCapturedLength()
    {
        return m_capturedLength;
    }

    /**
     * Return the time (seconds field) when was packet captured.
     *
     * @return the time (seconds field) when was packet captured.
     */
    public int getSeconds()
    {
        return m_seconds;
    }

    /**
     * Return the time (microseconds field) when was packet captured.
     *
     * @return the time (microseconds field) when was packet captured.
     */
    public int getUseconds()
    {
        return m_useconds;
    }

    /**
     * Return the packet data.
     *
     * @return the packet data.
     */
    public byte[] getData()
    {
        return m_data;
    }

    /**
     * @see Object#toString()
     */
    public String toString()
    {
        return
            "Raw[ LinkType=" + m_linkType +" L=" + getLength() + "]";
    }
}
