package com.resources;


import com.api.*;
import com.resources.PushAndRecalculate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.util.Map;

@Path("")
public class PushRecalculateAndEncrypt {

    private StreamDataStats stat;

    private SymmetricCryptoImpl crypto;

    public PushRecalculateAndEncrypt(SymmetricCryptoImpl symmetricEnc, StreamDataStats stat) {
        this.crypto = symmetricEnc;
        this.stat = stat;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(PushRecalculateAndEncrypt.class);


     /* Supports calculating new mean and standard deviation and encrypting both.
     ** Input format is: '{"signleNum":SINGLE INTEGER NUMBER}'
     **
     ** e.g.,
     **  curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d
     **  '{"signleNum":2}'  http://0.0.0.0:8080/PushAndRecalculate
     **
     **   OUTPUTS: '{"mean":{"cipherText":"0Npo/Y7s7IVxTCOxN2QCgw=="},
     **              "standardD":{"cipherText":"fyjNtPlmMCQb3Vhkr6z/Hw=="}}'
     */
    @POST
    @Path("/PushRecalculateAndEncrypt")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CipherStatsMap pushRecalculateAndEncrypt(@NotNull @Valid IntNumber num) {
        Map<String, Float> stats =  stat.getMovingStats(num.getNum());

        byte[] meanBytes = ByteBuffer.allocate(4).putFloat(stats.get("mean")).array();
        String encryptedMean;
        try {
            encryptedMean = crypto.encrypt(meanBytes);
        } catch (GeneralSecurityException e) {
            throw new WebApplicationException(e.getMessage(), e);
        }

        byte[] standardDBytes = ByteBuffer.allocate(4).putFloat(stats.get("standardD")).array();
        String encryptedStandardD;
        try {
            encryptedStandardD = crypto.encrypt(standardDBytes);
        } catch (GeneralSecurityException e) {
            throw new WebApplicationException(e.getMessage(), e);
        }
        CipherText mean = new CipherText(encryptedMean);
        CipherText standardD = new CipherText(encryptedStandardD);
        return new CipherStatsMap(mean, standardD);
    }

}
