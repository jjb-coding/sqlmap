package com.github.jjbcoding.sqlmap.templates.output;

import com.github.jjbcoding.sqlmap.templates.enums.Status;
import com.github.jjbcoding.sqlmap.api.interfaces.APIOutput;

/**
 * The output for SetDetails.
 * @param status	The status
 */
@SuppressWarnings("unused")
public record SetDetailsOutput(
	Status status
	) implements APIOutput {}
