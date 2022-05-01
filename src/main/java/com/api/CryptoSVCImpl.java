package com.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.validation.constraints.NotEmpty;
import java.security.NoSuchAlgorithmException;

public class CryptoSVCImpl {
    // Using singleton as for this version we only need to generate and use one symmetric key for encryption
    private static CryptoSVCImpl singleInstance = null;

    //Supporting various key sizes for algorithms that support variety of key sizes in future
    private int keySize;

    //Supporting various symmetric algorithms that a given crypto provider library supports JCE for instance supports
    // E.g., AES (128), DES (56), DESede (168) etc.
    private String cryptoAlgo;

    // Supporting various modes of given algorithm (e.g., AES/CBC/PKCS5Padding) Configurable through config.yml
    private String cipherMode;

    // Base64 version of the AES key used for encryption
    @NotEmpty
    private final SecretKeySpec key;

    /* TODO To enable usage of various number of keys per role or user with TTL or better to enable KMS integration
        and fetch keys per role or token for enc/decryption
    */
    private CryptoSVCImpl(String cipherMode, Integer size, String algo) throws NoSuchAlgorithmException {
        this.cipherMode = cipherMode;
        this.keySize = size != null ? size : 256;
        this.cryptoAlgo = algo != null ? algo : "AES";
        KeyGenerator keyGenerator = KeyGenerator.getInstance(this.cryptoAlgo);
        keyGenerator.init(this.keySize);
        byte[] keyGen = keyGenerator.generateKey().getEncoded();

        try {
            if (Cipher.getMaxAllowedKeyLength("AES") < 256) {
                throw new IllegalStateException("AES256 is not supported!");
            }
            this.key = new SecretKeySpec(keyGen, "AES");
        } catch(NoSuchAlgorithmException ex) {
            LOGGER.info("Error while setting AES symmetric key", ex);
            try {
                throw new GeneralSecurityException("Error while setting AES symmetric key");
            } catch (GeneralSecurityException e) {
                throw new RuntimeException(e);
            }
        }
    }
    // Generates a secure random initial vectors
    public byte[] randomIV() {
        SecureRandom sr = new SecureRandom();
        byte[] iv = new byte[16]; // AES uses 128 bit blocks

        sr.nextBytes(iv);
        return iv;
    }

    // To be extended to leverage and configure algorithm (e.g., AES, 3DES, etc.) and keysize
    // For now, abiding the requirements of only 1 key for the system
    public static CryptoSVCImpl getInstance(String cipherMode)
    {
        if (singleInstance == null) {
            try {
                singleInstance = new CryptoSVCImpl(cipherMode, null, null);
            } catch (NoSuchAlgorithmException e) {
                LOGGER.error("Error while generating the AES symmetric key singleton object", e);
                throw new RuntimeException(e);
            }
        }

        return singleInstance;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(CryptoSVCImpl.class);

    public byte[] encrypt(byte[] data, byte[] iv) throws GeneralSecurityException {

        try {
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            /* Alternatively and once the number of keys are extended, we shall use better blockcipher modes of AES
             ** e.g., CTR modes such as GCM --> AES/GCM/NoPadding  --> referenced here:
             **       https://csrc.nist.gov/projects/block-cipher-techniques/bcm/modes-development
             */
            Cipher cipher = Cipher.getInstance(cipherMode);
            cipher.init(Cipher.ENCRYPT_MODE, this.key, ivSpec);
            byte[] encrypted = cipher.doFinal(data);

            /* In order to keep track of IV used for encryption, combining the initialization vector (IV) byte array to
             ** the encrypted byte array as prefix so that we could split them in decryption phase and extract the IV
             ** there
             */

            byte[] combined = new byte[iv.length+encrypted.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);
            return  combined;
        } catch (Exception e) {
            LOGGER.error("Error while encrypting the data", e);
            throw new GeneralSecurityException("Error while encrypting the data");
        }
    }

    public byte[] decrypt(byte[] encryptedData, byte[] iv) throws GeneralSecurityException {
        try {
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            /* Alternatively and once the number of keys are extended, we shall use better blockcipher modes of AES
             ** e.g., CTR modes such as GCM --> AES/GCM/NoPadding  --> referenced here:
             **       https://csrc.nist.gov/projects/block-cipher-techniques/bcm/modes-development
             */
            Cipher cipher = Cipher.getInstance(cipherMode);
            cipher.init(Cipher.DECRYPT_MODE, this.key, ivSpec);
            byte[] decrypted = cipher.doFinal(encryptedData);

            return decrypted;

        } catch (Exception e) {
            LOGGER.error("Error while decrypting the data", e);
            throw new GeneralSecurityException("Error while decrypting the data");
        }
    }
}