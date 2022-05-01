package com.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class StreamDataStats {
    private float mean = 0;

    // Creates new AtomicLong with initial value of 0
    private AtomicLong count =  new AtomicLong();
    private float dSquared = 0;

    private float standardD = 0;

    public StreamDataStats() {
        this.mean = 0;
        this.dSquared = 0;
        this.standardD = 0;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(StreamDataStats.class);


    /* Using Welford's online algorithm for stream data standard deviation calculation
    ** Referenced here:
    ** https://en.wikipedia.org/wiki/Algorithms_for_calculating_variance
    ** https://nestedsoftware.com/2018/03/27/calculating-standard-deviation-on-streaming-data-253l.23919.html
    **
    ** Adding 'synchronized' To support multithreading and control the access to getMovingStats method
    ** TODO: Non-blocking mechanisms to be used instead or improving performance by architecturally addressing blocking
    **  requests
    */

    public synchronized Map<String, Float> getMovingStats(int singleNum) {
        if (count.longValue() == Long.MAX_VALUE) {
            throw new RuntimeException("Number of requests exceeded the server limit");
        }

        Map<String, Float> stats = new HashMap<>();

        count.incrementAndGet();
        float meanDiff = (singleNum - mean) / count.get();
        float newMean = mean + meanDiff;
        float dSquaredIncrement = (singleNum - newMean) * (singleNum - mean);
        float newDSquared = dSquared + dSquaredIncrement;
        mean = newMean;
        dSquared = newDSquared;
        standardD = (float) Math.sqrt(dSquared / count.get());

        stats.put("mean", mean);
        stats.put("standardD", standardD);

        return stats;
    }
    @JsonProperty
    public float getMean() {
        if (count.get() > 0) {
            return mean;
        }
        else {
            LOGGER.info("Count is 0 and mean is not yet defined");
            return 0;
        }
    }

    @JsonProperty
    public float getStd() {
        if (count.get() > 0) {
            return standardD;
        }
        else {
            LOGGER.info("Count is 0 and standardD is not yet defined");
            return 0;
        }
    }

}