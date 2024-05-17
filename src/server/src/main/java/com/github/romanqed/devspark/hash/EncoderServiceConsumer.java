package com.github.romanqed.devspark.hash;

import com.github.romanqed.devspark.di.ProviderConsumer;
import com.github.romanqed.devspark.di.ServiceProviderConsumer;
import io.github.amayaframework.di.ServiceProvider;
import io.github.amayaframework.di.ServiceProviderBuilder;

@ProviderConsumer
public final class EncoderServiceConsumer implements ServiceProviderConsumer {

    @Override
    public void pre(ServiceProviderBuilder builder) {
        builder.addSingleton(Encoder.class, PBKDF2Encoder.class);
    }

    @Override
    public void post(ServiceProvider provider) {
    }
}
