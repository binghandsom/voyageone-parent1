package com.voyageone.base.dao.mongodb.test.dao.support;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.*;

import static org.springframework.data.mongodb.core.query.Criteria.where;


@NoRepositoryBean
public class BaseMongoDao<T, ID extends Serializable> extends SimpleMongoRepository<T, ID> {
    // mongoTemplate
    protected MongoOperations mongoOperations;
    public MongoOperations getMongoOperations() {
        return mongoOperations;
    }
    // table Information
    protected MongoEntityInformation<T, ID> entityInformation;

    /**
     * Creates a new {@link SimpleMongoRepository} for the given {@link MongoEntityInformation} and {@link MongoTemplate}.
     *
     * @param metadata must not be {@literal null}.
     * @param mongoOperations must not be {@literal null}.
     */
    public BaseMongoDao(MongoEntityInformation<T, ID> metadata, MongoOperations mongoOperations) {
        super(metadata, mongoOperations);
        Assert.notNull(mongoOperations);
        Assert.notNull(metadata);

        this.entityInformation = metadata;
        this.mongoOperations = mongoOperations;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.CrudRepository#save(java.lang.Object)
     */
    public <S extends T> S save(S entity) {

        Assert.notNull(entity, "Entity must not be null!");

        if (entityInformation.isNew(entity)) {
            mongoOperations.insert(entity, entityInformation.getCollectionName());
        } else {
            mongoOperations.save(entity, entityInformation.getCollectionName());
        }

        return entity;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.CrudRepository#save(java.lang.Iterable)
     */
    public <S extends T> List<S> save(Iterable<S> entities) {

        Assert.notNull(entities, "The given Iterable of entities not be null!");

        List<S> result = convertIterableToList(entities);
        boolean allNew = true;

        for (S entity : entities) {
            if (allNew && !entityInformation.isNew(entity)) {
                allNew = false;
            }
        }

        if (allNew) {
            mongoOperations.insertAll(result);
        } else {

            for (S entity : result) {
                save(entity);
            }
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.CrudRepository#findOne(java.io.Serializable)
     */
    public T findOne(ID id) {
        Assert.notNull(id, "The given id must not be null!");
        return mongoOperations.findById(id, entityInformation.getJavaType(), entityInformation.getCollectionName());
    }

    private Query getIdQuery(Object id) {
        return new Query(getIdCriteria(id));
    }

    private Criteria getIdCriteria(Object id) {
        return where(entityInformation.getIdAttribute()).is(id);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.CrudRepository#exists(java.io.Serializable)
     */
    public boolean exists(ID id) {

        Assert.notNull(id, "The given id must not be null!");
        return mongoOperations.exists(getIdQuery(id), entityInformation.getJavaType(),
                entityInformation.getCollectionName());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.CrudRepository#count()
     */
    public long count() {
        return mongoOperations.getCollection(entityInformation.getCollectionName()).count();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.CrudRepository#delete(java.io.Serializable)
     */
    public void delete(ID id) {
        Assert.notNull(id, "The given id must not be null!");
        mongoOperations.remove(getIdQuery(id), entityInformation.getJavaType(), entityInformation.getCollectionName());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.CrudRepository#delete(java.lang.Object)
     */
    public void delete(T entity) {
        Assert.notNull(entity, "The given entity must not be null!");
        delete(entityInformation.getId(entity));
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.CrudRepository#delete(java.lang.Iterable)
     */
    public void delete(Iterable<? extends T> entities) {

        Assert.notNull(entities, "The given Iterable of entities not be null!");

        for (T entity : entities) {
            delete(entity);
        }
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.CrudRepository#deleteAll()
     */
    public void deleteAll() {
        mongoOperations.remove(new Query(), entityInformation.getCollectionName());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.CrudRepository#findAll()
     */
    public List<T> findAll() {
        return findAll(new Query());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.CrudRepository#findAll(java.lang.Iterable)
     */
    public Iterable<T> findAll(Iterable<ID> ids) {

        Set<ID> parameters = new HashSet<ID>(tryDetermineRealSizeOrReturn(ids, 10));
        for (ID id : ids) {
            parameters.add(id);
        }

        return findAll(new Query(new Criteria(entityInformation.getIdAttribute()).in(parameters)));
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.PagingAndSortingRepository#findAll(org.springframework.data.domain.Pageable)
     */
    public Page<T> findAll(final Pageable pageable) {

        Long count = count();
        List<T> list = findAll(new Query().with(pageable));

        return new PageImpl<T>(list, pageable, count);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.PagingAndSortingRepository#findAll(org.springframework.data.domain.Sort)
     */
    public List<T> findAll(Sort sort) {
        return findAll(new Query().with(sort));
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.mongodb.repository.MongoRepository#insert(java.lang.Object)
     */
    @Override
    public <S extends T> S insert(S entity) {

        Assert.notNull(entity, "Entity must not be null!");

        mongoOperations.insert(entity, entityInformation.getCollectionName());
        return entity;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.mongodb.repository.MongoRepository#insert(java.lang.Iterable)
     */
    @Override
    public <S extends T> List<S> insert(Iterable<S> entities) {

        Assert.notNull(entities, "The given Iterable of entities not be null!");

        List<S> list = convertIterableToList(entities);

        if (list.isEmpty()) {
            return list;
        }

        mongoOperations.insertAll(list);
        return list;
    }

    private List<T> findAll(Query query) {

        if (query == null) {
            return Collections.emptyList();
        }

        return mongoOperations.find(query, entityInformation.getJavaType(), entityInformation.getCollectionName());
    }

    private static <T> List<T> convertIterableToList(Iterable<T> entities) {

        if (entities instanceof List) {
            return (List<T>) entities;
        }

        int capacity = tryDetermineRealSizeOrReturn(entities, 10);

        if (capacity == 0 || entities == null) {
            return Collections.<T> emptyList();
        }

        List<T> list = new ArrayList<T>(capacity);
        for (T entity : entities) {
            list.add(entity);
        }

        return list;
    }

    private static int tryDetermineRealSizeOrReturn(Iterable<?> iterable, int defaultSize) {
        return iterable == null ? 0 : (iterable instanceof Collection) ? ((Collection<?>) iterable).size() : defaultSize;
    }
}
