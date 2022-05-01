package com.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;

//Wrapper class for type string for easy Jackson serialization/deserialization dropwizard.io provides
public class CipherText {
    @NotEmpty
    private String cipherText;

    // For Jackson deserialization
    public CipherText() {
    }

    public CipherText(String cipherText) {
        this.cipherText = cipherText;
    }

    /**
     * Returns a CipherText value
     */
    @JsonProperty
    public String getCipherText() {
        return cipherText;
    }

    /**
     * Sets a CipherText value
     */
    @JsonProperty
    public void setCipherText(String cipherText) {
        this.cipherText = cipherText;
    }
}
