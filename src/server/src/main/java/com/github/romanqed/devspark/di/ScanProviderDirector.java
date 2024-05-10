package com.github.romanqed.devspark.di;

import com.github.romanqed.jfunc.Exceptions;
import io.github.amayaframework.di.ServiceProvider;
import io.github.amayaframework.di.ServiceProviderBuilder;
import org.atteo.classindex.ClassIndex;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public final class ScanProviderDirector implements ServiceProviderDirector {
    private ServiceProviderBuilder builder;

    public ScanProviderDirector() {
        this.reset();
    }

    private static List<ServiceProviderConsumer> findAnnotatedConsumers() {
        var found = ClassIndex.getAnnotated(ProviderConsumer.class);
        var ret = new LinkedList<ServiceProviderConsumer>();
        for (var clazz : found) {
            if (!ServiceProviderConsumer.class.isAssignableFrom(clazz)) {
                throw new IllegalStateException("Invalid ServiceProviderConsumer implementation: " + clazz);
            }
            var object = (ServiceProviderConsumer) Exceptions.suppress(() -> clazz.getConstructor().newInstance());
            ret.add(object);
        }
        return ret;
    }

    private void reset() {
        this.builder = null;
    }

    @Override
    public void setBuilder(ServiceProviderBuilder builder) {
        this.builder = Objects.requireNonNull(builder);
    }

    private ServiceProvider innerBuild() {
        Objects.requireNonNull(builder);
        var consumers = findAnnotatedConsumers();
        for (var consumer : consumers) {
            consumer.pre(builder);
        }
        var ret = builder.build();
        for (var consumer : consumers) {
            consumer.post(ret);
        }
        return ret;
    }

    @Override
    public ServiceProvider build() {
        try {
            var ret = innerBuild();
            this.reset();
            return ret;
        } catch (Error | RuntimeException e) {
            this.reset();
            throw e;
        }
    }
}
