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

public class Approve_Reject_Delete_webservice {
	static String response;
	static String str;

	static String postURL = "http://leavemgr.specusa.com/json/change_status.php?";

	public static String ARD(String empId, String action, String id) {
		try

		{

			HttpClient client = new DefaultHttpClient();

			System.out.println("cancel leave");

			HttpPost post = new HttpPost(postURL);

			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("EmpId", empId));
			params.add(new BasicNameValuePair("Action", action));

			params.add(new BasicNameValuePair("Id", id));

			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,
					HTTP.UTF_8);

			post.setEntity(ent);

			HttpResponse responsePOST = client.execute(post);

			HttpEntity resEntity = responsePOST.getEntity();

			response = EntityUtils.toString(resEntity);

			response = response.trim();

			System.out.println("response from web staus=" + response);
		}

		catch (IOException e) {

			e.printStackTrace();
		}
		return response;
	}
}
