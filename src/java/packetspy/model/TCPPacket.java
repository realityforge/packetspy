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
 * Packet representing TCP packet.
 */
public class TCPPacket
    extends Packet
{
    /**
     * The source port.
     */
    private final int m_sourcePort;

    /**
     * The destination port.
     */
    private final int m_destinationPort;

    /**
     * The sequence.
     */
    private final long m_sequence;

    /**
     * The ack.
     */
    private final long m_ack;

    /**
     * The offset of the data in packet in 32 bit words.
     */
    private final byte m_dataOffset;

    /**
     * TRUE if the the URG flag is set.
     */
    private final boolean m_urgFlag;

    /**
     * TRUE if the the ACK flag is set.
     */
    private final boolean m_ackFlag;

    /**
     * TRUE if the the PSH flags is set.
     */
    private final boolean m_pshFlag;

    /**
     * TRUE if the the RST flags is set.
     */
    private final boolean m_rstFlag;

    /**
     * TRUE if the the SYN flags is set.
     */
    private final boolean m_synFlag;

    /**
     * TRUE if the the FIN flags is set.
     */
    private final boolean m_finFlag;

    /**
     * The window size.
     */
    private final int m_window;

    /**
     * The packet checksum.
     */
    private final int m_checksum;

    /**
     * The pointer to urgent data.
     */
    private final int m_urgentPointer;

    /**
     * Create TCP Packet.
     */
    public TCPPacket( final int sourcePort,
                    final int destinationPort,
                    final long sequence,
                    final long ack,
                    final byte dataOffset,
                    final boolean urgFlag,
                    final boolean ackFlag,
                    final boolean pshFlag,
                    final boolean rstFlag,
                    final boolean synFlag,
                    final boolean finFlag,
                    final int window,
                    final int checksum,
                    final int urgentPointer )
    {
        m_sourcePort = sourcePort;
        m_destinationPort = destinationPort;
        m_sequence = sequence;
        m_ack = ack;
        m_dataOffset = dataOffset;
        m_urgFlag = urgFlag;
        m_ackFlag = ackFlag;
        m_pshFlag = pshFlag;
        m_rstFlag = rstFlag;
        m_synFlag = synFlag;
        m_finFlag = finFlag;
        m_window = window;
        m_checksum = checksum;
        m_urgentPointer = urgentPointer;
    }

    /**
     * Return the source port.
     *
     * @return the source port.
     */
    public int getSourcePort()
    {
        return m_sourcePort;
    }

    /**
     * Return the destination port.
     *
     * @return the destination port.
     */
    public int getDestinationPort()
    {
        return m_destinationPort;
    }

    /**
     * Return the packets checksum.
     *
     * @return the packets checksum.
     */
    public int getChecksum()
    {
        return m_checksum;
    }

    /**
     * Return the sequence.
     *
     * @return the sequence.
     */
    public long getSequence()
    {
        return m_sequence;
    }

    /**
     * Return the ack.
     *
     * @return the ack.
     */
    public long getAck()
    {
        return m_ack;
    }

    /**
     * Return the offset of data in 32 bit words.
     *
     * @return the offset of data in 32 bit words.
     */
    public byte getDataOffset()
    {
        return m_dataOffset;
    }

    /**
     * Return true if URG flag set.
     *
     * @return true if URG flag set.
     */
    public boolean isUrgFlagSet()
    {
        return m_urgFlag;
    }

    /**
     * Return true if ACK flag set.
     *
     * @return true if ACK flag set.
     */
    public boolean isAckFlagSet()
    {
        return m_ackFlag;
    }

    /**
     * Return true if PSH flag set.
     *
     * @return true if PSH flag set.
     */
    public boolean isPshFlagSet()
    {
        return m_pshFlag;
    }

    /**
     * Return true if RST flag set.
     *
     * @return true if RST flag set.
     */
    public boolean isRstFlagSet()
    {
        return m_rstFlag;
    }

    /**
     * Return true if SYN flag set.
     *
     * @return true if SYN flag set.
     */
    public boolean isSynFlagSet()
    {
        return m_synFlag;
    }

    /**
     * Return true if FIN flag set.
     *
     * @return true if FIN flag set.
     */
    public boolean isFinFlagSet()
    {
        return m_finFlag;
    }

    /**
     * Return the data window.
     *
     * @return the data window.
     */
    public int getWindow()
    {
        return m_window;
    }

    /**
     * Return the pointer to urgent data.
     *
     * @return the pointer to urgent data.
     */
    public int getUrgentPointer()
    {
        return m_urgentPointer;
    }


    /**
     * @see Object#toString()
     */
    public String toString()
    {
        final IP4Packet ipPacket = (IP4Packet)getLowerLayerPacket();
        return
            "TCP[ " +
            RenderUtil.ipToString( ipPacket.getSource() ) + ":" + getSourcePort() +
            " ===> " +
            RenderUtil.ipToString( ipPacket.getDestination() ) + ":" + getDestinationPort() +
            " SEQ=" + getSequence() +
            " ACK=" + getAck() +
            " WIN=" + getWindow() +
            " DATA_OFF=" + getDataOffset() +
            " URG_PTR=" + getUrgentPointer() +
            " CHK=" + getChecksum() +
            " " +
            ((isAckFlagSet())?"ACK ":"") +
            ((isFinFlagSet())?"FIN ":"") +
            ((isPshFlagSet())?"PSH ":"") +
            ((isRstFlagSet())?"RST ":"") +
            ((isSynFlagSet())?"SYN ":"") +
            ((isUrgFlagSet())?"URG ":"") +
            "]";
    }
}
