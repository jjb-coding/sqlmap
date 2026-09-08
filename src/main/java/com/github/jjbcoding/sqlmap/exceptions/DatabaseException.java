package com.github.jjbcoding.sqlmap.exceptions;

import java.io.Serial;
import java.sql.SQLException;

public class DatabaseException extends RuntimeException {
	// *** FIELDS
	// The serial version ID.
	@Serial
	private static final long serialVersionUID = 1L;

	// *** CONSTRUCTORS
	public DatabaseException(String message, SQLException e) {
		super(message, e);
	}
}
