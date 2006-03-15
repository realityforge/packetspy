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
 * The base class for packets of specific layer.
 */
public class Packet
{
    /**
     * The upper layer packet.
     */
    private Packet m_upperLayerPacket;

    /**
     * The lower layer packet.
     */
    private Packet m_lowerLayerPacket;

    /**
     * The offset of payload relative to header offset.
     */
    private int m_payloadOffset;

    /**
     * The size of the payload in bytes.
     */
    private int m_payloadLength;

    /**
     * The underlying data array. This represents the complete capture and the data
     * for this packet is specified using the above fields.
     */
    private byte[] m_data;

    /**
     * Field that is lazily created the contains the payload data for packet.
     */
    private byte[] m_payloadData;

    /**
     * Return the size of the payload in bytes.
     *
     * @return the size of the payload in bytes.
     */
    public int getPayloadLength()
    {
        return m_payloadLength;
    }

    /**
     * Set the size of the payload in bytes.
     *
     * @param payloadLength the size of the payload in bytes.
     */
    public void setPayloadLength( final int payloadLength )
    {
        m_payloadLength = payloadLength;
    }

    /**
     * Return true if the complete payload for this packet was captured.
     *
     * @return true if the complete payload for this packet was captured.
     */
    public boolean isCaptureComplete()
    {
        if( null == m_data )
        {
            return false;
        }
        else
        {
            final int start = getPayloadStart();
            final int end = start + m_payloadLength;
            return m_data.length >= end;
        }
    }

    /**
     * Return the payload data.
     *
     * @return the payload data.
     */
    public byte[] getPayloadData()
    {
        if( null == m_payloadData )
        {
            final int start = getPayloadStart();
            final int end = start + m_payloadLength;
            final int actualEnd = Math.min( m_data.length, end );
            final int length = actualEnd - start;
            if( 0 >= length )
            {
                m_payloadData = new byte[0];
            }
            else
            {
                m_payloadData = new byte[length];
                System.arraycopy( m_data, start, m_payloadData, 0, length );
            }
        }
        return m_payloadData;
    }

    /**
     * Set the underlying data captured for complete packet hierarchy.
     *
     * @param data the data
     */
    public void setData( final byte[] data )
    {
        m_data = data;
        m_payloadData = null;
    }

    /**
     * Return the absolute position of the header in bytes.
     *
     * @return the absolute position of the header in bytes.
     */
    public int getHeaderStart()
    {
        final Packet packet = getLowerLayerPacket();
        if( null == packet )
        {
            return 0;
        }
        else
        {
            return packet.getPayloadStart();
        }
    }

    /**
     * Return the absolute position of the payload/data in bytes.
     *
     * @return the absolute position of the payload/data in bytes.
     */
    public int getPayloadStart()
    {
        final int start = getPayloadOffset();
        final Packet packet = getLowerLayerPacket();
        if( null == packet )
        {
            return start;
        }
        else
        {
            return packet.getPayloadStart() + start;
        }
    }

    /**
     * Return the offset of payload relative to header offset.
     *
     * @return the offset of payload relative to header offset.
     */
    public int getPayloadOffset()
    {
        return m_payloadOffset;
    }

    /**
     * Set the offset of payload relative to header offset.
     *
     * @param payloadOffset the offset of payload relative to header offset.
     */
    public void setPayloadOffset( final int payloadOffset )
    {
        m_payloadOffset = payloadOffset;
    }

    /**
     * Return the upper layer packet.
     *
     * @return the upper layer packet.
     */
    public Packet getUpperLayerPacket()
    {
        return m_upperLayerPacket;
    }

    /**
     * Set the upper layer packet.
     * This method will also set the specified packets
     * lower layer packet field.
     *
     * @param packet upper layer packet.
     */
    public void setUpperLayerPacket( final Packet packet )
    {
        final boolean identical = m_upperLayerPacket == packet;
        if( !identical )
        {
            final Packet oldPacket = m_upperLayerPacket;
            m_upperLayerPacket = packet;

            if( null != oldPacket )
            {
                oldPacket.setLowerLayerPacket( null );
            }
            if( null != packet )
            {
                packet.setLowerLayerPacket( this );
            }
        }
    }

    /**
     * Return the lower layer packet.
     *
     * @return the lower layer packet.
     */
    public Packet getLowerLayerPacket()
    {
        return m_lowerLayerPacket;
    }

    /**
     * Set the lower layer packet.
     * This method will also set the specified packets
     * upper layer packet field.
     *
     * @param packet the lower layer packet.
     */
    public void setLowerLayerPacket( final Packet packet )
    {
        final boolean identical = m_lowerLayerPacket == packet;
        if( !identical )
        {
            final Packet oldPacket = m_lowerLayerPacket;
            m_lowerLayerPacket = packet;
            if( null != oldPacket )
            {
                oldPacket.setUpperLayerPacket( null );
            }
            if( null != packet )
            {
                packet.setUpperLayerPacket( this );
            }
        }
    }
}
