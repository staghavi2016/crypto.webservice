package com;


import com.api.StreamDataStats;
import com.api.SymmetricCryptoImpl;
import com.resources.Decrypt;
import com.resources.PushAndRecalculate;
import com.resources.PushRecalculateAndEncrypt;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class webserviceApplication extends Application<webserviceConfiguration> {

    public static void main(final String[] args) throws Exception {
        new webserviceApplication().run(args);
    }

    @Override
    public String getName() {
        return "webservice";
    }

    @Override
    public void initialize(final Bootstrap<webserviceConfiguration> bootstrap) {
        // TODO: application initialization
    }


    @Override
    public void run(final webserviceConfiguration configuration,
                    final Environment env) {

        String cipherMode = configuration.getCipherMode();

        // Initializing SymmetricCrypto instance (
        // One AES key will be generated and used for all encryption/decryption operations
        // TODO: Expand this to per user/role key generation and key lifecycle management if needed be
        // TODO: Secure key storage or redesign using/leveraging KMS for key management
        SymmetricCryptoImpl symmetricEnc = new SymmetricCryptoImpl(cipherMode);

        // Initializing StreamDataStats instance
        // The running total for mean and standard deviations will be reset to 0 on server restart
        // TODO: Persist the running totals in an accumulator construct on persistent storage/DB
        StreamDataStats stats = new StreamDataStats();

        // Initializing all the RESTFUL resources
        final Decrypt DecryptResource = new Decrypt(symmetricEnc);
        final PushAndRecalculate PushAndRecalculateResource = new PushAndRecalculate(stats);
        final PushRecalculateAndEncrypt PushRecalculateAndEncryptResource = new PushRecalculateAndEncrypt(symmetricEnc, stats);

        // Registering all the RESTFUL resources with jersey
        env.jersey().register(DecryptResource);
        env.jersey().register(PushAndRecalculateResource);
        env.jersey().register(PushRecalculateAndEncryptResource);
    }

}