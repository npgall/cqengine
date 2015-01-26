/*
 * Copyright 2015 Jan Ouwens
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.jqno.equalsverifier.util.annotations;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Contains all properties of an annotation necessary to to make decisions about
 * that annotation.
 * 
 * Note that this object does not contain all possible properties; only the ones
 * that are actually used by EqualsVerifier.
 */
public class AnnotationProperties {
	private final String descriptor;
	private Map<String, Set<Object>> arrayValues = new HashMap<String, Set<Object>>();
	
	/**
	 * Constructor.
	 * 
	 * @param descriptor The annotation's descriptor string.
	 */
	public AnnotationProperties(String descriptor) {
		this.descriptor = descriptor;
	}
	
	/**
	 * Getter.
	 */
	public String getDescriptor() {
		return descriptor;
	}
	
	/**
	 * Adds the content of an array value property.
	 * 
	 * @param name The name of the array value property.
	 * @param values The content of the array value property.
	 */
	public void putArrayValues(String name, Set<Object> values) {
		arrayValues.put(name, values);
	}
	
	/**
	 * Retrieves the content of an array value property.
	 * 
	 * @param name The name of the array value property.
	 * @return The content of the array value property.
	 */
	public Set<Object> getArrayValues(String name) {
		return arrayValues.get(name);
	}
}
