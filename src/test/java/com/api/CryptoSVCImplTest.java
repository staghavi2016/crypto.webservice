package com.api;

import static org.junit.Assert.*;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.spec.IvParameterSpec;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Base64;

public class CryptoSVCImplTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CryptoSVCImplTest.class);

    @Test
    public void testEncryptAndDecrypt() throws GeneralSecurityException {
        Base64.Encoder encoder = Base64.getEncoder();
        Base64.Decoder decoder = Base64.getDecoder();

        CryptoSVCImpl SymmetricKey = CryptoSVCImpl.getInstance("AES/CBC/PKCS5Padding");
        byte[] iv = SymmetricKey.randomIV();
        byte[] data = new byte[16];
        data = "testing%$_  ".getBytes();

        String base64EncryptedData = encoder.encodeToString(SymmetricKey.encrypt(data, iv));

        byte[] encryptedCombined = decoder.decode(base64EncryptedData);
        byte[] usedIV = Arrays.copyOfRange(encryptedCombined, 0, 16);
        byte[] encryptedData = Arrays.copyOfRange(encryptedCombined, 16, encryptedCombined.length);

        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        byte[] decrypted = SymmetricKey.decrypt(encryptedData, iv);

        assertEquals("testing%$_  ", new String(decrypted));
    }


    @Test
    public void testDecryptInvalidInput() throws GeneralSecurityException {

        CryptoSVCImpl SymmetricKey = CryptoSVCImpl.getInstance("AES/CBC/PKCS5Padding");

        byte[] iv = SymmetricKey.randomIV();
        byte[] invalidData = "testing%$_  ".getBytes();

        try {
            SymmetricKey.decrypt(invalidData, iv);
        } catch (GeneralSecurityException e) {
            LOGGER.error("Decrypting invalid input is not going to work ", e);
        }
    }
}
