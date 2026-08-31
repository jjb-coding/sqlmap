package com.github.jblair.sqlmap.api.interfaces;

import com.github.jblair.sqlmap.exceptions.ExecutionException;
import com.github.jblair.sqlmap.service.APIService;

/**
 * Contains the parameters of an outgoing request to the API. Must be implemented by
 * all records to be scanned in the configured input package.
 * @param <T>	The return type, a record object from the output package.
 */
public interface APIInput<T extends APIOutput> {
	// *** DEFAULT METHODS
	/**
	 * Passes the execute call along to the service.
	 * @return		The response, a subclass of APIOutput.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	default T execute(APIService _apiService) {
		// Execute & cast
		Object object = _apiService.execute(this);
		
		T ret;
		try {
			ret = (T)object;
		}
		catch (Exception e) {
			throw new ExecutionException("APIInput[" + this.getClass().getSimpleName() + "]: Output record is of incorrect type. It is " + object.getClass().getSimpleName());
		}
		return ret;
	}
}