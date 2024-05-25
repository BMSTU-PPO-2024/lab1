package com.github.romanqed.devspark.mongo;

import com.github.romanqed.devspark.database.Pagination;
import com.github.romanqed.devspark.database.Repository;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import org.bson.conversions.Bson;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;

final class MongoRepository<V> implements Repository<V> {
    private final MongoCollection<V> collection;

    MongoRepository(MongoCollection<V> collection) {
        this.collection = collection;
    }

    private static List<Bson> asList(Map<String, ?> fields) {
        return fields
                .entrySet()
                .stream()
                .map(entry -> Filters.eq(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean put(V model) {
        return collection.insertOne(model).wasAcknowledged();
    }

    @Override
    public boolean update(String key, V model) {
        return collection.replaceOne(eq("_id", key), model).getModifiedCount() == 1;
    }

    @Override
    public long put(Iterable<V> entities) {
        var ret = 0L;
        for (var entity : entities) {
            ret += put(entity) ? 1 : 0;
        }
        return ret;
    }

    @Override
    public V get(String key) {
        return collection
                .find(eq(key))
                .first();
    }

    public V get(String key, List<String> fields) {
        return collection
                .find(eq(key))
                .projection(Projections.include(fields))
                .first();
    }

    @Override
    public V findFirstByField(String field, Object value) {
        return collection.find(Filters.eq(field, value)).first();
    }

    @Override
    public Iterable<V> findByField(String field, Object value, List<String> fields) {
        return collection
                .find(Filters.eq(field, value))
                .projection(Projections.include(fields));
    }

    @Override
    public Iterable<V> getAll() {
        return collection.find();
    }

    @Override
    public Iterable<V> getAll(Iterable<String> keys) {
        return collection.find(Filters.in("_id", keys));
    }

    @Override
    public boolean exists(String id) {
        return collection.countDocuments(Filters.eq("_id", id)) == 1;
    }

    @Override
    public boolean exists(Collection<String> ids) {
        if (ids.isEmpty()) {
            return true;
        }
        return collection.countDocuments(Filters.in("_id", ids)) == ids.size();
    }

    @Override
    public boolean exists(Collection<String> ids, String field, Object value) {
        if (ids.isEmpty()) {
            return true;
        }
        var filter = Filters.and(
                Filters.in("_id", ids),
                Filters.eq(field, value)
        );
        return collection.countDocuments(filter) == ids.size();
    }

    @Override
    public boolean exists(String field, Object value) {
        return collection.countDocuments(Filters.eq(field, value)) == 1;
    }

    @Override
    public boolean exists(String field, Iterable<Object> value) {
        return collection.countDocuments(Filters.in(field, value)) != 0;
    }

    @Override
    public boolean exists(Map<String, Object> fields) {
        var filters = asList(fields);
        return collection.countDocuments(Filters.and(filters)) != 0;
    }

    @Override
    public boolean delete(String key) {
        return collection.deleteOne(eq(key)).getDeletedCount() == 1;
    }

    @Override
    public boolean delete(Iterable<String> keys) {
        return collection.deleteOne(Filters.in("_id", keys)).getDeletedCount() == 1;
    }

    @Override
    public boolean delete(String field, Object value) {
        return collection.deleteOne(Filters.eq(field, value)).getDeletedCount() == 1;
    }

    @Override
    public boolean delete(String field, Iterable<Object> value) {
        return collection.deleteOne(Filters.in(field, value)).getDeletedCount() == 1;
    }

    @Override
    public boolean delete(Map<String, Object> fields) {
        var filters = asList(fields);
        return collection.deleteOne(Filters.and(filters)).getDeletedCount() == 1;
    }

    @Override
    public long deleteAll(Iterable<String> keys) {
        return collection.deleteMany(Filters.in("_id", keys)).getDeletedCount();
    }

    @Override
    public long deleteAll(String field, Object value) {
        return collection.deleteMany(Filters.eq(field, value)).getDeletedCount();
    }

    @Override
    public boolean deleteAll(String field, Collection<?> values) {
        return collection.deleteMany(Filters.in(field, values)).getDeletedCount() == values.size();
    }

    // Batched
    @Override
    public Iterable<V> getAll(Pagination pagination) {
        var batch = pagination.getBatch();
        var page = pagination.getPage();
        return collection
                .find()
                .skip(batch * (page - 1))
                .limit(batch);
    }

    @Override
    public Iterable<V> getAll(Iterable<String> keys, Pagination pagination) {
        var batch = pagination.getBatch();
        var page = pagination.getPage();
        return collection
                .find(Filters.in("_id", keys))
                .skip(batch * (page - 1))
                .limit(batch);
    }

    @Override
    public Iterable<V> findByField(String field, Object value, Pagination pagination) {
        var batch = pagination.getBatch();
        var page = pagination.getPage();
        return collection
                .find(Filters.eq(field, value))
                .skip(batch * (page - 1))
                .limit(batch);
    }

    @Override
    public Iterable<V> findByField(Map<String, Iterable<?>> in, Map<String, ?> eq, Pagination pagination) {
        var ins = in
                .entrySet()
                .stream()
                .map(entry -> Filters.in(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        var eqs = asList(eq);
        var filters = new LinkedList<Bson>();
        filters.addAll(ins);
        filters.addAll(eqs);
        var batch = pagination.getBatch();
        var page = pagination.getPage();
        return collection
                .find(Filters.and(filters))
                .skip(batch * (page - 1))
                .limit(batch);
    }

    @Override
    public Iterable<V> findAnd(Map<String, Object> fields, Pagination pagination) {
        var batch = pagination.getBatch();
        var page = pagination.getPage();
        var filters = asList(fields);
        return collection
                .find(Filters.and(filters))
                .skip(batch * (page - 1))
                .limit(batch);
    }

    @Override
    public Iterable<V> findOr(Map<String, Object> fields1, Map<String, Object> fields2, Pagination pagination) {
        var batch = pagination.getBatch();
        var page = pagination.getPage();
        var filters1 = asList(fields1);
        var filters2 = asList(fields2);
        return collection
                .find(Filters.or(Filters.and(filters1), Filters.and(filters2)))
                .skip(batch * (page - 1))
                .limit(batch);
    }

    @Override
    public Iterable<V> findMatched(String field, Pattern pattern, Pagination pagination) {
        var batch = pagination.getBatch();
        var page = pagination.getPage();
        return collection
                .find(Filters.regex(field, pattern))
                .skip(batch * (page - 1))
                .limit(batch);
    }

    @Override
    public Iterable<V> findMatchedWithFields(String field,
                                             Pattern pattern,
                                             Map<String, Object> fields,
                                             Pagination pagination) {
        var filters = asList(fields);
        filters.add(Filters.regex(field, pattern));
        var batch = pagination.getBatch();
        var page = pagination.getPage();
        return collection
                .find(Filters.and(filters))
                .skip(batch * (page - 1))
                .limit(batch);
    }

    @Override
    public Iterable<V> findMatchedWithFields(String field,
                                             Pattern pattern,
                                             Map<String, Object> fields1,
                                             Map<String, Object> fields2,
                                             Pagination pagination) {
        var filters1 = asList(fields1);
        var filters2 = asList(fields2);
        var regex = Filters.regex(field, pattern);
        var batch = pagination.getBatch();
        var page = pagination.getPage();
        return collection
                .find(Filters.and(regex, Filters.or(Filters.and(filters1), Filters.and(filters2))))
                .skip(batch * (page - 1))
                .limit(batch);
    }
}
