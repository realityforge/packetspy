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
package packetspy.model.io;

import packetspy.model.EthernetPacket;
import packetspy.model.ICMPPacket;
import packetspy.model.IGMPPacket;
import packetspy.model.IP4Packet;
import packetspy.model.Packet;
import packetspy.model.RawPacket;
import packetspy.model.TCPPacket;
import packetspy.model.UDPPacket;

/**
 * Model factory for turning raw capture data into packets.
 */
public class ModelPacketFactory
{
    /**
     * Utility method to parse a raw packet.
     *
     * @return the packet
     */
    public RawPacket parseRawPacket( final int linkType,
                                     final int length,
                                     final int capturedLength,
                                     final int seconds,
                                     final int useconds,
                                     final byte[] data )
    {
        final RawPacket raw = new RawPacket( linkType,
                                             length,
                                             capturedLength,
                                             seconds,
                                             useconds,
                                             data );
        raw.setData( data );
        raw.setPayloadOffset( 0 );
        raw.setPayloadLength( length );

        if( RawPacket.TYPE_802_3 == linkType && capturedLength >= 14 )
        {
            final EthernetPacket ethernet = parseEthernetFrame( raw );
            ethernet.setLowerLayerPacket( raw );
        }
        return raw;
    }

    /**
     * Utility method to parse an EthernetPacket packet.
     *
     * @param raw the raw packet
     * @return the packet.
     */
    EthernetPacket parseEthernetFrame( final RawPacket raw )
    {
        final byte[] data = raw.getData();
        final byte[] destination = readBytes( data, 0, 6 );
        final byte[] source = readBytes( data, 6, 6 );
        final int type = readUnsignedShort( data, 12 );
        final EthernetPacket packet = new EthernetPacket( destination, source, type );
        packet.setPayloadOffset( 14 );
        packet.setPayloadLength( raw.getLength() - packet.getPayloadOffset() );
        packet.setData( data );

        if( EthernetPacket.TYPE_IP == type && raw.getLength() >= 14 + 20 )
        {
            final IP4Packet ipPacket = parseIPPacket( 14, raw );
            if( null != ipPacket )
            {
                ipPacket.setLowerLayerPacket( packet );
            }
        }
        return packet;
    }

    /**
     * Utility method to parse an IPv4 packet.
     *
     * @param offset the offset
     * @param raw the raw packet
     * @return the packet.
     */
    IP4Packet parseIPPacket( final int offset, final RawPacket raw )
    {
        final byte[] data = raw.getData();

        final int version = ( data[offset] >> 4 ) & 0xF;
        if( version != 4 )
        {
            return null;
        }
        final int headerLength = ( data[offset] & 0xF ) * 4;
        final byte typeOfService = data[offset + 1];
        final int length = readUnsignedShort( data, offset + 2 );
        final int id = readUnsignedShort( data, offset + 4 );
        final boolean doNotFragmentFlag = ( data[offset + 6] & 0x40 ) == 0x40;
        final boolean moreFlag = ( data[offset + 6] & 0x20 ) == 0x20;

        final int fragment = ( readUnsignedShort( data, offset + 6 ) & 0x1FFF );
        final short ttl = readUnsignedByte( data, offset + 8 );
        final short protocol = readUnsignedByte( data, offset + 9 );
        final int checksum = readUnsignedShort( data, offset + 10 );
        final byte[] source = readBytes( data, offset + 12, 4 );
        final byte[] destination = readBytes( data, offset + 16, 4 );
        final IP4Packet packet = new IP4Packet( typeOfService,
                                                length,
                                                id,
                                                moreFlag,
                                                doNotFragmentFlag,
                                                fragment,
                                                ttl,
                                                protocol,
                                                checksum,
                                                source,
                                                destination );
        packet.setPayloadOffset( headerLength );
        packet.setPayloadLength( raw.getLength() - offset - headerLength );
        packet.setData( data );

        final int payloadStart = headerLength + offset;
        if( IP4Packet.PROTOCOL_UDP == protocol && raw.getCapturedLength() >= payloadStart + 8 )
        {
            final UDPPacket udpPacket = parseUDPPacket( payloadStart, raw );
            udpPacket.setLowerLayerPacket( packet );
        }
        else if( IP4Packet.PROTOCOL_TCP == protocol &&
                 raw.getCapturedLength() >= payloadStart + 20 )
        {
            final TCPPacket tcpPacket = parseTCPPacket( payloadStart, raw );
            tcpPacket.setLowerLayerPacket( packet );
        }
        else if( IP4Packet.PROTOCOL_ICMP == protocol &&
                 raw.getCapturedLength() >= payloadStart + 8 )
        {
            final ICMPPacket icmpPacket = parseICMPPacket( payloadStart, raw );
            icmpPacket.setLowerLayerPacket( packet );
        }
        else if( IP4Packet.PROTOCOL_IGMP == protocol &&
                 raw.getCapturedLength() >= payloadStart + 8 )
        {
            final Packet igmpPacket = parseIGMPPacket( payloadStart, raw );
            if( null != igmpPacket )
            {
                igmpPacket.setLowerLayerPacket( packet );
            }
        }

        return packet;
    }

    /**
     * Utility method to parse a IGMP packet.
     *
     * @param offset the offset
     * @param raw the raw packet
     * @return the packet.
     */
    Packet parseIGMPPacket( final int offset, final RawPacket raw )
    {
        final byte[] data = raw.getData();

        final byte type = (byte) data[offset + 0];
        if( IGMPPacket.V3_REPORT != type )
        {
            final short maxResponseTime = readUnsignedByte( data, offset + 1 );
            final int checksum = readUnsignedShort( data, offset + 2 );
            final byte[] address = readBytes( data, offset + 4, 4 );

            final IGMPPacket packet = new IGMPPacket( type, maxResponseTime, checksum, address );
            packet.setPayloadOffset( 8 );
            packet.setPayloadLength( raw.getLength() - offset - 8 );
            packet.setData( data );

            return packet;
        }
        else
        {
            //V3 Packets have a different format
            return null;
        }
    }

    /**
     * Utility method to parse a ICMP packet.
     *
     * @param offset the offset
     * @param raw the raw packet
     * @return the packet.
     */
    ICMPPacket parseICMPPacket( final int offset, final RawPacket raw )
    {
        final byte[] data = raw.getData();
        final byte type = data[offset + 0];
        final byte code = data[offset + 1];
        final int checksum = readUnsignedShort( data, offset + 2 );
        final long param = readUnsignedInteger( data, offset + 4 );

        final ICMPPacket packet = new ICMPPacket( type, code, checksum, param );
        packet.setPayloadOffset( 8 );
        packet.setPayloadLength( raw.getLength() - offset - 8 );
        packet.setData( data );
        return packet;
    }

    /**
     * Utility method to parse a TCP packet.
     *
     * @param offset the offset
     * @param raw the raw packet
     * @return the packet.
     */
    TCPPacket parseTCPPacket( final int offset, final RawPacket raw )
    {
        final byte[] data = raw.getData();
        final int sourcePort = readUnsignedShort( data, offset );
        final int destinationPort = readUnsignedShort( data, offset + 2 );
        final long seq = readUnsignedInteger( data, offset + 4 );
        final long ack = readUnsignedInteger( data, offset + 8 );
        final byte dataOffset = (byte) ( data[offset + 12] >> 4 );

        final boolean urgFlag = ( data[offset + 13] & 0x04 ) == 0x04;
        final boolean ackFlag = ( data[offset + 13] & 0x08 ) == 0x08;
        final boolean pshFlag = ( data[offset + 13] & 0x10 ) == 0x10;
        final boolean rstFlag = ( data[offset + 13] & 0x20 ) == 0x20;
        final boolean synFlag = ( data[offset + 13] & 0x40 ) == 0x40;
        final boolean finFlag = ( data[offset + 13] & 0x80 ) == 0x80;

        final int window = readUnsignedShort( data, offset + 14 );
        final int checksum = readUnsignedShort( data, offset + 16 );
        final int urgentPointer = readUnsignedShort( data, offset + 18 );

        //final int payloadStart = offset + (dataOffset * 4);
        TCPPacket packet = new TCPPacket( sourcePort,
                                          destinationPort,
                                          seq,
                                          ack,
                                          dataOffset,
                                          urgFlag,
                                          ackFlag,
                                          pshFlag,
                                          rstFlag,
                                          synFlag,
                                          finFlag,
                                          window,
                                          checksum,
                                          urgentPointer );
        packet.setPayloadOffset( dataOffset * 4 );
        packet.setPayloadLength( raw.getLength() - offset - (dataOffset * 4) );
        packet.setData( data );
        return packet;
    }

    /**
     * Utility method to parse a UDP packet.
     *
     * @param offset the offset
     * @param raw the raw packet
     * @return the packet.
     */
    UDPPacket parseUDPPacket( final int offset, final RawPacket raw )
    {
        final byte[] data = raw.getData();
        final int sourcePort = readUnsignedShort( data, offset );
        final int destinationPort = readUnsignedShort( data, offset + 2 );
        final int length = readUnsignedShort( data, offset + 4 );
        final int checksum = readUnsignedShort( data, offset + 6 );

        final UDPPacket packet = new UDPPacket( sourcePort, destinationPort, length, checksum );
        packet.setPayloadOffset( 8 );
        packet.setPayloadLength( raw.getLength() - offset - 8 );
        packet.setData( data );
        return packet;
    }

    /**
     * Read an unsigned integer.
     *
     * @param data the input data
     * @param offset the offset in input data
     * @return the unsigned integer
     */
    static long readUnsignedInteger( final byte[] data, final int offset )
    {
        return ( ( (long) ( data[offset + 0] & 0xff ) << 24 ) +
                 ( (long) ( data[offset + 1] & 0xff ) << 16 ) +
                 ( (long) ( data[offset + 2] & 0xff ) << 8 ) +
                 ( (long) ( data[offset + 3] & 0xff ) << 0 ) );
    }

    /**
     * Read an unsigned short.
     *
     * @param data the input data
     * @param offset the offset in input data
     * @return the unsigned short
     */
    static int readUnsignedShort( final byte[] data, final int offset )
    {
        final int v1 = data[offset + 0] & 0xFF;
        final int v2 = data[offset + 1] & 0xFF;
        return ( ( v1 << 8 ) + ( v2 << 0 ) );
    }

    /**
     * Read an unsigned byte.
     *
     * @param data the input data
     * @param offset the offset in input data
     * @return the unsigned byte
     */
    static short readUnsignedByte( final byte[] data, final int offset )
    {
        return (short) ( ( data[offset + 0] & 0xff ) << 0 );
    }

    /**
     * Read an array of data.
     *
     * @param data the source data
     * @param offset the offset in source data
     * @param count the number of data to read
     * @return the data read
     */
    static byte[] readBytes( final byte[] data, final int offset, final int count )
    {
        final byte[] result = new byte[count];
        System.arraycopy( data, offset, result, 0, result.length );
        return result;
    }
}
