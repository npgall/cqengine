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
package nl.jqno.equalsverifier.util.exceptions;

import nl.jqno.equalsverifier.util.Formatter;

/**
 * Signals that an EqualsVerfier assertion has failed.
 * 
 * @author Jan Ouwens
 */
@SuppressWarnings("serial")
public class AssertionException extends InternalException {
	public AssertionException(Formatter message) {
		super(message.format());
	}
	
	public AssertionException(Formatter message, Throwable cause) {
		super(message.format(), cause);
	}
}
