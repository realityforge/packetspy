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
 * Packet representing IGMP packet.
 */
public class IGMPPacket
    extends Packet
{
    /**
     * Membership Query.
     */
    public static final int QUERY = 0x11;

    /**
     * Version 1 Membership Report.
     */
    public static final int V1_REPORT = 0x12;

    /**
     * Version 2 Membership Report.
     */
    public static final int V2_REPORT = 0x16;

    /**
     * Version 3 Membership Report.
     */
    public static final int V3_REPORT = 0x22;

    /**
     * Leave group.
     */
    public static final int LEAVE = 0x17;

    /**
     * The type of IGMP Message.
     */
    private final byte m_type;

    /**
     * The max response time.
     */
    private final short m_maxResponseTime;

    /**
     * The packet checksum.
     */
    private final int m_checksum;

    /**
     * The associated address. Must be 4 bytes.
     */
    private final byte[] m_address;

    /**
     * Create IGMP packet.
     *
     * @param type the type
     * @param maxResponseTime the max response time
     * @param checksum the packet checksum
     * @param address the associated address
     */
    public IGMPPacket( final byte type,
                       final short maxResponseTime,
                       final int checksum,
                       final byte[] address )
    {
        m_type = type;
        m_maxResponseTime = maxResponseTime;
        m_checksum = checksum;
        m_address = address;
    }

    /**
     * Return type of IGMP Message.
     *
     * @return type of IGMP Message.
     */
    public byte getType()
    {
        return m_type;
    }

    /**
     * Return the max response time.
     *
     * @return the max response time.
     */
    public short getMaxResponseTime()
    {
        return m_maxResponseTime;
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
     * Return the associated address.
     *
     * @return the associated address.
     */
    public byte[] getAddress()
    {
        return m_address;
    }

    /**
     * @see Object#toString()
     */
    public String toString()
    {
        final IP4Packet ipPacket = (IP4Packet)getLowerLayerPacket();
        return
            "IGMP[ " +
            RenderUtil.ipToString( ipPacket.getSource() ) +
            " ===> " +
            RenderUtil.ipToString( ipPacket.getDestination() ) +
            " T=" + getType() +
            " CHK=" + getChecksum() +
            " ADDR=" + RenderUtil.ipToString( getAddress() ) +
            "]";
    }
}
