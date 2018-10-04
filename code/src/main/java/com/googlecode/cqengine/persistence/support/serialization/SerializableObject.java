package com.googlecode.cqengine.persistence.support.serialization;


import java.io.Serializable;

/**
 * 
 * @author Ildefonso Montero Pérez <ildefonso.montero@gmail.com>
 *
 */
@PersistenceConfig(polymorphic = false, serializer = DefaultSerializer.class)
public class SerializableObject implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String name;
	
	private byte[] content;
	
	protected SerializableObject() {}
	
	private SerializableObject(Builder builder) {
		this.name = builder.name;
		this.content = builder.content;
	}
	
	/**
	 * Creates builder to build {@link SerializableObject}.
	 * 
	 * @return created builder
	 */
	public static Builder builder() {
		return new Builder();
	}
	
	public String getName() {
		return name;
	}
	
	public byte[] getContent() {
		return content;
	}
	
	/**
	 * Builder to build {@link SerializableObject}.
	 */
	public static final class Builder {
		
		private String name;
		
		private byte[] content;
		
		private Builder() {}
		
		public Builder withName(String name) {
			this.name = name;
			return this;
		}
		
		public Builder withContent(byte[] content) {
			this.content = content;
			return this;
		}
		
		public SerializableObject build() {
			return new SerializableObject(this);
		}
	}
	
	
}
