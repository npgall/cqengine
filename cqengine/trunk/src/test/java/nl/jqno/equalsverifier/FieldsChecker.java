/*
 * Copyright 2009-2015 Jan Ouwens
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

import nl.jqno.equalsverifier.FieldInspector.FieldCheck;
import nl.jqno.equalsverifier.util.*;
import nl.jqno.equalsverifier.util.annotations.NonnullAnnotationChecker;
import nl.jqno.equalsverifier.util.annotations.SupportedAnnotations;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.EnumSet;
import java.util.Set;

import static nl.jqno.equalsverifier.util.Assert.*;
import static nl.jqno.equalsverifier.util.CachedHashCodeInitializer.getInitializedHashCode;

class FieldsChecker<T> implements Checker {
	private final ClassAccessor<T> classAccessor;
	private final PrefabValues prefabValues;
	private final EnumSet<Warning> warningsToSuppress;
	private final boolean allFieldsShouldBeUsed;
	private final Set<String> allFieldsShouldBeUsedExceptions;

	public FieldsChecker(ClassAccessor<T> classAccessor, EnumSet<Warning> warningsToSuppress, boolean allFieldsShouldBeUsed, Set<String> allFieldsShouldBeUsedExceptions) {
		this.classAccessor = classAccessor;
		this.prefabValues = classAccessor.getPrefabValues();
		this.warningsToSuppress = EnumSet.copyOf(warningsToSuppress);
		this.allFieldsShouldBeUsed = allFieldsShouldBeUsed;
		this.allFieldsShouldBeUsedExceptions = allFieldsShouldBeUsedExceptions;
	}
	
	@Override
	public void check() {
		FieldInspector<T> inspector = new FieldInspector<T>(classAccessor);
		
		if (classAccessor.declaresEquals()) {
			inspector.check(new ArrayFieldCheck());
			inspector.check(new FloatAndDoubleFieldCheck());
			inspector.check(new ReflexivityFieldCheck());
		}
		
		if (!ignoreMutability()) {
			inspector.check(new MutableStateFieldCheck());
		}
		
		if (!warningsToSuppress.contains(Warning.TRANSIENT_FIELDS)) {
			inspector.check(new TransientFieldsCheck());
		}
		
		inspector.check(new SignificantFieldCheck());
		inspector.check(new SymmetryFieldCheck());
		inspector.check(new TransitivityFieldCheck());
	}

	private boolean ignoreMutability() {
		return warningsToSuppress.contains(Warning.NONFINAL_FIELDS) ||
				classAccessor.hasAnnotation(SupportedAnnotations.IMMUTABLE) ||
				classAccessor.hasAnnotation(SupportedAnnotations.ENTITY);
	}
	
	private class SymmetryFieldCheck implements FieldCheck {
		@Override
		public void execute(FieldAccessor referenceAccessor, FieldAccessor changedAccessor) {
			checkSymmetry(referenceAccessor, changedAccessor);
			
			changedAccessor.changeField(prefabValues);
			checkSymmetry(referenceAccessor, changedAccessor);
			
			referenceAccessor.changeField(prefabValues);
			checkSymmetry(referenceAccessor, changedAccessor);
		}
		
		private void checkSymmetry(FieldAccessor referenceAccessor, FieldAccessor changedAccessor) {
			Object left = referenceAccessor.getObject();
			Object right = changedAccessor.getObject();
			assertTrue(Formatter.of("Symmetry: objects are not symmetric:\n  %%\nand\n  %%", left, right),
					left.equals(right) == right.equals(left));
		}
	}
	
	private class TransitivityFieldCheck implements FieldCheck {
		@Override
		public void execute(FieldAccessor referenceAccessor, FieldAccessor changedAccessor) {
			Object a1 = referenceAccessor.getObject();
			Object b1 = buildB1(changedAccessor);
			Object b2 = buildB2(a1, referenceAccessor.getField());
			
			boolean x = a1.equals(b1);
			boolean y = b1.equals(b2);
			boolean z = a1.equals(b2);
			
			if (countFalses(x, y, z) == 1) {
				fail(Formatter.of("Transitivity: two of these three instances are equal to each other, so the third one should be, too:\n-  %%\n-  %%\n-  %%", a1, b1, b2));
			}
		}
		
		private Object buildB1(FieldAccessor accessor) {
			accessor.changeField(prefabValues);
			return accessor.getObject();
		}
		
		private Object buildB2(Object a1, Field referenceField) {
			Object result = ObjectAccessor.of(a1).copy();
			ObjectAccessor<?> objectAccessor = ObjectAccessor.of(result);
			objectAccessor.fieldAccessorFor(referenceField).changeField(prefabValues);
			for (Field field : FieldIterable.of(result.getClass())) {
				if (!field.equals(referenceField)) {
					objectAccessor.fieldAccessorFor(field).changeField(prefabValues);
				}
			}
			return result;
		}
		
		private int countFalses(boolean... bools) {
			int result = 0;
			for (boolean b : bools) {
				if (!b) {
					result++;
				}
			}
			return result;
		}
	}
	
	private class SignificantFieldCheck implements FieldCheck {
		@Override
		public void execute(FieldAccessor referenceAccessor, FieldAccessor changedAccessor) {
			Object reference = referenceAccessor.getObject();
			Object changed = changedAccessor.getObject();
			String fieldName = referenceAccessor.getFieldName();
			
			boolean equalToItself = reference.equals(changed);
			
			changedAccessor.changeField(prefabValues);
			
			boolean equalsChanged = !reference.equals(changed);
			boolean hashCodeChanged = getInitializedHashCode(reference) != getInitializedHashCode(changed);
			
			if (equalsChanged != hashCodeChanged) {
				assertFalse(Formatter.of("Significant fields: equals relies on %%, but hashCode does not.", fieldName),
						equalsChanged);
				assertFalse(Formatter.of("Significant fields: hashCode relies on %%, but equals does not.", fieldName),
						hashCodeChanged);
			}
			
			if (allFieldsShouldBeUsed && !referenceAccessor.fieldIsStatic() && !referenceAccessor.fieldIsTransient()) {
				assertTrue(Formatter.of("Significant fields: equals does not use %%", fieldName), equalToItself);
				
				boolean thisFieldShouldBeUsed = allFieldsShouldBeUsed && !allFieldsShouldBeUsedExceptions.contains(fieldName);
				assertTrue(Formatter.of("Significant fields: equals does not use %%.", fieldName),
						!thisFieldShouldBeUsed || equalsChanged);
				assertTrue(Formatter.of("Significant fields: equals should not use %%, but it does.", fieldName),
						thisFieldShouldBeUsed || !equalsChanged);
			}
			
			referenceAccessor.changeField(prefabValues);
		}
	}
	
	private class ArrayFieldCheck implements FieldCheck {
		@Override
		public void execute(FieldAccessor referenceAccessor, FieldAccessor changedAccessor) {
			Class<?> arrayType = referenceAccessor.getFieldType();
			if (!arrayType.isArray()) {
				return;
			}
			
			String fieldName = referenceAccessor.getFieldName();
			Object reference = referenceAccessor.getObject();
			Object changed = changedAccessor.getObject();
			replaceInnermostArrayValue(changedAccessor);

			if (arrayType.getComponentType().isArray()) {
				assertDeep(fieldName, reference, changed);
			}
			else {
				assertArray(fieldName, reference, changed);
			}
		}

		private void replaceInnermostArrayValue(FieldAccessor accessor) {
			Object newArray = arrayCopy(accessor.get());
			accessor.set(newArray);
		}

		private Object arrayCopy(Object array) {
			Class<?> componentType = array.getClass().getComponentType();
			Object result = Array.newInstance(componentType, 1);
			if (componentType.isArray()) {
				Array.set(result, 0, arrayCopy(Array.get(array, 0)));
			}
			else {
				Array.set(result, 0, Array.get(array, 0));
			}
			return result;
		}

		private void assertDeep(String fieldName, Object reference, Object changed) {
			assertEquals(Formatter.of("Multidimensional array: ==, regular equals() or Arrays.equals() used instead of Arrays.deepEquals() for field %%.", fieldName),
					reference, changed);
			assertEquals(Formatter.of("Multidimensional array: regular hashCode() or Arrays.hashCode() used instead of Arrays.deepHashCode() for field %%.", fieldName),
					getInitializedHashCode(reference), getInitializedHashCode(changed));
		}
		
		private void assertArray(String fieldName, Object reference, Object changed) {
			assertEquals(Formatter.of("Array: == or regular equals() used instead of Arrays.equals() for field %%.", fieldName),
					reference, changed);
			assertEquals(Formatter.of("Array: regular hashCode() used instead of Arrays.hashCode() for field %%.", fieldName),
					getInitializedHashCode(reference), getInitializedHashCode(changed));
		}
	}
	
	private class FloatAndDoubleFieldCheck implements FieldCheck {
		@Override
		public void execute(FieldAccessor referenceAccessor, FieldAccessor changedAccessor) {
			Class<?> type = referenceAccessor.getFieldType();
			if (isFloat(type)) {
				referenceAccessor.set(Float.NaN);
				changedAccessor.set(Float.NaN);
				assertEquals(Formatter.of("Float: equals doesn't use Float.compare for field %%.", referenceAccessor.getFieldName()),
						referenceAccessor.getObject(), changedAccessor.getObject());
			}
			if (isDouble(type)) {
				referenceAccessor.set(Double.NaN);
				changedAccessor.set(Double.NaN);
				assertEquals(Formatter.of("Double: equals doesn't use Double.compare for field %%.", referenceAccessor.getFieldName()),
						referenceAccessor.getObject(), changedAccessor.getObject());
			}
		}

		private boolean isFloat(Class<?> type) {
			return type == float.class || type == Float.class;
		}
		
		private boolean isDouble(Class<?> type) {
			return type == double.class || type == Double.class;
		}
	}
	
	private class ReflexivityFieldCheck implements FieldCheck {
		@Override
		public void execute(FieldAccessor referenceAccessor, FieldAccessor changedAccessor) {
			if (warningsToSuppress.contains(Warning.IDENTICAL_COPY_FOR_VERSIONED_ENTITY)) {
				return;
			}
			
			referenceAccessor.changeField(prefabValues);
			changedAccessor.changeField(prefabValues);
			checkReflexivityFor(referenceAccessor, changedAccessor);
			
			boolean fieldIsPrimitive = referenceAccessor.fieldIsPrimitive();
			boolean fieldIsNonNull = NonnullAnnotationChecker.fieldIsNonnull(classAccessor, referenceAccessor.getField());
			boolean ignoreNull = fieldIsNonNull || warningsToSuppress.contains(Warning.NULL_FIELDS);
			if (fieldIsPrimitive || !ignoreNull) {
				referenceAccessor.defaultField();
				changedAccessor.defaultField();
				checkReflexivityFor(referenceAccessor, changedAccessor);
			}
		}
		
		private void checkReflexivityFor(FieldAccessor referenceAccessor, FieldAccessor changedAccessor) {
			Object left = referenceAccessor.getObject();
			Object right = changedAccessor.getObject();
			
			if (warningsToSuppress.contains(Warning.IDENTICAL_COPY)) {
				assertFalse(Formatter.of("Unnecessary suppression: %%. Two identical copies are equal.", Warning.IDENTICAL_COPY.toString()),
						left.equals(right));
			}
			else {
				Formatter f = Formatter.of("Reflexivity: object does not equal an identical copy of itself:\n  %%" +
						"\nIf this is intentional, consider suppressing Warning.%%", left, Warning.IDENTICAL_COPY.toString());
				assertEquals(f, left, right);
			}
		}
	}
	
	private class MutableStateFieldCheck implements FieldCheck {
		@Override
		public void execute(FieldAccessor referenceAccessor, FieldAccessor changedAccessor) {
			Object reference = referenceAccessor.getObject();
			Object changed = changedAccessor.getObject();
			
			changedAccessor.changeField(prefabValues);

			boolean equalsChanged = !reference.equals(changed);

			if (equalsChanged && !referenceAccessor.fieldIsFinal()) {
				fail(Formatter.of("Mutability: equals depends on mutable field %%.", referenceAccessor.getFieldName()));
			}
			
			referenceAccessor.changeField(prefabValues);
		}
	}
	
	private class TransientFieldsCheck implements FieldCheck {
		@Override
		public void execute(FieldAccessor referenceAccessor, FieldAccessor changedAccessor) {
			Object reference = referenceAccessor.getObject();
			Object changed = changedAccessor.getObject();
			
			changedAccessor.changeField(prefabValues);

			boolean equalsChanged = !reference.equals(changed);
			boolean fieldIsTransient = referenceAccessor.fieldIsTransient() ||
					classAccessor.fieldHasAnnotation(referenceAccessor.getField(), SupportedAnnotations.TRANSIENT);
			
			if (equalsChanged && fieldIsTransient) {
				fail(Formatter.of("Transient field %% should not be included in equals/hashCode contract.", referenceAccessor.getFieldName()));
			}
			
			referenceAccessor.changeField(prefabValues);
		}
	}
}
