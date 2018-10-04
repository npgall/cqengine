package com.googlecode.cqengine.persistence.support.serialization;

/**
 * 
 * @author Ildefonso Montero Pérez <ildefonso.montero@gmail.com>
 *
 */
public class DefaultSerializer<T extends SerializableObject> implements PojoSerializer<T> {
	
	private KryoSerializer<SerializableObject> serializer;
	
	public DefaultSerializer(Class<SerializableObject> objectType, PersistenceConfig persistenceConfig) {
		this.serializer = new KryoSerializer<>(objectType, persistenceConfig);
	}
	
	@Override
	public byte[] serialize(T model) {
		
		String name = model.getClass().getName();
		byte[] content = serializer.serialize(model);
		
		return serializer.serialize(
				SerializableObject.builder()
						.withName(name)
						.withContent(content)
						.build());
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T deserialize(byte[] bytes) {
		SerializableObject read = serializer.deserialize(bytes);
		return (T) serializer.deserialize(read.getContent());
	}
	
}
