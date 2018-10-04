package com.googlecode.cqengine.persistence.support.serialization;

import org.junit.Test;

/**
 * Unit tests for {@link DefaultSerializer}
 * 
 * @author Ildefonso Montero Pérez <ildefonso.montero@gmail.com>
 *
 */
public class DefaultSerializerTest {
	
	private static final String ONE_MORE_ID = "one-more-id";
	
	private static final String OTHER_ID = "other-id";
	
	private static final String ID = "id";
	
	@Test
	public void whenObjectExtendsSerializableObject_thenItIsSerialized() {
		
		DummyObject obj = new DummyObject();
		obj.setId(ID);
		obj.setOtherId(OTHER_ID);
		obj.setOneMoreId(ONE_MORE_ID);
		
	}

	/**
	 * Internal class to define a dummy serializable object
	 * 
	 * @author Ildefonso Montero Pérez <ildefonso.montero@gmail.com>
	 *
	 */
	class DummyObject extends SerializableObject {
		
		private static final long serialVersionUID = 1L;
		
		String id;
		
		String otherId;
		
		String oneMoreId;
		
		public String getId() {
			return id;
		}
		
		public void setId(String id) {
			this.id = id;
		}
		
		public String getOtherId() {
			return otherId;
		}
		
		public void setOtherId(String otherId) {
			this.otherId = otherId;
		}
		
		public String getOneMoreId() {
			return oneMoreId;
		}
		
		public void setOneMoreId(String oneMoreId) {
			this.oneMoreId = oneMoreId;
		}
		
	}
	
}
