package com.github.jblair.sqlmap.exceptions;

import java.io.Serial;

public class ExecutionException extends RuntimeException {
	// *** FIELDS
	// The serial version ID.
	@Serial
	private static final long serialVersionUID = 1L;

	// *** CONSTRUCTORS
	public ExecutionException(String message) {
		super(message);
	}
}
