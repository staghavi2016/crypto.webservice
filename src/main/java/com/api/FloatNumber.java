package com.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

// Wrapper class for type float (i.e., single statistics de/serialization)
// for easy Jackson serialization/deserialization dropwizard.io provides
public class FloatNumber {
    @NotNull
    private float floatNum;

    // For Jackson deserialization
    public FloatNumber() {
    }

    public FloatNumber(float floatNum) {
        this.floatNum = floatNum;
    }

    /**
     * Returns a FloatNumber value
     */
    @JsonProperty
    public float getFloatNum() {
        return floatNum;
    }

    /**
     * sets a FloatNumber value
     */
    @JsonProperty
    public void setFloatNum(float floatNum) {
        this.floatNum = floatNum;
    }
}