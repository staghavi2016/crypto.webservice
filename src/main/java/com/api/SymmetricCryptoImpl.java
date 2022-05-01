package com.api;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Base64;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SymmetricCryptoImpl {

    private SecretKeySpec key;

    private CryptoSVCImpl SymmetricKey;

    @JsonProperty(value= "EncryptedData", required = true)
    private String base64EncryptedData;

    // Used for validating the size of ciphertext to be decrypted
    private Integer encyptedSize;
    private static final Logger LOGGER = LoggerFactory.getLogger(SymmetricCryptoImpl.class);

     /* Constructor through which we initialize and generate the AES symmetric key and keep it
     ** on RAM for enc/decryption
     */
    public SymmetricCryptoImpl(String cipherMode) {
        encyptedSize = 0;
        SymmetricKey = CryptoSVCImpl.getInstance(cipherMode);
    }

    public String encrypt(byte[] data) throws GeneralSecurityException {
        Base64.Encoder encoder = Base64.getEncoder();

        byte[] iv = SymmetricKey.randomIV();

        try {
            base64EncryptedData = encoder.encodeToString(SymmetricKey.encrypt(data, iv));
            encyptedSize = base64EncryptedData.length();
            return  base64EncryptedData;
        } catch (Exception e) {
            LOGGER.error("Error while encrypting the data", e);
            throw new GeneralSecurityException("Error while encrypting the data");
        }
    }

    public byte[] decrypt(String data) throws GeneralSecurityException {
        Base64.Decoder decoder = Base64.getDecoder();

        /* Splitting the encrypted data to separate out IV from ciphertext
        ** First 16 bytes are IV (IV size is 16 bytes) and the rest of the input is actual ciphertext
        */
        // TODO: Move this portion to SecretKeyGenerator class
        byte[] encryptedCombined = decoder.decode(data);
        byte[] iv = Arrays.copyOfRange(encryptedCombined,0,16);
        byte[] encryptedData = Arrays.copyOfRange(encryptedCombined,16,encryptedCombined.length);

        try {
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            byte[] decrypted = SymmetricKey.decrypt(encryptedData, iv);

            return decrypted;

        } catch (Exception e) {
            LOGGER.error("Error while decrypting the data", e);
            throw new GeneralSecurityException("Error while decrypting the data");
        }
    }

    public float getEncryptedSize() {
        return encyptedSize;
    }

}