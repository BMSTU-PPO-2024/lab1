package com.github.romanqed.devspark.hash;

import com.github.romanqed.devspark.di.ProviderConsumer;
import com.github.romanqed.devspark.di.ServiceProviderConsumer;
import com.github.romanqed.jfunc.Exceptions;
import io.github.amayaframework.di.ServiceProvider;
import io.github.amayaframework.di.ServiceProviderBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@ProviderConsumer
public final class EncoderServiceConsumer implements ServiceProviderConsumer {
    private static final File SALT_FILE = new File(System.getenv("SALT_FILE"));
    private static final int SALT_LENGTH = 16;

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
        var salt = Exceptions.suppress(EncoderServiceConsumer::readSalt);
        var encoder = new PBKDF2Encoder(salt);
        builder.addService(Encoder.class, () -> encoder);
    }

    @Override
    public void post(ServiceProvider provider) {
    }
}
