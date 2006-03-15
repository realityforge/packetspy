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

public class RenderUtilTestCase
    extends TestCase
{
    public void test_nibbleToHex()
        throws Exception
    {
        assertEquals( "0x00", '0', RenderUtil.nibbleToHex( (byte)0x00 ) );
        assertEquals( "0x01", '1', RenderUtil.nibbleToHex( (byte)0x01 ) );
        assertEquals( "0x02", '2', RenderUtil.nibbleToHex( (byte)0x02 ) );
        assertEquals( "0x03", '3', RenderUtil.nibbleToHex( (byte)0x03 ) );
        assertEquals( "0x04", '4', RenderUtil.nibbleToHex( (byte)0x04 ) );
        assertEquals( "0x05", '5', RenderUtil.nibbleToHex( (byte)0x05 ) );
        assertEquals( "0x06", '6', RenderUtil.nibbleToHex( (byte)0x06 ) );
        assertEquals( "0x07", '7', RenderUtil.nibbleToHex( (byte)0x07 ) );
        assertEquals( "0x08", '8', RenderUtil.nibbleToHex( (byte)0x08 ) );
        assertEquals( "0x09", '9', RenderUtil.nibbleToHex( (byte)0x09 ) );
        assertEquals( "0x0A", 'A', RenderUtil.nibbleToHex( (byte)0x0A ) );
        assertEquals( "0x0B", 'B', RenderUtil.nibbleToHex( (byte)0x0B ) );
        assertEquals( "0x0C", 'C', RenderUtil.nibbleToHex( (byte)0x0C ) );
        assertEquals( "0x0D", 'D', RenderUtil.nibbleToHex( (byte)0x0D ) );
        assertEquals( "0x0E", 'E', RenderUtil.nibbleToHex( (byte)0x0E ) );
        assertEquals( "0x0F", 'F', RenderUtil.nibbleToHex( (byte)0x0F ) );
    }

    public void test_appendHexValue()
        throws Exception
    {
        doAppendTest( "00", 0x00 );
        doAppendTest( "11", 0x11 );
        doAppendTest( "23", 0x23 );
        doAppendTest( "46", 0x46 );
        doAppendTest( "69", 0x69 );

        doAppendTest( "A3", 0xA3 );
        doAppendTest( "B2", 0xB2 );
        doAppendTest( "C1", 0xC1 );
        doAppendTest( "DF", 0xDF );
        doAppendTest( "EE", 0xEE );
        doAppendTest( "FC", 0xFC );
    }

    public void test_macToString()
        throws Exception
    {
        final byte[] address =
            new byte[]{(byte)0xFF, (byte)0xE1, (byte)0xD2, (byte)0xC3, (byte)0xB4, (byte)0xA5};
        final String macString = RenderUtil.macToString( address );
        assertEquals( macString, "FF:E1:D2:C3:B4:A5" );
    }

    public void test_ipToString()
        throws Exception
    {
        final byte[] address =
            new byte[]{123, 21, 35, 17};
        final String macString = RenderUtil.ipToString( address );
        assertEquals( macString, "123.21.35.17" );
    }




    private void doAppendTest( final String str, final int val )
    {
        final StringBuffer sb = new StringBuffer( );
        RenderUtil.appendHexValue( sb, (byte)val );
        assertEquals( "0x" + str, str, sb.toString() );
    }

}
