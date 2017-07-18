package com.googlecode.cqengine.persistence.support.serialization;

/**
 * Interface implemented by serializers.
 * The serializer for a particular object can be configured via the {@link PersistenceConfig} annotation.
 * <p>
 *     Implementations of this interface are expected to provide a constructor which takes two arguments:
 *     (Class objectType, PersistenceConfig persistenceConfig).
 * </p>
 *
 * @author npgall
 */
public interface PojoSerializer<O> {

    byte[] serialize(O object);

    O deserialize(byte[] bytes);
}
