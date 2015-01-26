/*
 * Copyright 2010 Jan Ouwens
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

import nl.jqno.equalsverifier.util.Assert;
import nl.jqno.equalsverifier.util.Formatter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

class SignatureChecker<T> implements Checker {
	private final Class<T> type;

	public SignatureChecker(Class<T> type) {
		this.type = type;
	}
	
	@Override
	public void check() {
		List<Method> equalsMethods = getEqualsMethods();
		if (equalsMethods.size() > 1) {
			fail("More than one equals method found");
		}
		if (equalsMethods.size() == 0) {
			return;
		}
		checkEquals(equalsMethods.get(0));
	}
	
	private List<Method> getEqualsMethods() {
		List<Method> result = new ArrayList<Method>();
		
		for (Method method : type.getDeclaredMethods()) {
			if (method.getName().equals("equals")) {
				result.add(method);
			}
		}
		
		return result;
	}
	
	private void checkEquals(Method equals) {
		Class<?>[] parameterTypes = equals.getParameterTypes();
		if (parameterTypes.length > 1) {
			fail("Too many parameters");
		}
		if (parameterTypes.length == 0) {
			fail("No parameter");
		}
		Class<?> parameterType = parameterTypes[0];
		if (parameterType == type) {
			fail("Parameter should be an Object, not " + type.getSimpleName());
		}
		if (parameterType != Object.class) {
			fail("Parameter should be an Object");
		}
	}

	private void fail(String message) {
		Assert.fail(Formatter.of("Overloaded: %%.\nSignature should be: public boolean equals(Object obj)", message));
	}
}
