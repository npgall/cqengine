/*
 * Copyright 2014 Jan Ouwens
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

import nl.jqno.equalsverifier.util.exceptions.EqualsVerifierBugException;
import nl.jqno.equalsverifier.util.exceptions.ReflectionException;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds prefab values for classes that may or may not be present on the
 * classpath.
 * 
 * Will try to create precisely two prefab values for a given class by calling
 * its constructor, a factory method, or a public static final constant declared
 * within the class, and add them to a {@link PrefabValues} object.
 *
 * If the class is not present on the classpath, or if calling any of its
 * members fails, it will result in a no-op. ConditionalPrefabValueBuilder will
 * not throw an exception.
 *
 * @author Jan Ouwens
 */
public class ConditionalPrefabValueBuilder {
	private final Class<?> type;
	private boolean stop = false;
	private ConditionalInstantiator ci;
	private List<Object> instances = new ArrayList<Object>();

	/**
	 * Factory method.
	 *
	 * @param fullyQualifiedClassName
	 *            The fully qualified class name of the class for which we
	 *            intend to create prefab values.
	 * @return A ConditionalPrefabValueBuilder.
	 */
	public static ConditionalPrefabValueBuilder of(String fullyQualifiedClassName) {
		return new ConditionalPrefabValueBuilder(fullyQualifiedClassName);
	}

	/**
	 * Private constructor. Call {@link #of(String)} instead.
	 */
	private ConditionalPrefabValueBuilder(String fullyQualifiedClassName) {
		this.ci = new ConditionalInstantiator(fullyQualifiedClassName);
		this.type = ci.resolve();
		if (type == null) {
			stop = true;
		}
	}

	/**
	 * Provides a concrete implementing class in case the desired type is
	 * abstract or an interface.
	 *
	 * @param fullyQualifiedClassName
	 *            The fully qualified class name of the concrete implementing
	 *            class.
	 * @return {@code this}, for easy method chaining.
	 */
	public ConditionalPrefabValueBuilder withConcreteClass(String fullyQualifiedClassName) {
		if (stop) {
			return this;
		}
		ci = new ConditionalInstantiator(fullyQualifiedClassName);
		Class<?> concreteType = ci.resolve();
		if (concreteType == null) {
			stop = true;
			return this;
		}
		if (!type.isAssignableFrom(concreteType)) {
			throw new EqualsVerifierBugException("Concrete class " + fullyQualifiedClassName + " is not an " + type.getCanonicalName());
		}
		return this;
	}

	/**
	 * Attempts to instantiate the given type by calling its constructor. If
	 * this fails, it will short-circuit any further calls.
	 *
	 * @param paramTypes
	 *            A list of types that identifies the constructor to be called.
	 * @param paramValues
	 *            A list of values to pass to the constructor. Their types must
	 *            match the {@code paramTypes}.
	 * @return {@code this}, for easy method chaining.
	 */
	public ConditionalPrefabValueBuilder instantiate(Class<?>[] paramTypes, Object[] paramValues) {
		if (!stop) {
			validate();
			try {
				instances.add(ci.instantiate(paramTypes, paramValues));
			}
			catch (ReflectionException e) {
				stop = true;
			}
		}
		return this;
	}

	/**
	 * Attempts to instantiate the given type by calling a factory method. If
	 * this fails, it will short-circuit any further calls.
	 *
	 * @param factoryMethod
	 *            The name of the factory method.
	 * @param paramTypes
	 *            A list of types that identifies the factory method's overload
	 *            to be called.
	 * @param paramValues
	 *            A list of values to pass to the constructor. Their types must
	 *            match the {@code paramTypes}.
	 * @return {@code this}, for easy method chaining.
	 */
	public ConditionalPrefabValueBuilder callFactory(String factoryMethod, Class<?>[] paramTypes, Object[] paramValues) {
		if (!stop) {
			validate();
			try {
				instances.add(ci.callFactory(factoryMethod, paramTypes, paramValues));
			}
			catch (ReflectionException e) {
				stop = true;
			}
		}
		return this;
	}

	/**
	 * Attempts to obtain a reference to the given type by calling a public
	 * static final constant defined within the type. If this fails, it will
	 * short-circuit any further calls.
	 *
	 * @param constantName
	 *            The name of the constant.
	 * @return {@code this}, for easy method chaining.
	 */
	public ConditionalPrefabValueBuilder withConstant(String constantName) {
		if (!stop) {
			validate();
			try {
				instances.add(ci.returnConstant(constantName));
			}
			catch (ReflectionException e) {
				stop = true;
			}
		}
		return this;
	}

	/**
	 * Adds two instances of the given type to a {@link PrefabValues} object.
	 *
	 * @param prefabValues
	 *            The {@link PrefabValues} object to add the instances to.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addTo(PrefabValues prefabValues) {
		if (!stop) {
			if (instances.size() < 2) {
				throw new EqualsVerifierBugException("Not enough instances");
			}
			prefabValues.put((Class)type, instances.get(0), instances.get(1));
		}
	}
	
	private void validate() {
		if (instances.size() >= 2) {
			throw new EqualsVerifierBugException("Too many instances");
		}
	}
}
