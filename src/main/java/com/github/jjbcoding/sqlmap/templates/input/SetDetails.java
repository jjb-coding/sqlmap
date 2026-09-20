package com.github.jjbcoding.sqlmap.templates.input;

import com.github.jjbcoding.sqlmap.api.interfaces.APIInput;
import com.github.jjbcoding.sqlmap.api.annotations.ExpectsSession;
import com.github.jjbcoding.sqlmap.api.annotations.Output;
import com.github.jjbcoding.sqlmap.templates.output.SetDetailsOutput;

/**
 * Sets details.
 * @param firstName				The first name
 * @param surname				The surname
 * @param title					The title
 * @param contactEmail			The contact email
 * @param contactPhoneNumber	The contact phone number
 * @param street1				The first line of the street address
 * @param street2				The second line of the street address
 * @param city					The city
 * @param county				The county
 * @param postCode				The postcode
 */
@SuppressWarnings("unused")
@ExpectsSession
@Output(SetDetailsOutput.class)
public record SetDetails(
	String firstName,
	String surname,
	String title,
	String contactEmail,
	String contactPhoneNumber,
	String street1,
	String street2,
	String city,
	String county,
	String postCode
	) implements APIInput<SetDetailsOutput> {}
