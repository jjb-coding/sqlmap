package com.github.jjbcoding.sqlmap.templates.input;

import com.github.jjbcoding.sqlmap.api.interfaces.APIInput;
import com.github.jjbcoding.sqlmap.api.annotations.ExpectsSession;
import com.github.jjbcoding.sqlmap.api.annotations.Output;
import com.github.jjbcoding.sqlmap.templates.output.SetDetailsOutput;

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
