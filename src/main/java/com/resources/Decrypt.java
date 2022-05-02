package com.resources;


import com.api.*;
import io.dropwizard.jersey.caching.CacheControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.util.concurrent.TimeUnit;

import com.codahale.metrics.annotation.*;

@Path("")
public class Decrypt {

    private SymmetricCryptoImpl crypto;

    public Decrypt(SymmetricCryptoImpl symmetricEnc) {
        this.crypto = symmetricEnc;
    }
    private static final Logger LOGGER = LoggerFactory.getLogger(Decrypt.class);

    /* Supports decrypting an encrypted stat (namely mean or standard deviation) (no distinction on which)
    ** Input format is: '{"cipherText":"ENCRYPTED NUMBER"}'
    **
    ** e.g.,
    **  curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d
    **  '{"cipherText":"AhDjGm/TwOar80AcickyZfAICokOwTPwTqbJZA=="}' http://0.0.0.0:8080/Decrypt
    **
    **   OUTPUTS: {"floatNum":8.0}
    */
    @POST
    @Timed(name= "decrypt-post-requests-timed")
    @Metered(name= "decrypt-post-requests-metered")
    @CacheControl(maxAge = 1, maxAgeUnit = TimeUnit.MINUTES)
    @Path("/Decrypt")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FloatNumber Decrypt(@NotNull @Valid CipherText s) {
        if (s.getCipherText().length() != crypto.getEncryptedSize()) {
            // Note: This check prevents encryption service from trying to
            // decrypt very large INVALID input
            LOGGER.error("input Ciphertext is larger than expected size");
            throw new WebApplicationException("input Ciphertext is larger than expected size");
        }
        byte[] plainText;
        try {
            plainText = crypto.decrypt(s.getCipherText());
        } catch (GeneralSecurityException e) {
            throw new WebApplicationException(e.getMessage(), e);
        }
        float stat = ByteBuffer.wrap(plainText).getFloat();
        return new FloatNumber(stat);
    }

     /* Supports decrypting a hashmap of both mean and standard deviation
     ** Input format is
     ** '{"mean":{"cipherText":"ENCRYPTED MEAN"},"standardD":{"cipherText":"ENCRYPTED STD"}}'
     **
     ** e.g.,
     **  curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d
     **  '{"mean":{"cipherText":"0Npo/Y7s7IVxTCOxN2QCgw=="},"standardD":{"cipherText":"fyjNtPlmMCQb3Vhkr6z/Hw=="}}'
     **  http://0.0.0.0:8080/DecryptMap
     **
     **  OUTPUTS: {"mean":8.0,"standardD":1.0}
     */
    @POST
    @Timed(name= "decryptMap-post-requests-timed")
    @Metered(name= "decryptMap-post-requests-metered")
    @CacheControl(maxAge = 1, maxAgeUnit = TimeUnit.MINUTES)
    @Path("/DecryptMap")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public StatsMap DecryptMap(@NotNull @Valid CipherStatsMap s) {
        CipherText cipherMean = s.getMean();
        CipherText cipherStandardD = s.getStandardD();
        FloatNumber plainMean = Decrypt(cipherMean);
        FloatNumber plainStandardD = Decrypt(cipherStandardD);
        return new StatsMap(plainMean.getFloatNum(), plainStandardD.getFloatNum());
    }
}