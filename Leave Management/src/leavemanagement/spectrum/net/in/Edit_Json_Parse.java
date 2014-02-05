package leavemanagement.spectrum.net.in;

import org.json.JSONObject;

public class Edit_Json_Parse {

	static String Edit_result, Edit_reason, Edit_id;

	public static boolean Edit_Parse(String Data) {
		try {

			JSONObject jobj = new JSONObject(Data);
			Edit_result = jobj.getString("Result").toString();
			System.out.println("the result is..........." + Edit_result);

		} catch (Exception e) {

		}
		return true;

	}

	public static String Get_Edit_Result() {
		return Edit_result;

	}
}
