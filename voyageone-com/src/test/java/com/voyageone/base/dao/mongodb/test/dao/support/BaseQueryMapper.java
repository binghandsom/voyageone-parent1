package com.voyageone.base.dao.mongodb.test.dao.support;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mapping.Association;
import org.springframework.data.mapping.PropertyPath;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mapping.context.PersistentPropertyPath;
import org.springframework.data.mapping.model.MappingException;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.convert.QueryMapper;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by DELL on 2015/10/29.
 */
public class BaseQueryMapper extends QueryMapper {

    private static final List<String> DEFAULT_ID_NAMES = Arrays.asList("id", "_id");
    static final ClassTypeInformation<?> NESTED_DOCUMENT = ClassTypeInformation.from(NestedDocument.class);

    public BaseQueryMapper(MongoConverter converter) {
        super(converter);
    }

    @Override
    protected Field createPropertyField(MongoPersistentEntity<?> entity, String key,
                                        MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mappingContext) {
        return entity == null ? new Field(key) : new BaseMetadataBackedField(key, entity, mappingContext);
    }

    protected static class BaseMetadataBackedField extends Field {

        private static final String INVALID_ASSOCIATION_REFERENCE = "Invalid path reference %s! Associations can only be pointed to directly or via their id property!";

        private final MongoPersistentEntity<?> entity;
        private final MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mappingContext;
        private final MongoPersistentProperty property;
        private final PersistentPropertyPath<MongoPersistentProperty> path;
        private final Association<MongoPersistentProperty> association;

        public BaseMetadataBackedField(String name, MongoPersistentEntity<?> entity,
                                   MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> context) {
            this(name, entity, context, null);
        }

        public BaseMetadataBackedField(String name, MongoPersistentEntity<?> entity,
                                   MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> context,
                                   MongoPersistentProperty property) {

            super(name);

            Assert.notNull(entity, "MongoPersistentEntity must not be null!");

            this.entity = entity;
            this.mappingContext = context;

            this.path = getPath(name);
            this.property = path == null ? property : path.getLeafProperty();
            this.association = findAssociation();
        }

        /*
         * (non-Javadoc)
         * @see org.springframework.data.mongodb.core.convert.QueryMapper.Field#with(java.lang.String)
         */
        @Override
        public MetadataBackedField with(String name) {
            return new MetadataBackedField(name, entity, mappingContext, property);
        }

        /*
         * (non-Javadoc)
         * @see org.springframework.data.mongodb.core.convert.QueryMapper.Field#isIdKey()
         */
        @Override
        public boolean isIdField() {

            MongoPersistentProperty idProperty = entity.getIdProperty();

            if (idProperty != null) {
                return idProperty.getName().equals(name) || idProperty.getFieldName().equals(name);
            }

            return DEFAULT_ID_NAMES.contains(name);
        }

        /*
         * (non-Javadoc)
         * @see org.springframework.data.mongodb.core.convert.QueryMapper.Field#getProperty()
         */
        @Override
        public MongoPersistentProperty getProperty() {
            return association == null ? property : association.getInverse();
        }

        /*
         * (non-Javadoc)
         * @see org.springframework.data.mongodb.core.convert.QueryMapper.Field#getEntity()
         */
        @Override
        public MongoPersistentEntity<?> getPropertyEntity() {
            MongoPersistentProperty property = getProperty();
            return property == null ? null : mappingContext.getPersistentEntity(property);
        }

        /*
         * (non-Javadoc)
         * @see org.springframework.data.mongodb.core.convert.QueryMapper.Field#isAssociation()
         */
        @Override
        public boolean isAssociation() {
            return association != null;
        }

        /*
         * (non-Javadoc)
         * @see org.springframework.data.mongodb.core.convert.QueryMapper.Field#getAssociation()
         */
        @Override
        public Association<MongoPersistentProperty> getAssociation() {
            return association;
        }

        /**
         * Finds the association property in the {@link PersistentPropertyPath}.
         *
         * @return
         */
        private final Association<MongoPersistentProperty> findAssociation() {

            if (this.path != null) {
                for (MongoPersistentProperty p : this.path) {
                    if (p.isAssociation()) {
                        return p.getAssociation();
                    }
                }
            }

            return null;
        }

        /*
         * (non-Javadoc)
         * @see org.springframework.data.mongodb.core.convert.QueryMapper.Field#getTargetKey()
         */
        @Override
        public String getMappedKey() {
            return path == null ? name : path.toDotPath(isAssociation() ? getAssociationConverter() : getPropertyConverter());
        }

        protected PersistentPropertyPath<MongoPersistentProperty> getPath() {
            return path;
        }

        /**
         * Returns the {@link PersistentPropertyPath} for the given <code>pathExpression</code>.
         *
         * @param pathExpression
         * @return
         */
        private PersistentPropertyPath<MongoPersistentProperty> getPath(String pathExpression) {

            try {

                PropertyPath path = BasePropertyPath.from(pathExpression.replaceAll("\\.\\d", ""), entity.getTypeInformation());
                PersistentPropertyPath<MongoPersistentProperty> propertyPath = mappingContext.getPersistentPropertyPath(path);

                Iterator<MongoPersistentProperty> iterator = propertyPath.iterator();
                boolean associationDetected = false;

                while (iterator.hasNext()) {

                    MongoPersistentProperty property = iterator.next();

                    if (property.isAssociation()) {
                        associationDetected = true;
                        continue;
                    }

                    if (associationDetected && !property.isIdProperty()) {
                        throw new MappingException(String.format(INVALID_ASSOCIATION_REFERENCE, pathExpression));
                    }
                }

                return propertyPath;
            } catch (PropertyReferenceException e) {
                return null;
            }
        }

        /**
         * Return the {@link Converter} to be used to created the mapped key. Default implementation will use
         * {@link MongoPersistentProperty.PropertyToFieldNameConverter}.
         *
         * @return
         */
        protected Converter<MongoPersistentProperty, String> getPropertyConverter() {
            return new PositionParameterRetainingPropertyKeyConverter(name);
        }

        /**
         * Return the {@link Converter} to use for creating the mapped key of an association. Default implementation is
         * {@link AssociationConverter}.
         *
         * @return
         * @since 1.7
         */
        protected Converter<MongoPersistentProperty, String> getAssociationConverter() {
            return new AssociationConverter(getAssociation());
        }

        /**
         * @author Christoph Strobl
         * @since 1.8
         */
        static class PositionParameterRetainingPropertyKeyConverter implements Converter<MongoPersistentProperty, String> {

            private final KeyMapper keyMapper;

            public PositionParameterRetainingPropertyKeyConverter(String rawKey) {
                this.keyMapper = new KeyMapper(rawKey);
            }

            /*
             * (non-Javadoc)
             * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
             */
            @Override
            public String convert(MongoPersistentProperty source) {
                return keyMapper.mapPropertyName(source);
            }
        }

        /*
         * (non-Javadoc)
         * @see org.springframework.data.mongodb.core.convert.QueryMapper.Field#getTypeHint()
         */
        @Override
        public TypeInformation<?> getTypeHint() {

            MongoPersistentProperty property = getProperty();

            if (property == null) {
                return super.getTypeHint();
            }

            if (property.getActualType().isInterface()
                    || java.lang.reflect.Modifier.isAbstract(property.getActualType().getModifiers())) {
                return ClassTypeInformation.OBJECT;
            }

            return NESTED_DOCUMENT;
        }

        /**
         * @author Christoph Strobl
         * @since 1.8
         */
        static class KeyMapper {

            private final Iterator<String> iterator;

            public KeyMapper(String key) {

                this.iterator = Arrays.asList(key.split("\\.")).iterator();
                this.iterator.next();
            }

            /**
             * Maps the property name while retaining potential positional operator {@literal $}.
             *
             * @param property
             * @return
             */
            protected String mapPropertyName(MongoPersistentProperty property) {

                String mappedName = MongoPersistentProperty.PropertyToFieldNameConverter.INSTANCE.convert(property);
                boolean inspect = iterator.hasNext();

                while (inspect) {

                    String partial = iterator.next();
                    boolean isPositional = (isPositionalParameter(partial) && (property.isMap() || property.isCollectionLike()));

                    if (isPositional) {
                        mappedName += "." + partial;
                    }

                    inspect = isPositional && iterator.hasNext();
                }

                return mappedName;
            }

            private static boolean isPositionalParameter(String partial) {

                if (partial.equals("$")) {
                    return true;
                }

                try {
                    Long.valueOf(partial);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        }
    }

    static class NestedDocument {

    }
}
