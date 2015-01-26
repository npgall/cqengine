/*
 * Copyright 2010, 2013 Jan Ouwens
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
package nl.jqno.equalsverifier.util.exceptions;

/**
 * Exception internal to the EqualsVerifier project. Using this exception,
 * EqualsVerifier can distinguish between exceptions thrown internally, and
 * exceptions thrown by external code (e.g., in tested equals methods).
 * 
 * This is an abstract class; the subclasses indicate the type of exception
 * that is signalled by the EqualsVerifier internals.
 *
 * @author Jan Ouwens
 */
@SuppressWarnings("serial")
public abstract class InternalException extends RuntimeException {
	public InternalException() {
		super();
	}
	
	public InternalException(String message) {
		super(message);
	}
	
	public InternalException(Throwable cause) {
		super(cause);
	}
	
	public InternalException(String message, Throwable cause) {
		super(message, cause);
	}
}
