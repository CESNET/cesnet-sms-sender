package cz.metacentrum.smssender;

import java.io.IOException;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;

/**
 * Implementation of SmsManager interface to send SMS through Cesnet GSM gate.
 *
 * @author Jiri Mauritz <jirmauritz at gmail dot com>
 */
public class CesnetSmsManager implements SmsManager {

	// URL identificator
	public static final String url = "https://sms.cesnet.cz/sendSMS";

	/**
	 * Method sends sms with specified message to specified phone number.
	 * Used GSM gate is located at sms.cesnet.cz.
	 *
	 * @param phone receiver's phone number
	 * @param message sms text message
	 * @return server response which consists of status code and error message
	 */
	public ServerResponse sendSms(String phone, String message) {
		Response response;
		Content content;
		try {
			response = Request.Post(url)
				.bodyForm(Form.form().add("phoneNumber",  phone).add("message",  message).build())
				.execute();
			content = response.returnContent();
		} catch (IOException ex) {
			ServerResponse serverResponse = new ServerResponse(3, ex.getLocalizedMessage());
			return serverResponse;
		}

		// first line of response is a status code
		// second line of response is a error message
		String[] responseArr = content.asString().split("\n");
		int responseCode = Integer.parseInt(responseArr[0]);
		int outputCode = 0;

		/* converting cesnet GSM error codes to
			0 (successful),
			1 (wrong phone format),
			2 (wrong format of message)
			3 (internal error)
		   according to this table

		   0	Message succesfully sent.	Zpráva byla odeslána, žádná chyba.
          -1	Access denied.	Klient nebyl registrován, proveďte registraci u správce služby.
		  -2	Certificate hostname mismatch, access denied.	Jméno stroje v předmětu certifikátu nesouhlasí s jeho reverzním DNS záznamem.
		  -3	Request used GET rather than POST.	Argumenty byly poslány v URL, a ne POSTem.
		  -4	Required attribute phoneNumber is missing.	Telefonní číslo nebylo vyplněno, nebo POST data nebyla správně odeslána.
		  -5	Required attribute message is missing.	Zpráva pro příjemce nebyla vypněna.
		  -6	Attribute phoneNumber is not in the E.123 international notation (eg. +420123456789).	Telefonní číslo musí být bez meze, závorek a včetně mezinárodního volacího kódu země se znakem plus na začátku.
		  -7	Country calling code of attribute phoneNumber has not allowed.	Telefonní číslo obsahuje volací kód země, do které správce nepovolil odesílat SMS.
		  -8	Unknown attribute xy in the request.	Klient odeslal argument, který není podporovaný a server mu nerozumí.
		  -9	(Různá hlášení chyb modemu.)	Chyby komunikace s modemem (nezdařilý reset, timeout, výpadek signálu mobilní sítě, apod.
		  -255	Invalid server configuration.	V konfiguračním souboru serveru je syntaktická chyba, nebo v něm není požadovaná sekce či parametr.
		*/
		switch (responseCode) {
			case -1:	outputCode = 3;
					break;
			case -2:	outputCode = 3;
					break;
			case -3:	outputCode = 3;
					break;
			case -4:	outputCode = 1;
					break;
			case -5:	outputCode = 2;
					break;
			case -6:	outputCode = 1;
					break;
			case -7:	outputCode = 1;
					break;
			case -8:	outputCode = 3;
					break;
			case -9:	outputCode = 3;
					break;
			case -255:	outputCode = 3;
					break;
		}

		return new ServerResponse(outputCode, responseArr[1]);
	}

}
