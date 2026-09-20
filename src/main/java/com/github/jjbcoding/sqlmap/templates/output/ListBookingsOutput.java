package com.github.jjbcoding.sqlmap.templates.output;

import java.util.ArrayList;

import com.github.jjbcoding.sqlmap.templates.outputResult.ListBookingsResult;
import com.github.jjbcoding.sqlmap.templates.enums.Status;
import com.github.jjbcoding.sqlmap.api.interfaces.APIOutput;
import com.github.jjbcoding.sqlmap.api.annotations.ResultSet;

/**
 * Output for ListBookings.
 * @param status	The response status
 * @param bookings	Booking Result Set
 */
@SuppressWarnings("unused")
public record ListBookingsOutput(
	Status status,
	@ResultSet
	ArrayList<ListBookingsResult> bookings
	)
	implements APIOutput {}
