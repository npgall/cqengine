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
package nl.jqno.equalsverifier.util.exceptions;

/**
 * Signals a bug in EqualsVerifier.
 * 
 * @author Jan Ouwens
 */
@SuppressWarnings("serial")
public class EqualsVerifierBugException extends InternalException {
	
	private static final String BUG = "This is a bug in EqualsVerifier. Please report this in the issue tracker at http://www.jqno.nl/equalsverifier";
	
	public EqualsVerifierBugException() {
		super(BUG);
	}
	
	public EqualsVerifierBugException(String message) {
		super(BUG + "\n" + message);
	}
	
	public EqualsVerifierBugException(Throwable cause) {
		super(BUG, cause);
	}
	
	public EqualsVerifierBugException(String message, Throwable cause) {
		super(BUG + "\n" + message, cause);
	}
}
