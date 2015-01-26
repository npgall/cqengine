/*
 * Copyright 2012 Jan Ouwens
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
package nl.jqno.equalsverifier;

import nl.jqno.equalsverifier.util.FieldAccessor;
import nl.jqno.equalsverifier.util.FieldIterable;
import nl.jqno.equalsverifier.util.ObjectAccessor;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class StaticFieldValueStash {
	private final Map<Class<?>, Map<Field, Object>> stash = new HashMap<Class<?>, Map<Field, Object>>();

	public <T> void backup(Class<T> type) {
		if (stash.containsKey(type)) {
			return;
		}
		
		stash.put(type, new HashMap<Field, Object>());
		ObjectAccessor<T> objectAccessor = ObjectAccessor.of(null, type);
		for (Field field : FieldIterable.of(type)) {
			FieldAccessor accessor = objectAccessor.fieldAccessorFor(field);
			if (accessor.fieldIsStatic()) {
				stash.get(type).put(field, accessor.get());
			}
		}
	}
	
	public void restoreAll() {
		for (Class<?> type : stash.keySet()) {
			ObjectAccessor<?> objectAccessor = ObjectAccessor.of(null, type);
			for (Field field : FieldIterable.of(type)) {
				FieldAccessor accessor = objectAccessor.fieldAccessorFor(field);
				if (accessor.fieldIsStatic()) {
					accessor.set(stash.get(type).get(field));
				}
			}
		}
	}
}
