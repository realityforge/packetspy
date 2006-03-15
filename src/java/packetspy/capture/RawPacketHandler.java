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
package packetspy.capture;

/**
 * Interface implemented to receive raw data from packet capture.
 */
public interface RawPacketHandler
{
    /**
     * Method called to pass raw data from packet capture.
     *
     * @param linkType the link type data received from. See libpcap for definitions.
     * @param length the length of the packet
     * @param capturedLength the length of packet actually captured
     * @param seconds the capture time in seconds
     * @param useconds the capture time micro seconds
     * @param data the packet data
     */
    void handlePacket( int linkType,
                       int length,
                       int capturedLength,
                       int seconds,
                       int useconds,
                       byte[] data );
}
