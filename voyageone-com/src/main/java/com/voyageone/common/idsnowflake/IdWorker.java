package com.voyageone.common.idsnowflake;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IdWorker {
    private static final Logger logger = LoggerFactory.getLogger(IdWorker.class);

    public static final long Twepoch = 1288834974657L;

    static final int WorkerIdBits = 5;
    static final int DatacenterIdBits = 5;
    static final int SequenceBits = 12;
    static final long MaxWorkerId = ~(-1L << WorkerIdBits);
    static final long MaxDatacenterId = ~(-1L << DatacenterIdBits);

    private static final int WorkerIdShift = SequenceBits;
    private static final int DatacenterIdShift = SequenceBits + WorkerIdBits;
    public static final int TimestampLeftShift = SequenceBits + WorkerIdBits + DatacenterIdBits;
    private static final long SequenceMask = ~(-1L << SequenceBits);

    private long _sequence = 0L;
    private long _lastTimestamp = -1L;

    protected long WorkerId;
    protected long DatacenterId;

    public long getWorkerId() {
        return WorkerId;
    }

    public long getDatacenterId() {
        return DatacenterId;
    }

    public long getSequence() {
        return _sequence;
        //internal set { _sequence = value; }
    }

    public void setSequence(long Sequence) {
        this._sequence = Sequence;
    }

    /// <summary>
    ///
    /// </summary>
    /// <param name="workerId">2的5次方=32</param>
    /// <param name="datacenterId">2的5次方=32</param>
    public IdWorker(long workerId, long datacenterId) {
        this(workerId, datacenterId, 0);
    }

    /// <summary>
    ///
    /// </summary>
    /// <param name="workerId">2的5次方=32</param>
    /// <param name="datacenterId">2的5次方=32</param>
    /// <param name="sequence">2的12次方=4028</param>
    public IdWorker(long workerId, long datacenterId, long sequence) {
        this.WorkerId = workerId;
        this.DatacenterId = datacenterId;
        _sequence = sequence;

        // sanity check for workerId
        if (workerId > MaxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", MaxWorkerId));
        }

        if (datacenterId > MaxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", MaxDatacenterId));
        }
    }

    public synchronized long NextId() {
        long timestamp = this.timeGen();

        if (timestamp < _lastTimestamp) {
            try {
                throw new Exception(
                        String.format(
                                "Clock moved backwards.  Refusing to generate id for %d milliseconds",
                                this._lastTimestamp - timestamp));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

        if (_lastTimestamp == timestamp) {
            _sequence = (_sequence + 1) & SequenceMask;
            if (_sequence == 0) {
                timestamp = this.tilNextMillis(_lastTimestamp);
            }
        } else {
            _sequence = 0;
        }

        _lastTimestamp = timestamp;
        return ((timestamp - Twepoch) << TimestampLeftShift) |
                (DatacenterId << DatacenterIdShift) |
                (WorkerId << WorkerIdShift) | _sequence;

    }

    private long tilNextMillis(final long lastTimestamp) {
        long timestamp = this.timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = this.timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }
}