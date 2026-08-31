package com.github.jblair.sqlmap.templates.input;

import com.github.jblair.sqlmap.api.interfaces.APIInput;
import com.github.jblair.sqlmap.api.annotations.ExpectsSession;
import com.github.jblair.sqlmap.api.annotations.Output;
import com.github.jblair.sqlmap.templates.output.SetDetailsOutput;

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
