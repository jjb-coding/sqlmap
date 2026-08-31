package com.github.jblair.sqlmap.templates.output;

import com.github.jblair.sqlmap.templates.enums.Status;
import com.github.jblair.sqlmap.api.interfaces.APIOutput;

public record SetDetailsOutput(
	Status status
	) implements APIOutput {}
