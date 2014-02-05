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

public class Login_Webservices {
	static String response = null;
	static String str;

	// static String postURL =
	// "http://202.146.192.27/interns/intern6_selma/WorkFolder/SelmaJohn_WorkFolder/leave_magnt/Json/login.php?";
	static String postURL = "http://leavemgr.specusa.com/json/login.php?";

	public static String loginDetails(String uname, String pwd) {
		try {

			HttpClient client = new DefaultHttpClient();

			HttpPost post = new HttpPost(postURL);

			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("EmpId", uname));
			params.add(new BasicNameValuePair("Password", pwd));

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