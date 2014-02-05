package leavemanagement.spectrum.net.in;

import org.json.JSONObject;

public class Login_Jsonparse {
	static String emp_Id, name, empType, department, email_Id, phone_Number,
			uname = null;
	static int loginLen;
	static boolean status;

	public static boolean parseLogin(String data) {
		status = false;
		String statusString = "Failed";
		try {
			System.out.println("status data in parse====" + data);

			// JSONArray cArray = new JSONArray(data);
			// JSONObject job = job;
			JSONObject job = new JSONObject(data);
			statusString = job.getString("Status");

			System.out.println("status getting is" + statusString);

			if (statusString.equals("Success")) {
				status = true;
				uname = job.optString("User Name").toString();
				if (uname.equals("admin")) {
					empType = "Admin";
				} else {
					emp_Id = job.getString("Emp_Id").toString();

					name = job.getString("Name").toString();
					empType = job.getString("Type").toString();
					department = job.getString("Department").toString();
					email_Id = job.getString("Email_Id").toString();
					phone_Number = job.getString("Phone_Number").toString();
				}
			}

		}

		catch (Exception e) {
		}
		return status;
	}

	public static int loginLen() {
		return loginLen;

	}

	public static String getEmp_Id() {
		return emp_Id;
	}

	public static String getName() {
		return name;
	}

	public static String getEmpType() {
		return empType;
	}

	public static String getDepartment() {
		return department;
	}

	public static String getEmail_Id() {
		return email_Id;
	}

	public static String getPhone_Number() {
		return phone_Number;
	}

}
