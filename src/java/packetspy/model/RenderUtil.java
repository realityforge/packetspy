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
 * Utilities to help render network fields as strings.
 */
public class RenderUtil
{
    /**
     * Convert specified array of bytes into an ip-string.
     *
     * @param address the address
     * @return the ip address
     */
    public static String ipToString( final byte[] address )
    {
        final StringBuffer sb = new StringBuffer( address.length * 4 );
        for( int i = 0; i < address.length; i++ )
        {
            final int element = address[i] & 0xFF;
            if( 0 != i )
            {
                sb.append( '.' );
            }
            sb.append( element );
        }
        return sb.toString();
    }

    /**
     * Convert specified array of bytes into an mac address string.
     *
     * @param address the address
     * @return the mac address string
     */
    public static String macToString( final byte[] address )
    {
        final StringBuffer sb = new StringBuffer( address.length * 3 );
        for( int i = 0; i < address.length; i++ )
        {
            if( 0 != i )
            {
                sb.append( ':' );
            }
            appendHexValue( sb, address[i] );
        }
        return sb.toString();
    }


    /**
     * Convert a byte into hex value and add to buffer.
     *
     * @param sb the StringBuffer.
     * @param data the data.
     */
    static void appendHexValue( final StringBuffer sb, final byte data )
    {
        sb.append( nibbleToHex( (byte) ( data >> 4 ) ) );
        sb.append( nibbleToHex( data ) );
    }

    /**
     * Convert lower 4 bits into a hex character.
     *
     * @param data the data byte
     * @return the hex character
     */
    static char nibbleToHex( final byte data )
    {
        final int nibble = data & 0xf;
        if( nibble <= 9 )
        {
            return (char) ( '0' + nibble );
        }
        else
        {
            return (char) ( 'A' + nibble - 10 );
        }
    }
}
