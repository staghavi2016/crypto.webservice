package com.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

// Wrapper class for type int (i.e., single integer input de/serialization)
// for easy Jackson serialization/deserialization dropwizard.io provides
public class IntNumber {
    @NotNull
    private int singleNum;

    // For Jackson deserialization
    public IntNumber() {
    }

    public IntNumber(@JsonProperty("singleNum") int singleNum) {
        this.singleNum = singleNum;
    }

    /**
     * Returns a IntNumber value
     */
    @JsonProperty
    public int getNum() {
        return singleNum;
    }

    /**
     * sets a IntNumber value
     */
    @JsonProperty
    public void setNum(int singleNum) {
        this.singleNum = singleNum;
    }
}