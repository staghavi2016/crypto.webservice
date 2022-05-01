package com.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

// Wrapper class for type CipherStatsMap for easy Jackson serialization/deserialization dropwizard.io provides
// It will help serialize/deserialize a HashMap of encrypted mean and standard deviation
public class CipherStatsMap {
    @NotNull
    private CipherText mean;

    @NotNull
    private CipherText standardD;

    // For Jackson deserialization
    public CipherStatsMap() {
    }

    public CipherStatsMap(CipherText mean, CipherText standardD) {
        this.mean = mean;
        this.standardD = standardD;
    }

    /**
     * Returns CipherText value of moving mean
     */
    @JsonProperty
    public CipherText getMean() {
        return this.mean;
    }

    /**
     * Returns CipherText value of moving standard deviation
     */
    @JsonProperty
    public CipherText getStandardD() {
        return this.standardD;
    }
}
