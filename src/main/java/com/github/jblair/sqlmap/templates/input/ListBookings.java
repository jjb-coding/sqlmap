package com.github.jblair.sqlmap.templates.input;

import com.github.jblair.sqlmap.api.interfaces.APIInput;
import com.github.jblair.sqlmap.api.annotations.ExpectsSession;
import com.github.jblair.sqlmap.api.annotations.Output;
import com.github.jblair.sqlmap.templates.output.ListBookingsOutput;

@SuppressWarnings("unused")
@Output(ListBookingsOutput.class)
@ExpectsSession
public record ListBookings(
	Integer month,
	Integer year
	) implements APIInput<ListBookingsOutput> {}
