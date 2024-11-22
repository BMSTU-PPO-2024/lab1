package com.github.romanqed.devspark;

import com.github.romanqed.devspark.javalin.JavalinController;
import com.github.romanqed.devspark.javalin.Route;
import io.javalin.http.HandlerType;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.atteo.classindex.ClassIndex;
import org.objectweb.asm.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Scan {

    private static Map<String, Schema> extractFields(Class<?> c) {
        var ret = new HashMap<String, Schema>();
        for (var field : c.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            var s = new Schema<>();
            var t = field.getType();
            if (t == Set.class || t == List.class) {
                s.type("array");
            } else if (t != Integer.class
                    && t != String.class
                    && t != Boolean.class
                    && t != Object.class
            ) {
                s.type("string");
            } else {
                s.type(t.getSimpleName().toLowerCase(Locale.ENGLISH));
            }
            ret.put(field.getName(), s);
        }
        if (ret.isEmpty()) {
            return null;
        }
        return ret;
    }

    private static Schema extractSchema(Return r) {
        var ret = new Schema();
        var t = r.value();
        var s = r.sub();
        if (s == void.class) {
            ret.type("object");
            var f = extractFields(t);
            if (f == null) {
                return null;
            }
            ret.properties(f);
            return ret;
        }
        ret.type("array");
        var schema = new Schema();
        var f = extractFields(s);
        if (f == null) {
            return null;
        }
        schema.properties(f);
        ret.items(schema);
        return ret;
    }

    private static Content extractResponseBody(Method method) {
        var r = method.getAnnotation(Return.class);
        if (r == null) {
            return null;
        }
        var schema = extractSchema(r);
        if (schema == null) {
            return null;
        }
        var ret = new Content();
        var mt = new MediaType();
        mt.schema(schema);
        ret.addMediaType("application/json", mt);
        return ret;
    }

    public static void main(String[] args) throws IOException {
        var found = ClassIndex.getAnnotated(JavalinController.class);
        var cl = Thread.currentThread().getContextClassLoader();
        var pathes = new HashMap<String, PathItem>();
        for (var c : found) {
            scanController(c, new RouteHandler() {
                HandlerType type;
                String route;
                List<Parameter> params;
                RequestBody body;
                Content rspBody;

                @Override
                public void handle(Method m, HandlerType type, String route) {
                    this.type = type;
                    this.route = route;
                    this.params = new LinkedList<>();
                    this.rspBody = extractResponseBody(m);
                }

                @Override
                public void handlePathParam(String param, Type type) {
                    var parameter = new Parameter();
                    parameter.name(param);
                    parameter.in("path");
                    parameter.description("description");
                    parameter.required(true);
                    var schema = new Schema<>();
                    schema.type("string");
                    parameter.schema(schema);
                    params.add(parameter);
                }

                @Override
                public void handleQueryParam(String param, Type type) {
                    var parameter = new Parameter();
                    parameter.name(param);
                    parameter.in("query");
                    parameter.description("description");
                    parameter.required(false);
                    var schema = new Schema<>();
                    schema.type("string");
                    parameter.schema(schema);
                    params.add(parameter);
                }

                @Override
                public void handleBody(Type type) throws Exception {
                    body = new RequestBody();
                    body.setDescription("description");
                    var content = new Content();
                    var mt = new MediaType();
                    var schema = new Schema<>();
                    schema.type("object");
                    var map = new HashMap<String, Schema>();
                    var c = Class.forName(type.getClassName(), false, cl);
                    for (var field : c.getDeclaredFields()) {
                        if (Modifier.isStatic(field.getModifiers())) {
                            continue;
                        }
                        var s = new Schema<>();
                        var t = field.getType();
                        if (t == Set.class || t == List.class) {
                            s.type("array");
                        } else if (t != Integer.class
                                && t != String.class
                                && t != Boolean.class
                                && t != Object.class
                        ) {
                            s.type("string");
                        } else {
                            s.type(t.getSimpleName().toLowerCase(Locale.ENGLISH));
                        }
                        map.put(field.getName(), s);
                    }
                    schema.properties(map);
                    mt.schema(schema);
                    content.addMediaType("application/json", mt);
                    body.required(true);
                    body.content(content);
                }

                @Override
                public void commit() {
                    var item = pathes.computeIfAbsent(route, k -> new PathItem());
                    var operation = new Operation();
                    operation.setParameters(params);
                    operation.requestBody(body);
                    params = null;
                    body = null;
                    var rsp = new ApiResponses();
                    var r = new ApiResponse();
                    r.description("Success");
                    r.content(rspBody);
                    rspBody = null;
                    rsp.put("200", r);
                    operation.responses(rsp);
                    switch (type) {
                        case GET:
                            item.setGet(operation);
                            break;
                        case POST:
                            item.setPost(operation);
                            break;
                        case PUT:
                            item.setPut(operation);
                            break;
                        case DELETE:
                            item.setDelete(operation);
                            break;
                        case PATCH:
                            item.setPatch(operation);
                            break;
                        default:
                            throw new RuntimeException("UNHANDLED TYPE: " + type);
                    }
                }
            });
        }
        var openAPI = new OpenAPI();
        pathes.forEach(openAPI::path);
        var writer = Files.newBufferedWriter(Path.of("1.yaml"));
        Yaml.pretty().writeValue(writer, openAPI);
        writer.close();
    }

    static void scanController(Class<?> c, RouteHandler handler) throws IOException {
        var parent = c.getAnnotation(JavalinController.class).value();
        var methods = Arrays.stream(c.getMethods())
                .filter(m -> m.isAnnotationPresent(Route.class))
                .collect(Collectors.toList());
        for (var method : methods) {
            scanRouteMethod(parent, method, handler);
        }
    }

    interface RouteHandler {

        void handle(Method m, HandlerType type, String route);

        void handlePathParam(String param, Type type);

        void handleQueryParam(String param, Type type);

        void handleBody(Type type) throws Exception;

        void commit();
    }

    static void scanRouteMethod(String prev, Method m, RouteHandler handler) throws IOException {
        var route = m.getAnnotation(Route.class);
        handler.handle(m, route.method(), prev + route.route());
        walk(m, new Handler() {
            final LinkedList<Object> l = new LinkedList<>();

            @Override
            public void handle(Object o) {
                l.addFirst(o);
            }

            @Override
            public void handle(String mt, String owner, String name, String descriptor) throws Exception {
                if (owner.startsWith("com/github/romanqed/devspark")) {
                    walk(owner, name, descriptor, this);
                    return;
                }
                if (!owner.equals("io/javalin/http/Context")) {
                    return;
                }
                // Path param
                if (name.equals("pathParamAsClass")) {
                    var t = (Type) l.pollFirst();
                    var p = (String) l.pollFirst();
                    handler.handlePathParam(p, t);
                }
                if (name.equals("pathParam")) {
                    handler.handlePathParam((String) l.pollFirst(), null);
                }
                // Query param
                if (name.equals("queryParam")) {
                    handler.handleQueryParam((String) l.pollFirst(), null);
                }
                // Body
                if (name.equals("bodyAsClass")) {
                    handler.handleBody((Type) l.pollFirst());
                }
            }
        });
        handler.commit();
    }

    static byte[] readClass(String name) throws IOException {
        var loader = Thread.currentThread().getContextClassLoader();
        var resource = name.replace('.', '/') + ".class";
        var stream = loader.getResourceAsStream(resource);
        Objects.requireNonNull(stream);
        var ret = stream.readAllBytes();
        stream.close();
        return ret;
    }

    interface Handler {

        void handle(Object o) throws Exception;

        void handle(String mt, String owner, String name, String descriptor) throws Exception;
    }

    static void walk(Method m, Handler h) throws IOException {
        walk(m.getDeclaringClass().getName(), m.getName(), Type.getMethodDescriptor(m), h);
    }

    static void walk(String clazz, String method, String desc, Handler handler) throws IOException {
        var bytes = readClass(clazz);
        var reader = new ClassReader(bytes);
        var visitor = new ClassVisitor(Opcodes.ASM8) {

            @Override
            public MethodVisitor visitMethod(int access, String name, String d, String signature, String[] exceptions) {
                var ret = super.visitMethod(access, name, d, signature, exceptions);
                if (!name.equals(method) || !d.equals(desc)) {
                    return ret;
                }
                return new MethodVisitor(Opcodes.ASM8, ret) {

                    @Override
                    public void visitLdcInsn(Object value) {
                        try {
                            handler.handle(value);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        super.visitLdcInsn(value);
                    }

                    @Override
                    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
                        try {
                            handler.handle(clazz + method + d, owner, name, descriptor);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                    }
                };
            }
        };
        reader.accept(visitor, ClassReader.SKIP_FRAMES);
    }
}
