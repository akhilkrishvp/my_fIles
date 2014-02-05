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

public class Apply_Webservice {
	static String response = null;
	static String str;
	static String postURL = "http://leavemgr.specusa.com/json/apply_leave.php?";

	public static String ApplyForLeavDetails(String EmployId, String type,
			String ApplyDate, String NoOfDates, String FromDate, String ToDate,
			String reason) {

		try {

			HttpClient client = new DefaultHttpClient();
			System.out.println("reached companylist");

			HttpPost post = new HttpPost(postURL);

			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("EmpId", EmployId));
			params.add(new BasicNameValuePair("Leave_Type", type));
			params.add(new BasicNameValuePair("App_date", ApplyDate));
			params.add(new BasicNameValuePair("No_days", NoOfDates));
			params.add(new BasicNameValuePair("From_date", FromDate));
			params.add(new BasicNameValuePair("To_date", ToDate));
			params.add(new BasicNameValuePair("Reason", reason));

			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,
					HTTP.UTF_8);
			post.setEntity(ent);
			HttpResponse responsePOST = client.execute(post);
			HttpEntity resEntity = responsePOST.getEntity();
			response = EntityUtils.toString(resEntity);
			response = response.trim();

			System.out.println("response from Login =" + response);
		} catch (IOException e) {

			e.printStackTrace();
		}
		return response;
	}
}
