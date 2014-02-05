package leavemanagement.spectrum.net.in;

import org.json.JSONArray;
import org.json.JSONObject;

import android.R.string;

public class Pending_request_Json {
	static String No_of_request;
	public static String pendingRequest(String data)throws Exception {
			
		try {
			JSONObject pcobj = new JSONObject(data);
			System.out.println("No_Of_Requests isssssssss..");
			No_of_request=pcobj.getString("No_Of_Requests").toString();
			System.out.println("No_Of_Requests isssssssss......."+No_of_request);
			
		} catch (Exception e) {
		}
		return data;
				
	}
	
	
	public static String Get_No_of_requests()
	{
		return No_of_request;
				}
}
