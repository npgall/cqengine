/*
 * Copyright 2013 Jan Ouwens
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
package nl.jqno.equalsverifier.util;

import java.lang.reflect.Field;
import java.util.regex.Matcher;

/**
 * Formats a string with the contents of one or more objects.
 * 
 * If possible, uses each object's {@code toString} method.
 * If this throws an exception, Formatter creates its own string
 * representation of the object, containing its class name and
 * the contents of its fields.
 *
 * @author Jan Ouwens
 */
public class Formatter {
	private final String message;
	private Object[] objects;
	
	/**
	 * Factory method.
	 * 
	 * @param message The string that will be formatted.
	 * 				The substring %% represents the location where each
	 * 				object's will string representation will be inserted. 
	 * @param objects The objects whose string representation will be inserted
	 * 				into the message string.
	 * @return A {@code Formatter}.
	 */
	public static Formatter of(String message, Object... objects) {
		return new Formatter(message, objects);
	}
	
	/**
	 * Private constructor. Call {@link #of(String, Object...)} to instantiate.
	 */
	private Formatter(String message, Object... objects) {
		if (message == null) {
			throw new NullPointerException();
		}
		this.message = message;
		this.objects = objects;
	}
	
	/**
	 * Formats the message with the given objects.
	 * 
	 * @return The message, with the given objects's string representations
	 * 			inserted into it.
	 * @throws IllegalStateException if the number of %%'s in the message does
	 * 			not match the number of objects.
	 */
	public String format() {
		String result = message;
		for (int i = 0; i < objects.length; i++) {
			String s = result.replaceFirst("%%", Matcher.quoteReplacement(stringify(objects[i])));
			if (result.equals(s)) {
				throw new IllegalStateException("Too many parameters");
			}
			result = s;
		}
		if (result.contains("%%")) {
			throw new IllegalStateException("Not enough parameters");
		}
		return result;
	}
	
	private String stringify(Object obj) {
		if (obj == null) {
			return "null";
		}
		try {
			return obj.toString();
		}
		catch (Throwable e) {
			StringBuilder result = new StringBuilder();
			result.append(stringifyByReflection(obj));
			result.append("-throws ");
			result.append(e.getClass().getSimpleName());
			result.append("(");
			result.append(e.getMessage());
			result.append(")");
			return result.toString();
		}
	}
	
	private String stringifyByReflection(Object obj) {
		StringBuilder result = new StringBuilder();
		
		Class<?> type = obj.getClass();
		ObjectAccessor<?> accessor = ObjectAccessor.of(obj);
		
		result.append("[");
		String typeName = type.getSimpleName().replaceAll("\\$\\$EnhancerByCGLIB.*", "");
		result.append(typeName);
		
		for (Field field : FieldIterable.of(type)) {
			String fieldName = field.getName();
			if (!fieldName.startsWith("CGLIB$")) {
				result.append(" ");
				result.append(fieldName);
				result.append("=");
				Object value = accessor.fieldAccessorFor(field).get();
				result.append(stringify(value));
			}
		}
		
		result.append("]");
		return result.toString();
	}
}
