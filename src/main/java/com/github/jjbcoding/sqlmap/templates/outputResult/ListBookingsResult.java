package com.github.jjbcoding.sqlmap.templates.outputResult;

import java.sql.Timestamp;

import com.github.jjbcoding.sqlmap.api.interfaces.APIOutputResult;

public record ListBookingsResult(
	String BookingUUID,
	String DoctorUUID,
	Timestamp BookingTimestamp,
	Boolean WasAttended
	) implements APIOutputResult {}
