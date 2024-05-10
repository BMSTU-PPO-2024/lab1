package com.github.romanqed.devspark.di;

import io.github.amayaframework.di.ServiceProvider;
import io.github.amayaframework.di.ServiceProviderBuilder;

public interface ServiceProviderConsumer {

    void pre(ServiceProviderBuilder builder);

    void post(ServiceProvider provider);
}
