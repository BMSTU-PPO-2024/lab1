package com.github.romanqed.devspark.hash;

import com.github.romanqed.devspark.di.ProviderConsumer;
import com.github.romanqed.devspark.di.ServiceProviderConsumer;
import com.github.romanqed.jfunc.Exceptions;
import io.github.amayaframework.di.ServiceProvider;
import io.github.amayaframework.di.ServiceProviderBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.SecureRandom;

@ProviderConsumer
public final class EncoderServiceConsumer implements ServiceProviderConsumer {
    private static final File SALT_FILE = new File("salt.pbkdf2");
    private static final int SALT_LENGTH = 16;

    private static byte[] generateSalt() {
        var random = new SecureRandom();
        var ret = new byte[SALT_LENGTH];
        random.nextBytes(ret);
        return ret;
    }

    private static byte[] readSalt() throws IOException {
        var stream = new FileInputStream(SALT_FILE);
        var ret = new byte[SALT_LENGTH];
        if (stream.read(ret, 0, SALT_LENGTH) != SALT_LENGTH) {
            throw new IllegalArgumentException("Invalid salt cache");
        }
        stream.close();
        return ret;
    }

    @Override
    public void pre(ServiceProviderBuilder builder) {
        var salt = (byte[]) null;
        if (!SALT_FILE.exists()) {
            var bytes = generateSalt();
            Exceptions.suppress(() -> Files.write(SALT_FILE.toPath(), bytes, StandardOpenOption.CREATE));
            salt = bytes;
        } else {
            salt = Exceptions.suppress(EncoderServiceConsumer::readSalt);
        }
        var encoder = new PBKDF2Encoder(salt);
        builder.addService(Encoder.class, () -> encoder);
    }

    @Override
    public void post(ServiceProvider provider) {
    }
}
