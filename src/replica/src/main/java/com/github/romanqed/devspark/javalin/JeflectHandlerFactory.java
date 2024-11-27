package com.github.romanqed.devspark.javalin;

import com.github.romanqed.jeflect.meta.LambdaType;
import com.github.romanqed.jeflect.meta.MetaFactory;
import io.javalin.http.Handler;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public final class JeflectHandlerFactory implements HandlerFactory {
    private static final MetaFactory META_FACTORY = new MetaFactory(MethodHandles.lookup());
    private static final LambdaType<Handler> HANDLER_TYPE = LambdaType.fromClass(Handler.class);

    @Override
    public Map<HandlerData, Handler> create(Object object) {
        var ret = new HashMap<HandlerData, Handler>();
        var clazz = object.getClass();
        var prefix = clazz.getAnnotation(JavalinController.class).value();
        for (var method : clazz.getMethods()) {
            var route = method.getAnnotation(Route.class);
            if (route == null) {
                continue;
            }
            var data = new HandlerData("/api/v1" + prefix + route.route(), route.method());
            var handler = process(method, object);
            if (ret.containsKey(data)) {
                throw new IllegalStateException("Duplicate route found");
            }
            ret.put(data, handler);
        }
        return ret;
    }

    private Handler process(Method method, Object object) {
        object = Modifier.isStatic(method.getModifiers()) ? null : object;
        try {
            return META_FACTORY.packLambdaMethod(HANDLER_TYPE, method, object);
        } catch (Throwable e) {
            throw new IllegalStateException("Cannot pack method due to", e);
        }
    }
}
