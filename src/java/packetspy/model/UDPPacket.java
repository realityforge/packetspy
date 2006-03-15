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
 * Packet representing UDP packet.
 */
public class UDPPacket
    extends Packet
{
    /**
     * The source port.
     */
    private final int m_sourcePort;

    /**
     * The destination port.
     */
    private final int m_destinationPort;

    /**
     * The length of the packet.
     */
    private final int m_length;

    /**
     * The packet checksum.
     */
    private final int m_checksum;

    /**
     * Create UDP packet.
     *
     * @param sourcePort the source port
     * @param destinationPort the destination port
     * @param length the length of packet
     * @param checksum the packet checksum (0 to disable)
     */
    public UDPPacket( final int sourcePort,
                      final int destinationPort,
                      final int length,
                      final int checksum )
    {
        m_sourcePort = sourcePort;
        m_destinationPort = destinationPort;
        m_length = length;
        m_checksum = checksum;
    }

    /**
     * Return the source port.
     *
     * @return the source port.
     */
    public int getSourcePort()
    {
        return m_sourcePort;
    }

    /**
     * Return the destination port.
     *
     * @return the destination port.
     */
    public int getDestinationPort()
    {
        return m_destinationPort;
    }

    /**
     * Return the packet length.
     *
     * @return the packet length.
     */
    public int getLength()
    {
        return m_length;
    }

    /**
     * Return the packets checksum.
     *
     * @return the packets checksum.
     */
    public int getChecksum()
    {
        return m_checksum;
    }

    /**
     * @see Object#toString()
     */
    public String toString()
    {
        final IP4Packet ipPacket = (IP4Packet)getLowerLayerPacket();
        return
            "UDP[ " +
            RenderUtil.ipToString( ipPacket.getSource() ) + ":" + getSourcePort() +
            " ===> " +
            RenderUtil.ipToString( ipPacket.getDestination() ) + ":" + getDestinationPort() +
            " L=" + getLength() +
            " CHK=" + getChecksum() +
            "]";
    }
}
