package com.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

// Wrapper class for type StatsMap for easy Jackson serialization/deserialization dropwizard.io provides
// It will help serialize/deserialize a HashMap of mean and standard deviation
public class StatsMap {
    @NotNull
    private float mean;

    @NotNull
    private float standardD;

    // For Jackson deserialization
    public StatsMap() {
    }

    public StatsMap(float mean, float standardD) {
        this.mean = mean;
        this.standardD = standardD;
    }

    /**
     * Returns float value of moving mean
     */
    @JsonProperty
    public float getMean() {
        return mean;
    }

    /**
     * Sets float value of moving standard deviation
     */
    @JsonProperty
    public float getStandardD() {
        return standardD;
    }
}
