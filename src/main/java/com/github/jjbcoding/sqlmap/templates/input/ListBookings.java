package com.github.jjbcoding.sqlmap.templates.input;

import com.github.jjbcoding.sqlmap.api.interfaces.APIInput;
import com.github.jjbcoding.sqlmap.api.annotations.ExpectsSession;
import com.github.jjbcoding.sqlmap.api.annotations.Output;
import com.github.jjbcoding.sqlmap.templates.output.ListBookingsOutput;

@SuppressWarnings("unused")
@Output(ListBookingsOutput.class)
@ExpectsSession
public record ListBookings(
	Integer month,
	Integer year
	) implements APIInput<ListBookingsOutput> {}
