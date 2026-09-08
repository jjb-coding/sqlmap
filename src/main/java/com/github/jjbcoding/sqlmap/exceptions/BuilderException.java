package com.github.jjbcoding.sqlmap.exceptions;

import java.io.Serial;

public class BuilderException extends RuntimeException {
	// *** FIELDS
	// The serial version ID.
	@Serial
	private static final long serialVersionUID = 1L;

	// *** CONSTRUCTORS
	public BuilderException(String message) {
		super(message);
	}
}
