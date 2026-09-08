package com.github.jjbcoding.sqlmap.templates.output;

import com.github.jjbcoding.sqlmap.templates.enums.Status;
import com.github.jjbcoding.sqlmap.api.interfaces.APIOutput;

public record SetDetailsOutput(
	Status status
	) implements APIOutput {}
