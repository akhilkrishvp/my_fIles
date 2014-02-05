package leavemanagement.spectrum.net.in;

import org.json.JSONArray;
import org.json.JSONObject;

public class Calendar_JsonParse {

	static String[] date;
	static String[] event;

	static int eventLen;

	public static boolean parseEvents(String data) throws Exception {
		boolean status = false;
		String statusString = "Failed";
		try {
			JSONObject cobj = new JSONObject(data);
			statusString = cobj.getString("Status").toString();
			if (statusString.equals("Success")) {
				status = true;
				JSONArray cArray = cobj.getJSONArray("Holidays");
				eventLen = cArray.length();
				date = new String[eventLen];
				event = new String[eventLen];
				System.out.println("EventLen=" + eventLen);
				for (int i = 0; i < eventLen; i++) {
					date[i] = cArray.getJSONObject(i).getString("Date")
							.toString();
					event[i] = cArray.getJSONObject(i).getString("Event")
							.toString();
					System.out.println("Date " + date[i] + " " + event[i]);
				}

			}

		} catch (Exception e) {

		}
		return status;

	}

	public static int EventLen() {
		return eventLen;

	}

	public static String[] getEventDate() {
		return date;
	}

	public static String[] getEventDescriptioin() {
		return event;
	}
}