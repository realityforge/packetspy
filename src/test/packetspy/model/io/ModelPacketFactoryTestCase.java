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

import junit.framework.TestCase;
import packetspy.model.EthernetPacket;
import packetspy.model.IP4Packet;
import packetspy.model.RawPacket;
import packetspy.model.RenderUtil;
import packetspy.model.UDPPacket;

public class ModelPacketFactoryTestCase
    extends TestCase
{
    public void testUDPCapture()
        throws Exception
    {
        final ModelPacketFactory factory = new ModelPacketFactory();
        final int length = UDP_PACKET.length;
        final RawPacket raw = factory.parseRawPacket( RawPacket.TYPE_802_3,
                                                      length,
                                                      length,
                                                      0,
                                                      0,
                                                      UDP_PACKET );

        assertNotNull( "raw", raw );
        // Raw[ LinkType=1 L=47]
        assertEquals( "raw.getLength()", UDP_PACKET.length, raw.getLength() );
        assertEquals( "raw.getCapturedLength()", UDP_PACKET.length, raw.getCapturedLength() );
        assertEquals( "raw.getLinkType()", RawPacket.TYPE_802_3, raw.getLinkType() );
        assertEquals( "raw.getData()", UDP_PACKET, raw.getData() );

        assertEquals( "raw.getPayloadStart()", 0, raw.getPayloadStart() );
        assertEquals( "raw.getPayloadOffset()", 0, raw.getPayloadOffset() );
        assertEquals( "raw.getPayloadLength()", UDP_PACKET.length, raw.getPayloadLength() );

        assertEquals( "raw.getLowerLayerPacket()", null, raw.getLowerLayerPacket() );
        assertNotNull( "raw.getUpperLayerPacket()", raw.getUpperLayerPacket() );
        assertTrue( "raw.getUpperLayerPacket() instanceof Ethernet2",
                    raw.getUpperLayerPacket() instanceof EthernetPacket );

        final EthernetPacket ethernet = (EthernetPacket) raw.getUpperLayerPacket();
        //Ethernet[ 00:0C:F1:E0:AD:5E ===> 00:50:BA:C1:68:E2 T=2048]
        assertEquals( "ethernet.getType()", EthernetPacket.TYPE_IP, ethernet.getType() );
        assertEquals( "ethernet.getSource()",
                      "00:0C:F1:E0:AD:5E",
                      RenderUtil.macToString( ethernet.getSource() ) );
        assertEquals( "ethernet.getDestination()",
                      "00:50:BA:C1:68:E2",
                      RenderUtil.macToString( ethernet.getDestination() ) );

        assertEquals( "ethernet.getPayloadStart()", 14, ethernet.getPayloadStart() );
        assertEquals( "ethernet.getPayloadOffset()", 14, ethernet.getPayloadOffset() );
        assertEquals( "ethernet.getPayloadLength()",
                      UDP_PACKET.length - 14,
                      ethernet.getPayloadLength() );

        assertNotNull( "ethernet.getLowerLayerPacket()", ethernet.getLowerLayerPacket() );
        assertTrue( "ethernet.getLowerLayerPacket() instanceof RAW",
                    ethernet.getLowerLayerPacket() instanceof RawPacket );
        assertNotNull( "ethernet.getUpperLayerPacket()", ethernet.getUpperLayerPacket() );
        assertTrue( "ethernet.getUpperLayerPacket() instanceof IP",
                    ethernet.getUpperLayerPacket() instanceof IP4Packet );

        final IP4Packet ip = (IP4Packet) ethernet.getUpperLayerPacket();
        //IP[ 192.168.0.240 ===> 192.168.0.1 P=17 TOS=0 TTL=128 CHK=19727
        // FRG_OFF=0 ID=27515 DF=false MORE=false L=33]

        assertEquals( "ip.getSource()", "192.168.0.240", RenderUtil.ipToString( ip.getSource() ) );
        assertEquals( "ip.getDestination()",
                      "192.168.0.1",
                      RenderUtil.ipToString( ip.getDestination() ) );
        assertEquals( "ip.getTypeOfService()", 0, ip.getTypeOfService() );
        assertEquals( "ip.getTimeToLive()", 128, ip.getTimeToLive() );
        assertEquals( "ip.getChecksum()", 19727, ip.getChecksum() );
        assertEquals( "ip.getFragmentOffset()", 0, ip.getFragmentOffset() );
        assertEquals( "ip.getId()", 27515, ip.getId() );
        assertEquals( "ip.isDontFragmentFlagSet()", false, ip.isDontFragmentFlagSet() );
        assertEquals( "ip.isMoreFlagSet()", false, ip.isMoreFlagSet() );
        assertEquals( "ip.getLength()", 33, ip.getLength() );

        assertEquals( "ip.getPayloadStart()", 14 + 20, ip.getPayloadStart() );
        assertEquals( "ip.getPayloadOffset()", 20, ip.getPayloadOffset() );
        assertEquals( "ip.getPayloadLength()",
                      UDP_PACKET.length - 14 - 20,
                      ip.getPayloadLength() );

        assertNotNull( "ip.getLowerLayerPacket()", ip.getLowerLayerPacket() );
        assertTrue( "ip.getLowerLayerPacket() instanceof Ethernet2",
                    ip.getLowerLayerPacket() instanceof EthernetPacket );
        assertNotNull( "ip.getUpperLayerPacket()", ip.getUpperLayerPacket() );
        assertTrue( "ip.getUpperLayerPacket() instanceof UDP",
                    ip.getUpperLayerPacket() instanceof UDPPacket );

        final UDPPacket udp = (UDPPacket) ip.getUpperLayerPacket();
        //UDP[ 192.168.0.240:1655 ===> 192.168.0.1:5317 L=13 CHK=16004]
        assertEquals( "udp.getSourcePort()", 1655, udp.getSourcePort() );
        assertEquals( "udp.getDestinationPort()", 5317, udp.getDestinationPort() );
        assertEquals( "udp.getChecksum()", 16004, udp.getChecksum() );
        assertEquals( "udp.getLength()", 13, udp.getLength() );

        assertEquals( "udp.getPayloadStart()", 14 + 20 + 8, udp.getPayloadStart() );
        assertEquals( "udp.getPayloadOffset()", 8, udp.getPayloadOffset() );
        assertEquals( "udp.getPayloadLength()",
                      UDP_PACKET.length - 14 - 20 - 8,
                      udp.getPayloadLength() );

        assertNotNull( "udp.getLowerLayerPacket()", udp.getLowerLayerPacket() );
        assertTrue( "udp.getLowerLayerPacket() instanceof Ip4",
                    udp.getLowerLayerPacket() instanceof IP4Packet );
        assertNull( "udp.getUpperLayerPacket()", udp.getUpperLayerPacket() );

        final String message = new String( udp.getPayloadData() );
        assertEquals( "udp.getPayloadData()", "Hello", message );
    }

    static byte toByte( final int val )
    {
        final int iv = val & 0xFF;
        if( iv > Byte.MAX_VALUE )
        {
            return (byte) ( iv - 256 );
        }
        else
        {
            return (byte) iv;
        }
    }

    static final byte[] UDP_PACKET = {toByte( 0x00 ), toByte( 0x50 ), toByte( 0xBA ), toByte( 0xC1 ), toByte(
        0x68 ), toByte( 0xE2 ), toByte( 0x00 ), toByte( 0x0C ), toByte( 0xF1 ), toByte( 0xE0 ), toByte(
            0xAD ), toByte( 0x5E ), toByte( 0x08 ), toByte( 0x00 ), toByte( 0x45 ), toByte( 0x00 ), toByte(
                0x00 ), toByte( 0x21 ), toByte( 0x6B ), toByte( 0x7B ), toByte( 0x00 ), toByte(
                    0x00 ), toByte( 0x80 ), toByte( 0x11 ), toByte( 0x4D ), toByte( 0x0F ), toByte(
                        0xC0 ), toByte( 0xA8 ), toByte( 0x00 ), toByte( 0xF0 ), toByte( 0xC0 ), toByte(
                            0xA8 ), toByte( 0x00 ), toByte( 0x01 ), toByte( 0x06 ), toByte( 0x77 ), toByte(
                                0x14 ), toByte( 0xC5 ), toByte( 0x00 ), toByte( 0x0D ), toByte(
                                    0x3E ), toByte( 0x84 ), toByte( 0x48 ), toByte( 0x65 ), toByte(
                                        0x6C ), toByte( 0x6C ), toByte( 0x6F )};

}
