package com.github.romanqed.devspark.gson;

import com.github.romanqed.devspark.di.ProviderConsumer;
import com.github.romanqed.devspark.di.ServiceProviderConsumer;
import com.google.gson.Gson;
import io.github.amayaframework.di.ServiceProvider;
import io.github.amayaframework.di.ServiceProviderBuilder;
import io.javalin.json.JsonMapper;

@ProviderConsumer
public final class GsonServiceConsumer implements ServiceProviderConsumer {

    @Override
    public void pre(ServiceProviderBuilder builder) {
        var gson = new Gson();
        builder.addService(Gson.class, () -> gson);
        builder.addSingleton(JsonMapper.class, GsonMapper.class);
    }

    @Override
    public void post(ServiceProvider provider) {
    }
}
