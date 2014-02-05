package leavemanagement.spectrum.net.in;

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

public class Leave_Type_Webservices {
	static String response = null;
	static String str;
	static String postURL = "http://leavemgr.specusa.com/json/leave_type.php?";

	public static String Leave_type_Details(String Employee_Id) {
		try {
			HttpClient client = new DefaultHttpClient();

			HttpPost post = new HttpPost(postURL);
			System.out.println("The employee Id is........." + Employee_Id);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("EmpId", Employee_Id));

			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,
					HTTP.UTF_8);
			post.setEntity(ent);
			HttpResponse responsePOST = client.execute(post);
			HttpEntity resEntity = responsePOST.getEntity();
			response = EntityUtils.toString(resEntity);
			response = response.trim();
System.out.println("lllllllllllllllllllllll------->"+response);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return response;

	}
}
