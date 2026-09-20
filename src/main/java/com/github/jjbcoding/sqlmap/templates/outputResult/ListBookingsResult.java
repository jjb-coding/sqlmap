package com.github.jjbcoding.sqlmap.templates.outputResult;

import java.sql.Timestamp;

import com.github.jjbcoding.sqlmap.api.interfaces.APIOutputResult;

/**
 * The Result Set for ListBookings.
 * @param bookingUUID			The booking UUID
 * @param bookingAgentUUID		The booking agent UUID
 * @param bookingTimestamp		The timestamp
 * @param wasAttended           Whether it was attended
 */
@SuppressWarnings("unused")
public record ListBookingsResult(
	String bookingUUID,
	String bookingAgentUUID,
	Timestamp bookingTimestamp,
	Boolean wasAttended
	) implements APIOutputResult {}
