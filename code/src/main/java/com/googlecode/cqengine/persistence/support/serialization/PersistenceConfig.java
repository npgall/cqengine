package com.googlecode.cqengine.persistence.support.serialization;

import java.lang.annotation.*;

/**
 * @author npgall
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PersistenceConfig {

    Class<? extends PojoSerializer> serializer() default KryoSerializer.class;

    boolean polymorphic() default false;

    static PersistenceConfig DEFAULT_CONFIG = new PersistenceConfig() {

        @Override
        public Class<? extends Annotation> annotationType() {
            return PersistenceConfig.class;
        }

        @Override
        public Class<? extends PojoSerializer> serializer() {
            return KryoSerializer.class;
        }

        @Override
        public boolean polymorphic() {
            return false;
        }
    };
}
