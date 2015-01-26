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

import nl.jqno.equalsverifier.util.FieldIterable;
import nl.jqno.equalsverifier.util.Formatter;
import nl.jqno.equalsverifier.util.ObjectAccessor;

import java.lang.reflect.Field;
import java.util.List;

import static nl.jqno.equalsverifier.util.Assert.*;
import static nl.jqno.equalsverifier.util.CachedHashCodeInitializer.getInitializedHashCode;

class ExamplesChecker<T> implements Checker {
	private final Class<T> type;
	private final List<T> equalExamples;
	private final List<T> unequalExamples;

	public ExamplesChecker(Class<T> type, List<T> equalExamples, List<T> unequalExamples) {
		this.type = type;
		this.equalExamples = equalExamples;
		this.unequalExamples = unequalExamples;
	}
	
	@Override
	public void check() {
		for (int i = 0; i < equalExamples.size(); i++) {
			T reference = equalExamples.get(i);
			checkSingle(reference);
			
			for (int j = i + 1; j < equalExamples.size(); j++) {
				T other = equalExamples.get(j);
				checkEqualButNotIdentical(reference, other);
				checkHashCode(reference, other);
			}
		}
		
		for (int i = 0; i < unequalExamples.size(); i++) {
			T reference = unequalExamples.get(i);
			checkSingle(reference);
		}
	}
	
	private void checkEqualButNotIdentical(T reference, T other) {
		assertFalse(Formatter.of("Precondition: the same object appears twice:\n  %%", reference),
				reference == other);
		assertFalse(Formatter.of("Precondition: two identical objects appear:\n  %%", reference),
				isIdentical(reference, other));
		assertTrue(Formatter.of("Precondition: not all equal objects are equal:\n  %%\nand\n  %%", reference, other),
				reference.equals(other));
	}

	private void checkSingle(T reference) {
		final T copy = ObjectAccessor.of(reference, type).copy();
		
		checkReflexivity(reference);
		checkNonNullity(reference);
		checkTypeCheck(reference);
		checkHashCode(reference, copy);
	}
	
	private void checkReflexivity(T reference) {
		assertEquals(Formatter.of("Reflexivity: object does not equal itself:\n  %%", reference),
				reference, reference);
	}

	private void checkNonNullity(T reference) {
		try {
			boolean nullity = reference.equals(null);
			assertFalse(Formatter.of("Non-nullity: true returned for null value"), nullity);
		}
		catch (NullPointerException e) {
			fail(Formatter.of("Non-nullity: NullPointerException thrown"), e);
		}
	}
	
	private void checkTypeCheck(T reference) {
		class SomethingElse {}
		SomethingElse somethingElse = new SomethingElse();
		try {
			reference.equals(somethingElse);
		}
		catch (ClassCastException e) {
			fail(Formatter.of("Type-check: equals throws ClassCastException.\nAdd an instanceof or getClass() check."), e);
		}
		catch (Exception e) {
			fail(Formatter.of("Type-check: equals throws %%.\nAdd an instanceof or getClass() check.", e.getClass().getSimpleName()), e);
		}
	}

	private void checkHashCode(T reference, T copy) {
		assertEquals(Formatter.of("hashCode: hashCode should be consistent:\n  %% (%%)", reference, getInitializedHashCode(reference)),
				getInitializedHashCode(reference), getInitializedHashCode(reference));
		
		if (!reference.equals(copy)) {
			return;
		}
		
		Formatter f = Formatter.of("hashCode: hashCodes should be equal:\n  %% (%%)\nand\n  %% (%%)", reference, getInitializedHashCode(reference), copy, getInitializedHashCode(copy));
		assertEquals(f, getInitializedHashCode(reference), getInitializedHashCode(copy));
	}
	
	private boolean isIdentical(T reference, T other) {
		for (Field field : FieldIterable.of(reference.getClass())) {
			try {
				field.setAccessible(true);
				if (!nullSafeEquals(field.get(reference), field.get(other))) {
					return false;
				}
			}
			catch (IllegalArgumentException e) {
				return false;
			}
			catch (IllegalAccessException e) {
				return false;
			}
		}
		
		return true;
	}
	
	private boolean nullSafeEquals(Object x, Object y) {
		return x == null ? y == null : x.equals(y);
	}
}
