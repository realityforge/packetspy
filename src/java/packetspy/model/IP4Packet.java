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
 * Class representing an IP Packet.
 */
public class IP4Packet
    extends Packet
{
    /**
     * Type Of Service Flag - Set to minimize delay.
     */
    public static final int TOS_MINIMIZE_DELAY = 0x10;

    /**
     * Type Of Service Flag - Set to maximize throughput.
     */
    public static final int TOS_MAXIMIZE_THROUGHPUT = 0x08;

    /**
     * Type Of Service Flag - Set to maximize reliability.
     */
    public static final int TOS_MAXIMIZE_RELIABILITY = 0x04;

    /**
     * Type Of Service Flag - Set to monetary cost.
     */
    public static final int TOS_MINIMIZE_MONETARY_COST = 0x02;

    /**
     * Internet Control Message Protocol.
     */
    public static final int PROTOCOL_ICMP = 1;

    /**
     * Internet Group Management Protocol.
     */
    public static final int PROTOCOL_IGMP = 2;

    /**
     * Transmission Control Protocol.
     */
    public static final int PROTOCOL_TCP = 6;

    /**
     * User Datagram Protocol.
     */
    public static final int PROTOCOL_UDP = 17;


    /**
     * The type of service. Must be one of the TOS_* constants.
     */
    private final byte m_typeOfService;

    /**
     * The ip length.
     */
    private final int m_length;

    /**
     * The packet id.
     */
    private final int m_id;

    /**
     * The more Flag.
     */
    private final boolean m_moreFlag;

    /**
     * The dontFragment Flag.
     */
    private final boolean m_dontFragmentFlag;

    /**
     * The packet fragment offset.
     */
    private final int m_fragmentOffset;

    /**
     * The packet fragment id.
     */
    private final short m_timeToLive;

    /**
     * The packet protocol.
     * Supported protocols are PROTOCOL_*.
     */
    private final short m_protocol;

    /**
     * The packet checksum.
     */
    private final int m_checksum;

    /**
     * The source IP Address. Must be 4 bytes.
     */
    private final byte[] m_source;

    /**
     * The source IP Address. Must be 4 bytes.
     */
    private final byte[] m_destination;

    /**
     * Create packet.
     *
     * @param typeOfService the type of service
     * @param length the length of the packet
     * @param id the packet identification
     * @param moreFlag the more Flag
     * @param dontFragmentFlag the dontFragment Flag
     * @param fragmentOffset the fragment offset
     * @param timeToLive the time to live
     * @param protocol the higher level protocol
     * @param checksum the checksum
     * @param source the source address
     * @param destination the destination address
     */
    public IP4Packet( final byte typeOfService,
                      final int length,
                      final int id,
                      final boolean moreFlag,
                      final boolean dontFragmentFlag,
                      final int fragmentOffset,
                      final short timeToLive,
                      final short protocol,
                      int checksum,
                      byte[] source,
                      byte[] destination )
    {
        if( null == source )
        {
            throw new NullPointerException( "source" );
        }
        if( null == destination )
        {
            throw new NullPointerException( "destination" );
        }
        if( 4 != source.length )
        {
            throw new IllegalArgumentException( "4 != source.length" );
        }
        if( 4 != destination.length )
        {
            throw new IllegalArgumentException( "4 != destination.length" );
        }
        m_typeOfService = typeOfService;
        m_length = length;
        m_id = id;
        m_moreFlag = moreFlag;
        m_dontFragmentFlag = dontFragmentFlag;
        m_fragmentOffset = fragmentOffset;
        m_timeToLive = timeToLive;
        m_protocol = protocol;
        m_checksum = checksum;
        m_source = source;
        m_destination = destination;
    }

    /**
     * Return the type of service.
     *
     * @return the type of service.
     */
    public byte getTypeOfService()
    {
        return m_typeOfService;
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
     * Return the packet id.
     *
     * @return the packet id.
     */
    public int getId()
    {
        return m_id;
    }

    /**
     * Return true if dont fragment flag is set.
     *
     * @return true if dont fragment flag is set.
     */
    public boolean isDontFragmentFlagSet()
    {
        return m_dontFragmentFlag;
    }

    /**
     * Return true if dont more flag is set.
     *
     * @return true if dont more flag is set.
     */
    public boolean isMoreFlagSet()
    {
        return m_moreFlag;
    }

    /**
     * Return the fragment offset.
     *
     * @return the fragment offset.
     */
    public int getFragmentOffset()
    {
        return m_fragmentOffset;
    }

    /**
     * Return the time to live.
     *
     * @return the time to live.
     */
    public short getTimeToLive()
    {
        return m_timeToLive;
    }

    /**
     * Return the higher level protocol.
     *
     * @return the higher level protocol.
     */
    public short getProtocol()
    {
        return m_protocol;
    }

    /**
     * Return the ip checksum.
     *
     * @return the ip checksum.
     */
    public int getChecksum()
    {
        return m_checksum;
    }

    /**
     * Return the destination IP Address.
     *
     * @return the destination IP Address.
     */
    public byte[] getDestination()
    {
        return m_destination;
    }

    /**
     * Return the source IP Address.
     *
     * @return the source IP Address.
     */
    public byte[] getSource()
    {
        return m_source;
    }

    /**
     * @see Object#toString()
     */
    public String toString()
    {
        return
            "IP[ " +
            RenderUtil.ipToString( getSource() ) +
            " ===> " +
            RenderUtil.ipToString( getDestination() ) +
            " P=" + getProtocol() +
            " TOS=" + getTypeOfService() +
            " TTL=" + getTimeToLive() +
            " CHK=" + getChecksum() +
            " FRG_OFF=" + getFragmentOffset() +
            " ID=" + getId() +
            " DF=" + isDontFragmentFlagSet() +
            " MORE=" + isMoreFlagSet() +
            " L=" + getLength() + "]";
    }
}
