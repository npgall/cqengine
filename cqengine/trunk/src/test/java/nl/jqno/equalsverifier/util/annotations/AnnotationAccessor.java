/*
 * Copyright 2011, 2015 Jan Ouwens
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

import nl.jqno.equalsverifier.util.exceptions.ReflectionException;
import org.objectweb.asm.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Provides access to the annotations that are defined on a class
 * and its fields.
 * 
 * @author Jan Ouwens
 */
public class AnnotationAccessor {
	private final Annotation[] supportedAnnotations;
	private final Class<?> type;
	private final boolean ignoreFailure;
	private final Set<Annotation> classAnnotations = new HashSet<Annotation>();
	private final Map<String, Set<Annotation>> fieldAnnotations = new HashMap<String, Set<Annotation>>();
	
	private boolean processed = false;
	private boolean shortCircuit = false;

	/**
	 * Constructor
	 * 
	 * @param supportedAnnotations Collection of annotations to query.
	 * @param type The class whose annotations need to be queried.
	 * @param ignoreFailure Ignore when processing annotations fails when the
	 * 			class file cannot be read.
	 */
	public AnnotationAccessor(Annotation[] supportedAnnotations, Class<?> type, boolean ignoreFailure) {
		this.supportedAnnotations = supportedAnnotations;
		this.type = type;
		this.ignoreFailure = ignoreFailure;
	}
	
	/**
	 * Determines whether {@link #type} has a particular annotation. 
	 * 
	 * @param annotation The annotation we want to find.
	 * @return True if {@link #type} has an annotation with the supplied name.
	 */
	public boolean typeHas(Annotation annotation) {
		if (shortCircuit) {
			return false;
		}
		process();
		return classAnnotations.contains(annotation);
	}
	
	/**
	 * Determines whether {@link #type} has a particular annotation on a
	 * particular field.
	 *  
	 * @param fieldName The name of the field for which we want to know if it
	 * 			has the annotation.
	 * @param annotation The annotation we want to find.
	 * @return True if the specified field in {@link #type} has the specified
	 * 			annotation.
	 * @throws nl.jqno.equalsverifier.util.exceptions.ReflectionException if {@link #type} does not have the specified
	 * 			field.
	 */
	public boolean fieldHas(String fieldName, Annotation annotation) {
		if (shortCircuit || fieldName.startsWith("CGLIB$")) {
			return false;
		}
		process();
		Set<Annotation> annotations = fieldAnnotations.get(fieldName);
		if (annotations == null) {
			throw new ReflectionException("Class " + type.getName() + " does not have field " + fieldName);
		}
		return annotations.contains(annotation);
	}
	
	private void process() {
		if (processed) {
			return;
		}
		
		visit();
		processed = true;
	}
	
	private void visit() {
		visitType(type, false);
		Class<?> i = type.getSuperclass();
		while (i != null && i != Object.class) {
			visitType(i, true);
			i = i.getSuperclass();
		}
	}
	
	private void visitType(Class<?> type, boolean inheriting) {
		ClassLoader classLoader = getClassLoaderFor(type);
		Type asmType = Type.getType(type);
		String url = asmType.getInternalName() + ".class";
		InputStream is = classLoader.getResourceAsStream(url);
		
		Visitor v = new Visitor(inheriting);
		try {
			ClassReader cr = new ClassReader(is);
			cr.accept(v, 0);
		}
		catch (IOException e) {
			if (ignoreFailure) {
				shortCircuit = true;
			}
			else {
				throw new ReflectionException("Cannot read class file for " + type.getSimpleName() +
						".\nSuppress Warning.ANNOTATION to skip annotation processing phase.");
			}
		}
	}
	
	private ClassLoader getClassLoaderFor(Class<?> type) {
		ClassLoader result = type.getClassLoader();
		if (result == null) {
			result = ClassLoader.getSystemClassLoader();
		}
		return result;
	}
	
	private class Visitor extends ClassVisitor {
		private final boolean inheriting;
		
		public Visitor(boolean inheriting) {
			super(Opcodes.ASM4);
			this.inheriting = inheriting;
		}
		
		@Override
		public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
			return new MyAnnotationVisitor(descriptor, classAnnotations, inheriting, true);
		}
		
		@Override
		public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
			HashSet<Annotation> annotations = new HashSet<Annotation>();
			fieldAnnotations.put(name, annotations);
			return new MyFieldVisitor(annotations, inheriting);
		}
	}
	
	private class MyFieldVisitor extends FieldVisitor {
		private final Set<Annotation> fieldAnnotations;
		private final boolean inheriting;
		
		public MyFieldVisitor(Set<Annotation> fieldAnnotations, boolean inheriting) {
			super(Opcodes.ASM4);
			this.fieldAnnotations = fieldAnnotations;
			this.inheriting = inheriting;
		}
		
		@Override
		public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
			return new MyAnnotationVisitor(descriptor, fieldAnnotations, inheriting, false);
		}
	}
	
	private class MyAnnotationVisitor extends AnnotationVisitor {
		private final String annotationDescriptor;
		private final Set<Annotation> annotations;
		private final boolean inheriting;
		private final boolean inspectAnnotation;
		
		private final AnnotationProperties properties;
		
		public MyAnnotationVisitor(String annotationDescriptor, Set<Annotation> annotations, boolean inheriting, boolean inspectAnnotation) {
			super(Opcodes.ASM4);
			this.annotationDescriptor = annotationDescriptor;
			this.annotations = annotations;
			this.inheriting = inheriting;
			this.inspectAnnotation = inspectAnnotation;
			properties = new AnnotationProperties(annotationDescriptor);
		}
		
		@Override
		public AnnotationVisitor visitArray(String name) {
			if (!inspectAnnotation) {
				return null;
			}
			
			Set<Object> annotations = new HashSet<Object>();
			properties.putArrayValues(name, annotations);
			return new AnnotationArrayValueVisitor(annotations);
		}
		
		@Override
		public void visitEnd() {
			for (Annotation annotation : supportedAnnotations) {
				if (!inheriting || annotation.inherits()) {
					for (String descriptor : annotation.descriptors()) {
						String asBytecodeIdentifier = descriptor.replaceAll("\\.", "/") + ";";
						if (annotationDescriptor.endsWith(asBytecodeIdentifier) && annotation.validate(properties)) {
							annotations.add(annotation);
						}
					}
				}
			}
		}
	}
	
	private class AnnotationArrayValueVisitor extends AnnotationVisitor {
		private final Set<Object> objects;
		
		public AnnotationArrayValueVisitor(Set<Object> annotations) {
			super(Opcodes.ASM4);
			this.objects = annotations;
		}
		
		@Override
		public void visit(String name, Object value) {
			objects.add(value);
		}
		
		@Override
		public void visitEnum(String name, String desc, String value) {
			objects.add(value);
		}
	}
}
