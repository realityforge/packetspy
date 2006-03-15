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

import java.io.File;
import java.io.FileOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;
import packetspy.capture.NativePacketCaptureDriver;
import packetspy.model.io.ModelPacketListener;
import packetspy.model.io.RawToModelPacketHandler;

public class CaptureTestData
    implements ModelPacketListener
{
    private static RawPacket c_packet;
    private static boolean c_done;

    public static void main( final String[] args )
        throws Exception
    {
        if( 1 != args.length )
        {
            System.out.println( "Usage:" );
            System.out.println( "    java Main deviceNumber" );
            return;
        }

        final int deviceNumber = Integer.parseInt( args[0] );

        final NativePacketCaptureDriver capture = new NativePacketCaptureDriver();
        System.err.println( "Looking for Devices." );
        final String[] devices = NativePacketCaptureDriver.lookupDevices();
        System.err.println( "Found Devices: " + Arrays.asList( devices ) );

        final String device = devices[deviceNumber];
        System.err.println( "Opening Device: " + device );

        final RawToModelPacketHandler factory = new RawToModelPacketHandler( new CaptureTestData() );
        capture.open( device, NativePacketCaptureDriver.MAX_SNAPSHOT_LENGTH, true, 1000, "udp" );

        System.err.println( "Device open - preparing to capture" );

        startGeneratingUDPTestData();

        c_done = false;
        capture.capture( 1, factory );
        c_done = true;
        final String code = toJavaByteArrayCode( "UDP_PACKET", c_packet.getData() );
        final FileOutputStream fout = new FileOutputStream( new File( "output.txt" ) );
        fout.write( code.getBytes() );
        fout.close();
    }

    public void handlePacket( final Packet packet )
    {
        c_packet = (RawPacket) packet;
    }

    static void startGeneratingUDPTestData()
    {
        final Runnable runnable = new Runnable()
        {
            public void run()
            {
                generateUDPTestData();
            }
        };
        final Thread thread = new Thread( runnable );
        thread.start();
    }

    static void generateUDPTestData()
    {
        while( !c_done )
        {
            try
            {
                final DatagramSocket socket = new DatagramSocket();
                final byte[] ip = new byte[]{(byte) 192, (byte) 168, 0, 1};
                final InetAddress dest = InetAddress.getByAddress( ip );
                final InetSocketAddress address = new InetSocketAddress( dest, 5317 );
                socket.connect( address );
                final byte[] message = "Hello".getBytes();
                final DatagramPacket datagramPacket = new DatagramPacket( message,
                                                                          message.length,
                                                                          address );
                System.out.println( "Sending data packet." );
                socket.send( datagramPacket );
                Thread.sleep( 30 );
            }
            catch( final Throwable t )
            {
                t.printStackTrace();
            }
        }
    }

    static String toJavaByteArrayCode( final String arrayName, final byte[] data )
    {
        final StringBuffer sb = new StringBuffer( data.length * 5 + 40 );

        sb.append( "static final byte[] " );
        sb.append( arrayName );
        sb.append( " = \n{\n" );

        for( int i = 0; i < data.length; i++ )
        {
            if( 0 != i )
            {
                sb.append( ",\n" );
            }
            sb.append( " toByte( 0x" );
            RenderUtil.appendHexValue( sb, data[i] );
            sb.append( " )" );
        }

        sb.append( "\n};\n" );
        return sb.toString();
    }
}
