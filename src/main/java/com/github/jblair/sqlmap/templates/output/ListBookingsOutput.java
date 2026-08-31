package com.github.jblair.sqlmap.templates.output;

import java.util.ArrayList;

import com.github.jblair.sqlmap.templates.outputResult.ListBookingsResult;
import com.github.jblair.sqlmap.templates.enums.Status;
import com.github.jblair.sqlmap.api.interfaces.APIOutput;
import com.github.jblair.sqlmap.api.annotations.ResultSet;

public record ListBookingsOutput(
	Status status,
	@ResultSet
	ArrayList<ListBookingsResult> bookings
	)
	implements APIOutput {}
