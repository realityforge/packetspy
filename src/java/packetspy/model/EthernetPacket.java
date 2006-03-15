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
 * Class representing an Ethernet frame.
 *
 * Semantic:  | Destination |   Source    | Protocol |
 * Type:      | MAC Address | MAC Address |     UINT |
 * Length:    |           6 |           6 |        2 |   
 */
public class EthernetPacket
    extends Packet
{
    /**
     * IP protocol.
     */
    public static final int TYPE_IP = 0x0800;
    /**
     * Address resolution protocol.
     */
    public static final int TYPE_ARP = 0x0806;

    /**
     * The destination MAC Address. Must be 6 bytes.
     */
    private final byte[] m_destination;

    /**
     * The source MAC Address. Must be 6 bytes.
     */
    private final byte[] m_source;

    /**
     * The type of the underlying packet.
     * The TYPE_* fields specify currently supported types.
     */
    private final int m_type;

    /**
     * Create an enternet packet.
     *
     * @param destination the destination MAC Address
     * @param source the source MAC Address
     * @param type the frame type / protocol
     */
    public EthernetPacket( final byte[] destination, final byte[] source, final int type )
    {
        if( null == destination )
        {
            throw new NullPointerException( "destination" );
        }
        if( null == source )
        {
            throw new NullPointerException( "source" );
        }
        if( 6 != destination.length )
        {
            throw new IllegalArgumentException( "6 != destination.length" );
        }
        if( 6 != source.length )
        {
            throw new IllegalArgumentException( "6 != source.length" );
        }
        m_destination = destination;
        m_source = source;
        m_type = type;
    }

    /**
     * Return the destination MAC Address.
     *
     * @return the destination MAC Address.
     */
    public byte[] getDestination()
    {
        return m_destination;
    }

    /**
     * Return the source MAC Address.
     *
     * @return the source MAC Address.
     */
    public byte[] getSource()
    {
        return m_source;
    }

    /**
     * Return the frame type.
     *
     * @return the frame type.
     */
    public int getType()
    {
        return m_type;
    }


    /**
     * @see Object#toString()
     */
    public String toString()
    {
        return
            "Ethernet[ " +
            RenderUtil.macToString( getSource() ) +
            " ===> " +
            RenderUtil.macToString( getDestination() ) +
            " T=" + getType() + "]";
    }
}
