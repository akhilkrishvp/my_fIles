package leavemanagement.spectrum.net.in;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class Edit_Webservices {
	static String edit_response;
	static String postURL = "http://leavemgr.specusa.com/json/edit_leave.php?";

	public static String EditLeavDetails(String EmployId, String type,
			String ApplyDate, String NoOfDates, String FromDate, String ToDate,
			String reason, String id) {
		try {

			HttpClient client = new DefaultHttpClient();

			HttpPost post = new HttpPost(postURL);

			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("EmpId", EmployId));
			params.add(new BasicNameValuePair("Leave_Type", type));
			params.add(new BasicNameValuePair("App_date", ApplyDate));
			params.add(new BasicNameValuePair("No_days", NoOfDates));
			params.add(new BasicNameValuePair("From_date", FromDate));
			params.add(new BasicNameValuePair("To_date", ToDate));
			params.add(new BasicNameValuePair("Reason", reason));
			params.add(new BasicNameValuePair("Id", id));

			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,
					HTTP.UTF_8);
			post.setEntity(ent);
			HttpResponse responsePOST = client.execute(post);
			HttpEntity resEntity = responsePOST.getEntity();
			edit_response = EntityUtils.toString(resEntity);
			edit_response = edit_response.trim();

			System.out
					.println("Edit response isssssssssssss =" + edit_response);

		} catch (IOException e) {

			e.printStackTrace();
		}
		return edit_response;
	}

}
