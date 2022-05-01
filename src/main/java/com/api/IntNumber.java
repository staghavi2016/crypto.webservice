package com.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof IntNumber)) {
            return false;
        }

        IntNumber number = (IntNumber) o;

        return singleNum == number.getNum();
    }

    @Override
    public int hashCode() {
        return Objects.hash(singleNum);
    }
}
