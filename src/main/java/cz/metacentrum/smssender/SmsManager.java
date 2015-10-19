package cz.metacentrum.smssender;

/**
 *
 * @author Jiri Mauritz <jirmauritz at gmail dot com>
 */
public interface SmsManager {
	
	/**
	 * Method sends sms with specified message to specified phone number.
	 * The GSM gate depends on implementation of this interface.
	 * 
	 * @param phone receiver's phone number
	 * @param message sms text message
	 * @return server response which consists of status code and error message
	 */
	public ServerResponse sendSms(String phone, String message);
	
}
