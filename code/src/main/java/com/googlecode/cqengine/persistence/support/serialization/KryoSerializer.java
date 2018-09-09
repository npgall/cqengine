package com.googlecode.cqengine.persistence.support.serialization;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.util.DefaultInstantiatorStrategy;
import de.javakaffee.kryoserializers.ArraysAsListSerializer;
import de.javakaffee.kryoserializers.SynchronizedCollectionsSerializer;
import de.javakaffee.kryoserializers.UnmodifiableCollectionsSerializer;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

/**
 * Uses <a href="https://github.com/EsotericSoftware/kryo">Kryo</a> to serialize and deserialize objects;
 * for use with CQEngine's disk and off-heap indexes and persistence.
 * <p/>
 * A {@link #validateObjectIsRoundTripSerializable(Object)} method is also provided, to validate
 * the compatibility of end-user POJOs with this serializer.
 *
 * @author npgall
 */
public class KryoSerializer<O> implements PojoSerializer<O> {

    protected final Class<O> objectType;
    protected final boolean polymorphic;

    protected final ThreadLocal<Kryo> kryoCache = new ThreadLocal<Kryo>() {
        @Override
        protected Kryo initialValue() {
            return createKryo(objectType);
        }
    };

    /**
     * Creates a new Kryo serializer which is configured to serialize objects of the given type.
     *
     * @param objectType The type of the object
     * @param persistenceConfig Configuration for the serializer, in particular the polymorphic parameter which
     *                          if true, causes Kryo to persist the name of the class with every object, to allow
     *                          the collection to contain a mix of object types within an inheritance hierarchy;
     *                          if false causes Kryo to skip persisting the name of the class and to assume all objects
     *                          in the collection will be instances of the same class.
     *
     */
    public KryoSerializer(Class<O> objectType, PersistenceConfig persistenceConfig) {
        this.objectType = objectType;
        this.polymorphic = persistenceConfig.polymorphic();
    }

    /**
     * Creates a new instance of Kryo serializer, for use with the given object type.
     * <p/>
     * Note: this method is public to allow end-users to validate compatibility of their POJOs,
     * with the Kryo serializer as used by CQEngine.
     *
     * @param objectType The type of object which the instance of Kryo will serialize
     * @return a new instance of Kryo serializer
     */
    @SuppressWarnings({"ArraysAsListWithZeroOrOneArgument", "WeakerAccess"})
    protected Kryo createKryo(Class<?> objectType) {
        Kryo kryo = new Kryo();
        // Instantiate serialized objects via a no-arg constructor when possible, falling back to Objenesis...
        kryo.setInstantiatorStrategy(new DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
        // Register the object which this index will persist...
        kryo.register(objectType);
        kryo.setRegistrationRequired(false);
        // Register additional serializers which are not built-in to Kryo 3.0...
        kryo.register(Arrays.asList().getClass(), new ArraysAsListSerializer());
        UnmodifiableCollectionsSerializer.registerSerializers(kryo);
        SynchronizedCollectionsSerializer.registerSerializers(kryo);
        return kryo;
    }

    /**
     * Serializes the given object, using the given instance of Kryo serializer.
     *
     * @param object The object to serialize
     * @return The serialized form of the object as a byte array
     */
    @Override
    public byte[] serialize(O object) {
        if (object == null) {
            throw new NullPointerException("Object was null");
        }
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Output output = new Output(baos);
            Kryo kryo = kryoCache.get();
            if (polymorphic) {
                kryo.writeClassAndObject(output, object);
            }
            else {
                kryo.writeObject(output, object);
            }
            output.close();
            return baos.toByteArray();
        }
        catch (Exception e) {
            throw new IllegalStateException("Failed to serialize object, object type: " + objectType + ". " +
                    "Configure @PersistenceConfig.polymorphic if the collection will contain a mix of object types. " +
                    "Use the KryoSerializer.validateObjectIsRoundTripSerializable() method " +
                    "to test your object is compatible with CQEngine.", e);
        }
    }

    /**
     * Deserializes the given bytes, into an object of the given type, using the given instance of Kryo serializer.
     *
     * @param bytes The serialized form of the object as a byte array
     * @return The deserialized object
     */
    @Override
    @SuppressWarnings("unchecked")
    public O deserialize(byte[] bytes) {
        try {
            Input input = new Input(new ByteArrayInputStream(bytes));
            Kryo kryo = kryoCache.get();
            O object;
            if (polymorphic) {
                object = (O) kryo.readClassAndObject(input);
            }
            else {
                object = kryo.readObject(input, objectType);
            }
            input.close();
            return object;
        }
        catch (Exception e) {
            throw new IllegalStateException("Failed to deserialize object, object type: " + objectType + ". " +
                    "Configure @PersistenceConfig.polymorphic if the collection will contain a mix of object types. " +
                    "Use the KryoSerializer.validateObjectIsRoundTripSerializable() method " +
                    "to test your object is compatible with CQEngine.", e);
        }
    }

    /**
     * Performs sanity tests on the given POJO object, to check if it can be serialized and deserialized with Kryo
     * serialzier as used by CQEngine.
     * <p/>
     * If a POJO fails this test, then it typically means CQEngine will be unable to serialize or deserialize
     * it, and thus the POJO can't be used with CQEngine's off-heap or disk indexes or persistence.
     * <p/>
     * Failing the test typically means the data structures or data types within the POJO are too complex. Simplifying
     * the POJO will usually improve compatibility.
     * <p/>
     * This method will return normally if the POJO passes the tests, or will throw an exception if it fails.
     *
     * @param candidatePojo The POJO to test
     */
    @SuppressWarnings("unchecked")
    public static <O> void validateObjectIsRoundTripSerializable(O candidatePojo) {
        Class<O> objectType = (Class<O>) candidatePojo.getClass();
        KryoSerializer.validateObjectIsRoundTripSerializable(candidatePojo, objectType, PersistenceConfig.DEFAULT_CONFIG);
    }

    static <O> void validateObjectIsRoundTripSerializable(O candidatePojo, Class<O> objectType, PersistenceConfig persistenceConfig) {
        try {
            KryoSerializer<O> serializer = new KryoSerializer<O>(
                    objectType,
                    persistenceConfig
            );
            byte[] serialized = serializer.serialize(candidatePojo);
            O deserializedPojo = serializer.deserialize(serialized);
            serializer.kryoCache.remove();  // clear cached Kryo instance
            validateObjectEquality(candidatePojo, deserializedPojo);
            validateHashCodeEquality(candidatePojo, deserializedPojo);
        }
        catch (Exception e) {
            throw new IllegalStateException("POJO object failed round trip serialization-deserialization test, object type: " + objectType + ", object: " + candidatePojo, e);
        }
    }

    static void validateObjectEquality(Object candidate, Object deserializedPojo) {
        if (!(deserializedPojo.equals(candidate))) {
            throw new IllegalStateException("The POJO after round trip serialization is not equal to the original POJO");
        }
    }

    static void validateHashCodeEquality(Object candidate, Object deserializedPojo) {
        if (!(deserializedPojo.hashCode() == candidate.hashCode())) {
            throw new IllegalStateException("The POJO's hashCode after round trip serialization differs from its original hashCode");
        }
    }
}
