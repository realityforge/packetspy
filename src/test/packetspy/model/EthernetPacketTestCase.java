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

import junit.framework.TestCase;

public class EthernetPacketTestCase
    extends TestCase
{
    private static final byte[] SRC = new byte[]{(byte)0xFF, (byte)0xE1, (byte)0xD2, (byte)0xC3, (byte)0xB4, (byte)0xA5};
    private static final byte[] DEST = new byte[]{(byte)0xFE, (byte)0xEE, (byte)0xDE, (byte)0xCE, (byte)0xBE, (byte)0xAE};
    private static final int TYPE = EthernetPacket.TYPE_IP;

    public void test_accessors()
        throws Exception
    {

        final EthernetPacket packet = new EthernetPacket( DEST, SRC, TYPE );
        assertEquals( "dest", packet.getDestination(), DEST );
        assertEquals( "src", packet.getSource(), SRC );
        assertEquals( "type", packet.getType(), TYPE );

        assertEquals( "toString", packet.toString(),
                      "Ethernet[ FF:E1:D2:C3:B4:A5 ===> FE:EE:DE:CE:BE:AE T=2048]" );
    }

    public void testNull_source_PassedInto_Ctor()
        throws Exception
    {
        try
        {
            new EthernetPacket( DEST, null, TYPE );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMesage()", "source", npe.getMessage() );
            return;
        }
        fail( "Expected a NullPointerException for source passed into Ctor" );
    }

    public void testNull_destination_PassedInto_Ctor()
        throws Exception
    {
        try
        {
            new EthernetPacket( null, SRC, TYPE );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMesage()", "destination", npe.getMessage() );
            return;
        }
        fail( "Expected a NullPointerException for destination passed into Ctor" );
    }

    public void testBad_source_PassedInto_Ctor()
        throws Exception
    {
        try
        {
            new EthernetPacket( DEST, new byte[2], TYPE );
        }
        catch( final IllegalArgumentException iae )
        {
            assertEquals( "npe.getMesage()", "6 != source.length", iae.getMessage() );
            return;
        }
        fail( "Expected a IllegalArgumentException for bad source passed into Ctor" );
    }

    public void testBad_destination_PassedInto_Ctor()
        throws Exception
    {
        try
        {
            new EthernetPacket( new byte[2], SRC, TYPE );
        }
        catch( final IllegalArgumentException iae )
        {
            assertEquals( "npe.getMesage()", "6 != destination.length", iae.getMessage() );
            return;
        }
        fail( "Expected a IllegalArgumentException for bad destination passed into Ctor" );
    }
}
