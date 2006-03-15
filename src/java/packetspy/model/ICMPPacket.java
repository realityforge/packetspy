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
 * Packet representing ICMP packet.
 */
public class ICMPPacket
    extends Packet
{
    /**
     * Echo reply message.
     */
    public static final int TYPE_ECHO_REPLY = 0;

    /**
     * Host unreachable.
     */
    public static final int TYPE_DESTINATION_UNREACHABLE = 3;

    /**
     * Polite request to stop sending so much data.
     */
    public static final int TYPE_SOURCE_QUENCH = 4;

    /**
     * Indicates the host should use the specified gateway to
     * reach the IP address contained in the returned message.
     */
    public static final int TYPE_REDIRECT = 5;

    /**
     * Echo request message.
     */
    public static final int TYPE_ECHO_REQUEST = 8;

    /**
     * Message returned by the discovering router when the TTL
     * count reaches 0 in the IP header or timeout problem with
     * fragmentation.
     */
    public static final int TYPE_TIME_EXCEEDED = 11;

    public static final int TYPE_PARAMETER_PROBLEM = 12;
    public static final int TYPE_TIMESTAMP_REQUEST = 13;
    public static final int TYPE_TIMESTAMP_REPLY = 14;
    public static final int TYPE_ADDRESS_MASK_REQUEST = 17;
    public static final int TYPE_ADDRESS_MASK_REPLY = 18;

    /**
     * Time Exceeded code - TTL reached zero.
     */
    public static final int TE_TTL_ZERO = 0;

    /**
     * Time Exceeded code - fragment reassembly time exceeded.
     */
    public static final int TE_REASSEMBLE_TIMEOUT = 1;

    /**
     * Redirect code - redirect datagrams for net (obsolete).
     */
    public static final int RE_NET = 0;

    /**
     * Redirect code - redirect datagrams for host.
     */
    public static final int RE_HOST = 1;

    /**
     * Redirect code - redirect datagrams for ToS and net.
     */
    public static final int RE_TOS_NET = 2;

    /**
     * Redirect code - redirect datagrams for ToS and host.
     */
    public static final int RE_TOS_HOST = 3;

    /**
     * Unreachable code - Network unreachable.
     */
    public static final int UR_NET = 0;

    /**
     * Unreachable code - Host unreachable.
     */
    public static final int UR_HOST = 1;

    /**
     * Unreachable code - Protocol unreachable.
     */
    public static final int UR_PROTOCOL = 2;

    /**
     * Unreachable code - Port unreachable.
     */
    public static final int UR_PORT = 3;

    /**
     * Unreachable code - Frag needed but DF set.
     */
    public static final int UR_FRAG_REQ = 4;

    /**
     * Unreachable code - Source route failed.
     */
    public static final int UR_SOURCE_ROUTE = 4;

    /**
     * Unreachable code - Destination network unknown.
     */
    public static final int UR_UNKNOWN_NET = 6;

    /**
     * Unreachable code - Destination host unknown.
     */
    public static final int UR_UNKNOWN_HOST = 7;

    /**
     * Unreachable code - Source host isolated.
     */
    public static final int UR_SOURCE_ISOLATED = 8;

    /**
     * Unreachable code - Network access prohibited.
     */
    public static final int UR_PROHIBIT_NET = 9;

    /**
     * Unreachable code - Host access prohibited.
     */
    public static final int UR_PROHIBIT_HOST = 10;

    /**
     * Unreachable code - Network unreachable for ToS.
     */
    public static final int UR_TOS_NET = 11;

    /**
     * Unreachable code - Host unreachable for ToS.
     */
    public static final int UR_TOS_HOST = 12;

    /**
     * The type of ICMP Message.
     */
    private final int m_type;

    /**
     * The code associated with message.
     * (This is type specific).
     */
    private final int m_code;

    /**
     * The packet checksum.
     */
    private final int m_checksum;

    /**
     * Arbitary parameters associated with message.
     * This is type specific and only the lower
     * 32 bits are relevent.
     */
    private final long m_param;

    /**
     * Create ICMP packet.
     *
     * @param type the type
     * @param code the code
     * @param checksum the packet checksum
     * @param param param
     */
    public ICMPPacket( final int type,
                       final int code,
                       final int checksum,
                       final long param )
    {
        m_type = type;
        m_code = code;
        m_checksum = checksum;
        m_param = param;
    }

    /**
     * Return type of ICMP Message.
     *
     * @return type of ICMP Message.
     */
    public int getType()
    {
        return m_type;
    }

    /**
     * Return the code associated with message.
     *
     * @return the code associated with message.
     */
    public int getCode()
    {
        return m_code;
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
     * Return the param associated with message.
     *
     * @return the param associated with message.
     */
    public long getParam()
    {
        return m_param;
    }

    /**
     * @see Object#toString()
     */
    public String toString()
    {
        final IP4Packet ipPacket = (IP4Packet)getLowerLayerPacket();
        return
            "ICMP[ " +
            RenderUtil.ipToString( ipPacket.getSource() ) +
            " ===> " +
            RenderUtil.ipToString( ipPacket.getDestination() ) +
            " T/C=" + getType() + "/" + getCode() +
            " CHK=" + getChecksum() +
            " PARAM=" + getParam() +
            "]";
    }
}
