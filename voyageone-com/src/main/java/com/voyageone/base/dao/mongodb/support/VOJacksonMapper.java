package com.voyageone.base.dao.mongodb.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jongo.Mapper;
import org.jongo.ObjectIdUpdater;
import org.jongo.ReflectiveObjectIdUpdater;
import org.jongo.marshall.Marshaller;
import org.jongo.marshall.Unmarshaller;
import org.jongo.marshall.jackson.JacksonEngine;
import org.jongo.marshall.jackson.JacksonIdFieldSelector;
import org.jongo.marshall.jackson.configuration.AbstractMappingBuilder;
import org.jongo.query.QueryFactory;


public class VOJacksonMapper implements Mapper {
    private final JacksonEngine engine;
    private final ObjectIdUpdater objectIdUpdater;
    private final QueryFactory queryFactory;

    private VOJacksonMapper(JacksonEngine engine, QueryFactory queryFactory, ObjectIdUpdater objectIdUpdater) {
        this.engine = engine;
        this.queryFactory = queryFactory;
        this.objectIdUpdater = objectIdUpdater;
    }

    public Marshaller getMarshaller() {
        return engine;
    }

    public Unmarshaller getUnmarshaller() {
        return engine;
    }

    public ObjectIdUpdater getObjectIdUpdater() {
        return objectIdUpdater;
    }

    public QueryFactory getQueryFactory() {
        return queryFactory;
    }

    public static class Builder extends AbstractMappingBuilder<Builder> {

        private QueryFactory queryFactory;
        private ObjectIdUpdater objectIdUpdater;

        public Builder() {
            super();
        }

        public Builder(ObjectMapper mapper) {
            super(mapper);
        }

        public Mapper build() {
            JacksonEngine jacksonEngine = new JacksonEngine(createMapping());
            if (queryFactory == null) {
                queryFactory = new VOBsonQueryFactory(jacksonEngine);
            }
            if (objectIdUpdater == null) {
                objectIdUpdater = new ReflectiveObjectIdUpdater(new JacksonIdFieldSelector());
            }
            return new VOJacksonMapper(jacksonEngine, queryFactory, objectIdUpdater);
        }

        public Builder withQueryFactory(QueryFactory factory) {
            this.queryFactory = factory;
            return getBuilderInstance();
        }

        public Builder withObjectIdUpdater(ObjectIdUpdater objectIdUpdater) {
            this.objectIdUpdater = objectIdUpdater;
            return getBuilderInstance();
        }

        @Override
        protected Builder getBuilderInstance() {
            return this;
        }
    }
}
