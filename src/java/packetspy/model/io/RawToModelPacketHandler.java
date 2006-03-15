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

import packetspy.capture.RawPacketHandler;
import packetspy.model.RawPacket;

/**
 * Model factory for turning raw capture data into packets.
 */
public class RawToModelPacketHandler
    implements RawPacketHandler
{
    /**
     * The underlying factory.
     */
    private final ModelPacketFactory m_factory = new ModelPacketFactory();

    /**
     * The listener to pass on built packets to.
     */
    private final ModelPacketListener m_listener;

    /**
     * Create factory that passes on created packets to specified listener.
     *
     * @param listener the listener
     */
    public RawToModelPacketHandler( final ModelPacketListener listener )
    {
        if( null == listener )
        {
            throw new NullPointerException( "listener" );
        }
        m_listener = listener;
    }

    /**
     * Handler passed packet data that constructs packet hierarchy and
     * passes it on to listener.
     */
    public void handlePacket( final int linkType,
                              final int length,
                              final int capturedLength,
                              final int seconds,
                              final int useconds,
                              final byte[] data )
    {
        final RawPacket raw = m_factory.parseRawPacket( linkType,
                                                        length,
                                                        capturedLength,
                                                        seconds,
                                                        useconds,
                                                        data );

        m_listener.handlePacket( raw );
    }
}
