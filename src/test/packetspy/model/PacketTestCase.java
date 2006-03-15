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

public class PacketTestCase
    extends TestCase
{
    public void testAccessors()
        throws Exception
    {
        final Packet inner = new Packet();
        inner.setPayloadOffset( 10 );
        inner.setPayloadLength( 15 );

        assertEquals( "inner.payLoadOffset", 10, inner.getPayloadOffset() );
        assertEquals( "inner.payLoadLength", 15, inner.getPayloadLength() );
        assertEquals( "inner.payLoadStart", 10, inner.getPayloadStart() );

        final Packet outer = new Packet();
        outer.setPayloadOffset( 7 );
        outer.setPayloadLength( 25 );

        assertEquals( "outer.payLoadOffset", 7, outer.getPayloadOffset() );
        assertEquals( "outer.payLoadLength", 25, outer.getPayloadLength() );
        assertEquals( "outer.payLoadStart", 7, outer.getPayloadStart() );

        outer.setUpperLayerPacket( inner );
        assertEquals( "PostLink1 inner.payLoadStart", 17, inner.getPayloadStart() );
        outer.setUpperLayerPacket( null );

        inner.setLowerLayerPacket( outer );
        assertEquals( "PostLink2 inner.payLoadStart", 17, inner.getPayloadStart() );
        inner.setLowerLayerPacket( null );

        //Setup packet again for data tests
        inner.setLowerLayerPacket( outer );

        assertEquals( "inner.isCaptureComplete() PRE Data", false, inner.isCaptureComplete() );
        assertEquals( "outer.isCaptureComplete() PRE Data", false, outer.isCaptureComplete() );

        final byte[] data1 = new byte[ 32 ];
        inner.setData( data1 );
        outer.setData( data1 );


        assertEquals( "inner.isCaptureComplete() POST Data1", true, inner.isCaptureComplete() );
        assertEquals( "outer.isCaptureComplete() POST Data1", true, outer.isCaptureComplete() );
        assertEquals( "inner.getPayloadData().length POST Data1", 15, inner.getPayloadData().length );
        assertEquals( "outer.getPayloadData().length POST Data1", 25, outer.getPayloadData().length );

        final byte[] data2 = new byte[ 28 ];
        inner.setData( data2 );
        outer.setData( data2 );

        assertEquals( "inner.isCaptureComplete() POST Data2", false, inner.isCaptureComplete() );
        assertEquals( "outer.isCaptureComplete() POST Data2", false, outer.isCaptureComplete() );
        assertEquals( "inner.getPayloadData().length POST Data2", 11, inner.getPayloadData().length );
        assertEquals( "outer.getPayloadData().length POST Data2", 21, outer.getPayloadData().length );
    }

}
