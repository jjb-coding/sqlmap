package com.github.jjbcoding.sqlmap.api.interfaces;

import com.github.jjbcoding.sqlmap.exceptions.ExecutionException;
import com.github.jjbcoding.sqlmap.service.APIService;
import com.github.jjbcoding.sqlmap.service.APIServiceLocatorSingleton;

/**
 * Contains the parameters of an outgoing request to the API. Must be implemented by
 * all records to be scanned in the configured input package.
 * @param <T>	The return type, a record object from the output package.
 */
public interface APIInput<T extends APIOutput> {
	// ----- DYNAMIC
	/**
	 * Passes the execute call along to the service.
	 * @param _apiService	The API service
	 * @return				The response, a subclass of APIOutput.
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

	/**
	 * Passes the execute call along to the service.
	 * @return				The response, a subclass of APIOutput.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	default T execute() {
		// Get API Service
		APIService service = APIServiceLocatorSingleton.getAPIService();
		if (service == null)
			throw new ExecutionException("api:execute: Execute was called without an APIService, but APIServiceLocatorSingleton has not been configured");

		// Execute & cast
		Object object = service.execute(this);

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