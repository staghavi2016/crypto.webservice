package com;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotEmpty;

public class webserviceConfiguration extends Configuration {

    /**
     * Hexadecimal encoded value of 256 bit AES key
     */
    @NotEmpty
    private String cipherMode;


     /**
     * String value of cipher mode for AES (e.g., AES/CBC/PKCS5Padding)
     */
    @JsonProperty
    public String getCipherMode() {
        return cipherMode;
    }

    /**
     * String value of cipher mode for AES (e.g., AES/CBC/PKCS5Padding)
     */
    @JsonProperty
    public void setCipherMode(String cipherMode) {
        this.cipherMode = cipherMode;
    }
}