package cz.metacentrum.smssender;

import java.util.Arrays;

/**
 *
 * @author Jiri Mauritz <jirmauritz at gmail dot com>
 */
public class Main {
	
	/**
	 * Application sends sms to phone number with message both specified in program arguments.
	 * The GSM gate is dependent on implementation of SmsManager.
	 * 
	 * Arguments:
	 * <table>
	 * <tr> <td> -p </td> <td> phone number in international format (e.g. +420123456789) </td> </tr>
	 * <tr> <td> -m </td> <td> sms text message  </td> </tr>
	 * </table>
	 * 
	 * Error codes:
	 * <table>
	 * <tr> <td> 0 </td> <td> SMS was successfully sent. </td> </tr>
	 * <tr> <td> 1 </td> <td> Phone number is not correct.  </td> </tr>
	 * <tr> <td> 2 </td> <td> Text message is not correct.  </td> </tr>
	 * <tr> <td> 3 </td> <td> Internal Error. Communication problem with the server.  </td> </tr>
	 * </table>
	 */
	public static void main(String[] args) {
		
		String phone = "";
		String message = "";
		
		if (args.length == 0) {
			System.err.println("Bad usage. Wrong number of arguments, 2 required: phone number and message.\n");
			System.err.println("Your input: " + Arrays.toString(args));
			System.err.println(help());
			System.exit(1);
		} else if (args[0].equals("-h") || args[0].equals("--help")) {
			System.err.println(help());
			System.exit(1);
		} else if (args.length != 4) {
			System.err.println("Bad usage. Wrong number of arguments, 2 required: phone number and message.\n");
			System.err.println("Your input: " + Arrays.toString(args));
			System.err.println(help());
			System.exit(1);
		} else if ((args[0].equals("-p")) && (args[2].equals("-m"))) {
			phone = args[1];
			message = args[3];
		} else if ((args[0].equals("-m")) && (args[2].equals("-p"))) {
			message = args[1];
			phone = args[3];
		} else {
			System.err.println("Bad usage. Wrong arguments, required: tel.number and message.\n");
			System.err.println("Your input: " + Arrays.toString(args));
			System.err.println(help());
			System.exit(1);
		}
		
		// create sms manager instance
		SmsManager sms = new CesnetSmsManager();
		
		// send sms
		ServerResponse serverResponse = sms.sendSms(phone, message);
		
		// handle errors
		switch (serverResponse.getStatusCode()) {
			case 0: System.out.println("SMS was successfully sent.");
				return;
			case 1: System.err.println("ERROR: Phone number is not correct.");
				System.err.println(serverResponse.getErrorMsg());
				System.exit(1);
			case 2: System.err.println("ERROR: Text message is not correct.");
				System.err.println(serverResponse.getErrorMsg());
				System.exit(2);
			case 3: System.err.println("ERROR: Internal Error. Communication problem with the server. Check your connection.");
				System.err.println(serverResponse.getErrorMsg());
				System.exit(3);
			default:System.out.println("ERROR SMS was not sent.");
				System.exit(4);
		}
		
	}
	
	private static String help() {
		StringBuilder sb = new StringBuilder();
		sb.append("--------------HELP-------------");
		sb.append('\n');
		sb.append("-h | --help       =>  help ");
		sb.append('\n');
		sb.append("-p phone_number   =>  specify phone number as a receiver of sms ");
		sb.append('\n');
		sb.append("-m message        =>  specify text message of sms ");
		sb.append('\n');
		return sb.toString();
	}
	
}
