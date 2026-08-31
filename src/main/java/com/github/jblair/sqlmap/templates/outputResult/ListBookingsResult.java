package com.github.jblair.sqlmap.templates.outputResult;

import java.sql.Timestamp;

import com.github.jblair.sqlmap.api.interfaces.APIOutputResult;

public record ListBookingsResult(
	String BookingUUID,
	String DoctorUUID,
	Timestamp BookingTimestamp,
	Boolean WasAttended
	) implements APIOutputResult {}
