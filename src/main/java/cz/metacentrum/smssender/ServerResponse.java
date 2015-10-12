package cz.metacentrum.smssender;

/**
 * Object representing a response from server.
 * 
 * @author Jiri Mauritz <jirmauritz at gmail dot com>
 */
public class ServerResponse {
	
	// http status code
	private final int statusCode;
	// error message from server; when statusCode = 0 then errMsg 
	private final String errorMsg;

	public ServerResponse(int statusCode, String errorMsg) {
		this.statusCode = statusCode;
		this.errorMsg = errorMsg;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}
}
